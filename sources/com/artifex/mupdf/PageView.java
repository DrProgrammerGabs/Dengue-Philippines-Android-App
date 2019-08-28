package com.artifex.mupdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import com.p000hl.android.C0048R;

public abstract class PageView extends ViewGroup {
    private static final int BACKGROUND_COLOR = -1;
    private static final int HIGHLIGHT_COLOR = -2141891073;
    private static final int LINK_COLOR = -2130719608;
    private static final int PROGRESS_DIALOG_DELAY = 200;
    /* access modifiers changed from: private */
    public ProgressBar mBusyIndicator;
    /* access modifiers changed from: private */
    public final Context mContext;
    private AsyncTask<Void, Void, LinkInfo[]> mDrawEntire;
    private AsyncTask<PatchInfo, Void, PatchInfo> mDrawPatch;
    /* access modifiers changed from: private */
    public ImageView mEntire;
    /* access modifiers changed from: private */
    public Bitmap mEntireBm;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mHighlightLinks;
    /* access modifiers changed from: private */
    public boolean mIsBlank;
    /* access modifiers changed from: private */
    public LinkInfo[] mLinks;
    protected int mPageNumber;
    private Point mParentSize;
    /* access modifiers changed from: private */
    public ImageView mPatch;
    /* access modifiers changed from: private */
    public Rect mPatchArea;
    /* access modifiers changed from: private */
    public Point mPatchViewSize;
    /* access modifiers changed from: private */
    public RectF[] mSearchBoxes;
    private View mSearchView;
    protected Point mSize;
    protected float mSourceScale;
    private boolean mUsingHardwareAcceleration;

    /* access modifiers changed from: protected */
    public abstract Bitmap drawPage(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    /* access modifiers changed from: protected */
    public abstract void drawPage(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6);

    /* access modifiers changed from: protected */
    public abstract LinkInfo[] getLinkInfo();

    public PageView(Context c, Point parentSize) {
        super(c);
        this.mContext = c;
        this.mParentSize = parentSize;
        setBackgroundColor(-1);
        this.mUsingHardwareAcceleration = VERSION.SDK != null && Integer.valueOf(VERSION.SDK).intValue() >= 14;
    }

    public void blank(int page) {
        if (this.mDrawEntire != null) {
            this.mDrawEntire.cancel(true);
            this.mDrawEntire = null;
        }
        this.mIsBlank = true;
        this.mPageNumber = page;
        if (this.mSize == null) {
            this.mSize = this.mParentSize;
        }
        if (this.mEntire != null) {
            this.mEntire.setImageBitmap(null);
        }
        if (this.mPatch != null) {
            this.mPatch.setImageBitmap(null);
        }
        if (this.mBusyIndicator == null) {
            this.mBusyIndicator = new ProgressBar(this.mContext);
            this.mBusyIndicator.setIndeterminate(true);
            this.mBusyIndicator.setBackgroundResource(C0048R.drawable.busy);
            addView(this.mBusyIndicator);
        }
    }

    public void setPage(int page, PointF size) {
        if (this.mDrawEntire != null) {
            this.mDrawEntire.cancel(true);
            this.mDrawEntire = null;
        }
        this.mIsBlank = false;
        this.mPageNumber = page;
        if (this.mEntire == null) {
            this.mEntire = new OpaqueImageView(this.mContext);
            this.mEntire.setScaleType(ScaleType.FIT_CENTER);
            addView(this.mEntire);
        }
        this.mSourceScale = Math.min(((float) this.mParentSize.x) / size.x, ((float) this.mParentSize.y) / size.y);
        Point newSize = new Point((int) (size.x * this.mSourceScale), (int) (size.y * this.mSourceScale));
        this.mSize = newSize;
        if (this.mUsingHardwareAcceleration) {
            this.mEntire.setImageBitmap(null);
            this.mEntireBm = null;
        }
        if (!(this.mEntireBm != null && this.mEntireBm.getWidth() == newSize.x && this.mEntireBm.getHeight() == newSize.y)) {
            this.mEntireBm = Bitmap.createBitmap(this.mSize.x, this.mSize.y, Config.ARGB_8888);
        }
        this.mDrawEntire = new AsyncTask<Void, Void, LinkInfo[]>() {
            /* access modifiers changed from: protected */
            public LinkInfo[] doInBackground(Void... v) {
                PageView.this.drawPage(PageView.this.mEntireBm, PageView.this.mSize.x, PageView.this.mSize.y, 0, 0, PageView.this.mSize.x, PageView.this.mSize.y);
                return PageView.this.getLinkInfo();
            }

            /* access modifiers changed from: protected */
            public void onPreExecute() {
                PageView.this.mEntire.setImageBitmap(null);
                if (PageView.this.mBusyIndicator == null) {
                    PageView.this.mBusyIndicator = new ProgressBar(PageView.this.mContext);
                    PageView.this.mBusyIndicator.setIndeterminate(true);
                    PageView.this.mBusyIndicator.setBackgroundResource(C0048R.drawable.busy);
                    PageView.this.addView(PageView.this.mBusyIndicator);
                    PageView.this.mBusyIndicator.setVisibility(4);
                    PageView.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (PageView.this.mBusyIndicator != null) {
                                PageView.this.mBusyIndicator.setVisibility(0);
                            }
                        }
                    }, 200);
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LinkInfo[] v) {
                PageView.this.removeView(PageView.this.mBusyIndicator);
                PageView.this.mBusyIndicator = null;
                PageView.this.mEntire.setImageBitmap(PageView.this.mEntireBm);
                PageView.this.mLinks = v;
                PageView.this.invalidate();
            }
        };
        this.mDrawEntire.execute(new Void[0]);
        if (this.mSearchView == null) {
            this.mSearchView = new View(this.mContext) {
                /* access modifiers changed from: protected */
                public void onDraw(Canvas canvas) {
                    RectF[] arr$;
                    RectF[] arr$2;
                    super.onDraw(canvas);
                    float scale = (PageView.this.mSourceScale * ((float) getWidth())) / ((float) PageView.this.mSize.x);
                    Paint paint = new Paint();
                    if (!PageView.this.mIsBlank && PageView.this.mSearchBoxes != null) {
                        paint.setColor(PageView.HIGHLIGHT_COLOR);
                        for (RectF rect : PageView.this.mSearchBoxes) {
                            canvas.drawRect(rect.left * scale, rect.top * scale, rect.right * scale, rect.bottom * scale, paint);
                        }
                    }
                    if (!PageView.this.mIsBlank && PageView.this.mLinks != null && PageView.this.mHighlightLinks) {
                        paint.setColor(PageView.LINK_COLOR);
                        for (RectF rect2 : PageView.this.mLinks) {
                            canvas.drawRect(rect2.left * scale, rect2.top * scale, rect2.right * scale, rect2.bottom * scale, paint);
                        }
                    }
                }
            };
            addView(this.mSearchView);
        }
        requestLayout();
    }

    public void setSearchBoxes(RectF[] searchBoxes) {
        this.mSearchBoxes = searchBoxes;
        if (this.mSearchView != null) {
            this.mSearchView.invalidate();
        }
    }

    public void setLinkHighlighting(boolean f) {
        this.mHighlightLinks = f;
        if (this.mSearchView != null) {
            this.mSearchView.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x;
        int y;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case 0:
                x = this.mSize.x;
                break;
            default:
                x = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case 0:
                y = this.mSize.y;
                break;
            default:
                y = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(x, y);
        if (this.mBusyIndicator != null) {
            int limit = Math.min(this.mParentSize.x, this.mParentSize.y) / 2;
            this.mBusyIndicator.measure(Integer.MIN_VALUE | limit, Integer.MIN_VALUE | limit);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int w = right - left;
        int h = bottom - top;
        if (this.mEntire != null) {
            this.mEntire.layout(0, 0, w, h);
        }
        if (this.mSearchView != null) {
            this.mSearchView.layout(0, 0, w, h);
        }
        if (this.mPatchViewSize != null) {
            if (this.mPatchViewSize.x == w && this.mPatchViewSize.y == h) {
                this.mPatch.layout(this.mPatchArea.left, this.mPatchArea.top, this.mPatchArea.right, this.mPatchArea.bottom);
            } else {
                this.mPatchViewSize = null;
                this.mPatchArea = null;
                if (this.mPatch != null) {
                    this.mPatch.setImageBitmap(null);
                }
            }
        }
        if (this.mBusyIndicator != null) {
            int bw = this.mBusyIndicator.getMeasuredWidth();
            int bh = this.mBusyIndicator.getMeasuredHeight();
            this.mBusyIndicator.layout((w - bw) / 2, (h - bh) / 2, (w + bw) / 2, (h + bh) / 2);
        }
    }

    public void addHq() {
        Rect viewArea = new Rect(getLeft(), getTop(), getRight(), getBottom());
        if (viewArea.width() != this.mSize.x || viewArea.height() != this.mSize.y) {
            Point patchViewSize = new Point(viewArea.width(), viewArea.height());
            Rect patchArea = new Rect(0, 0, this.mParentSize.x, this.mParentSize.y);
            if (patchArea.intersect(viewArea)) {
                patchArea.offset(-viewArea.left, -viewArea.top);
                if (!patchArea.equals(this.mPatchArea) || !patchViewSize.equals(this.mPatchViewSize)) {
                    if (this.mDrawPatch != null) {
                        this.mDrawPatch.cancel(true);
                        this.mDrawPatch = null;
                    }
                    if (this.mPatch == null) {
                        this.mPatch = new OpaqueImageView(this.mContext);
                        this.mPatch.setScaleType(ScaleType.FIT_CENTER);
                        addView(this.mPatch);
                        this.mSearchView.bringToFront();
                    }
                    Bitmap bm = Bitmap.createBitmap(patchArea.width(), patchArea.height(), Config.ARGB_8888);
                    this.mDrawPatch = new AsyncTask<PatchInfo, Void, PatchInfo>() {
                        /* access modifiers changed from: protected */
                        public PatchInfo doInBackground(PatchInfo... v) {
                            PageView.this.drawPage(v[0].f1bm, v[0].patchViewSize.x, v[0].patchViewSize.y, v[0].patchArea.left, v[0].patchArea.top, v[0].patchArea.width(), v[0].patchArea.height());
                            return v[0];
                        }

                        /* access modifiers changed from: protected */
                        public void onPostExecute(PatchInfo v) {
                            PageView.this.mPatchViewSize = v.patchViewSize;
                            PageView.this.mPatchArea = v.patchArea;
                            PageView.this.mPatch.setImageBitmap(v.f1bm);
                            PageView.this.mPatch.layout(PageView.this.mPatchArea.left, PageView.this.mPatchArea.top, PageView.this.mPatchArea.right, PageView.this.mPatchArea.bottom);
                            PageView.this.invalidate();
                        }
                    };
                    this.mDrawPatch.execute(new PatchInfo[]{new PatchInfo(bm, patchViewSize, patchArea)});
                }
            }
        }
    }

    public void removeHq() {
        if (this.mDrawPatch != null) {
            this.mDrawPatch.cancel(true);
            this.mDrawPatch = null;
        }
        this.mPatchViewSize = null;
        this.mPatchArea = null;
        if (this.mPatch != null) {
            this.mPatch.setImageBitmap(null);
        }
    }

    public int getPage() {
        return this.mPageNumber;
    }

    public boolean isOpaque() {
        return true;
    }
}
