package org.vudroid.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.conn.params.ConnManagerParams;
import org.vudroid.core.DecodeService.DecodeCallback;
import org.vudroid.core.events.ZoomListener;
import org.vudroid.core.models.CurrentPageModel;
import org.vudroid.core.models.DecodingProgressModel;
import org.vudroid.core.models.ZoomModel;
import org.vudroid.core.multitouch.MultiTouchZoom;

public class DocumentView extends View implements ZoomListener {
    private static final int DOUBLE_TAP_TIME = 500;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private File cacheDir;
    protected CurrentPageModel currentPageModel;
    DecodeService decodeService;
    /* access modifiers changed from: private */
    public int decodingPage;
    protected GestureDetector gestureDetector;
    private boolean inZoom;
    private boolean isChanged;
    private boolean isInitialized;
    private boolean isPortrait;
    private long lastDownEventTime;
    private float lastX;
    private float lastY;
    private ReentrantLock lock;
    private MultiTouchZoom multiTouchZoom;
    private int pageCount;
    public int pageIndex;
    /* access modifiers changed from: private */
    public final HashMap<Integer, Page> pages;
    DecodingProgressModel progressModel;
    private final Scroller scroller;
    private VelocityTracker velocityTracker;
    private RectF viewRect;
    protected ZoomModel zoomModel;

    class MyGestureDetector extends SimpleOnGestureListener {
        MyGestureDetector() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 120.0f && Math.abs(velocityX) > 200.0f) {
                DocumentView.this.changePage(1);
                return true;
            } else if (e2.getX() - e1.getX() <= 120.0f || Math.abs(velocityX) <= 200.0f) {
                return false;
            } else {
                DocumentView.this.changePage(-1);
                return true;
            }
        }
    }

    public DocumentView(Context context) {
        this(context, null, null);
    }

    public DocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pages = new HashMap<>();
        this.isInitialized = false;
        this.decodingPage = -1;
        this.lock = new ReentrantLock();
        this.zoomModel = new ZoomModel();
        this.zoomModel.addEventListener(this);
        initMultiTouchZoomIfAvailable(this.zoomModel);
        this.scroller = new Scroller(getContext());
        setKeepScreenOn(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.gestureDetector = new GestureDetector(new MyGestureDetector());
    }

    public DocumentView(Context context, DecodingProgressModel progressModel2, CurrentPageModel currentPageModel2) {
        super(context);
        this.pages = new HashMap<>();
        this.isInitialized = false;
        this.decodingPage = -1;
        this.lock = new ReentrantLock();
        setKeepScreenOn(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.progressModel = progressModel2;
        this.currentPageModel = currentPageModel2;
        this.zoomModel = new ZoomModel();
        initMultiTouchZoomIfAvailable(this.zoomModel);
        this.zoomModel.addEventListener(this);
        this.scroller = new Scroller(getContext());
        this.gestureDetector = new GestureDetector(new MyGestureDetector());
    }

    public void refresh() {
        this.isChanged = true;
        ((Page) this.pages.get(Integer.valueOf(this.pageIndex))).setBitmap(null);
        this.zoomModel.setZoom(1.0f);
        this.zoomModel.commit();
    }

    public void setProgressModel(DecodingProgressModel model) {
        this.progressModel = model;
    }

    public void setPageModel(CurrentPageModel model) {
        this.currentPageModel = model;
    }

    public void openDoc(String filePath) {
        if (this.decodeService != null) {
            this.decodeService.open(filePath);
            this.pageCount = this.decodeService.getPageCount();
            if ("mounted".equals(Environment.getExternalStorageState())) {
                this.cacheDir = new File(Environment.getExternalStorageDirectory(), new File(filePath).getName() + ".tmp");
                this.cacheDir.mkdirs();
                if (!this.cacheDir.exists()) {
                    this.cacheDir = null;
                }
            }
            post(new Runnable() {
                public void run() {
                    DocumentView.this.init();
                    DocumentView.this.updatePageVisibility();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void initMultiTouchZoomIfAvailable(ZoomModel zoomModel2) {
        try {
            this.multiTouchZoom = (MultiTouchZoom) Class.forName("org.vudroid.core.multitouch.MultiTouchZoomImpl").getConstructor(new Class[]{ZoomModel.class}).newInstance(new Object[]{zoomModel2});
        } catch (Exception e) {
            System.out.println("Multi touch zoom is not available: " + e);
        }
    }

    public void setDecodeService(DecodeService decodeService2) {
        this.decodeService = decodeService2;
        this.decodeService.setContainerView(this);
    }

    /* access modifiers changed from: private */
    public void init() {
        if (!this.isInitialized) {
            int width = this.decodeService.getEffectivePagesWidth();
            int height = this.decodeService.getEffectivePagesHeight();
            for (int i = 0; i < this.decodeService.getPageCount(); i++) {
                this.pages.put(Integer.valueOf(i), new Page(this, i));
                ((Page) this.pages.get(Integer.valueOf(i))).setAspectRatio(width, height);
            }
            this.isInitialized = true;
            invalidatePageSizes();
            goToPageImpl(this.pageIndex);
        }
    }

    public void recyle() {
        for (Page page : this.pages.values()) {
            page.dispose();
        }
    }

    private void goToPageImpl(final int toPage) {
        this.pageIndex = toPage;
        System.out.println("goto page " + toPage);
        final Page page = (Page) this.pages.get(Integer.valueOf(toPage));
        if (page.getBitmap() == null) {
            Bitmap bitmap = loadPage(page.index);
            if (bitmap != null) {
                page.setBitmap(bitmap);
            } else if (this.decodingPage != toPage) {
                this.decodingPage = toPage;
                this.decodeService.stopDecoding(this);
                this.decodeService.decodePage(this, toPage, new DecodeCallback() {
                    public void decodeComplete(Bitmap bitmap, float zoom) {
                        DocumentView.this.decodingPage = -1;
                        page.setBitmap(bitmap);
                        DocumentView.this.post(new Runnable() {
                            public void run() {
                                DocumentView.this.postInvalidate();
                                DocumentView.this.updatePageVisibility();
                            }
                        });
                        DocumentView.this.savePage(page);
                        int min = toPage - 1;
                        int max = toPage + 1;
                        for (Page page : DocumentView.this.pages.values()) {
                            if (page.getBitmap() != null && (page.index < min || page.index > max)) {
                                System.out.println("dispose page " + page.index);
                                page.dispose();
                            }
                        }
                    }
                });
            }
        }
        scrollTo(0, page.getTop());
        post(new Runnable() {
            public void run() {
                if (DocumentView.this.currentPageModel != null) {
                    DocumentView.this.currentPageModel.setCurrentPageIndex(DocumentView.this.getCurrentPage());
                }
            }
        });
        this.zoomModel.setZoom(1.0f);
        this.zoomModel.commit();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!this.inZoom && this.decodingPage == -1) {
            post(new Runnable() {
                public void run() {
                    DocumentView.this.updatePageVisibility();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updatePageVisibility() {
        for (Page page : this.pages.values()) {
            page.updateVisibility();
        }
    }

    public void commitZoom() {
        for (Page page : this.pages.values()) {
            page.invalidate();
        }
        this.inZoom = false;
    }

    public void showDocument() {
        post(new Runnable() {
            public void run() {
                DocumentView.this.init();
                DocumentView.this.updatePageVisibility();
            }
        });
    }

    public void changePage(int delta) {
        int toPage;
        int toPage2 = this.pageIndex;
        if (delta == Integer.MIN_VALUE) {
            toPage = 0;
        } else if (delta == Integer.MAX_VALUE) {
            toPage = this.pageCount - 1;
        } else {
            toPage = toPage2 + delta;
            if (toPage < 0) {
                toPage = 0;
            }
            if (toPage > this.pageCount - 1) {
                toPage = this.pageCount - 1;
            }
        }
        goToPage(toPage);
    }

    public void goToPage(int toPage) {
        if (this.isInitialized) {
            goToPageImpl(toPage);
        } else {
            this.pageIndex = toPage;
        }
    }

    public int getCurrentPage() {
        return this.pageIndex;
    }

    public void zoomChanged(float newZoom, float oldZoom) {
        this.inZoom = true;
        stopScroller();
        float ratio = newZoom / oldZoom;
        invalidatePageSizes();
        scrollTo((int) ((((float) (getScrollX() + (getWidth() / 2))) * ratio) - ((float) (getWidth() / 2))), (int) ((((float) (getScrollY() + (getHeight() / 2))) * ratio) - ((float) (getHeight() / 2))));
        postInvalidate();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (!this.gestureDetector.onTouchEvent(ev)) {
            if (this.multiTouchZoom != null) {
                if (!this.multiTouchZoom.onTouchEvent(ev)) {
                    if (this.multiTouchZoom.isResetLastPointAfterZoom()) {
                        setLastPosition(ev);
                        this.multiTouchZoom.setResetLastPointAfterZoom(false);
                    }
                }
            }
            if (this.velocityTracker == null) {
                this.velocityTracker = VelocityTracker.obtain();
            }
            this.velocityTracker.addMovement(ev);
            switch (ev.getAction()) {
                case 0:
                    stopScroller();
                    setLastPosition(ev);
                    if (ev.getEventTime() - this.lastDownEventTime >= 500) {
                        this.lastDownEventTime = ev.getEventTime();
                        break;
                    } else {
                        this.zoomModel.toggleZoomControls();
                        break;
                    }
                case 1:
                    this.velocityTracker.computeCurrentVelocity(1000);
                    this.scroller.fling(getScrollX(), getScrollY(), (int) (-this.velocityTracker.getXVelocity()), (int) (-this.velocityTracker.getYVelocity()), getLeftLimit(), getRightLimit(), getTopLimit(), getBottomLimit());
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                    break;
                case 2:
                    scrollBy((int) (this.lastX - ev.getX()), (int) (this.lastY - ev.getY()));
                    setLastPosition(ev);
                    break;
            }
        }
        return true;
    }

    private void setLastPosition(MotionEvent ev) {
        this.lastX = ev.getX();
        this.lastY = ev.getY();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                    verticalDpadScroll(-1);
                    return true;
                case ConnManagerParams.DEFAULT_MAX_TOTAL_CONNECTIONS /*20*/:
                    verticalDpadScroll(1);
                    return true;
                case 21:
                    lineByLineMoveTo(-1);
                    return true;
                case 22:
                    lineByLineMoveTo(1);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void verticalDpadScroll(int direction) {
        this.scroller.startScroll(getScrollX(), getScrollY(), 0, (getHeight() * direction) / 2);
        invalidate();
    }

    private void lineByLineMoveTo(int direction) {
        if (direction != 1 ? getScrollX() == getLeftLimit() : getScrollX() == getRightLimit()) {
            this.scroller.startScroll(getScrollX(), getScrollY(), direction * (getLeftLimit() - getRightLimit()), (int) ((((Page) this.pages.get(Integer.valueOf(getCurrentPage()))).bounds.height() * ((float) direction)) / 50.0f));
        } else {
            this.scroller.startScroll(getScrollX(), getScrollY(), (getWidth() * direction) / 2, 0);
        }
        invalidate();
    }

    private int getTopLimit() {
        int top = ((Page) this.pages.get(Integer.valueOf(getCurrentPage()))).getTop();
        int topDelt = (int) ((((float) getHeight()) - ((Page) this.pages.get(Integer.valueOf(this.pageIndex))).bounds.height()) / 2.0f);
        if (topDelt > 0) {
            return top + topDelt;
        }
        return top;
    }

    private int getLeftLimit() {
        return 0;
    }

    private int getBottomLimit() {
        int bottom = ((Page) this.pages.get(Integer.valueOf(getCurrentPage()))).getBottom() - getHeight();
        int bottomDelt = (int) ((((float) getHeight()) - ((Page) this.pages.get(Integer.valueOf(this.pageIndex))).bounds.height()) / 2.0f);
        if (bottomDelt > 0) {
            return bottom + bottomDelt;
        }
        return bottom;
    }

    private int getRightLimit() {
        return ((int) (((float) getWidth()) * this.zoomModel.getZoom())) - getWidth();
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(Math.min(Math.max(x, getLeftLimit()), getRightLimit()), Math.min(Math.max(y, getTopLimit()), getBottomLimit()));
        this.viewRect = null;
    }

    /* access modifiers changed from: 0000 */
    public RectF getViewRect() {
        if (this.viewRect == null) {
            this.viewRect = new RectF((float) getScrollX(), (float) getScrollY(), (float) (getScrollX() + getWidth()), (float) (getScrollY() + getHeight()));
        }
        return this.viewRect;
    }

    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            scrollTo(this.scroller.getCurrX(), this.scroller.getCurrY());
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Page page : this.pages.values()) {
            page.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float scrollScaleRatio = getScrollScaleRatio();
        invalidatePageSizes();
        invalidateScroll(scrollScaleRatio);
        commitZoom();
        if (this.isChanged) {
            goToPage(this.pageIndex);
            this.isChanged = false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void invalidatePageSizes() {
        if (this.isInitialized) {
            float heightAccum = 0.0f;
            int width = getWidth();
            float zoom = this.zoomModel.getZoom();
            for (int i = 0; i < this.pages.size(); i++) {
                Page page = (Page) this.pages.get(Integer.valueOf(i));
                float pageHeight = page.getPageHeight(width, zoom);
                page.setBounds(new RectF(0.0f, heightAccum, ((float) width) * zoom, heightAccum + pageHeight));
                heightAccum += pageHeight;
            }
        }
    }

    private void invalidateScroll(float ratio) {
        if (this.isInitialized) {
            stopScroller();
            Page page = (Page) this.pages.get(Integer.valueOf(0));
            if (page != null && page.bounds != null) {
                scrollTo((int) (((float) getScrollX()) * ratio), (int) (((float) getScrollY()) * ratio));
            }
        }
    }

    private float getScrollScaleRatio() {
        Page page = (Page) this.pages.get(Integer.valueOf(0));
        if (page == null || page.bounds == null) {
            return 0.0f;
        }
        return (((float) getWidth()) * this.zoomModel.getZoom()) / page.bounds.width();
    }

    private void stopScroller() {
        if (!this.scroller.isFinished()) {
            this.scroller.abortAnimation();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x008a A[SYNTHETIC, Splitter:B:20:0x008a] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0095 A[SYNTHETIC, Splitter:B:25:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void savePage(org.vudroid.core.Page r9) {
        /*
            r8 = this;
            android.graphics.Bitmap r0 = r9.getBitmap()
            java.io.File r5 = r8.cacheDir
            if (r5 == 0) goto L_0x000a
            if (r0 != 0) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            android.content.res.Resources r5 = r8.getResources()
            android.content.res.Configuration r1 = r5.getConfiguration()
            int r5 = r1.orientation
            r6 = 1
            if (r5 != r6) goto L_0x0067
            java.io.File r2 = new java.io.File
            java.io.File r5 = r8.cacheDir
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            int r7 = r9.index
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = "_p.png"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r2.<init>(r5, r6)
        L_0x0034:
            r3 = 0
            java.io.BufferedOutputStream r4 = new java.io.BufferedOutputStream     // Catch:{ FileNotFoundException -> 0x0087, all -> 0x0092 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0087, all -> 0x0092 }
            r5.<init>(r2)     // Catch:{ FileNotFoundException -> 0x0087, all -> 0x0092 }
            r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0087, all -> 0x0092 }
            android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            r6 = 100
            r0.compress(r5, r6, r4)     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            java.io.PrintStream r5 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            r6.<init>()     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            java.lang.String r7 = "save page "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            int r7 = r9.index     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            java.lang.String r6 = r6.toString()     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            r5.println(r6)     // Catch:{ FileNotFoundException -> 0x009e, all -> 0x009b }
            if (r4 == 0) goto L_0x00a1
            r4.close()     // Catch:{ IOException -> 0x0084 }
            r3 = r4
            goto L_0x000a
        L_0x0067:
            java.io.File r2 = new java.io.File
            java.io.File r5 = r8.cacheDir
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            int r7 = r9.index
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = "_l.png"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r2.<init>(r5, r6)
            goto L_0x0034
        L_0x0084:
            r5 = move-exception
            r3 = r4
            goto L_0x000a
        L_0x0087:
            r5 = move-exception
        L_0x0088:
            if (r3 == 0) goto L_0x000a
            r3.close()     // Catch:{ IOException -> 0x008f }
            goto L_0x000a
        L_0x008f:
            r5 = move-exception
            goto L_0x000a
        L_0x0092:
            r5 = move-exception
        L_0x0093:
            if (r3 == 0) goto L_0x0098
            r3.close()     // Catch:{ IOException -> 0x0099 }
        L_0x0098:
            throw r5
        L_0x0099:
            r6 = move-exception
            goto L_0x0098
        L_0x009b:
            r5 = move-exception
            r3 = r4
            goto L_0x0093
        L_0x009e:
            r5 = move-exception
            r3 = r4
            goto L_0x0088
        L_0x00a1:
            r3 = r4
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: org.vudroid.core.DocumentView.savePage(org.vudroid.core.Page):void");
    }

    /* access modifiers changed from: protected */
    public Bitmap loadPage(int index) {
        File file;
        if (this.cacheDir == null) {
            return null;
        }
        if (getResources().getConfiguration().orientation == 1) {
            file = new File(this.cacheDir, index + "_p.png");
        } else {
            file = new File(this.cacheDir, index + "_l.png");
        }
        if (!file.exists()) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        System.out.println("load page " + index);
        return bitmap;
    }

    public void setPortrait(boolean isPortrait2) {
        this.lock.lock();
        try {
            this.isPortrait = isPortrait2;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isPortrait() {
        this.lock.lock();
        try {
            return this.isPortrait;
        } finally {
            this.lock.unlock();
        }
    }
}
