package com.p000hl.android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import com.p000hl.android.HLActivity;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.ContainerEntity;
import com.p000hl.android.book.entity.GifComponentEntity;
import com.p000hl.android.book.entity.GroupEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.EventDispatcher;
import com.p000hl.android.core.helper.AnimationHelper;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.component.AudioComponent;
import com.p000hl.android.view.component.HLCounterComponent;
import com.p000hl.android.view.component.ImageGifComponent;
import com.p000hl.android.view.component.PDFDocumentViewComponentMU;
import com.p000hl.android.view.component.ScrollTextViewComponentEN;
import com.p000hl.android.view.component.TimerComponent;
import com.p000hl.android.view.component.VideoComponent;
import com.p000hl.android.view.component.WebComponent;
import com.p000hl.android.view.component.helper.ComponentHelper;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import com.p000hl.android.view.component.moudle.HLCameraUIComponent;
import com.p000hl.android.view.component.moudle.HLHorRightUIComponent;
import com.p000hl.android.view.component.moudle.HLVerBottomUIComponent;
import com.p000hl.android.view.component.moudle.HLViewFlipper;
import com.p000hl.android.view.component.moudle.slide.HorizontalSlide;
import com.p000hl.android.view.component.moudle.slide.VerticleSlide;
import com.p000hl.callback.Action;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.ViewPage */
public class ViewPage extends ViewGroup implements OnComponentCallbackListener {
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 50;
    public static String PAGE_TYPE_HOR = "PAGE_TYPE_HOR";
    public static String PAGE_TYPE_NONE = "PAGE_TYPE_NONE";
    public static String PAGE_TYPE_VER = "PAGE_TYPE_VER";
    /* access modifiers changed from: private */
    public HLActivity activity;
    AnimatorListener ans = new AnimatorListener() {
        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            ViewPage page = BookController.getInstance().commonPage;
            if (page != null) {
                if (page.getX() > 0.0f) {
                    ViewPage tmpPage = BookController.getInstance().nextCommonPage;
                    if (tmpPage != null) {
                        tmpPage.clean();
                    }
                    BookController.getInstance().nextCommonPage = BookController.getInstance().commonPage;
                    BookController.getInstance().commonPage = BookController.getInstance().prevCommonPage;
                    BookController.getInstance().prevCommonPage = null;
                    if (BookController.getInstance().commonPage.getEntity() != null) {
                        BookController.getInstance().commonPageID = BookController.getInstance().commonPage.getEntity().getID();
                    } else {
                        BookController.getInstance().commonPageID = "";
                    }
                    ViewPage.this.activity.commonLayout.removeView(tmpPage);
                } else if (page.getX() < 0.0f) {
                    ViewPage tmpPage2 = BookController.getInstance().prevCommonPage;
                    if (tmpPage2 != null) {
                        tmpPage2.clean();
                    }
                    BookController.getInstance().prevCommonPage = BookController.getInstance().commonPage;
                    BookController.getInstance().commonPage = BookController.getInstance().nextCommonPage;
                    if (BookController.getInstance().commonPage.getEntity() != null) {
                        BookController.getInstance().commonPageID = BookController.getInstance().commonPage.getEntity().getID();
                    } else {
                        BookController.getInstance().commonPageID = "";
                    }
                    BookController.getInstance().nextCommonPage = null;
                    ViewPage.this.activity.commonLayout.removeView(tmpPage2);
                }
            }
        }

        public void onAnimationCancel(Animator animation) {
        }
    };
    AutoPageCountDown autoPageViewCountDown;
    public int autoPlayCount = 0;
    private int autoPlayFinishCount = 0;
    private Book book;
    /* access modifiers changed from: private */
    public long currentWaitTime;
    private GestureDetector detector = null;
    MotionEvent downEvent;
    public String endPage;
    /* access modifiers changed from: private */
    public PageEntity entity;
    /* access modifiers changed from: private */
    public int groupindex;
    protected boolean hasDoFling;
    /* access modifiers changed from: private */
    public boolean hasLoadPreAndNextPage = false;
    public boolean isCommonPage = false;
    public boolean isHorMove = false;
    private boolean issequence;
    SimpleOnGestureListener listener = new SimpleOnGestureListener() {
        public boolean onSingleTapUp(MotionEvent e) {
            if (BookController.getInstance().getBook().getBookInfo().bookNavType.equals("indesign_slider_view")) {
                if (ViewPage.this.activity.getUPNav().isShowing()) {
                    ViewPage.this.activity.getUPNav().dismiss();
                    ViewPage.this.activity.getBottomNav().dismiss();
                } else {
                    ViewPage.this.activity.getUPNav().show();
                    ViewPage.this.activity.getBottomNav().show();
                }
            }
            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (BookController.getInstance().mainViewPage.getEntity().isEnableNavigation() && ViewPage.this.isHorMove && BookSetting.FLIPCODE == 1 && BookSetting.FLIP_CHANGE_PAGE && !BookController.getInstance().isPlayingChangePageAni) {
                BookController bookController = BookController.getInstance();
                if (e1.getRawX() - e2.getRawX() <= 50.0f || Math.abs(velocityX) <= 50.0f || e1.getRawX() - e2.getRawX() <= e1.getRawY() - e2.getRawY()) {
                    if (e2.getRawX() - e1.getRawX() <= 50.0f || Math.abs(velocityX) <= 50.0f || Math.abs(velocityX) <= Math.abs(velocityY) || e2.getRawX() - e1.getRawX() <= e2.getRawY() - e1.getRawY()) {
                        ViewPage.this.hasDoFling = false;
                    } else if (bookController.getPrePageId() == null) {
                        ViewPage.this.hasDoFling = false;
                    } else {
                        if (ViewPage.this.doMoveAni((-bookController.getViewPage().getX()) + ((float) BookSetting.BOOK_WIDTH))) {
                            int i = 0;
                            while (true) {
                                if (i >= bookController.getHLBookLayout().getChildCount()) {
                                    break;
                                }
                                ViewPage page = (ViewPage) bookController.getHLBookLayout().getChildAt(i);
                                if (page.getEntity().getID().equals(bookController.getPrePageId())) {
                                    ViewPage.this.mAniEndPage = page;
                                    ViewPage.this.endPage = "prev";
                                    break;
                                }
                                i++;
                            }
                            ViewPage.this.hasDoFling = true;
                        }
                    }
                } else if (bookController.getNextPageId() == null) {
                    ViewPage.this.hasDoFling = false;
                } else {
                    if (ViewPage.this.doMoveAni((-bookController.getViewPage().getX()) - ((float) BookSetting.BOOK_WIDTH))) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= bookController.getHLBookLayout().getChildCount()) {
                                break;
                            }
                            ViewPage page2 = (ViewPage) bookController.getHLBookLayout().getChildAt(i2);
                            if (page2.getEntity().getID().equals(bookController.getNextPageId())) {
                                ViewPage.this.mAniEndPage = page2;
                                ViewPage.this.endPage = "next";
                                break;
                            }
                            i2++;
                        }
                        ViewPage.this.hasDoFling = true;
                    }
                }
            }
            return false;
        }

        public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
            return false;
        }

        public void onLongPress(MotionEvent event) {
        }
    };
    /* access modifiers changed from: private */
    public ViewPage mAniEndPage;
    Action mHLCallBack = null;
    /* access modifiers changed from: private */

    /* renamed from: mc */
    public CountDownTimer f22mc;
    private float offSetY = 0.0f;
    private MotionEvent oldEvent4moveX = null;
    private MotionEvent oldEvent4moveY = null;
    public int pageHeight;
    public int pageWidth;
    private int pageY;
    private float ratio = 1.0f;
    private float ratiox = 1.0f;
    private float ratioy = 1.0f;
    /* access modifiers changed from: private */
    public int shouldStopIndex = -1001;
    private float tempRatio = BookSetting.PAGE_RATIO;
    private float tempRatioX = BookSetting.PAGE_RATIOX;
    private float tempRatioY = BookSetting.PAGE_RATIOY;
    private ArrayList<VideoComponent> videoComponnetList;
    private ViewPageState viewPageState;

    /* renamed from: com.hl.android.view.ViewPage$AutoPageCountDown */
    public class AutoPageCountDown extends CountDownTimer {
        public AutoPageCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            BookController.getInstance().flipPage(1);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    /* renamed from: com.hl.android.view.ViewPage$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            try {
                if (ViewPage.this.groupindex >= ViewPage.this.entity.getSequence().Group.size()) {
                    ViewPage.this.stopSequence();
                    ViewPage.this.shouldStopIndex = -1001;
                    return;
                }
                ViewPage.this.playSequence();
                if (ViewPage.this.currentWaitTime != 0) {
                    ViewPage.this.f22mc = new MyCount(ViewPage.this.currentWaitTime, ViewPage.this.currentWaitTime).start();
                }
            } catch (Exception ex) {
                Log.e("hl", " finish", ex);
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public ViewPage(Context context) {
        super(context);
    }

    public ViewPage(Context context, PageEntity entity2, Book book2) {
        super(context);
        this.entity = entity2;
        this.book = book2;
        setDrawingCacheEnabled(true);
        this.viewPageState = new ViewPageState();
        EventDispatcher.getInstance().init();
        this.activity = (HLActivity) context;
    }

    public PageEntity getEntity() {
        if (this.entity == null) {
            Log.d("wdy", "为啥会是空的");
        }
        return this.entity;
    }

    public void setEntity(PageEntity entity2) {
        if (entity2 == null) {
            Log.d("wdy", "为啥会是空的");
        }
        this.entity = entity2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeigth);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int widthSpec = 0;
            int heightSpec = 0;
            LayoutParams params = v.getLayoutParams();
            if (params.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(params.width, 1073741824);
            } else if (params.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, 1073741824);
            } else if (params.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, Integer.MIN_VALUE);
            }
            if (params.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(params.height, 1073741824);
            } else if (params.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth, 1073741824);
            } else if (params.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureWidth, Integer.MIN_VALUE);
            }
            v.measure(widthSpec, heightSpec);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int i = 0;
        while (i < getChildCount()) {
            try {
                ViewCell view = (ViewCell) getChildAt(i);
                view.doSlideAction(this.offSetY);
                MarginLayoutParams marginLp = (MarginLayoutParams) view.getLayoutParams();
                if (BookSetting.FITSCREEN_TENSILE) {
                    if (this.tempRatioX != BookSetting.PAGE_RATIOX) {
                        this.ratiox = BookSetting.PAGE_RATIOX / this.tempRatioX;
                        this.tempRatioX = BookSetting.PAGE_RATIOX;
                    }
                    if (this.tempRatioY != BookSetting.PAGE_RATIOY) {
                        this.ratioy = BookSetting.PAGE_RATIOY / this.tempRatioY;
                        this.tempRatioY = BookSetting.PAGE_RATIOY;
                    }
                    view.layout((int) (((float) marginLp.leftMargin) + (((float) view.getEntity().f7x) * this.ratiox) + ((float) view.moveX)), (int) (((float) marginLp.topMargin) + (((float) view.getEntity().f8y) * this.ratioy) + ((float) view.moveY)), (int) (((float) marginLp.leftMargin) + (((float) view.getEntity().f7x) * this.ratiox) + (((float) marginLp.width) * this.ratiox) + ((float) view.moveX)), (int) (((float) marginLp.topMargin) + (((float) view.getEntity().f8y) * this.ratioy) + (((float) marginLp.height) * this.ratioy) + ((float) view.moveY)));
                } else {
                    if (this.tempRatio != BookSetting.PAGE_RATIO) {
                        this.ratio = BookSetting.PAGE_RATIO / this.tempRatio;
                        this.tempRatio = BookSetting.PAGE_RATIO;
                    }
                    view.layout((int) (((float) marginLp.leftMargin) + (((float) view.getEntity().f7x) * this.ratio) + ((float) view.moveX)), (int) (((float) marginLp.topMargin) + (((float) view.getEntity().f8y) * this.ratio) + ((float) view.moveY)), (int) (((float) marginLp.leftMargin) + (((float) view.getEntity().f7x) * this.ratio) + (((float) marginLp.width) * this.ratio) + ((float) view.moveX)), (int) (((float) marginLp.topMargin) + (((float) view.getEntity().f8y) * this.ratio) + (((float) marginLp.height) * this.ratio) + ((float) view.moveY)));
                }
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    public LayoutParams getCurrentLayoutParams() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
        if (BookSetting.FITSCREEN_TENSILE) {
            this.tempRatioX = BookSetting.PAGE_RATIOX;
            this.tempRatioY = BookSetting.PAGE_RATIOY;
            BookSetting.PAGE_RATIOX = BookSetting.BOOK_WIDTH4CALCULATE / getEntity().getWidth();
            int initBookHeight = BookController.getInstance().getBook().getBookInfo().bookHeight;
            int initBookWidth = BookController.getInstance().getBook().getBookInfo().bookWidth;
            BookSetting.PAGE_RATIOY = BookSetting.BOOK_HEIGHT4CALCULATE / ((float) initBookHeight);
            if (getEntity().getWidth() < getEntity().getHeight()) {
                BookSetting.PAGE_RATIOY = BookSetting.BOOK_HEIGHT4CALCULATE / ((float) Math.max(initBookHeight, initBookWidth));
            }
            lp.width = (int) ScreenUtils.getHorScreenValue(getEntity().getWidth());
            lp.height = (int) ScreenUtils.getVerScreenValue(getEntity().getHeight());
            lp.addRule(15);
            lp.addRule(14);
            if (!(this.tempRatioX == BookSetting.PAGE_RATIOX && this.tempRatioY == BookSetting.PAGE_RATIOY)) {
                requestLayout();
            }
        } else {
            this.tempRatio = BookSetting.PAGE_RATIO;
            BookSetting.PAGE_RATIO = BookSetting.BOOK_WIDTH4CALCULATE / getEntity().getWidth();
            lp.width = (int) ScreenUtils.getHorScreenValue(getEntity().getWidth());
            lp.height = (int) ScreenUtils.getVerScreenValue(getEntity().getHeight());
            lp.addRule(15);
            lp.addRule(14);
            if (this.tempRatio != BookSetting.PAGE_RATIO) {
                requestLayout();
            }
        }
        return lp;
    }

    public void load(PageEntity entity2) {
        this.entity = entity2;
        this.autoPlayCount = 0;
        this.autoPlayFinishCount = 0;
        load();
    }

    public void startPlay() {
        try {
            AnimationHelper.animatiorMap.clear();
            if (this.issequence) {
                this.groupindex = 0;
                if (this.entity.IsGroupPlay || BookSetting.IS_AUTOPAGE) {
                    this.currentWaitTime = ((Long) this.entity.getSequence().Delay.get(0)).longValue();
                    this.f22mc = new MyCount(this.currentWaitTime, this.currentWaitTime);
                    this.f22mc.start();
                    return;
                }
            }
            if (!this.hasLoadPreAndNextPage && BookSetting.FLIP_CHANGE_PAGE && BookSetting.FLIPCODE == 1 && !this.isCommonPage) {
                BookController.getInstance().getAndSetPreAndNextViewPage();
                this.hasLoadPreAndNextPage = true;
                this.mAniEndPage = BookController.getInstance().getViewPage();
                this.endPage = "this";
            }
            for (int i = 0; i < getChildCount(); i++) {
                ViewCell cell = (ViewCell) getChildAt(i);
                if (cell.getEntity().isHideAtBegining) {
                    cell.setVisibility(8);
                }
                Component component = cell.getComponent();
                if (component instanceof HLCounterComponent) {
                    ((HLCounterComponent) component).setCounterText();
                }
                if (component instanceof TimerComponent) {
                    ((TimerComponent) component).syncBean();
                }
                ComponentEntity entity2 = cell.getEntity();
                if (entity2.isPlayVideoOrAudioAtBegining) {
                    cell.play();
                    if (component instanceof ImageGifComponent) {
                        if (((GifComponentEntity) entity2).isIsPlayOnetime() && !entity2.isAutoLoop()) {
                            this.autoPlayCount++;
                        }
                    } else if (!entity2.isAutoLoop()) {
                        this.autoPlayCount++;
                    }
                    if (component instanceof TimerComponent) {
                        ((TimerComponent) component).playTimer();
                    }
                    Book book2 = BookController.getInstance().getBook();
                    if (book2 != null) {
                        if (getEntity().getID().equals(book2.getStartPageID())) {
                            if (component instanceof AudioComponent) {
                                ((AudioComponent) component).setControlUnable();
                            }
                            if (component instanceof VideoComponent) {
                                ((VideoComponent) component).setControlUnable();
                            }
                        }
                    }
                }
                if (entity2.isPlayAnimationAtBegining && entity2.getAnims() != null && entity2.getAnims().size() > 0) {
                    AnimationHelper.playAnimation(cell);
                    if (!component.getEntity().isAutoLoop()) {
                        this.autoPlayCount++;
                    }
                }
                if ((component instanceof PDFDocumentViewComponentMU) || (component instanceof WebComponent) || (component instanceof HLViewFlipper) || (component instanceof VerticleSlide) || (component instanceof ScrollTextViewComponentEN) || (component instanceof HorizontalSlide)) {
                    cell.play();
                }
            }
            if (this.autoPlayCount == 0 && this.mHLCallBack != null && !this.viewPageState.isStopped()) {
                new CountDownTimer(1, 1) {
                    public void onFinish() {
                        ViewPage.this.mHLCallBack.doAction();
                        ViewPage.this.mHLCallBack = null;
                    }

                    public void onTick(long arg0) {
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.activity.finish();
        } catch (Error e2) {
            this.activity.finish();
        }
    }

    public void setPageCompletion(Action callBack) {
        if (this.videoComponnetList == null || this.videoComponnetList.size() <= 0) {
            this.mHLCallBack = callBack;
        } else {
            ((VideoComponent) this.videoComponnetList.get(0)).doCompletAction = callBack;
        }
    }

    public GestureDetector getDetector() {
        return this.detector;
    }

    public void playVideo() {
        if (this.videoComponnetList != null && this.videoComponnetList.size() > 0) {
            if (this.viewPageState.stopped) {
                Iterator i$ = this.videoComponnetList.iterator();
                while (i$.hasNext()) {
                    ((VideoComponent) ((Component) i$.next())).continuePlay();
                }
                return;
            }
            Iterator i$2 = this.videoComponnetList.iterator();
            while (i$2.hasNext()) {
                Component videoCompent = (Component) i$2.next();
                if (!HLSetting.isNewActivityForVideo) {
                    addView((View) videoCompent);
                    ((VideoComponent) videoCompent).getSurfaceView().setZOrderOnTop(true);
                    ((View) videoCompent).bringToFront();
                    if (videoCompent.getEntity().isHideAtBegining) {
                        ((View) videoCompent).setVisibility(4);
                    } else {
                        ((View) videoCompent).setVisibility(0);
                    }
                }
            }
        }
    }

    public void pause() {
        if (!this.viewPageState.stopped) {
            try {
                this.viewPageState.stopped = true;
                if (getChildCount() > 0) {
                    for (int i = 0; i < getChildCount(); i++) {
                        ((ViewCell) getChildAt(i)).pause();
                    }
                }
                if (this.issequence) {
                    stopSequence();
                }
            } catch (Exception e) {
                Log.e("hl", "stop ", e);
            }
        }
    }

    public void resume() {
        if (this.viewPageState.stopped) {
            this.viewPageState.stopped = false;
            try {
                if (getChildCount() > 0) {
                    for (int i = 0; i < getChildCount(); i++) {
                        ((ViewCell) getChildAt(i)).resume();
                    }
                }
            } catch (Exception e) {
                Log.e("hl", "resume ", e);
            }
        }
    }

    public void stopVideo() {
        Iterator i$ = this.videoComponnetList.iterator();
        while (i$.hasNext()) {
            VideoComponent vc = (VideoComponent) i$.next();
            vc.stop();
            vc.setVisibility(8);
            removeView(vc);
        }
    }

    public void stop() {
        try {
            AnimationHelper.animatiorMap.clear();
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    ViewCell viewCell = (ViewCell) getChildAt(i);
                    AnimationHelper.stopAnimation(viewCell);
                    viewCell.stop();
                }
            }
            if (this.issequence) {
                stopSequence();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().toString(), "stop", e);
        }
        this.viewPageState.setStoped();
    }

    private void setBackground() {
        if (!StringUtils.isEmpty(this.entity.getBackground().f9ID) && !StringUtils.isEmpty(this.entity.getBackground().getComponent().getClassName())) {
            addView(new ViewCell(getContext(), this, this.entity.getBackground()));
        }
    }

    private void load() {
        this.detector = new GestureDetector(this.activity, this.listener);
        BookSetting.PAGE_RATIO = BookSetting.BOOK_WIDTH4CALCULATE / this.entity.getWidth();
        BookSetting.PAGE_RATIOX = BookSetting.BOOK_WIDTH4CALCULATE / this.entity.getWidth();
        int initBookHeight = BookController.getInstance().getBook().getBookInfo().bookHeight;
        int initBookWidth = BookController.getInstance().getBook().getBookInfo().bookWidth;
        BookSetting.PAGE_RATIOY = BookSetting.BOOK_HEIGHT4CALCULATE / ((float) initBookHeight);
        if (getEntity().getWidth() < getEntity().getHeight()) {
            BookSetting.PAGE_RATIOY = BookSetting.BOOK_HEIGHT4CALCULATE / ((float) Math.max(initBookHeight, initBookWidth));
        }
        this.pageWidth = BookSetting.BOOK_WIDTH;
        this.pageHeight = (int) (this.entity.getHeight() * BookSetting.PAGE_RATIO);
        if (BookSetting.FITSCREEN_TENSILE) {
            this.pageHeight = (int) (this.entity.getHeight() * BookSetting.PAGE_RATIOY);
        }
        this.videoComponnetList = new ArrayList<>();
        setBackground();
        try {
            Iterator i$ = this.entity.getContainers().iterator();
            while (i$.hasNext()) {
                ContainerEntity containerEntity = (ContainerEntity) i$.next();
                Component component = ComponentHelper.getComponent(containerEntity, this);
                if (component != null) {
                    if ((component instanceof HLCameraUIComponent) && ((HLCameraUIComponent) component).camera != null) {
                        ((HLCameraUIComponent) component).camera.stopPreview();
                        ((HLCameraUIComponent) component).camera.release();
                        ((HLCameraUIComponent) component).camera = null;
                        ((HLCameraUIComponent) component).holder = null;
                        ((HLCameraUIComponent) component).surface = null;
                    }
                    addView(new ViewCell(getContext(), this, containerEntity));
                }
            }
            if (this.entity.getSequence() == null || this.entity.getSequence().Group.size() <= 0) {
                this.issequence = false;
                return;
            }
            this.issequence = true;
            this.groupindex = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void stopSequence() {
        if (this.f22mc == null) {
            return;
        }
        if (BookSetting.IS_AUTOPAGE || this.entity.IsGroupPlay) {
            this.f22mc.cancel();
        }
    }

    public void hideMenu() {
        int i = 0;
        while (i < getChildCount()) {
            try {
                if (getChildAt(i) instanceof HLVerBottomUIComponent) {
                    ((HLVerBottomUIComponent) getChildAt(i)).hideMenu();
                }
                if (getChildAt(i) instanceof HLHorRightUIComponent) {
                    ((HLHorRightUIComponent) getChildAt(i)).hideMenu();
                }
                i++;
            } catch (Exception e) {
                Log.e("hl", "hideMenu ", e);
                return;
            }
        }
    }

    public void playSequence() {
        try {
            if (!this.issequence) {
                return;
            }
            if (this.groupindex - 1 == this.shouldStopIndex || this.groupindex == this.shouldStopIndex) {
                stopSequence();
                Iterator i$ = ((GroupEntity) this.entity.getSequence().Group.get(this.shouldStopIndex)).ContainerID.iterator();
                while (i$.hasNext()) {
                    ViewCell c = getCellByID((String) i$.next());
                    if (!(c == null || c.getEntity().getAnims() == null)) {
                        AnimationHelper.stopAnimation(c);
                    }
                }
                this.groupindex = this.entity.getSequence().Group.size();
                this.shouldStopIndex = -1001;
                return;
            }
            Iterator i$2 = ((GroupEntity) this.entity.getSequence().Group.get(this.groupindex)).ContainerID.iterator();
            while (i$2.hasNext()) {
                String cID = (String) i$2.next();
                this.currentWaitTime = 0;
                ViewCell c2 = getCellByID(cID);
                if (!(c2 == null || c2.getEntity().getAnims() == null)) {
                    for (int i = 0; i < c2.getEntity().getAnims().size(); i++) {
                        AnimationEntity curAnimationEntity = (AnimationEntity) c2.getEntity().getAnims().get(i);
                        this.currentWaitTime += Long.parseLong(curAnimationEntity.Delay) + (Long.parseLong(curAnimationEntity.Duration) * Long.parseLong(curAnimationEntity.Repeat));
                    }
                    AnimationHelper.playAnimation(c2);
                }
                if (c2 != null) {
                    c2.play();
                }
            }
            this.groupindex++;
            invalidate();
        } catch (Exception e) {
            Log.e("hl", "playSequence ", e);
        }
    }

    public ViewCell getCellByID(String id) {
        return (ViewCell) BookController.getInstance().hlActivity.mainLayout.findViewById(Integer.parseInt(id.substring(8)));
    }

    public void computeScroll() {
        super.computeScroll();
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z = false;
        long startTime = System.currentTimeMillis();
        if (!BookController.getInstance().doFlipSubPage) {
            if (this.isCommonPage) {
                BookController.getInstance().getHLBookLayout().dispatchTouchEvent(event);
            } else if (BookController.getInstance().mainViewPage.getEntity().isEnableNavigation()) {
                if (event.getAction() == 0) {
                    this.downEvent = MotionEvent.obtain(event);
                    this.isHorMove = false;
                    if (this.entity.enablePageTurnByHand && this.entity.isEnableNavigation() && event.getPointerCount() == 1) {
                        EventDispatcher.getInstance().onTouch(this.downEvent);
                    }
                    getDetector().onTouchEvent(this.downEvent);
                    if (BookSetting.FLIP_CHANGE_PAGE && BookSetting.FLIPCODE == 1 && BookController.getInstance().mainViewPage.getEntity().isEnableNavigation()) {
                        doTouchAction4MovePageBetweenPage(this.downEvent);
                    }
                } else {
                    if (event.getAction() == 2 && this.downEvent != null) {
                        if (Math.abs(event.getRawX() - this.downEvent.getRawX()) > Math.abs(event.getRawY() - this.downEvent.getRawY())) {
                            z = true;
                        }
                        this.isHorMove = z;
                        this.downEvent = null;
                    }
                    Log.d("zhaoq", "is hor move" + this.isHorMove + " action is " + event.getRawX());
                    getDetector().onTouchEvent(event);
                    if (!this.isHorMove || BookSetting.FLIPCODE != 1 || !BookController.getInstance().mainViewPage.getEntity().isEnableNavigation()) {
                        if (this.entity.enablePageTurnByHand && this.entity.isEnableNavigation() && event.getPointerCount() == 1) {
                            EventDispatcher.getInstance().onTouch(event);
                        }
                    } else if (BookSetting.FLIP_CHANGE_PAGE) {
                        doTouchAction4MovePageBetweenPage(event);
                    } else if (this.entity.enablePageTurnByHand && this.entity.isEnableNavigation() && event.getPointerCount() == 1) {
                        EventDispatcher.getInstance().onTouch(event);
                    }
                }
                if (BookSetting.BOOK_HEIGHT < this.pageHeight && event.getAction() == 2) {
                    moveDy((float) ((int) (event.getRawY() - this.oldEvent4moveY.getRawY())));
                }
                this.oldEvent4moveY = MotionEvent.obtain(event);
                Log.d("wdy", "touch Time:endTime-startTime:" + (System.currentTimeMillis() - startTime));
            }
        }
        return true;
    }

    public void doTouchAction4MovePageBetweenPage(MotionEvent event) {
        if (!BookController.getInstance().isPlayingChangePageAni) {
            int action = event.getAction();
            if (action == 2) {
                float dx = 0.0f;
                if (this.oldEvent4moveX != null) {
                    dx = event.getRawX() - this.oldEvent4moveX.getRawX();
                }
                if (BookController.getInstance().getNextPageId() == null && dx < 0.0f && getX() <= 0.0f) {
                    dx = 0.0f;
                }
                if (BookController.getInstance().getPrePageId() == null && dx > 0.0f && getX() >= 0.0f) {
                    dx = 0.0f;
                }
                for (int i = 0; i < BookController.getInstance().getHLBookLayout().getChildCount(); i++) {
                    View v = BookController.getInstance().getHLBookLayout().getChildAt(i);
                    v.setX(v.getX() + dx);
                }
            } else if (action == 1) {
                if (this.hasDoFling) {
                    this.hasDoFling = false;
                    return;
                }
                if (getX() < ((float) (-BookSetting.BOOK_WIDTH)) / 2.0f) {
                    if (doMoveAni((-BookController.getInstance().getViewPage().getX()) - ((float) BookSetting.BOOK_WIDTH))) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= BookController.getInstance().getHLBookLayout().getChildCount()) {
                                break;
                            }
                            ViewPage page = (ViewPage) BookController.getInstance().getHLBookLayout().getChildAt(i2);
                            if (page.getEntity().getID().equals(BookController.getInstance().getNextPageId())) {
                                this.mAniEndPage = page;
                                this.endPage = "next";
                                break;
                            }
                            i2++;
                        }
                    }
                } else if (getX() > ((float) BookSetting.BOOK_WIDTH) / 2.0f) {
                    if (doMoveAni((-BookController.getInstance().getViewPage().getX()) + ((float) BookSetting.BOOK_WIDTH))) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= BookController.getInstance().getHLBookLayout().getChildCount()) {
                                break;
                            }
                            ViewPage page2 = (ViewPage) BookController.getInstance().getHLBookLayout().getChildAt(i3);
                            if (page2.getEntity().getID().equals(BookController.getInstance().getPrePageId())) {
                                this.mAniEndPage = page2;
                                this.endPage = "prev";
                                break;
                            }
                            i3++;
                        }
                    }
                } else if (doMoveAni(-BookController.getInstance().getViewPage().getX())) {
                    this.mAniEndPage = BookController.getInstance().getViewPage();
                    this.endPage = "this";
                    BookController.getInstance().shouldKeepMainPage = true;
                }
                if (this.mAniEndPage != null) {
                    BookController.getInstance().setDefaultView(BookController.getInstance().getSectionPagePosition(this.mAniEndPage.getEntity().getID()));
                }
                this.hasDoFling = false;
            }
            this.oldEvent4moveX = MotionEvent.obtain(event);
        }
    }

    public void setCommonPageX() {
        boolean prev;
        boolean next;
        this.activity.commonLayout.setVisibility(0);
        float targetX = BookController.getInstance().viewPage.getX();
        if (BookController.getInstance().prevCommonPage != null) {
            prev = true;
        } else {
            prev = false;
        }
        if (BookController.getInstance().nextCommonPage != null) {
            next = true;
        } else {
            next = false;
        }
        if (prev) {
            BookController.getInstance().prevCommonPage.setX(targetX - ((float) BookSetting.BOOK_WIDTH));
        }
        if (next) {
            BookController.getInstance().nextCommonPage.setX(((float) BookSetting.BOOK_WIDTH) + targetX);
        }
        if (BookController.getInstance().commonPage != null) {
            ViewPage page = BookController.getInstance().commonPage;
            if (!prev && targetX > 0.0f && !BookController.getInstance().prevCommonPageEmpty) {
                page.setX(0.0f);
            } else if (next || targetX >= 0.0f || BookController.getInstance().nextCommonPageEmpty) {
                page.setX(targetX);
            } else {
                page.setX(0.0f);
            }
        }
    }

    public boolean doMoveAni(float moveX) {
        if (BookController.getInstance().isPlayingChangePageAni) {
            return false;
        }
        BookController.getInstance().isPlayingChangePageAni = true;
        boolean hasSetListenner = false;
        for (int i = 0; i < BookController.getInstance().getHLBookLayout().getChildCount(); i++) {
            View page = BookController.getInstance().getHLBookLayout().getChildAt(i);
            ObjectAnimator animator = ObjectAnimator.ofFloat(page, "x", new float[]{page.getX() + moveX});
            animator.setDuration(500);
            if (!hasSetListenner) {
                setListenner(animator);
                hasSetListenner = true;
            }
            animator.start();
        }
        return true;
    }

    public void setAniEndPage(ViewPage aniEndPage) {
        this.mAniEndPage = aniEndPage;
    }

    private void setListenner(ObjectAnimator animator) {
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                ViewPage.this.activity.getUPNav().dismiss();
                ViewPage.this.activity.getBottomNav().dismiss();
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                new CountDownTimer(50, 50) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        BookController.getInstance().isPlayingChangePageAni = false;
                        ViewPage.this.hasLoadPreAndNextPage = false;
                        if (!BookController.getInstance().shouldKeepMainPage) {
                            BookController.getInstance().showPageWithLoadedPage(ViewPage.this.mAniEndPage, ViewPage.this.endPage);
                        } else {
                            ViewPage.this.hasLoadPreAndNextPage = true;
                            BookController.getInstance().shouldKeepMainPage = false;
                        }
                        if (ViewPage.this.mAniEndPage != ViewPage.this) {
                            BookController.getInstance().startPlay();
                        }
                    }
                }.start();
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    public void moveDy(float dy) {
        this.offSetY += dy;
        if (this.offSetY >= 0.0f) {
            this.offSetY = 0.0f;
        } else if (this.offSetY < ((float) (BookSetting.BOOK_HEIGHT - this.pageHeight))) {
            this.offSetY = (float) (BookSetting.BOOK_HEIGHT - this.pageHeight);
        }
        requestLayout();
    }

    public float getOffSetY() {
        return this.offSetY;
    }

    public boolean isHasChildPage() {
        if (this.entity.getNavePageIds() == null || this.entity.getNavePageIds().size() <= 0) {
            return false;
        }
        return true;
    }

    public void clean() {
        int i = 0;
        while (i < getChildCount()) {
            try {
                ((ViewCell) getChildAt(i)).recyle();
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        clearAnimation();
        this.groupindex = 0;
        System.gc();
    }

    public void cleanForChangePage() {
        int i = 0;
        while (i < getChildCount()) {
            try {
                removeView(getChildAt(i));
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    public int getPageWidth() {
        return this.pageWidth;
    }

    public void setX(float x) {
        super.setX(x);
        if (this == BookController.getInstance().viewPage) {
            setCommonPageX();
        }
        float rate = (-getX()) / ((float) BookSetting.BOOK_WIDTH);
        int i = 0;
        while (i < getChildCount()) {
            try {
                ViewCell view = (ViewCell) getChildAt(i);
                if (view.getEntity().isPageTweenSlide) {
                    view.doSlideAction4TweenPage(rate);
                }
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    public void setPageWidth(int pageWidth2) {
        this.pageWidth = pageWidth2;
    }

    public int getPageY() {
        return this.pageY;
    }

    public void setPageY(int pageY2) {
        this.pageY = pageY2;
    }

    public int getPageHeight() {
        return this.pageHeight;
    }

    public void setPageHeight(int pageHeight2) {
        this.pageHeight = pageHeight2;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book2) {
        this.book = book2;
    }

    public boolean getIsSequence() {
        return this.issequence;
    }

    public ViewPageState getViewPageState() {
        return this.viewPageState;
    }

    public void setViewPageState(ViewPageState viewPageState2) {
        this.viewPageState = viewPageState2;
    }

    public void setPlayComplete() {
        this.autoPlayFinishCount++;
        if (this.autoPlayCount == this.autoPlayFinishCount && BookSetting.IS_AUTOPAGE && this.entity.IsGroupPlay) {
            this.autoPageViewCountDown = new AutoPageCountDown(2000, 1000);
            this.autoPageViewCountDown.start();
        } else if (this.autoPlayCount == this.autoPlayFinishCount && this.autoPlayCount > 0 && this.mHLCallBack != null) {
            this.mHLCallBack.doAction();
            this.mHLCallBack = null;
        }
    }

    public void bringToFront() {
        super.bringToFront();
    }

    public void stopGroupAtSomeWhere(String value) {
        this.shouldStopIndex = Integer.parseInt(value);
    }
}
