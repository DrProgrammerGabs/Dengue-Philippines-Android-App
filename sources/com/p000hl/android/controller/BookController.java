package com.p000hl.android.controller;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLActivity;
import com.p000hl.android.book.BookDecoder;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.ButtonEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.book.entity.SnapshotEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.helper.BookHelper;
import com.p000hl.android.core.helper.animation.MyAnimation4CubeEffect;
import com.p000hl.android.core.helper.animation.MyAnimation4FlipEffect;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.ViewPage;
import com.p000hl.android.view.component.AudioComponent;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.HLTableViewComponent;
import com.p000hl.android.view.component.bean.TimerShowBean;
import com.p000hl.android.view.gallary.base.AbstractGalley;
import com.p000hl.android.view.layout.HLRelativeLayout;
import com.p000hl.android.view.pageflip.AbstractPageFlipView;
import com.p000hl.android.view.pageflip.ActionOnEnd;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.hl.android.controller.BookController */
public class BookController {
    private static BookController bookController;
    public static String lastPageID = "";
    private View adView;
    public TimerShowBean ascShow = new TimerShowBean();
    private AutoPageCountDown autoPageViewCountDown = null;
    private AudioComponent backgroundMusic;
    public Book book;
    public ViewPage commonPage;
    public String commonPageID = "";
    public int count = -1;
    public int currendsectionindex = 0;
    public TimerShowBean descShow = new TimerShowBean();
    public boolean doFlipSubPage;
    /* access modifiers changed from: private */
    public AbstractPageFlipView flipView;
    protected FrameLayout frameLayout;
    /* access modifiers changed from: private */
    public AbstractGalley gallery;
    private ImageButton galleyButton;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (BookController.this.nextButton != null) {
                    BookController.this.nextButton.setClickable(true);
                }
                if (BookController.this.preButton != null) {
                    BookController.this.preButton.setClickable(true);
                }
                BookController.this.getHLBookLayout().removeView(BookController.this.frameLayout);
                BookController.this.hlActivity.refreshSnapshots();
                BookController.this.setOrien();
                BookController.this.removeNotShowViewPage();
                BookController.this.startPlay();
            }
        }
    };
    public HLActivity hlActivity;
    private HLRelativeLayout hlBookLayout;
    private ImageButton homeButton;
    boolean isCommonKeep = true;
    public boolean isPlayingChangePageAni;
    /* access modifiers changed from: private */
    public boolean isSettingGallery = false;
    public String mainPageID;
    public ViewPage mainViewPage;
    private String newPageID = "";
    /* access modifiers changed from: private */
    public ImageButton nextButton;
    public ViewPage nextCommonPage;
    public boolean nextCommonPageEmpty = false;
    public String nextPageID;
    public ViewPage nextViewPage;
    private ArrayList<String> pageHistory;
    private RelativeLayout pdfMenuBarelativeLayout;
    /* access modifiers changed from: private */
    public ImageButton preButton;
    public String prePageID;
    public ViewPage preViewPage;
    public ViewPage prevCommonPage;
    public boolean prevCommonPageEmpty = false;
    JSONArray pushJsonList;
    Bitmap resizeBmp = null;
    public SectionEntity section;
    public boolean shouldKeepMainPage = false;
    private ArrayList<String> snapshots;
    private ArrayList<String> subPageList = null;
    private AbstractPageFlipView subPageViewFlip;
    public ViewPage viewPage;
    private WindowManager windwoManager;

    /* renamed from: com.hl.android.controller.BookController$AutoPageCountDown */
    public class AutoPageCountDown extends CountDownTimer {
        public AutoPageCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            BookController.this.flipPage(1);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    /* renamed from: com.hl.android.controller.BookController$MyActionOnEnd */
    class MyActionOnEnd implements ActionOnEnd {
        /* access modifiers changed from: private */
        public int pageIndex = 0;

        public MyActionOnEnd(int pageIndex2) {
            this.pageIndex = pageIndex2;
        }

        public void doAction() {
            BookController.this.getHLBookLayout().post(new Runnable() {
                public void run() {
                    BookController.this.setDefaultView(MyActionOnEnd.this.pageIndex);
                    BookController.this.mainViewPage.bringToFront();
                    BookController.this.flipView.recycleBitmap();
                    BookState.getInstance().restoreFlipState();
                    BookController.this.setOrien();
                    BookController.this.hlActivity.refreshMark();
                    BookController.this.removeNotShowViewPage();
                }
            });
        }
    }

    public static BookController getInstance() {
        if (bookController == null) {
            bookController = new BookController();
        }
        return bookController;
    }

    public static void recyle() {
        if (bookController != null) {
            bookController = null;
        }
    }

    public void loadStartPage(ViewPage _viewPage) {
        _viewPage.load(BookDecoder.getInstance().decodePageEntity(getHLActivity(), this.book.getStartPageID()));
    }

    public void playStartPage(ViewPage _viewPage) {
        this.viewPage = _viewPage;
        getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
        this.viewPage.startPlay();
        this.viewPage.playVideo();
    }

    public void playStartPage(ViewPage _viewPage, PageEntity entity) {
        this.viewPage = _viewPage;
        this.viewPage.load(entity);
        getSectionPagePosition(entity.getID());
        getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
        startPlay();
        setDefaultView(0);
        if (this.book.getBookInfo().backgroundMusicId != null && this.book.getBookInfo().backgroundMusicId.length() > 0) {
            playBackgroundMusic();
        }
        this.subPageList = this.viewPage.getEntity().getNavePageIds();
        this.mainPageID = this.viewPage.getEntity().getID();
        this.mainViewPage = this.viewPage;
    }

    public void playBook() {
        if (this.book != null) {
            if (this.book.getBookInfo().backgroundMusicId != null && this.book.getBookInfo().backgroundMusicId.length() > 0) {
                playBackgroundMusic();
            }
            if (this.book.getSections() != null) {
                this.section = (SectionEntity) this.book.getSections().get(0);
                if (this.section.pages != null && this.section.getPages().size() != 0) {
                    getPageHistory().clear();
                    changePageById((String) this.section.getPages().get(0));
                }
            }
        }
    }

    public void playBackgroundMusic() {
        if (!StringUtils.isEmpty(this.book.getBookInfo().backgroundMusicId)) {
            if (this.backgroundMusic == null) {
                ComponentEntity componentEntity = new ComponentEntity();
                componentEntity.setLocalSourceId(this.book.getBookInfo().backgroundMusicId);
                componentEntity.autoLoop = true;
                componentEntity.isHideAtBegining = true;
                this.backgroundMusic = new AudioComponent(getHLActivity(), componentEntity);
                this.backgroundMusic.isBackGroundMusic = true;
                this.backgroundMusic.setBackgroundColor(0);
                this.backgroundMusic.load();
                this.backgroundMusic.setEntity(componentEntity);
                this.backgroundMusic.play();
                HLSetting.PlayBackGroundMusic = true;
                return;
            }
            this.backgroundMusic.play();
        }
    }

    public void setSubDefaultView() {
        if (HLSetting.IsAD && this.adView != null) {
            this.adView.bringToFront();
        }
        if (this.homeButton != null) {
            this.homeButton.bringToFront();
        }
        if (this.galleyButton != null) {
            this.galleyButton.bringToFront();
        }
        try {
            if (this.viewPage.getEntity().isEnableNavigation()) {
                if (this.homeButton != null) {
                    this.homeButton.setVisibility(0);
                }
                if (this.galleyButton != null) {
                    this.galleyButton.setVisibility(0);
                    return;
                }
                return;
            }
            if (this.homeButton != null) {
                this.homeButton.setVisibility(8);
            }
            if (this.galleyButton != null) {
                this.galleyButton.setVisibility(8);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDefaultView(int pageindex) {
        if (!BookSetting.IS_NO_NAVIGATION) {
            if (this.gallery != null) {
                this.gallery.setSelection(pageindex);
            } else {
                return;
            }
        }
        try {
            if (this.viewPage != null && this.viewPage.getEntity() != null) {
                if (this.viewPage.getEntity().isEnableNavigation()) {
                    if (this.preButton != null) {
                        if (pageindex == 0) {
                            this.preButton.setVisibility(4);
                        } else {
                            this.preButton.setVisibility(0);
                        }
                    }
                    if (this.nextButton != null) {
                        if (pageindex == this.section.pages.size() - 1) {
                            this.nextButton.setVisibility(4);
                        } else {
                            this.nextButton.setVisibility(0);
                        }
                    }
                    if (this.homeButton != null) {
                        this.homeButton.setVisibility(0);
                    }
                    if (this.galleyButton != null) {
                        this.galleyButton.setVisibility(0);
                    }
                } else {
                    if (this.preButton != null) {
                        this.preButton.setVisibility(8);
                    }
                    if (this.nextButton != null) {
                        this.nextButton.setVisibility(8);
                    }
                    if (this.homeButton != null) {
                        this.homeButton.setVisibility(8);
                    }
                    if (this.galleyButton != null) {
                        this.galleyButton.setVisibility(8);
                    }
                }
                setGlobalButtonVisibale(this.preButton);
                setGlobalButtonVisibale(this.nextButton);
                setGlobalButtonVisibale(this.homeButton);
                setGlobalButtonVisibale(this.galleyButton);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setGlobalButtonVisibale(ImageButton imgBtn) {
        try {
            if (!((ButtonEntity) imgBtn.getTag()).isVisible()) {
                imgBtn.setVisibility(8);
            }
        } catch (Exception e) {
        }
    }

    private void prepare4Switch() {
        LayoutParams flipViewLp = new LayoutParams(BookSetting.BOOK_WIDTH, BookSetting.BOOK_HEIGHT);
        flipViewLp.gravity = 17;
        this.flipView.setLayoutParams(flipViewLp);
        PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, this.newPageID);
        this.isCommonKeep = pageEntity.beCoveredPageID.equals(this.viewPage.getEntity().beCoveredPageID);
        Bitmap bl = getCurrentBookSnap();
        if (!this.isCommonKeep) {
            this.hlActivity.commonLayout.removeView(this.commonPage);
            if (this.commonPage != null) {
                this.commonPage.stop();
                this.commonPage.clean();
                this.commonPage = null;
            }
        }
        this.flipView.setViewPage(this.hlBookLayout);
        this.flipView.setBitmap(bl);
        this.flipView.show();
        this.viewPage.stopVideo();
        this.viewPage.stop();
        this.viewPage.clean();
        getHLBookLayout().removeView(this.viewPage);
        this.viewPage.getRootView().invalidate();
        if (pageEntity.isCashSnapshot()) {
            this.flipView.setPreLoad(false);
            this.resizeBmp = getSnapShotCashImage(pageEntity);
            this.flipView.setNewBitmap(this.resizeBmp);
            return;
        }
        this.flipView.setPreLoad(true);
        showPageOnly(this.newPageID);
    }

    public Bitmap getCurrentBookSnap() {
        return getSnapshot(this.hlBookLayout);
    }

    public Bitmap getSnapShotCashImage(PageEntity pageEntity) {
        if (pageEntity == null) {
            return null;
        }
        return BitmapUtils.getBitMap(pageEntity.getSnapShotID(), this.hlActivity, BookSetting.SCREEN_WIDTH, BookSetting.SCREEN_HEIGHT);
    }

    public Bitmap getCurrentSnapShotCashImage() {
        return getSnapShotCashImage(getPageEntityByID(this.mainPageID));
    }

    public Bitmap getSmallSnapShotCashImage(PageEntity pageEntity) {
        return BitmapUtils.getBitMap(pageEntity.getSnapShotID(), this.hlActivity, 100, 80);
    }

    public void pageViewPost() {
        if (BookSetting.IS_AUTOPAGE) {
            this.autoPageViewCountDown = new AutoPageCountDown(2000, 1000);
            this.autoPageViewCountDown.start();
        }
    }

    public void flipPage(int tag) {
        if (tag == -1) {
            prePage();
        } else {
            nextPage();
        }
    }

    public void prePage() {
        int currentPageIndex = getSectionPagePosition(this.mainPageID);
        if (currentPageIndex != 0 && this.viewPage.getEntity().isEnableNavigation() && BookState.getInstance().setFlipState()) {
            if (this.gallery != null) {
                this.gallery.hideGalleryInfor();
            }
            switchPage(currentPageIndex, currentPageIndex - 1);
        }
    }

    public void switchPage(int currentPageIndex, int newPageIndex) {
        if (this.viewPage != null && this.viewPage.getEntity() != null) {
            this.newPageID = (String) ((SectionEntity) getBook().getSections().get(this.currendsectionindex)).pages.get(newPageIndex);
            prepare4Switch();
            this.flipView.play(currentPageIndex, newPageIndex, new MyActionOnEnd(newPageIndex));
            if (this.commonPage != null) {
                this.commonPage.bringToFront();
            }
            setDefaultView(newPageIndex);
        }
    }

    public void nextPage() {
        int currentPageIndex = getSectionPagePosition(this.mainPageID);
        if (currentPageIndex != ((SectionEntity) getBook().getSections().get(this.currendsectionindex)).pages.size() - 1 && this.viewPage.getEntity().isEnableNavigation() && BookState.getInstance().setFlipState()) {
            if (this.gallery != null) {
                this.gallery.hideGalleryInfor();
            }
            switchPage(currentPageIndex, currentPageIndex + 1);
        }
    }

    public String getNextPageId() {
        return this.nextPageID;
    }

    public String getPrePageId() {
        return this.prePageID;
    }

    public LinearLayout getLoadLayout() {
        LinearLayout lay = new LinearLayout(this.hlActivity);
        lay.setGravity(17);
        lay.setOrientation(1);
        ProgressBar pb = new ProgressBar(this.hlActivity);
        pb.setMax(100);
        TextView loadText = new TextView(this.hlActivity);
        loadText.setTextColor(-16777216);
        lay.addView(loadText);
        lay.addView(pb, new LinearLayout.LayoutParams(100, 100));
        return lay;
    }

    public void playPageById(String pageId) {
        if (isPageExist(pageId) && BookState.getInstance().setFlipState()) {
            if (this.gallery != null && this.gallery.getVisibility() == 0) {
                this.gallery.setVisibility(8);
                this.gallery.hideGalleryInfor();
            }
            if (this.mainPageID == null || pageId == null || !this.mainPageID.equals(pageId)) {
                storePageHistory(pageId);
                this.newPageID = pageId;
                int currentPageIndex = 0;
                SectionEntity currentSection = this.section;
                if (currentSection.isShelves) {
                    BookSetting.BOOK_PATH = currentSection.bookPath;
                    HLSetting.IsResourceSD = currentSection.isResourceSD;
                    BookSetting.IS_SHELVES_COMPONENT = currentSection.isShelves;
                    BookDecoder.getInstance().initBookItemList();
                }
                if (this.viewPage.getEntity() != null) {
                    currentPageIndex = getSectionPagePosition(this.viewPage.getEntity().getID());
                }
                int newPageIndex = getSectionPagePosition(this.newPageID);
                if (currentSection.f16ID.equals(this.section.f16ID)) {
                    switchPage(currentPageIndex, newPageIndex);
                } else if (this.book.getSections().indexOf(currentSection) < this.book.getSections().indexOf(this.section)) {
                    switchPage(-1, newPageIndex);
                } else {
                    switchPage(999, newPageIndex);
                }
            } else {
                removeNotShowViewPage();
                startPlay();
            }
        }
    }

    private void storePageHistory(String pageId) {
        if (getPageHistory().isEmpty()) {
            getPageHistory().add(pageId);
        } else if (!((String) getPageHistory().get(getPageHistory().size() - 1)).equals(pageId)) {
            getPageHistory().add(pageId);
        }
    }

    public void changePageWithEffect(String type, String direction, long duration, long delay, String pageId) {
        if (PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, pageId) == null) {
            Log.d("wdy", "page " + pageId + " is not exist，please check");
            Log.e("wdy", "page " + pageId + " is not exist，please check");
            return;
        }
        final String str = pageId;
        final String str2 = type;
        final String str3 = direction;
        final long j = duration;
        getHLBookLayout().post(new Runnable() {
            public void run() {
                if (BookController.this.gallery != null && BookController.this.gallery.getVisibility() == 0) {
                    BookController.this.gallery.setVisibility(8);
                    BookController.this.gallery.hideGalleryInfor();
                }
                if (BookController.this.nextButton != null) {
                    BookController.this.nextButton.setClickable(false);
                }
                if (BookController.this.preButton != null) {
                    BookController.this.preButton.setClickable(false);
                }
                ImageView imageView = new ImageView(BookController.this.hlActivity);
                ImageView imageView2 = new ImageView(BookController.this.hlActivity);
                Bitmap curBitmap1 = BookController.this.getSnapShotCashImage(BookController.this.viewPage.getEntity());
                BookController.this.viewPage.stop();
                BookController.this.getHLBookLayout().removeView(BookController.this.viewPage);
                BookController.this.viewPage.clean();
                Bitmap nextBitmap = BookController.this.getSnapShotCashImage(PageEntityController.getInstance().getPageEntityByPageId(BookController.this.hlActivity, str));
                imageView.setImageBitmap(curBitmap1);
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView2.setImageBitmap(nextBitmap);
                imageView2.setScaleType(ScaleType.FIT_XY);
                LayoutParams layoutParams = new LayoutParams(BookController.this.viewPage.getPageWidth(), BookController.this.viewPage.getPageHeight());
                BookController.this.frameLayout = new FrameLayout(BookController.this.hlActivity);
                BookController.this.frameLayout.addView(imageView, layoutParams);
                BookController.this.frameLayout.addView(imageView2, layoutParams);
                BookController.this.getHLBookLayout().addView(BookController.this.frameLayout, BookController.this.viewPage.getCurrentLayoutParams());
                BookController.this.showPage(str);
                AnimationSet curImageAnim = new AnimationSet(true);
                AnimationSet animationSet = new AnimationSet(true);
                if (str2.equals("transitionFade")) {
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    curImageAnim.setZAdjustment(1);
                    animationSet.setZAdjustment(-1);
                } else if (str2.equals("transitionMoveIn")) {
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    TranslateAnimation transAni = null;
                    if (str3.equals("left")) {
                        transAni = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("right")) {
                        transAni = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("up")) {
                        transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                    } else if (str3.equals("down")) {
                        transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                    }
                    animationSet.addAnimation(transAni);
                } else if (str2.equals("transitionReveal")) {
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    TranslateAnimation transAni2 = null;
                    if (str3.equals("left")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("right")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("up")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                    } else if (str3.equals("down")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                    }
                    curImageAnim.addAnimation(transAni2);
                    curImageAnim.setZAdjustment(1);
                    animationSet.setZAdjustment(-1);
                } else if (str2.equals("transitionPush")) {
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    TranslateAnimation transAni4curImage = null;
                    TranslateAnimation transAni4nextImage = null;
                    if (str3.equals("left")) {
                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                        transAni4nextImage = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("right")) {
                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                        transAni4nextImage = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else if (str3.equals("up")) {
                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                        transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                    } else if (str3.equals("down")) {
                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                        transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                    }
                    curImageAnim.addAnimation(transAni4curImage);
                    animationSet.addAnimation(transAni4nextImage);
                } else if (str2.equals("cubeEffect")) {
                    if (str3.equals("left") || str3.equals("up")) {
                        curImageAnim.addAnimation(new MyAnimation4CubeEffect(0.0f, -90.0f, str3));
                        animationSet.addAnimation(new MyAnimation4CubeEffect(90.0f, 0.0f, str3));
                    } else if (str3.equals("right") || str3.equals("down")) {
                        curImageAnim.addAnimation(new MyAnimation4CubeEffect(0.0f, 90.0f, str3));
                        animationSet.addAnimation(new MyAnimation4CubeEffect(-90.0f, 0.0f, str3));
                    }
                } else if (str2.equals("flipEffect")) {
                    ImageView imageView3 = imageView;
                    animationSet.addAnimation(new MyAnimation4FlipEffect(0.0f, 180.0f, imageView3, nextBitmap, str3));
                    BookController.this.frameLayout.removeView(imageView2);
                } else {
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    curImageAnim.setZAdjustment(1);
                    animationSet.setZAdjustment(-1);
                }
                curImageAnim.setDuration(j);
                animationSet.setDuration(j);
                animationSet.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation arg0) {
                    }

                    public void onAnimationRepeat(Animation arg0) {
                    }

                    public void onAnimationEnd(Animation arg0) {
                        BookController.this.handler.sendEmptyMessage(0);
                    }
                });
                BookController.this.frameLayout.bringToFront();
                BookController.this.frameLayout.setBackgroundColor(-1);
                imageView.setLayerType(2, null);
                if (str2.equals("flipEffect")) {
                    imageView.startAnimation(animationSet);
                    return;
                }
                imageView.startAnimation(curImageAnim);
                imageView2.startAnimation(animationSet);
            }
        });
    }

    public void changePageById(final String pageId) {
        if (PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, pageId) == null) {
            Log.d("wdy", "page " + pageId + " is not exist，please check");
            Log.e("wdy", "page " + pageId + " is not exist，please check");
        } else if (!BookState.getInstance().setFlipState()) {
        } else {
            if (!isPageExist(pageId)) {
                BookState.getInstance().restoreFlipState();
                return;
            }
            if (this.gallery != null && this.gallery.getVisibility() == 0) {
                this.gallery.setVisibility(8);
                this.gallery.hideGalleryInfor();
            }
            getHLBookLayout().post(new Runnable() {
                public void run() {
                    BookHelper.setupScreen(BookController.this.hlActivity);
                    BookController.this.getHLBookLayout().removeView(BookController.this.viewPage);
                    BookController.this.viewPage.stop();
                    BookController.this.viewPage.clean();
                    BookController.this.showPage(pageId);
                    BookController.this.setOrien();
                    BookController.this.removeNotShowViewPage();
                    BookController.this.startPlay();
                    BookController.this.hlActivity.relayoutGlobalButton();
                    BookController.this.hlActivity.progressHandler.sendEmptyMessage(1);
                    BookController.this.hlActivity.refreshSnapshots();
                }
            });
        }
    }

    public void showPageOnly(String pageID) {
        long startTime = System.currentTimeMillis();
        loadPage(pageID);
        Log.d("ww", "load use time is :" + (System.currentTimeMillis() - startTime));
        getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
        this.subPageList = this.viewPage.getEntity().getNavePageIds();
        lastPageID = this.mainPageID;
        storePageHistory(pageID);
        this.mainPageID = this.viewPage.getEntity().getID();
        this.mainViewPage = this.viewPage;
        this.mainViewPage.bringToFront();
        this.hlActivity.refreshSnapshots();
    }

    public void showPageWithLoadedPage(ViewPage viewPage2, String endPage) {
        this.subPageList = viewPage2.getEntity().getNavePageIds();
        String temp = this.mainPageID;
        this.mainPageID = viewPage2.getEntity().getID();
        if (!(this.mainPageID == null || temp == null || temp.equals(this.mainPageID))) {
            lastPageID = temp;
            storePageHistory(viewPage2.getEntity().getID());
        }
        this.mainViewPage = viewPage2;
        this.mainViewPage.bringToFront();
        this.viewPage = this.mainViewPage;
        if (!StringUtils.isEmpty(this.mainViewPage.getEntity().beCoveredPageID)) {
            this.commonPageID = this.mainViewPage.getEntity().beCoveredPageID;
            if (!(this.prevCommonPage == null || this.prevCommonPage.getEntity() == null || !this.prevCommonPage.getEntity().getID().equals(this.commonPageID))) {
                if (endPage.equals("prev")) {
                    this.nextCommonPage = this.commonPage;
                    this.commonPage = this.prevCommonPage;
                    this.prevCommonPage = null;
                }
                Log.d("zhaoq", "commonLayout.removeView(prevCommonPage)");
            }
            if (!(this.nextCommonPage == null || this.nextCommonPage.getEntity() == null || !this.nextCommonPage.getEntity().getID().equals(this.commonPageID))) {
                if (endPage.equals("next")) {
                    this.prevCommonPage = this.commonPage;
                    this.commonPage = this.nextCommonPage;
                    this.nextCommonPage = null;
                }
                Log.d("zhaoq", "commonLayout.removeView(prevCommonPage)");
            }
        } else {
            if (this.commonPage != null) {
                this.commonPageID = "";
                this.commonPage.stop();
                this.commonPage.clean();
                this.hlActivity.commonLayout.removeView(this.commonPage);
                this.commonPage = null;
            }
            if (this.prevCommonPage != null) {
                this.prevCommonPage.stop();
                this.prevCommonPage.clean();
                this.hlActivity.commonLayout.removeView(this.prevCommonPage);
                this.prevCommonPage = null;
            }
            if (this.nextCommonPage != null) {
                this.nextCommonPage.stop();
                this.nextCommonPage.clean();
                this.hlActivity.commonLayout.removeView(this.nextCommonPage);
                this.nextCommonPage = null;
            }
        }
        setOrien();
        removeNotShowViewPage();
        getSectionPagePosition(this.mainPageID);
        if (this.hlActivity.getMarkView4NavMenu() != null) {
            this.hlActivity.getMarkView4NavMenu().refresh();
        }
        this.hlActivity.refreshSnapshots();
    }

    public void removeNotShowViewPage() {
        int i = 0;
        while (i < getHLBookLayout().getChildCount()) {
            View page = getHLBookLayout().getChildAt(i);
            if ((page instanceof ViewPage) && this.mainViewPage != page) {
                if (!((ViewPage) page).isCommonPage) {
                    ((ViewPage) page).stopVideo();
                    ((ViewPage) page).stop();
                    ((ViewPage) page).clean();
                    getHLBookLayout().removeView(page);
                    i--;
                } else if (this.commonPage == null || !((ViewPage) page).getEntity().getID().equals(this.commonPageID)) {
                    ((ViewPage) page).stopVideo();
                    ((ViewPage) page).stop();
                    ((ViewPage) page).clean();
                    getHLBookLayout().removeView(page);
                    i--;
                }
            }
            i++;
        }
        int i2 = 0;
        while (i2 < this.hlActivity.commonLayout.getChildCount()) {
            View page2 = this.hlActivity.commonLayout.getChildAt(i2);
            if (page2 instanceof ViewPage) {
                try {
                    if (this.commonPage == null || !((ViewPage) page2).getEntity().getID().equals(this.commonPageID)) {
                        ((ViewPage) page2).stopVideo();
                        ((ViewPage) page2).stop();
                        ((ViewPage) page2).clean();
                        this.hlActivity.commonLayout.removeView(page2);
                        i2--;
                    }
                } catch (Exception e) {
                }
            }
            i2++;
        }
        getHLBookLayout().requestLayout();
        this.hlActivity.commonLayout.requestLayout();
    }

    public void showPage(String pageID) {
        storePageHistory(pageID);
        loadPage(pageID);
        int pageIndex = getSectionPagePosition(pageID);
        getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
        this.subPageList = this.viewPage.getEntity().getNavePageIds();
        lastPageID = this.mainPageID;
        this.mainPageID = this.viewPage.getEntity().getID();
        this.mainViewPage = this.viewPage;
        this.mainViewPage.bringToFront();
        getHLBookLayout().postInvalidate();
        setDefaultView(pageIndex);
        this.hlActivity.refreshSnapshots();
    }

    public void goHomePage() {
        String pageID;
        String str = "";
        if (StringUtils.isEmpty(this.book.getBookInfo().homePageID)) {
            pageID = (String) this.section.getPages().get(0);
        } else {
            pageID = this.book.getBookInfo().homePageID;
        }
        if (BookSetting.IS_HOR_VER) {
            PageEntity entity = getPageEntityByID(pageID);
            if (BookSetting.IS_HOR && entity.getType().equals(ViewPage.PAGE_TYPE_VER) && entity.getLinkPageID().equals("")) {
                this.hlActivity.setRequestedOrientation(1);
                BookHelper.setupScreen(this.hlActivity);
                this.hlActivity.setFlipView();
                BookSetting.IS_HOR = false;
                BookState.getInstance().isChangeTo = true;
            } else if (!BookSetting.IS_HOR && (entity.getType().equals(ViewPage.PAGE_TYPE_HOR) || entity.getType().equals(ViewPage.PAGE_TYPE_NONE))) {
                if (entity.getLinkPageID().equals("")) {
                    this.hlActivity.setRequestedOrientation(0);
                    BookHelper.setupScreen(this.hlActivity);
                    this.hlActivity.setFlipView();
                    BookSetting.IS_HOR = true;
                    BookState.getInstance().isChangeTo = true;
                } else {
                    pageID = entity.getLinkPageID();
                }
            }
        }
        playPageById(pageID);
        this.count = -1;
    }

    public void loadAndMoveTo(String pageID) {
        int a = -1;
        if (pageID != null && !pageID.equals(this.mainPageID)) {
            ViewPage newPage = new ViewPage(this.hlActivity, null, null);
            loadPage(pageID, newPage);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.width = BookSetting.BOOK_WIDTH;
            lp.height = (int) ScreenUtils.getHorScreenValue(newPage.getEntity().getHeight());
            newPage.setLayoutParams(lp);
            getHLBookLayout().addView(newPage);
            if (getSectionPagePosition(pageID) > getSectionPagePosition(this.mainPageID)) {
                a = 1;
            }
            newPage.setX(this.mainViewPage.getX() + ((float) (BookSetting.BOOK_WIDTH * a)));
            this.mainViewPage.setAniEndPage(newPage);
            this.mainViewPage.doMoveAni((float) ((-a) * BookSetting.BOOK_WIDTH));
        }
    }

    public void startPlay() {
        try {
            this.doFlipSubPage = false;
            if (!StringUtils.isEmpty(this.commonPageID)) {
                this.hlActivity.commonLayout.setVisibility(0);
            }
            this.viewPage.startPlay();
            if (this.commonPage != null && (this.commonPage.getTag(C0048R.C0049id.tag_firsttimeplay_commonpage) == null || !((Boolean) this.commonPage.getTag(C0048R.C0049id.tag_firsttimeplay_commonpage)).booleanValue())) {
                this.commonPage.startPlay();
                this.commonPage.setTag(C0048R.C0049id.tag_firsttimeplay_commonpage, Boolean.valueOf(true));
            }
            this.viewPage.playVideo();
            BookState.getInstance().setPlayViewPage();
            if (BookSetting.IS_AUTOPAGE && this.viewPage.autoPlayCount == 0) {
                pageViewPost();
            }
        } catch (Exception ex) {
            Log.e("hl", "start play error", ex);
        }
    }

    public void getAndSetPreAndNextViewPage() {
        if (this.mainViewPage != null) {
            this.nextPageID = null;
            this.prePageID = null;
            int currentRootPageIndex = getSectionPagePosition(this.mainViewPage.getEntity().getID());
            int currentRootsectionindex = this.currendsectionindex;
            this.prevCommonPageEmpty = false;
            this.nextCommonPageEmpty = false;
            if (currentRootPageIndex != ((SectionEntity) getBook().getSections().get(currentRootsectionindex)).pages.size() - 1) {
                this.nextPageID = (String) ((SectionEntity) getBook().getSections().get(currentRootsectionindex)).pages.get(currentRootPageIndex + 1);
                this.nextViewPage = new ViewPage(this.hlActivity, null, null);
                loadPage(this.nextPageID, this.nextViewPage);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
                lp.width = BookSetting.BOOK_WIDTH;
                lp.height = (int) ScreenUtils.getHorScreenValue(this.nextViewPage.getEntity().getHeight());
                this.nextViewPage.setLayoutParams(lp);
                getHLBookLayout().addView(this.nextViewPage);
                this.nextViewPage.setX(this.mainViewPage.getX() + ((float) BookSetting.BOOK_WIDTH));
                if (!this.nextViewPage.getEntity().beCoveredPageID.equals(this.commonPageID)) {
                    if (!StringUtils.isEmpty(this.nextViewPage.getEntity().beCoveredPageID)) {
                        this.nextCommonPage = new ViewPage(this.hlActivity, null, null);
                        this.nextCommonPage.isCommonPage = true;
                        loadPage(this.nextViewPage.getEntity().beCoveredPageID, this.nextCommonPage);
                        this.nextCommonPage.setX(this.mainViewPage.getX() + ((float) BookSetting.BOOK_WIDTH));
                        this.hlActivity.commonLayout.addView(this.nextCommonPage);
                    } else {
                        this.nextCommonPageEmpty = true;
                        if (this.nextCommonPage != null) {
                            this.nextCommonPage.clean();
                            this.hlActivity.commonLayout.removeView(this.nextCommonPage);
                            this.nextCommonPage = null;
                        }
                    }
                } else if (this.nextCommonPage != null) {
                    this.nextCommonPage.clean();
                    this.hlActivity.commonLayout.removeView(this.nextCommonPage);
                    this.nextCommonPage = null;
                }
            } else {
                this.nextCommonPageEmpty = true;
                if (this.nextCommonPage != null) {
                    this.nextCommonPage.clean();
                    this.hlActivity.commonLayout.removeView(this.nextCommonPage);
                    this.nextCommonPage = null;
                }
            }
            if (currentRootPageIndex > 0) {
                this.prePageID = (String) ((SectionEntity) getBook().getSections().get(currentRootsectionindex)).pages.get(currentRootPageIndex - 1);
                this.preViewPage = new ViewPage(this.hlActivity, null, null);
                loadPage(this.prePageID, this.preViewPage);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(-1, -1);
                lp2.width = BookSetting.BOOK_WIDTH;
                lp2.height = (int) ScreenUtils.getHorScreenValue(this.preViewPage.getEntity().getHeight());
                this.preViewPage.setLayoutParams(lp2);
                getHLBookLayout().addView(this.preViewPage);
                this.preViewPage.setX(this.mainViewPage.getX() - ((float) BookSetting.BOOK_WIDTH));
                if (!this.preViewPage.getEntity().beCoveredPageID.equals(this.commonPageID)) {
                    if (!StringUtils.isEmpty(this.preViewPage.getEntity().beCoveredPageID)) {
                        this.prevCommonPage = new ViewPage(this.hlActivity, null, null);
                        this.prevCommonPage.isCommonPage = true;
                        loadPage(this.preViewPage.getEntity().beCoveredPageID, this.prevCommonPage);
                        this.prevCommonPage.setX(this.mainViewPage.getX() - ((float) BookSetting.BOOK_WIDTH));
                        this.hlActivity.commonLayout.addView(this.prevCommonPage);
                        return;
                    }
                    this.prevCommonPageEmpty = true;
                    if (this.prevCommonPage != null) {
                        this.prevCommonPage.clean();
                        this.hlActivity.commonLayout.removeView(this.prevCommonPage);
                        this.prevCommonPage = null;
                    }
                } else if (this.prevCommonPage != null) {
                    this.prevCommonPage.clean();
                    this.hlActivity.commonLayout.removeView(this.prevCommonPage);
                    this.prevCommonPage = null;
                }
            } else if (this.prevCommonPage != null) {
                this.prevCommonPageEmpty = true;
                this.prevCommonPage.clean();
                this.hlActivity.commonLayout.removeView(this.prevCommonPage);
                this.prevCommonPage = null;
            }
        }
    }

    public void loadPage(String pageId) {
        this.viewPage = new ViewPage(this.viewPage.getContext(), null, null);
        PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.viewPage.getContext(), pageId);
        String linkPageID = pageEntity.getLinkPageID();
        if (BookSetting.IS_HOR_VER) {
            BookHelper.setupScreen(this.hlActivity);
            if (!BookSetting.IS_HOR && ((pageEntity.getType().equals(ViewPage.PAGE_TYPE_HOR) || pageEntity.getType().equals(ViewPage.PAGE_TYPE_NONE)) && !StringUtils.isEmpty(linkPageID))) {
                getSectionPagePosition(linkPageID);
                pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, linkPageID);
            }
            if (BookSetting.IS_HOR && pageEntity.getType().equals(ViewPage.PAGE_TYPE_VER) && !StringUtils.isEmpty(linkPageID)) {
                getSectionPagePosition(linkPageID);
                pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, linkPageID);
            }
        }
        if (StringUtils.isEmpty(pageEntity.beCoveredPageID)) {
            Log.d("zhaoq", "common page is hiden");
            this.hlActivity.commonLayout.removeView(this.commonPage);
            this.hlBookLayout.removeView(this.commonPage);
            if (this.commonPage != null) {
                this.commonPage.stop();
                this.commonPage.clean();
                this.commonPage = null;
            }
            this.commonPageID = "nothing";
            this.hlActivity.commonLayout.setVisibility(8);
        } else if (this.commonPage == null || !pageEntity.beCoveredPageID.equals(this.commonPageID)) {
            this.commonPage = new ViewPage(this.hlActivity, null, null);
            this.commonPage.isCommonPage = true;
            loadPage(pageEntity.beCoveredPageID, this.commonPage);
            this.commonPageID = pageEntity.beCoveredPageID;
            this.hlActivity.commonLayout.addView(this.commonPage, new ViewGroup.LayoutParams(-1, -1));
            this.hlActivity.commonLayout.setVisibility(8);
            if (this.commonPage.getEntity() == null) {
                this.hlActivity.commonLayout.removeView(this.commonPage);
                this.commonPage = null;
                this.commonPageID = "";
            }
        }
        this.viewPage.load(pageEntity);
    }

    public void loadPage(String pageId, ViewPage viewPage2) {
        if (!StringUtils.isEmpty(pageId)) {
            PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(viewPage2.getContext(), pageId);
            if (pageEntity != null) {
                String linkPageID = pageEntity.getLinkPageID();
                if (BookSetting.IS_HOR_VER) {
                    BookHelper.setupScreen(this.hlActivity);
                    if (!BookSetting.IS_HOR && ((pageEntity.getType().equals(ViewPage.PAGE_TYPE_HOR) || pageEntity.getType().equals(ViewPage.PAGE_TYPE_NONE)) && !StringUtils.isEmpty(linkPageID))) {
                        getSectionPagePosition(linkPageID);
                        pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, linkPageID);
                    }
                    if (BookSetting.IS_HOR && pageEntity.getType().equals(ViewPage.PAGE_TYPE_VER) && !StringUtils.isEmpty(linkPageID)) {
                        getSectionPagePosition(linkPageID);
                        pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.hlActivity, linkPageID);
                    }
                }
                viewPage2.load(pageEntity);
            }
        }
    }

    public PageEntity getPageEntityByID(String pageID) {
        return BookDecoder.getInstance().decodePageEntity(getHLActivity(), pageID);
    }

    public void flipSubPage(int tag) {
        if (this.subPageList != null && this.subPageList.size() > 0) {
            if (tag < 0) {
                flipSubPageUp();
            } else {
                flipSubPageDown();
            }
            setSubDefaultView();
        }
    }

    private void flipSubPageUp() {
        String pageID;
        if (!this.mainPageID.equals(this.viewPage.getEntity().getID())) {
            int currentPageIndex = this.subPageList.indexOf(this.viewPage.getEntity().getID());
            String str = "";
            if (currentPageIndex == 0) {
                pageID = this.mainPageID;
            } else {
                pageID = (String) this.subPageList.get(currentPageIndex - 1);
            }
            if (BookState.getInstance().setFlipState()) {
                pageSubViewPrepare(-1);
                loadPage(pageID);
                getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
                playSubPageFlipAnimation(1, 0);
                this.doFlipSubPage = true;
            }
        }
    }

    private void flipSubPageDown() {
        int currentPageIndex = this.subPageList.indexOf(this.viewPage.getEntity().getID());
        if (currentPageIndex != this.subPageList.size() - 1 && BookState.getInstance().setFlipState()) {
            pageSubViewPrepare(1);
            loadPage((String) this.subPageList.get(currentPageIndex + 1));
            getHLBookLayout().addView(this.viewPage, this.viewPage.getCurrentLayoutParams());
            playSubPageFlipAnimation(0, 1);
            this.doFlipSubPage = true;
        }
    }

    private void pageSubViewPrepare(int current) {
        flipSubPageAnimation(current);
        this.viewPage.stopVideo();
        this.viewPage.stop();
        this.viewPage.clean();
        getHLBookLayout().removeView(this.viewPage);
        this.viewPage.getRootView().invalidate();
    }

    private void flipSubPageAnimation(int currentIndex) {
        try {
            this.subPageViewFlip.setBitmap(this.hlBookLayout.getCurrentScreen());
            this.subPageViewFlip.setViewPage(this.hlBookLayout);
            this.subPageViewFlip.show();
        } catch (Exception e) {
            Log.e("hl", "bookcontroller  flipanimaiotn", e);
        }
    }

    private void playSubPageFlipAnimation(int currentIndex, int newIndex) {
        this.subPageViewFlip.play(currentIndex, newIndex, new ActionOnEnd() {
            public void doAction() {
                BookState.getInstance().restoreFlipState();
            }
        });
    }

    public boolean runBehavior(ComponentEntity entity, String eventName, String eventValue) {
        if (!BookSetting.IS_CLOSED && entity.behaviors != null) {
            Iterator i$ = entity.behaviors.iterator();
            while (i$.hasNext()) {
                BehaviorEntity e = (BehaviorEntity) i$.next();
                if (Behavior.BEHAVIOR_ON_LIST_ITEM_CLICK.equals(eventName)) {
                    try {
                        JSONObject json = new JSONObject(e.EventValue);
                        String key = json.getString("key");
                        String value = json.getString("value");
                        JSONObject map = HLTableViewComponent.curItemMap;
                        if (map == null) {
                            break;
                        } else if (map.opt(key).equals(value)) {
                            runBehavior(e);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else if ("BEHAVIOR_ON_TEXT_CHANGE_FAILED".equals(eventName)) {
                    if (eventName.equals(e.EventName) && !eventValue.equals(e.EventValue)) {
                        ViewCell viewCell = this.viewPage.getCellByID(e.FunctionObjectID);
                        if (viewCell == null && this.commonPage != null) {
                            viewCell = this.commonPage.getCellByID(e.FunctionObjectID);
                        }
                        if (viewCell == null || StringUtils.isEmpty(e.FunctionObjectID)) {
                            e.FunctionObjectID = entity.componentId;
                        }
                        runBehavior(e);
                    }
                } else if ("BEHAVIOR_ON_TEXT_CHANGE".equals(eventName)) {
                    if (eventName.equals(e.EventName) && eventValue.equals(e.EventValue)) {
                        ViewCell viewCell2 = this.viewPage.getCellByID(e.FunctionObjectID);
                        if (viewCell2 == null && this.commonPage != null) {
                            viewCell2 = this.commonPage.getCellByID(e.FunctionObjectID);
                        }
                        if (viewCell2 == null || StringUtils.isEmpty(e.FunctionObjectID)) {
                            e.FunctionObjectID = entity.componentId;
                        }
                        runBehavior(e);
                    }
                } else if (eventName.equals(e.EventName) && (StringUtils.isEmpty(eventValue) || eventValue.equals(e.EventValue))) {
                    ViewCell viewCell3 = this.viewPage.getCellByID(e.FunctionObjectID);
                    if (viewCell3 == null && this.commonPage != null) {
                        viewCell3 = this.commonPage.getCellByID(e.FunctionObjectID);
                    }
                    if (viewCell3 == null || StringUtils.isEmpty(e.FunctionObjectID)) {
                        e.FunctionObjectID = entity.componentId;
                    }
                    runBehavior(e);
                }
            }
        }
        return false;
    }

    public boolean runBehavior(ComponentEntity entity, String eventName) {
        if (BookSetting.IS_CLOSED) {
            return false;
        }
        return runBehavior(entity, eventName, null);
    }

    private boolean isMyPage(BehaviorEntity behavior) {
        if (StringUtils.isEmpty(this.mainPageID) || behavior.triggerPageID.equals(this.mainPageID) || behavior.triggerPageID.equals(this.commonPageID)) {
            return true;
        }
        if (this.subPageList.size() <= 0 || !this.subPageList.contains(behavior.triggerPageID)) {
            return false;
        }
        return true;
    }

    public void runBehavior(BehaviorEntity behavior) {
        if (isMyPage(behavior)) {
            BehaviorHelper.doBehavior(behavior);
        }
    }

    public int getSectionPagePosition(String pageID) {
        int j = 0;
        while (j < getBook().getSections().size()) {
            try {
                for (int i = 0; i < ((SectionEntity) getBook().getSections().get(j)).getPages().size(); i++) {
                    if (((String) ((SectionEntity) getBook().getSections().get(j)).getPages().get(i)).equals(pageID)) {
                        int res = i;
                        if (this.currendsectionindex != j) {
                            this.currendsectionindex = j;
                        }
                        this.section = (SectionEntity) getBook().getSections().get(j);
                        return res;
                    }
                }
                j++;
            } catch (Exception e) {
                Log.e("hl", " getSectionPagePosition ", e);
            }
        }
        return -1;
    }

    public boolean isPageExist(String pageID) {
        int j = 0;
        while (j < getBook().getSections().size()) {
            try {
                for (int i = 0; i < ((SectionEntity) getBook().getSections().get(j)).getPages().size(); i++) {
                    if (((String) ((SectionEntity) getBook().getSections().get(j)).getPages().get(i)).equals(pageID)) {
                        return true;
                    }
                }
                j++;
            } catch (Exception e) {
                Log.e("hl", " getSectionPagePosition ", e);
                return false;
            }
        }
        return false;
    }

    public int getSectionPagePositionForGallery(String pageID) {
        int j = 0;
        while (j < getBook().getSections().size()) {
            try {
                for (int i = 0; i < ((SectionEntity) getBook().getSections().get(j)).getPages().size(); i++) {
                    if (((String) ((SectionEntity) getBook().getSections().get(j)).getPages().get(i)).equals(pageID)) {
                        return i;
                    }
                }
                j++;
            } catch (Exception e) {
                Log.e("hl", " getSectionPagePosition ", e);
            }
        }
        return -1;
    }

    public void resume() {
        this.viewPage.resume();
    }

    public String getSectionPageIdByPosition(int position) {
        return (String) ((SectionEntity) getBook().getSections().get(this.currendsectionindex)).getPages().get(position);
    }

    /* access modifiers changed from: private */
    public void initBookState() {
        this.gallery.setVisibility(8);
        this.gallery.hideGalleryInfor();
        BookSetting.IS_AUTOPAGE = false;
    }

    public void registerButton(ImageButton pre, ImageButton next, ImageButton galleyButton2, ImageButton homeButton2) {
        this.galleyButton = galleyButton2;
        this.homeButton = homeButton2;
        if (BookSetting.FLIPCODE == 0) {
            if (pre != null) {
                this.preButton = pre;
            }
            if (next != null) {
                this.nextButton = next;
            }
        }
        if (this.preButton != null) {
            this.preButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BookController.this.flipPage(-1);
                }
            });
        }
        if (this.nextButton != null) {
            this.nextButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BookController.this.flipPage(1);
                }
            });
        }
        if (this.homeButton != null) {
            this.homeButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (BookController.this.book.getSections() != null) {
                        BookController.this.section = (SectionEntity) BookController.this.book.getSections().get(0);
                        BookController.this.initBookState();
                        if (BookController.this.section.pages != null) {
                            BookController.this.goHomePage();
                        }
                    }
                }
            });
        }
        if (this.galleyButton != null) {
            this.galleyButton.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    if (!BookController.this.isSettingGallery) {
                        try {
                            if (BookController.this.gallery.getVisibility() == 0) {
                                BookController.this.gallery.hideGalleryInfor();
                                BookController.this.viewPage.playVideo();
                                BookState.getInstance().setPlayViewPage();
                            } else if (!BookController.this.isSettingGallery) {
                                if (BookSetting.IS_HOR_VER) {
                                    BookController.this.getHLActivity().hideGalley();
                                    BookController.this.getHLActivity().addGallery();
                                }
                                BookController.this.gallery.setVisibility(0);
                                BookController.this.gallery.bringToFront();
                                BookController.this.setGarllary();
                                BookController.this.gallery.setSelection(BookController.this.getSectionPagePosition(BookController.this.mainPageID));
                                BookController.this.viewPage.stopVideo();
                            }
                        } catch (Exception e) {
                            e.fillInStackTrace();
                        }
                        if (BookController.this.hlActivity.waterStain != null) {
                            BookController.this.hlActivity.waterStain.bringToFront();
                        }
                    }
                }
            });
        }
        setDefaultView(getSectionPagePosition(this.mainPageID));
    }

    public void setGarllary() {
        try {
            if (!(this.gallery == null || this.gallery.getCurrentSectionIndex() == this.currendsectionindex)) {
                this.snapshots = getCurrentSnapshots();
                this.gallery.setSnapshots(this.snapshots);
                this.gallery.setCurrentSectionIndex(this.currendsectionindex);
            }
            if (this.snapshots != null) {
                this.gallery.setClickable(true);
                this.gallery.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        try {
                            BookController.this.gallery.setVisibility(8);
                            BookController.this.gallery.hideGalleryInfor();
                            String pageID = BookController.getInstance().getSectionPageIdByPosition(position);
                            if (!BookController.this.mainPageID.equals(pageID)) {
                                BookController.getInstance().playPageById(pageID);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.gallery.showGalleryInfor();
                this.gallery.invalidate();
                this.isSettingGallery = false;
            }
        } catch (Exception e) {
            Log.e("hl", "BookController setGarllary ", e);
        }
    }

    public ArrayList<String> getCurrentSnapshots() {
        this.snapshots = new ArrayList<>();
        Iterator i$ = ((SectionEntity) getBook().getSections().get(this.currendsectionindex)).getPages().iterator();
        while (i$.hasNext()) {
            this.snapshots.add(getSnapshotIdByPageId((String) i$.next()));
        }
        return this.snapshots;
    }

    public String getSnapshotIdByPageId(String pageID) {
        Iterator i$ = this.book.getSnapshots().iterator();
        while (i$.hasNext()) {
            SnapshotEntity entity = (SnapshotEntity) i$.next();
            if (entity.pageID.equals(pageID)) {
                return entity.f19id;
            }
        }
        return "";
    }

    public Book getBook() {
        return this.book;
    }

    public JSONArray getPushList() {
        return this.pushJsonList;
    }

    public void setPushList(JSONArray p) {
        this.pushJsonList = p;
    }

    public void setBook(Book book2) {
        this.book = book2;
    }

    public View getFlipView() {
        return this.flipView;
    }

    public void setFlipView(AbstractPageFlipView flipView2) {
        this.flipView = flipView2;
    }

    public ViewPage getViewPage() {
        return this.viewPage;
    }

    public void setViewPage(ViewPage viewPage2) {
        this.viewPage = viewPage2;
    }

    public AudioComponent getBackgroundMusic() {
        return this.backgroundMusic;
    }

    public WindowManager getWindwoManager() {
        return this.windwoManager;
    }

    public void setWindwoManager(WindowManager windwoManager2) {
        this.windwoManager = windwoManager2;
    }

    public RelativeLayout getHLBookLayout() {
        return this.hlBookLayout;
    }

    public int getCurrentsectionindex() {
        return this.currendsectionindex;
    }

    public void setHLBookLayout(RelativeLayout hlBookLayout2) {
        this.hlBookLayout = (HLRelativeLayout) hlBookLayout2;
    }

    public void setGallery(AbstractGalley g) {
        this.gallery = g;
    }

    public View getAdView() {
        return this.adView;
    }

    public void setAdView(View adView2) {
        this.adView = adView2;
    }

    public RelativeLayout getPdfMenuBarelativeLayout() {
        return this.pdfMenuBarelativeLayout;
    }

    public void setPdfMenuBarelativeLayout(RelativeLayout pdfMenuBarelativeLayout2) {
        this.pdfMenuBarelativeLayout = pdfMenuBarelativeLayout2;
    }

    public AbstractPageFlipView getSubPageViewFlip() {
        return this.subPageViewFlip;
    }

    public void setSubPageViewFlip(AbstractPageFlipView subPageViewFlip2) {
        this.subPageViewFlip = subPageViewFlip2;
    }

    public HLActivity getHLActivity() {
        return this.hlActivity;
    }

    public void setHLActivity(HLActivity hlActivity2) {
        this.hlActivity = hlActivity2;
    }

    public void openShelves() {
        this.hlActivity.progressHandler.sendEmptyMessage(0);
        new AsyncTask<String, String, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(String... params) {
                Book shelvesBook = BookDecoder.getInstance().decode(FileUtils.getInstance().getFileInputStream("book.xml"));
                BookSetting.buttons = shelvesBook.getButtons();
                ArrayList<SectionEntity> sections = shelvesBook.getSections();
                BookController.this.currendsectionindex = BookController.getInstance().book.getSections().size();
                BookController.getInstance().book.getSections().addAll(sections);
                BookController.getInstance().book.getSnapshots().addAll(shelvesBook.getSnapshots());
                String pageID = (String) ((SectionEntity) sections.get(0)).getPages().get(0);
                BookController.this.section = (SectionEntity) BookController.getInstance().book.getSections().get(BookController.this.currendsectionindex);
                BookDecoder.getInstance().initBookItemList();
                return pageID;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String pageID) {
                BookController.getInstance().changePageById(pageID);
                BookController.getInstance().hlActivity.relayoutGlobalButton();
            }
        }.execute(new String[]{""});
    }

    public void closeShelves() {
        this.hlActivity.progressHandler.sendEmptyMessage(0);
        new AsyncTask<String, String, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(String... params) {
                String lastPageID = BookController.this.section.lastPageID;
                String oladBookID = BookController.this.section.bookID;
                ArrayList<SectionEntity> delSectionList = new ArrayList<>();
                boolean isSetingLast = false;
                Iterator it = BookController.this.getBook().getSections().iterator();
                while (it.hasNext()) {
                    SectionEntity _section = (SectionEntity) it.next();
                    if (!isSetingLast) {
                        Iterator i$ = _section.getPages().iterator();
                        while (true) {
                            if (i$.hasNext()) {
                                if (((String) i$.next()).equals(lastPageID)) {
                                    BookController.this.section = _section;
                                    BookSetting.BOOK_PATH = BookController.this.section.bookPath;
                                    HLSetting.IsResourceSD = BookController.this.section.isResourceSD;
                                    BookSetting.IS_SHELVES_COMPONENT = BookController.this.section.isShelves;
                                    BookDecoder.getInstance().initBookItemList();
                                    isSetingLast = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    if (_section.bookID.equals(oladBookID)) {
                        delSectionList.add(_section);
                    }
                }
                ArrayList<SnapshotEntity> delSnapList = new ArrayList<>();
                Iterator i$2 = BookController.this.getBook().getSnapshots().iterator();
                while (i$2.hasNext()) {
                    SnapshotEntity snap = (SnapshotEntity) i$2.next();
                    if (snap.bookID.equals(oladBookID)) {
                        delSnapList.add(snap);
                    }
                }
                BookController.this.getBook().getSections().removeAll(delSectionList);
                BookController.this.getBook().getSnapshots().removeAll(delSnapList);
                System.gc();
                return lastPageID;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String lastPageID) {
                BookController.getInstance().hlActivity.relayoutGlobalButton();
                BookController.getInstance().changePageById(lastPageID);
            }
        }.execute(new String[]{""});
    }

    /* access modifiers changed from: private */
    public void setOrien() {
        PageEntity pageEntity = this.mainViewPage.getEntity();
        String linkPageID = pageEntity.getLinkPageID();
        if ((pageEntity.getType().equals(ViewPage.PAGE_TYPE_HOR) || pageEntity.getType().equals(ViewPage.PAGE_TYPE_NONE)) && StringUtils.isEmpty(linkPageID)) {
            this.hlActivity.setRequestedOrientation(0);
            BookSetting.IS_HOR = true;
        } else if (!pageEntity.getType().equals(ViewPage.PAGE_TYPE_VER) || !StringUtils.isEmpty(linkPageID)) {
            this.hlActivity.setRequestedOrientation(4);
        } else {
            BookSetting.IS_HOR = false;
            this.hlActivity.setRequestedOrientation(1);
        }
    }

    public void revokeCommonPage() {
        for (int index = 0; index < this.hlBookLayout.getChildCount(); index++) {
            if (this.hlBookLayout.getChildAt(index) == this.commonPage) {
                this.hlBookLayout.removeView(this.commonPage);
                this.hlActivity.commonLayout.addView(this.commonPage);
                this.hlActivity.commonLayout.setVisibility(8);
                return;
            }
        }
    }

    public Bitmap getSnapshot(View v) {
        int width;
        int height;
        Bitmap bitmap;
        this.mainViewPage.destroyDrawingCache();
        v.setBackgroundColor(-1);
        if (this.mainViewPage.getEntity().isCashSnapshot()) {
            return getInstance().getSnapShotCashImage(this.mainViewPage.getEntity());
        }
        if (BookSetting.FIX_SIZE) {
            width = BookSetting.INIT_SCREEN_WIDTH;
            height = BookSetting.INIT_SCREEN_HEIGHT;
        } else {
            width = v.getWidth();
            height = v.getHeight();
        }
        try {
            bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
            try {
                v.draw(new Canvas(bitmap));
                return bitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (OutOfMemoryError e) {
            bitmap = Bitmap.createBitmap(width, height, Config.ALPHA_8);
        }
    }

    public ArrayList<String> getPageHistory() {
        if (this.pageHistory == null) {
            this.pageHistory = new ArrayList<>();
        }
        return this.pageHistory;
    }
}
