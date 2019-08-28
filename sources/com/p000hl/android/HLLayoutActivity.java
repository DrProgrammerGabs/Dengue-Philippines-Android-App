package com.p000hl.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.p000hl.android.book.entity.Book;
import com.p000hl.android.book.entity.ButtonEntity;
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.LogHelper;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ImageUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.core.utils.WebUtils;
import com.p000hl.android.view.ViewPage;
import com.p000hl.android.view.component.IndesignBottom;
import com.p000hl.android.view.component.IndesignBottom.BottomNavListenner;
import com.p000hl.android.view.component.IndesignMiddle;
import com.p000hl.android.view.component.IndesignUP;
import com.p000hl.android.view.component.IndesignUP.NavMenuListenner;
import com.p000hl.android.view.component.MYSearchView;
import com.p000hl.android.view.component.MarkView4NavMenu;
import com.p000hl.android.view.component.bookmark.MarkViewLayout;
import com.p000hl.android.view.gallary.GalleyHelper;
import com.p000hl.android.view.gallary.base.AbstractGalley;
import com.p000hl.android.view.layout.HLRelativeLayout;
import com.p000hl.android.view.pageflip.AbstractPageFlipView;
import com.p000hl.android.view.pageflip.PageFlipVerticleView;
import com.p000hl.android.view.pageflip.PageFlipView;
import com.p000hl.android.view.pageflip.PageWidgetNew;
import com.p000hl.callback.Action;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.hl.android.HLLayoutActivity */
public class HLLayoutActivity extends Activity {
    public static int MINGLOBEALBUTTONWIDTH = 30;
    private static final int NAVMENU_ID = 65552;
    private int BOTTOM_NAV_ID = 65553;
    private int MIDDLE_NAV_ID = 65554;
    public AbstractPageFlipView absPageView = null;
    public View adViewLayout;
    private ImageButton backButton;
    private BaseAdapter basAdapter;
    public RelativeLayout commonLayout;
    public HLRelativeLayout contentLayout;
    private LayoutParams contentLp = new LayoutParams(-1, -1);
    public RelativeLayout coverLayout;
    private LayoutParams coverLp = new LayoutParams(-1, -1);
    private TextView dummyVideo;
    private FrameLayout frameLayout;
    protected AbstractGalley gallery;
    protected int gallery_ID = 9910021;
    protected ImageButton galleyButton;
    protected ButtonEntity galleyEntity;
    protected ImageButton homeButton;
    protected int homeButton_h_ID = 9910002;
    protected int homeButton_v_ID = 9910001;
    protected ButtonEntity homeEntity;
    protected boolean isShelves = false;
    protected ImageButton leftButton;
    protected ButtonEntity leftEntity;
    /* access modifiers changed from: private */
    public ListView listView4ShowSnapshots;
    /* access modifiers changed from: private */
    public IndesignBottom mBottomNav;
    /* access modifiers changed from: private */
    public IndesignMiddle mMiddleNav;
    private IndesignUP mUPNav;
    WindowManager mWindowManager;
    public FrameLayout mainLayout;
    private MarkViewLayout markLayout;
    /* access modifiers changed from: private */
    public MarkView4NavMenu markView4NavMenu;
    protected Action preLoadAction;
    protected Action prePlayAction;
    protected Action preShowViewAction;
    protected ImageButton pushButton;
    Handler pushHandler = new Handler() {
        public void handleMessage(Message msg) {
            HLLayoutActivity.this.setInfor((JSONObject) msg.obj);
        }
    };
    public RelativeLayout pushLayout;
    public LinearLayout pushListLayout;
    public WindowManager.LayoutParams pushListParams;
    public WindowManager.LayoutParams pushParams;
    protected Action recycleAction;
    public RelativeLayout relpushListLayout;
    protected ImageButton rightButton;
    protected ButtonEntity rightEntity;
    RelativeLayout.LayoutParams rlp;
    /* access modifiers changed from: private */
    public MYSearchView searchView;
    protected ImageButton vergalleyButton;
    protected ButtonEntity vergalleyEntity;
    protected ImageButton verhomeButton;
    protected ButtonEntity verhomeEntity;
    protected ImageButton verleftButton;
    protected ButtonEntity verleftEntity;
    protected ImageButton verrightButton;
    protected ButtonEntity verrightEntity;
    public WindowManager.LayoutParams wmParams;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.frameLayout = new MFrameLayout(this);
        this.pushLayout = new RelativeLayout(this);
        this.pushLayout.setBackgroundColor(-16777216);
        this.pushLayout.setVisibility(8);
        this.pushLayout.setPadding(10, 10, 10, 10);
        this.pushLayout.setAlpha(0.8f);
        this.relpushListLayout = new RelativeLayout(this);
        this.relpushListLayout.setVisibility(8);
        this.relpushListLayout.setBackgroundColor(-16777216);
        this.relpushListLayout.setPadding(10, 10, 10, 10);
        this.relpushListLayout.setAlpha(0.8f);
        ImageView imgClose = new ImageView(this);
        imgClose.setImageResource(C0048R.drawable.close_button);
        imgClose.setId(C0048R.C0049id.push_list_close);
        RelativeLayout.LayoutParams lpClose = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, 40.0f), ScreenUtils.dip2px(this, 40.0f));
        lpClose.addRule(11);
        lpClose.addRule(10);
        this.relpushListLayout.addView(imgClose, lpClose);
        imgClose.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.relpushListLayout.setVisibility(8);
            }
        });
        ImageView imgClear = new ImageView(this);
        imgClear.setImageResource(C0048R.drawable.trash_button);
        RelativeLayout.LayoutParams lpClear = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, 50.0f), ScreenUtils.dip2px(this, 50.0f));
        lpClear.addRule(9);
        lpClear.addRule(10);
        this.relpushListLayout.addView(imgClear, lpClear);
        imgClear.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.pushListLayout.removeAllViews();
            }
        });
        this.pushListLayout = new LinearLayout(this);
        this.pushListLayout.setBackgroundColor(-16777216);
        this.pushListLayout.setPadding(10, 10, 10, 10);
        this.pushListLayout.setAlpha(0.8f);
        RelativeLayout.LayoutParams listLp = new RelativeLayout.LayoutParams(-1, -1);
        listLp.addRule(3, C0048R.C0049id.push_list_close);
        this.relpushListLayout.addView(this.pushListLayout, listLp);
        this.mainLayout = new FrameLayout(this);
        this.contentLayout = new HLRelativeLayout(this);
        this.contentLayout.setDrawingCacheEnabled(true);
        this.contentLayout.buildDrawingCache();
        this.mainLayout.addView(this.contentLayout, this.contentLp);
        this.commonLayout = new RelativeLayout(this);
        this.mainLayout.addView(this.commonLayout, this.contentLp);
        this.coverLayout = new RelativeLayout(this);
        this.contentLp.gravity = 17;
        this.frameLayout.addView(this.mainLayout, this.contentLp);
        this.wmParams = new WindowManager.LayoutParams();
        this.mWindowManager = getWindowManager();
        this.wmParams.type = 2;
        this.wmParams.format = 1;
        this.wmParams.flags = 131080;
        this.wmParams.gravity = 51;
        this.wmParams.x = 0;
        this.wmParams.y = 0;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
        this.pushParams = new WindowManager.LayoutParams();
        this.pushParams.type = 2;
        this.pushParams.format = 1;
        this.pushParams.flags = 131080;
        this.pushParams.gravity = 49;
        this.pushParams.width = -1;
        this.pushParams.height = -2;
        this.pushListParams = new WindowManager.LayoutParams();
        this.pushListParams.type = 2;
        this.pushListParams.format = 1;
        this.pushListParams.flags = 131080;
        this.pushListParams.gravity = 49;
        this.pushListParams.width = -1;
        this.pushListParams.height = -1;
        this.frameLayout.addView(new SurfaceView(this), new ViewGroup.LayoutParams(0, 0));
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        setContentView(this.frameLayout);
        this.dummyVideo = new TextView(this);
        this.dummyVideo.setBackgroundColor(-16777216);
        this.dummyVideo.setVisibility(8);
        this.rlp = new RelativeLayout.LayoutParams(100, 100);
        this.coverLayout.addView(this.dummyVideo, this.rlp);
    }

    public void printSize() {
        int screenWidth = this.frameLayout.getWidth();
        Log.d("zhaoq", "actual size is " + screenWidth + "*" + this.frameLayout.getHeight());
        int screenWidth2 = ScreenUtils.getScreenWidth(this);
        Log.d("zhaoq", "screen size is " + screenWidth2 + "*" + ScreenUtils.getScreenHeight(this));
    }

    public void layout(Book book, int orientation) {
        int screenWidth;
        int screenHeight;
        int screenWidth2 = this.frameLayout.getWidth();
        int screenHeight2 = this.frameLayout.getHeight();
        int bookWidth = book.getBookInfo().bookWidth;
        int bookHeight = book.getBookInfo().bookHeight;
        if (BookSetting.IS_HOR_VER && orientation == 1) {
            int tmp = bookWidth;
            bookWidth = bookHeight;
            bookHeight = tmp;
        }
        int tmpW = screenWidth2;
        int tmpH = screenHeight2;
        if (BookSetting.IS_HOR) {
            screenWidth = Math.max(tmpW, tmpH);
            screenHeight = Math.min(tmpW, tmpH);
        } else {
            screenWidth = Math.min(tmpW, tmpH);
            screenHeight = Math.max(tmpW, tmpH);
        }
        if (HLSetting.FitScreen) {
            BookSetting.BOOK_WIDTH = screenWidth;
            BookSetting.BOOK_HEIGHT = screenHeight;
            if (!BookSetting.FITSCREEN_TENSILE) {
                int bwidth = Math.max(bookWidth, bookHeight);
                int bheight = Math.min(bookWidth, bookHeight);
                if (!BookSetting.IS_HOR) {
                    bwidth = Math.min(bookWidth, bookHeight);
                    bheight = Math.max(bookWidth, bookHeight);
                }
                float ratio = ((float) screenWidth) / ((float) bwidth);
                if (((float) BookSetting.BOOK_HEIGHT) > ((float) bheight) * ratio) {
                    BookSetting.BOOK_HEIGHT = (int) (((float) bheight) * ratio);
                }
            }
            BookSetting.BOOK_WIDTH4CALCULATE = (float) BookSetting.BOOK_WIDTH;
            BookSetting.BOOK_HEIGHT4CALCULATE = (float) BookSetting.BOOK_HEIGHT;
        } else {
            int bwidth2 = Math.max(bookWidth, bookHeight);
            int bheight2 = Math.min(bookWidth, bookHeight);
            if (!BookSetting.IS_HOR) {
                bwidth2 = Math.min(bookWidth, bookHeight);
                bheight2 = Math.max(bookWidth, bookHeight);
            }
            float widthRatio = ((float) bwidth2) / ((float) screenWidth);
            float heightRatio = ((float) bheight2) / ((float) screenHeight);
            if (widthRatio > heightRatio) {
                BookSetting.BOOK_WIDTH4CALCULATE = (float) screenWidth;
                BookSetting.BOOK_WIDTH = (int) BookSetting.BOOK_WIDTH4CALCULATE;
                BookSetting.BOOK_HEIGHT4CALCULATE = ((float) bheight2) / widthRatio;
                BookSetting.BOOK_HEIGHT = (int) BookSetting.BOOK_HEIGHT4CALCULATE;
            } else {
                BookSetting.BOOK_HEIGHT4CALCULATE = (float) screenHeight;
                BookSetting.BOOK_HEIGHT = (int) BookSetting.BOOK_HEIGHT4CALCULATE;
                BookSetting.BOOK_WIDTH4CALCULATE = ((float) bwidth2) / heightRatio;
                BookSetting.BOOK_WIDTH = (int) BookSetting.BOOK_WIDTH4CALCULATE;
            }
        }
        this.contentLp.width = BookSetting.BOOK_WIDTH;
        this.contentLp.height = BookSetting.BOOK_HEIGHT;
        this.frameLayout.requestLayout();
        this.contentLayout.requestLayout();
        ViewPage page = BookController.getInstance().getViewPage();
        if (page != null) {
            page.setLayoutParams(page.getCurrentLayoutParams());
            page.pageHeight = (int) (page.getEntity().getHeight() * BookSetting.PAGE_RATIO);
            if (BookSetting.FITSCREEN_TENSILE) {
                page.pageHeight = (int) (page.getEntity().getHeight() * BookSetting.PAGE_RATIOY);
            }
            page.requestLayout();
        }
        this.coverLayout.requestLayout();
        relayoutGlobalButton();
        LogHelper.trace("BookSetting.BOOK_HEIGHT", BookSetting.BOOK_HEIGHT + "", false);
        this.pushLayout.getLayoutParams().width = BookSetting.BOOK_WIDTH;
        this.relpushListLayout.getLayoutParams().width = BookSetting.BOOK_WIDTH;
    }

    public void updateCoverPosition() {
        if ("addToWindowManager".equals(this.coverLayout.getTag())) {
            this.wmParams.x = (this.frameLayout.getWidth() - BookSetting.BOOK_WIDTH) / 2;
            this.wmParams.y = (this.frameLayout.getHeight() - BookSetting.BOOK_HEIGHT) / 2;
            this.wmParams.width = BookSetting.BOOK_WIDTH;
            this.wmParams.height = BookSetting.BOOK_HEIGHT;
            this.pushParams.x = (this.pushLayout.getWidth() - BookSetting.BOOK_WIDTH) / 2;
            this.pushParams.width = BookSetting.BOOK_WIDTH;
            this.pushListParams.x = (this.relpushListLayout.getWidth() - BookSetting.BOOK_WIDTH) / 2;
            this.pushListParams.width = BookSetting.BOOK_WIDTH;
            this.mWindowManager.updateViewLayout(this.pushLayout, this.pushParams);
            this.mWindowManager.updateViewLayout(this.relpushListLayout, this.pushListParams);
            this.mWindowManager.updateViewLayout(this.coverLayout, this.wmParams);
        }
    }

    public void layout(Book book) {
        int screenWidth;
        int screenHeight;
        int screenWidth2 = this.frameLayout.getWidth();
        int screenHeight2 = this.frameLayout.getHeight();
        int bookWidth = book.getBookInfo().bookWidth;
        int bookHeight = book.getBookInfo().bookHeight;
        int tmpW = screenWidth2;
        int tmpH = screenHeight2;
        if (BookSetting.IS_HOR) {
            screenWidth = Math.max(tmpW, tmpH);
            screenHeight = Math.min(tmpW, tmpH);
        } else {
            screenWidth = Math.min(tmpW, tmpH);
            screenHeight = Math.max(tmpW, tmpH);
        }
        if (HLSetting.FitScreen) {
            BookSetting.BOOK_WIDTH = screenWidth;
            BookSetting.BOOK_HEIGHT = screenHeight;
            if (!BookSetting.FITSCREEN_TENSILE) {
                int bwidth = Math.max(bookWidth, bookHeight);
                int bheight = Math.min(bookWidth, bookHeight);
                if (!BookSetting.IS_HOR) {
                    bwidth = Math.min(bookWidth, bookHeight);
                    bheight = Math.max(bookWidth, bookHeight);
                }
                float ratio = ((float) screenWidth) / ((float) bwidth);
                if (((float) BookSetting.BOOK_HEIGHT) > ((float) bheight) * ratio) {
                    BookSetting.BOOK_HEIGHT = (int) (((float) bheight) * ratio);
                }
            }
            BookSetting.BOOK_WIDTH4CALCULATE = (float) BookSetting.BOOK_WIDTH;
            BookSetting.BOOK_HEIGHT4CALCULATE = (float) BookSetting.BOOK_HEIGHT;
        } else {
            int bwidth2 = Math.max(bookWidth, bookHeight);
            int bheight2 = Math.min(bookWidth, bookHeight);
            if (!BookSetting.IS_HOR) {
                bwidth2 = Math.min(bookWidth, bookHeight);
                bheight2 = Math.max(bookWidth, bookHeight);
            }
            float widthRatio = ((float) bwidth2) / ((float) screenWidth);
            float heightRatio = ((float) bheight2) / ((float) screenHeight);
            if (widthRatio > heightRatio) {
                BookSetting.BOOK_WIDTH4CALCULATE = (float) screenWidth;
                BookSetting.BOOK_WIDTH = (int) BookSetting.BOOK_WIDTH4CALCULATE;
                BookSetting.BOOK_HEIGHT4CALCULATE = ((float) bheight2) / widthRatio;
                BookSetting.BOOK_HEIGHT = (int) BookSetting.BOOK_HEIGHT4CALCULATE;
            } else {
                BookSetting.BOOK_HEIGHT4CALCULATE = (float) screenHeight;
                BookSetting.BOOK_HEIGHT = (int) BookSetting.BOOK_HEIGHT4CALCULATE;
                BookSetting.BOOK_WIDTH4CALCULATE = ((float) bwidth2) / heightRatio;
                BookSetting.BOOK_WIDTH = (int) BookSetting.BOOK_WIDTH4CALCULATE;
            }
        }
        this.contentLp.width = BookSetting.BOOK_WIDTH;
        this.contentLp.height = BookSetting.BOOK_HEIGHT;
        this.frameLayout.requestLayout();
        this.contentLayout.requestLayout();
        ViewPage page = BookController.getInstance().getViewPage();
        if (page != null) {
            page.setLayoutParams(page.getCurrentLayoutParams());
            page.pageHeight = (int) (page.getEntity().getHeight() * BookSetting.PAGE_RATIO);
            if (BookSetting.FITSCREEN_TENSILE) {
                page.pageHeight = (int) (page.getEntity().getHeight() * BookSetting.PAGE_RATIOY);
            }
            page.requestLayout();
        }
        ViewPage prePage = BookController.getInstance().preViewPage;
        if (prePage != null) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.width = BookSetting.BOOK_WIDTH;
            lp.height = (int) ScreenUtils.getHorScreenValue(prePage.getEntity().getHeight());
            prePage.setLayoutParams(lp);
            prePage.setX(BookController.getInstance().getViewPage().getX() - ((float) BookSetting.BOOK_WIDTH));
            prePage.requestLayout();
        }
        ViewPage nextPage = BookController.getInstance().nextViewPage;
        if (nextPage != null) {
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(-1, -1);
            lp2.width = BookSetting.BOOK_WIDTH;
            lp2.height = (int) ScreenUtils.getHorScreenValue(nextPage.getEntity().getHeight());
            nextPage.setLayoutParams(lp2);
            nextPage.setX(BookController.getInstance().getViewPage().getX() + ((float) BookSetting.BOOK_WIDTH));
            nextPage.requestLayout();
        }
        this.coverLayout.requestLayout();
        relayoutGlobalButton();
        LogHelper.trace("BookSetting.BOOK_HEIGHT", BookSetting.BOOK_HEIGHT + "", false);
    }

    public void showMark() {
        initMarkView();
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) (-BookSetting.SCREEN_HEIGHT), 0.0f);
        animation.setDuration(300);
        animation.setRepeatCount(0);
        this.markLayout.setAnimation(animation);
        animation.startNow();
        this.markLayout.setVisibility(0);
    }

    public void initPushView() {
        TextView txtTitle = new TextView(this);
        txtTitle.setTextColor(-1);
        txtTitle.setId(C0048R.C0049id.push_txt_title);
        TextView txtContent = new TextView(this);
        txtContent.setId(C0048R.C0049id.push_txt_content);
        txtContent.setTextColor(-1);
        TextView txtTime = new TextView(this);
        txtTime.setId(C0048R.C0049id.push_txt_time);
        txtTime.setTextColor(Color.rgb(152, 152, 152));
        txtTime.setGravity(5);
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(-2, -2);
        lpTitle.addRule(10);
        lpTitle.addRule(9);
        txtTitle.setTextSize(2, 14.0f);
        txtTitle.setText("我是title");
        this.pushLayout.addView(txtTitle, lpTitle);
        RelativeLayout.LayoutParams lpContent = new RelativeLayout.LayoutParams(-1, -2);
        lpContent.addRule(3, C0048R.C0049id.push_txt_title);
        lpContent.addRule(9);
        txtContent.setTextSize(2, 14.0f);
        txtContent.setText("我是content");
        this.pushLayout.addView(txtContent, lpContent);
        RelativeLayout.LayoutParams lpTime = new RelativeLayout.LayoutParams(-2, -2);
        lpTime.addRule(3, C0048R.C0049id.push_txt_content);
        lpTime.addRule(11);
        txtTime.setText("我是time");
        txtTime.setTextSize(2, 12.0f);
        this.pushLayout.addView(txtTime, lpTime);
        ImageView img = new ImageView(this);
        img.setImageResource(C0048R.drawable.close_select);
        RelativeLayout.LayoutParams lpClose = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, 20.0f), ScreenUtils.dip2px(this, 20.0f));
        lpClose.addRule(11);
        lpTitle.addRule(10);
        this.pushLayout.addView(img, lpClose);
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.pushLayout.setVisibility(8);
            }
        });
        this.pushLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) arg0.getTag())));
            }
        });
    }

    public RelativeLayout getPushItemView(JSONObject pushInfor, int id) {
        RelativeLayout layItem = new RelativeLayout(this);
        layItem.setId(id);
        TextView txtTitle = new TextView(this);
        txtTitle.setTextColor(-1);
        txtTitle.setId(C0048R.C0049id.push_txt_title);
        TextView txtContent = new TextView(this);
        txtContent.setId(C0048R.C0049id.push_txt_content);
        txtContent.setTextColor(-1);
        TextView txtTime = new TextView(this);
        txtTime.setId(C0048R.C0049id.push_txt_time);
        txtTime.setTextColor(Color.rgb(152, 152, 152));
        txtTime.setGravity(5);
        ImageView img = new ImageView(this);
        img.setImageResource(C0048R.drawable.close_button);
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(-2, -2);
        lpTitle.addRule(10);
        lpTitle.addRule(9);
        txtTitle.setTextSize(2, 14.0f);
        txtTitle.setText(pushInfor.optString("dttile"));
        layItem.addView(txtTitle, lpTitle);
        RelativeLayout.LayoutParams lpContent = new RelativeLayout.LayoutParams(-1, -2);
        lpContent.addRule(3, C0048R.C0049id.push_txt_title);
        lpContent.addRule(9);
        txtContent.setTextSize(2, 14.0f);
        txtContent.setText(pushInfor.optString("dtcont"));
        layItem.addView(txtContent, lpContent);
        RelativeLayout.LayoutParams lpTime = new RelativeLayout.LayoutParams(-2, -2);
        lpTime.addRule(3, C0048R.C0049id.push_txt_content);
        lpTime.addRule(11);
        txtTime.setText(pushInfor.optString("crdate"));
        txtTime.setTextSize(2, 12.0f);
        layItem.addView(txtTime, lpTime);
        RelativeLayout.LayoutParams lpClose = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, 40.0f), ScreenUtils.dip2px(this, 40.0f));
        lpClose.addRule(11);
        lpTitle.addRule(10);
        layItem.addView(img, lpClose);
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.pushListLayout.findViewById(((Integer) arg0.getTag()).intValue()).setVisibility(8);
            }
        });
        img.setTag(Integer.valueOf(id));
        layItem.setTag(pushInfor.optString("url"));
        layItem.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) arg0.getTag())));
            }
        });
        return layItem;
    }

    public void setInfor(JSONObject pushInfor) {
        if (pushInfor == null) {
            this.pushLayout.setVisibility(8);
            return;
        }
        this.pushLayout.setVisibility(0);
        ((TextView) this.pushLayout.findViewById(C0048R.C0049id.push_txt_title)).setText(pushInfor.optString("dttile"));
        ((TextView) this.pushLayout.findViewById(C0048R.C0049id.push_txt_content)).setText(pushInfor.optString("dtcont"));
        ((TextView) this.pushLayout.findViewById(C0048R.C0049id.push_txt_time)).setText(pushInfor.optString("crdate"));
        this.pushLayout.setTag(pushInfor.optString("url"));
        this.pushLayout.bringToFront();
        this.frameLayout.bringChildToFront(this.pushLayout);
    }

    public void refreshMark() {
        if (this.markLayout != null) {
            this.markLayout.refresh();
        }
        if (this.markView4NavMenu != null) {
            this.markView4NavMenu.refresh();
        }
    }

    public void refreshSnapshots() {
        if (this.basAdapter != null) {
            this.basAdapter.notifyDataSetChanged();
        }
    }

    private void initMarkView() {
        if (this.markLayout == null) {
            this.markLayout = new MarkViewLayout(this);
            int markViewHeight = BookSetting.SCREEN_HEIGHT / 2;
            if (markViewHeight > 500) {
                markViewHeight = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            }
            RelativeLayout.LayoutParams markViewLp = new RelativeLayout.LayoutParams(260, markViewHeight);
            markViewLp.addRule(11);
            markViewLp.addRule(15);
            this.coverLayout.addView(this.markLayout, markViewLp);
        }
    }

    public void setVideoCover(int x, int y, int w, int h) {
        this.rlp.leftMargin = x;
        this.rlp.topMargin = y;
        this.rlp.width = w;
        this.rlp.height = h;
    }

    private void setAD(RelativeLayout aa) {
    }

    private void createPushButton() {
        this.pushButton = new ImageButton(this);
        this.pushButton.setBackgroundResource(C0048R.drawable.btn_gopush_selector);
        int btnWidth = (int) ScreenUtils.getHorScreenValue(80.0f);
        RelativeLayout.LayoutParams backLp = new RelativeLayout.LayoutParams(btnWidth, btnWidth);
        backLp.addRule(10);
        if (this.coverLayout.findViewById(this.gallery_ID) != null) {
            backLp.addRule(11);
            backLp.rightMargin = (int) ScreenUtils.getHorScreenValue(80.0f);
        } else {
            backLp.addRule(11);
        }
        this.coverLayout.addView(this.pushButton, backLp);
        this.pushButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HLLayoutActivity.this.showPushList();
            }
        });
    }

    public void showPushList() {
        this.relpushListLayout.setVisibility(0);
        this.relpushListLayout.bringToFront();
    }

    private void createGlobaButton(ArrayList<ButtonEntity> buttonList) {
        if (buttonList != null && buttonList.size() != 0) {
            Iterator i$ = buttonList.iterator();
            while (i$.hasNext()) {
                ButtonEntity buttonEntity = (ButtonEntity) i$.next();
                if (buttonEntity.getWidth() != 0) {
                    String btnType = buttonEntity.getType();
                    if (ButtonEntity.OPEN_NAVIGATE_BTN.equals(btnType)) {
                        if (!BookSetting.IS_NO_NAVIGATION) {
                            this.galleyEntity = buttonEntity;
                            this.galleyButton = drawGlobalButton(buttonEntity, C0048R.drawable.btngarally);
                            this.coverLayout.addView(this.galleyButton);
                        }
                    } else if (ButtonEntity.HOME_PAGE_BTN.equals(btnType)) {
                        this.homeEntity = buttonEntity;
                        this.homeButton = drawGlobalButton(buttonEntity, C0048R.drawable.home);
                        this.homeButton.setId(this.homeButton_h_ID);
                        this.coverLayout.addView(this.homeButton);
                    } else if (ButtonEntity.PRE_PAGE_BTN.equals(btnType)) {
                        this.leftEntity = buttonEntity;
                        this.leftButton = drawGlobalButton(buttonEntity, C0048R.drawable.left);
                        this.coverLayout.addView(this.leftButton);
                    } else if (ButtonEntity.NEXT_PAGE_BTN.equals(btnType)) {
                        this.rightEntity = buttonEntity;
                        this.rightButton = drawGlobalButton(buttonEntity, C0048R.drawable.right);
                        this.coverLayout.addView(this.rightButton);
                    } else if (ButtonEntity.VER_OPEN_NAVIGATE_BTN.equals(btnType)) {
                        if (!BookSetting.IS_NO_NAVIGATION) {
                            this.vergalleyEntity = buttonEntity;
                            this.vergalleyButton = drawGlobalButton(buttonEntity, C0048R.drawable.btngarally);
                            this.coverLayout.addView(this.vergalleyButton);
                        }
                    } else if (ButtonEntity.VER_HOME_PAGE_BTN.equals(btnType)) {
                        this.verhomeEntity = buttonEntity;
                        this.verhomeButton = drawGlobalButton(buttonEntity, C0048R.drawable.home);
                        this.verhomeButton.setId(this.homeButton_v_ID);
                        this.coverLayout.addView(this.verhomeButton);
                    } else if (ButtonEntity.VER_PRE_PAGE_BTN.equals(btnType)) {
                        this.verleftEntity = buttonEntity;
                        this.verleftButton = drawGlobalButton(buttonEntity, C0048R.drawable.left);
                        this.coverLayout.addView(this.verleftButton);
                    } else if (ButtonEntity.VER_NEXT_PAGE_BTN.equals(btnType)) {
                        this.verrightEntity = buttonEntity;
                        this.verrightButton = drawGlobalButton(buttonEntity, C0048R.drawable.right);
                        this.coverLayout.addView(this.verrightButton);
                    }
                }
            }
            if (BookSetting.IS_SHELVES) {
                if (!HLSetting.FitScreen && ((int) (((float) ScreenUtils.getScreenHeight(this)) - ScreenUtils.getVerScreenValue(BookController.getInstance().getViewPage().getEntity().getHeight()))) / 2 < 0) {
                }
                this.backButton = new ImageButton(this);
                this.backButton.setBackgroundResource(C0048R.drawable.back);
                int btnWidth = (int) ScreenUtils.getHorScreenValue(45.0f);
                if (btnWidth < (BookSetting.BOOK_WIDTH * 45) / 1024) {
                    btnWidth = (BookSetting.BOOK_WIDTH * 45) / 1024;
                }
                RelativeLayout.LayoutParams backLp = new RelativeLayout.LayoutParams(btnWidth, btnWidth);
                backLp.addRule(10);
                backLp.addRule(14);
                this.coverLayout.addView(this.backButton, backLp);
                this.backButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        HLLayoutActivity.this.onBackPressed();
                    }
                });
            }
            relayoutGlobalButton();
        }
    }

    private void hidGlobalButton(ImageButton btn) {
        if (btn != null) {
            btn.setVisibility(8);
        }
    }

    public void relayoutHorGlobalButton() {
        hidGlobalButton(this.rightButton);
        hidGlobalButton(this.leftButton);
        hidGlobalButton(this.homeButton);
        hidGlobalButton(this.galleyButton);
        hidGlobalButton(this.verrightButton);
        hidGlobalButton(this.verleftButton);
        hidGlobalButton(this.verhomeButton);
        hidGlobalButton(this.vergalleyButton);
        if (BookSetting.FLIPCODE == 0) {
            setGlobalLayoutParams(this.rightEntity, this.rightButton);
        }
        if (BookSetting.FLIPCODE == 0) {
            setGlobalLayoutParams(this.leftEntity, this.leftButton);
        }
        setGlobalLayoutParams(this.homeEntity, this.homeButton);
        setGlobalLayoutParams(this.galleyEntity, this.galleyButton);
        BookController.getInstance().registerButton(this.leftButton, this.rightButton, this.galleyButton, this.homeButton);
    }

    public void relayoutVerGlobalButton() {
        hidGlobalButton(this.rightButton);
        hidGlobalButton(this.leftButton);
        hidGlobalButton(this.homeButton);
        hidGlobalButton(this.galleyButton);
        hidGlobalButton(this.verrightButton);
        hidGlobalButton(this.verleftButton);
        hidGlobalButton(this.verhomeButton);
        hidGlobalButton(this.vergalleyButton);
        if (BookSetting.FLIPCODE == 0) {
            setGlobalLayoutParams(this.verrightEntity, this.verrightButton);
        }
        if (BookSetting.FLIPCODE == 0) {
            setGlobalLayoutParams(this.verleftEntity, this.verleftButton);
        }
        setGlobalLayoutParams(this.verhomeEntity, this.verhomeButton);
        setGlobalLayoutParams(this.vergalleyEntity, this.vergalleyButton);
        BookController.getInstance().registerButton(this.verleftButton, this.verrightButton, this.vergalleyButton, this.verhomeButton);
    }

    public void relayoutGlobalButton() {
        try {
            if (!BookSetting.IS_HOR_VER) {
                relayoutHorGlobalButton();
            } else if (BookSetting.IS_HOR) {
                Log.d("hl", "relayoutHorGlobalButton");
                relayoutHorGlobalButton();
            } else {
                Log.d("hl", "relayoutVerGlobalButton");
                relayoutVerGlobalButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hl", "布局按钮出错");
        }
    }

    private void setGlobalLayoutParams(ButtonEntity buttonEntity, ImageButton buttonView) {
        if (buttonEntity != null && buttonView != null) {
            buttonView.setTag(buttonEntity);
            if (buttonEntity.isVisible()) {
                buttonView.setVisibility(8);
            }
            int initx = 0;
            int inity = 0;
            if (!HLSetting.FitScreen) {
                initx = ((int) (((float) BookSetting.BOOK_WIDTH) - ScreenUtils.getHorScreenValue(BookController.getInstance().getViewPage().getEntity().getWidth()))) / 2;
                if (initx < 0) {
                    initx = 0;
                }
                inity = ((int) (((float) BookSetting.BOOK_HEIGHT) - ScreenUtils.getVerScreenValue(BookController.getInstance().getViewPage().getEntity().getHeight()))) / 2;
                if (inity < 0) {
                    inity = 0;
                }
            }
            float floatLeftMargin = ScreenUtils.getHorScreenValue(buttonEntity.getX() + ((float) buttonEntity.getWidth()));
            int btnWidth = (int) ScreenUtils.getHorScreenValue((float) buttonEntity.getWidth());
            if (btnWidth < ScreenUtils.dip2px(this, (float) MINGLOBEALBUTTONWIDTH)) {
                btnWidth = ScreenUtils.dip2px(this, (float) MINGLOBEALBUTTONWIDTH);
            }
            int leftMargin = (int) (floatLeftMargin - ((float) btnWidth));
            if (leftMargin < 0) {
                leftMargin = 0;
            }
            float floatbtnHeight = (buttonEntity.getY() * ((float) BookSetting.BOOK_HEIGHT)) / ((float) BookController.getInstance().getBook().getBookInfo().bookHeight);
            if (BookSetting.IS_HOR) {
                floatbtnHeight = (buttonEntity.getY() * ((float) BookSetting.BOOK_HEIGHT)) / ((float) BookController.getInstance().getBook().getBookInfo().bookWidth);
            }
            int btnHeight = (int) ScreenUtils.getHorScreenValue((float) buttonEntity.getHeight());
            if (btnHeight < ScreenUtils.dip2px(this, (float) MINGLOBEALBUTTONWIDTH)) {
                btnHeight = ScreenUtils.dip2px(this, (float) MINGLOBEALBUTTONWIDTH);
            }
            RelativeLayout.LayoutParams btnLp = new RelativeLayout.LayoutParams(btnWidth, btnHeight);
            btnLp.addRule(9);
            btnLp.addRule(10);
            btnLp.leftMargin = initx + leftMargin;
            btnLp.topMargin = (int) (((float) inity) + floatbtnHeight);
            buttonView.setLayoutParams(btnLp);
            buttonView.setVisibility(8);
        }
    }

    private ImageButton drawGlobalButton(ButtonEntity buttonEntity, int defultRes) {
        ImageButton btn = new ImageButton(this);
        btn.setTag(buttonEntity);
        String btnResource = buttonEntity.getSource();
        String btnSelectResource = buttonEntity.getSelectedSource();
        if (StringUtils.isEmpty(btnResource)) {
            btn.setBackgroundResource(defultRes);
        } else if (StringUtils.isEmpty(btnSelectResource)) {
            btn.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.getBitMap(btnResource, this, buttonEntity.getWidth(), buttonEntity.getHeight())));
        } else {
            btn.setBackgroundDrawable(ImageUtils.getButtonDrawable(btnResource, btnSelectResource, this));
        }
        btn.setVisibility(8);
        return btn;
    }

    public void setupViews() {
        try {
            if (this.preShowViewAction != null) {
                this.preShowViewAction.doAction();
            }
            if (!BookController.getInstance().getBook().getBookInfo().bookNavType.equals("indesign_slider_view")) {
                createGlobaButton(BookController.getInstance().getBook().getButtons());
                addGallery();
            } else {
                this.mUPNav = getUPNav();
                this.mUPNav.setId(NAVMENU_ID);
                this.mUPNav.setNavMenuListenner(new NavMenuListenner() {
                    public void onItem6Click(View itemView) {
                        if (HLLayoutActivity.this.markView4NavMenu == null) {
                            HLLayoutActivity.this.loadMarkView4ShowMark();
                        }
                        HLLayoutActivity.this.toggleViewVisibility("markView4NavMenu");
                        if (HLLayoutActivity.this.markView4NavMenu.getVisibility() == 0) {
                            if (HLLayoutActivity.this.listView4ShowSnapshots != null && HLLayoutActivity.this.listView4ShowSnapshots.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("listView4ShowSnapshots");
                            }
                            if (HLLayoutActivity.this.searchView != null && HLLayoutActivity.this.searchView.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("searchView");
                            }
                        }
                        HLLayoutActivity.this.toggleBottomAndMiddleNavMenu();
                    }

                    public void onItem5Click(View itemView) {
                        if (HLLayoutActivity.this.searchView == null) {
                            HLLayoutActivity.this.loadSearchView();
                        }
                        HLLayoutActivity.this.toggleViewVisibility("searchView");
                        if (HLLayoutActivity.this.searchView.getVisibility() == 0) {
                            if (HLLayoutActivity.this.listView4ShowSnapshots != null && HLLayoutActivity.this.listView4ShowSnapshots.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("listView4ShowSnapshots");
                            }
                            if (HLLayoutActivity.this.markView4NavMenu != null && HLLayoutActivity.this.markView4NavMenu.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("markView4NavMenu");
                            }
                        }
                        HLLayoutActivity.this.toggleBottomAndMiddleNavMenu();
                        HLLayoutActivity.this.searchView.listViewAdapter.notifyDataSetChanged();
                    }

                    public void onItem4Click(View itemView) {
                        HLLayoutActivity.this.onBackPressed();
                    }

                    public void onItem3Click(View itemView) {
                        if (HLLayoutActivity.this.listView4ShowSnapshots == null) {
                            HLLayoutActivity.this.loadListView4ShowSnapshots();
                        }
                        HLLayoutActivity.this.refreshSnapshots();
                        HLLayoutActivity.this.toggleViewVisibility("listView4ShowSnapshots");
                        if (HLLayoutActivity.this.listView4ShowSnapshots.getVisibility() == 0) {
                            if (HLLayoutActivity.this.searchView != null && HLLayoutActivity.this.searchView.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("searchView");
                            }
                            if (HLLayoutActivity.this.markView4NavMenu != null && HLLayoutActivity.this.markView4NavMenu.getVisibility() == 0) {
                                HLLayoutActivity.this.toggleViewVisibility("markView4NavMenu");
                            }
                        }
                        HLLayoutActivity.this.toggleBottomAndMiddleNavMenu();
                    }

                    public void onItem2Click(View itemView) {
                        if (!HLLayoutActivity.this.getMiddleNav().isShowing()) {
                            BookController bookController = BookController.getInstance();
                            ArrayList<String> pageHistory = bookController.getPageHistory();
                            if (pageHistory.size() >= 2) {
                                pageHistory.remove(pageHistory.size() - 1);
                                bookController.playPageById((String) pageHistory.get(pageHistory.size() - 1));
                            }
                            BookController controller = BookController.getInstance();
                            ArrayList<String> pagesIDs = ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages();
                            int currentPageIndex = pagesIDs.indexOf(controller.mainViewPage.getEntity().getID());
                            HLLayoutActivity.this.mBottomNav.seekTo((((float) currentPageIndex) * 1.0f) / ((float) pagesIDs.size()));
                            HLLayoutActivity.this.getMiddleNav().changeSelection((((float) currentPageIndex) * 1.0f) / ((float) pagesIDs.size()));
                        }
                    }

                    public void onItem1Click(View itemView) {
                        String pageID;
                        if (!HLLayoutActivity.this.getMiddleNav().isShowing()) {
                            String str = "";
                            BookController bookController = BookController.getInstance();
                            Book book = bookController.getBook();
                            if (StringUtils.isEmpty(book.getBookInfo().homePageID)) {
                                pageID = (String) ((SectionEntity) book.getSections().get(0)).getPages().get(0);
                            } else {
                                pageID = book.getBookInfo().homePageID;
                            }
                            bookController.loadAndMoveTo(pageID);
                        }
                    }

                    public void onShow() {
                        HLLayoutActivity.this.refreshSnapshots();
                        HLLayoutActivity.this.wmParams.flags = 131104;
                        HLLayoutActivity.this.updateCoverPosition();
                    }

                    public void onDismiss() {
                        if (HLLayoutActivity.this.listView4ShowSnapshots != null) {
                            HLLayoutActivity.this.listView4ShowSnapshots.setVisibility(4);
                            HLLayoutActivity.this.listView4ShowSnapshots.setAdapter(null);
                            HLLayoutActivity.this.listView4ShowSnapshots = null;
                        }
                        if (HLLayoutActivity.this.markView4NavMenu != null) {
                            HLLayoutActivity.this.markView4NavMenu.setVisibility(4);
                        }
                        if (HLLayoutActivity.this.searchView != null) {
                            HLLayoutActivity.this.searchView.setVisibility(4);
                        }
                        HLLayoutActivity.this.wmParams.flags = 131080;
                        HLLayoutActivity.this.updateCoverPosition();
                    }
                });
                this.mBottomNav = getBottomNav();
                this.mBottomNav.setId(this.BOTTOM_NAV_ID);
                this.mBottomNav.setBottomNavListenner(new BottomNavListenner() {
                    public void onShow() {
                        BookController controller = BookController.getInstance();
                        ArrayList<String> pagesIDs = ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages();
                        int currentPageIndex = pagesIDs.indexOf(controller.mainViewPage.getEntity().getID());
                        HLLayoutActivity.this.mBottomNav.seekTo((((float) currentPageIndex) * 1.0f) / ((float) pagesIDs.size()));
                        HLLayoutActivity.this.getMiddleNav().checkChangeSelection((((float) currentPageIndex) * 1.0f) / ((float) pagesIDs.size()), false);
                    }

                    public void onDismiss() {
                    }

                    public void onSliderPositionChanged(float newPosition, float totalLength) {
                        HLLayoutActivity.this.getMiddleNav().checkChangeSelection(newPosition / totalLength, true);
                    }

                    public void onSliderTouchDown() {
                        if (HLLayoutActivity.this.mMiddleNav != null && !HLLayoutActivity.this.mMiddleNav.isShowing()) {
                            HLLayoutActivity.this.mMiddleNav.show();
                        }
                    }
                });
                this.mMiddleNav = getMiddleNav();
                this.mMiddleNav.setId(this.MIDDLE_NAV_ID);
            }
            setAD(this.contentLayout);
        } catch (Exception e) {
        }
        Book book = BookController.getInstance().getBook();
        if (BookController.getInstance().getBook().ActivePush) {
            initPushView();
            createPushButton();
            String pushUrl = Book.push_server + "&device_id=" + book.device_id + "&pmsg_id=" + book.PushID;
            new AsyncTask<String, String, String>() {
                /* access modifiers changed from: protected */
                public String doInBackground(String... arg0) {
                    return WebUtils.getUrlContent(arg0[0], "utf-8");
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(String result) {
                    try {
                        JSONArray jsonList = new JSONArray(result);
                        BookController.getInstance().setPushList(jsonList);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
                        for (int index = 0; index < jsonList.length(); index++) {
                            JSONObject data = jsonList.optJSONObject(index);
                            long time = (long) (index * 3000);
                            Message msg = new Message();
                            msg.obj = data;
                            HLLayoutActivity.this.pushHandler.sendMessageDelayed(msg, time);
                            HLLayoutActivity.this.pushListLayout.addView(HLLayoutActivity.this.getPushItemView(data, index), lp);
                        }
                        HLLayoutActivity.this.pushHandler.sendMessageDelayed(new Message(), (long) ((jsonList.length() + 1) * 3000));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[]{pushUrl});
        }
    }

    /* access modifiers changed from: private */
    public void toggleBottomAndMiddleNavMenu() {
        if (this.markView4NavMenu != null && this.markView4NavMenu.getVisibility() == 0) {
            if (this.mBottomNav != null && this.mBottomNav.isShowing()) {
                this.mBottomNav.setVisibility(4);
            }
            if (this.mMiddleNav != null && this.mMiddleNav.isShowing()) {
                this.mMiddleNav.dismiss();
            }
        } else if (this.searchView != null && this.searchView.getVisibility() == 0) {
            if (this.mBottomNav != null && this.mBottomNav.isShowing()) {
                this.mBottomNav.setVisibility(4);
            }
            if (this.mMiddleNav != null && this.mMiddleNav.isShowing()) {
                this.mMiddleNav.dismiss();
            }
        } else if (this.listView4ShowSnapshots != null && this.listView4ShowSnapshots.getVisibility() == 0) {
            if (this.mBottomNav != null && this.mBottomNav.isShowing()) {
                this.mBottomNav.setVisibility(4);
            }
            if (this.mMiddleNav != null && this.mMiddleNav.isShowing()) {
                this.mMiddleNav.dismiss();
            }
        } else if (this.mBottomNav != null && !this.mBottomNav.isShowing()) {
            this.mBottomNav.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void toggleViewVisibility(String viewName) {
        if (viewName.equals("markView4NavMenu")) {
            if (this.markView4NavMenu == null) {
                return;
            }
            if (this.markView4NavMenu.getVisibility() != 0) {
                this.markView4NavMenu.setVisibility(0);
                this.mUPNav.getItem(5).setBackgroundResource(C0048R.drawable.indesign_showconordown);
                return;
            }
            this.markView4NavMenu.setVisibility(4);
            this.mUPNav.getItem(5).setBackgroundResource(C0048R.drawable.btn_showconor_selecor);
        } else if (viewName.equals("searchView")) {
            if (this.searchView == null) {
                return;
            }
            if (this.searchView.getVisibility() != 0) {
                this.searchView.setVisibility(0);
                this.mUPNav.getItem(4).setBackgroundResource(C0048R.drawable.indesign_searchbtndown);
                return;
            }
            this.searchView.setVisibility(4);
            this.mUPNav.getItem(4).setBackgroundResource(C0048R.drawable.btn_search_selector);
        } else if (viewName.equals("listView4ShowSnapshots") && this.listView4ShowSnapshots != null) {
            if (this.listView4ShowSnapshots.getVisibility() != 0) {
                this.listView4ShowSnapshots.setVisibility(0);
                this.mUPNav.getItem(2).setBackgroundResource(C0048R.drawable.indesign_navcatabtndown);
                return;
            }
            this.listView4ShowSnapshots.setVisibility(4);
            this.mUPNav.getItem(2).setBackgroundResource(C0048R.drawable.btn_cata_selector);
        }
    }

    /* access modifiers changed from: protected */
    public void loadSearchView() {
        this.searchView = new MYSearchView(this);
        this.searchView.setVisibility(4);
        RelativeLayout.LayoutParams params4Snapshots = new RelativeLayout.LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, -2);
        params4Snapshots.addRule(3, NAVMENU_ID);
        params4Snapshots.addRule(11);
        params4Snapshots.topMargin = -1;
        this.coverLayout.addView(this.searchView, params4Snapshots);
    }

    /* access modifiers changed from: protected */
    public void loadMarkView4ShowMark() {
        this.markView4NavMenu = new MarkView4NavMenu(this);
        this.markView4NavMenu.setVisibility(4);
        RelativeLayout.LayoutParams params4Snapshots = new RelativeLayout.LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, -2);
        params4Snapshots.addRule(3, NAVMENU_ID);
        params4Snapshots.addRule(11);
        params4Snapshots.topMargin = -1;
        this.coverLayout.addView(this.markView4NavMenu, params4Snapshots);
    }

    public void loadListView4ShowSnapshots() {
        this.listView4ShowSnapshots = new ListView(this);
        this.listView4ShowSnapshots.setBackgroundResource(C0048R.drawable.indesign_colle_bgimg);
        RelativeLayout.LayoutParams params4Snapshots = new RelativeLayout.LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, -1);
        params4Snapshots.addRule(3, NAVMENU_ID);
        params4Snapshots.leftMargin = -5;
        params4Snapshots.topMargin = -2;
        this.coverLayout.addView(this.listView4ShowSnapshots, params4Snapshots);
        this.listView4ShowSnapshots.setVisibility(4);
        this.listView4ShowSnapshots.setFadingEdgeLength(0);
        this.listView4ShowSnapshots.setCacheColorHint(0);
        this.listView4ShowSnapshots.setSelector(new ColorDrawable(0));
        this.basAdapter = new BaseAdapter() {

            /* renamed from: com.hl.android.HLLayoutActivity$13$ViewHolder */
            class ViewHolder {
                ImageView imageView;
                LinearLayout linearLayout;
                TextView textView;
                TextView textView4tittle;

                ViewHolder() {
                }
            }

            /* JADX WARNING: type inference failed for: r17v1, types: [android.view.View] */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public android.view.View getView(int r16, android.view.View r17, android.view.ViewGroup r18) {
                /*
                    r15 = this;
                    if (r17 != 0) goto L_0x016b
                    com.hl.android.HLLayoutActivity$13$ViewHolder r3 = new com.hl.android.HLLayoutActivity$13$ViewHolder
                    r3.<init>()
                    android.widget.RelativeLayout r17 = new android.widget.RelativeLayout
                    com.hl.android.HLLayoutActivity r11 = com.p000hl.android.HLLayoutActivity.this
                    r0 = r17
                    r0.<init>(r11)
                    android.widget.AbsListView$LayoutParams r8 = new android.widget.AbsListView$LayoutParams
                    r11 = 300(0x12c, float:4.2E-43)
                    r12 = 100
                    r8.<init>(r11, r12)
                    r0 = r17
                    r0.setLayoutParams(r8)
                    android.widget.ImageView r11 = new android.widget.ImageView
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    r11.<init>(r12)
                    r3.imageView = r11
                    android.widget.ImageView r11 = r3.imageView
                    r12 = 131088(0x20010, float:1.83693E-40)
                    r11.setId(r12)
                    android.widget.ImageView r11 = r3.imageView
                    android.widget.ImageView$ScaleType r12 = android.widget.ImageView.ScaleType.FIT_XY
                    r11.setScaleType(r12)
                    r11 = r17
                    android.view.ViewGroup r11 = (android.view.ViewGroup) r11
                    android.widget.ImageView r12 = r3.imageView
                    r11.addView(r12)
                    android.widget.LinearLayout r11 = new android.widget.LinearLayout
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    r11.<init>(r12)
                    r3.linearLayout = r11
                    android.widget.LinearLayout r11 = r3.linearLayout
                    r12 = 1
                    r11.setOrientation(r12)
                    android.widget.TextView r11 = new android.widget.TextView
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    r11.<init>(r12)
                    r3.textView4tittle = r11
                    android.widget.TextView r11 = r3.textView4tittle
                    r12 = 1097859072(0x41700000, float:15.0)
                    r11.setTextSize(r12)
                    android.widget.TextView r11 = r3.textView4tittle
                    java.lang.String r12 = "END"
                    android.text.TextUtils$TruncateAt r12 = android.text.TextUtils.TruncateAt.valueOf(r12)
                    r11.setEllipsize(r12)
                    android.widget.TextView r11 = r3.textView4tittle
                    r12 = 1
                    r11.setSingleLine(r12)
                    android.widget.TextView r11 = r3.textView4tittle
                    r12 = -1
                    r11.setTextColor(r12)
                    android.widget.LinearLayout$LayoutParams r10 = new android.widget.LinearLayout$LayoutParams
                    r11 = -2
                    r12 = -2
                    r10.<init>(r11, r12)
                    r11 = 20
                    r12 = 0
                    r13 = 20
                    r14 = 0
                    r10.setMargins(r11, r12, r13, r14)
                    android.widget.LinearLayout r11 = r3.linearLayout
                    android.widget.TextView r12 = r3.textView4tittle
                    r11.addView(r12, r10)
                    android.widget.TextView r11 = new android.widget.TextView
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    r11.<init>(r12)
                    r3.textView = r11
                    android.widget.TextView r11 = r3.textView
                    r12 = 1094713344(0x41400000, float:12.0)
                    r11.setTextSize(r12)
                    android.widget.TextView r11 = r3.textView
                    java.lang.String r12 = "END"
                    android.text.TextUtils$TruncateAt r12 = android.text.TextUtils.TruncateAt.valueOf(r12)
                    r11.setEllipsize(r12)
                    android.widget.TextView r11 = r3.textView
                    r12 = 1
                    r11.setSingleLine(r12)
                    android.widget.TextView r11 = r3.textView
                    r12 = -1
                    r11.setTextColor(r12)
                    android.widget.LinearLayout$LayoutParams r9 = new android.widget.LinearLayout$LayoutParams
                    r11 = -2
                    r12 = -2
                    r9.<init>(r11, r12)
                    r11 = 20
                    r12 = 0
                    r13 = 20
                    r14 = 0
                    r9.setMargins(r11, r12, r13, r14)
                    android.widget.LinearLayout r11 = r3.linearLayout
                    android.widget.TextView r12 = r3.textView
                    r11.addView(r12, r9)
                    android.widget.RelativeLayout$LayoutParams r6 = new android.widget.RelativeLayout$LayoutParams
                    r11 = -1
                    r12 = -2
                    r6.<init>(r11, r12)
                    r11 = 15
                    r6.addRule(r11)
                    r11 = 1
                    android.widget.ImageView r12 = r3.imageView
                    int r12 = r12.getId()
                    r6.addRule(r11, r12)
                    r11 = r17
                    android.view.ViewGroup r11 = (android.view.ViewGroup) r11
                    android.widget.LinearLayout r12 = r3.linearLayout
                    r11.addView(r12, r6)
                    r0 = r17
                    r0.setTag(r3)
                L_0x00ee:
                    com.hl.android.controller.BookController r11 = com.p000hl.android.controller.BookController.getInstance()
                    java.util.ArrayList r11 = r11.getCurrentSnapshots()
                    r0 = r16
                    java.lang.Object r11 = r11.get(r0)
                    java.lang.String r11 = (java.lang.String) r11
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    android.graphics.Bitmap r1 = com.p000hl.android.core.utils.BitmapUtils.getBitMap(r11, r12)
                    android.widget.ImageView r11 = r3.imageView
                    r11.setImageBitmap(r1)
                    android.widget.RelativeLayout$LayoutParams r7 = new android.widget.RelativeLayout$LayoutParams
                    int r11 = r1.getWidth()
                    int r11 = r11 * 60
                    int r12 = r1.getHeight()
                    int r11 = r11 / r12
                    r12 = 60
                    r7.<init>(r11, r12)
                    r11 = 9
                    r7.addRule(r11)
                    r11 = 15
                    r7.addRule(r11)
                    r11 = 20
                    r7.leftMargin = r11
                    android.widget.ImageView r11 = r3.imageView
                    r11.setLayoutParams(r7)
                    com.hl.android.controller.BookController r2 = com.p000hl.android.controller.BookController.getInstance()
                    com.hl.android.book.entity.Book r11 = r2.getBook()
                    java.util.ArrayList r11 = r11.getSections()
                    int r12 = r2.currendsectionindex
                    java.lang.Object r11 = r11.get(r12)
                    com.hl.android.book.entity.SectionEntity r11 = (com.p000hl.android.book.entity.SectionEntity) r11
                    java.util.ArrayList r11 = r11.getPages()
                    r0 = r16
                    java.lang.Object r5 = r11.get(r0)
                    java.lang.String r5 = (java.lang.String) r5
                    com.hl.android.controller.PageEntityController r11 = com.p000hl.android.controller.PageEntityController.getInstance()
                    com.hl.android.HLLayoutActivity r12 = com.p000hl.android.HLLayoutActivity.this
                    com.hl.android.book.entity.PageEntity r4 = r11.getPageEntityByPageId(r12, r5)
                    android.widget.TextView r11 = r3.textView4tittle
                    java.lang.String r12 = r4.getTitle()
                    r11.setText(r12)
                    android.widget.TextView r11 = r3.textView
                    java.lang.String r12 = r4.getDescription()
                    r11.setText(r12)
                    return r17
                L_0x016b:
                    java.lang.Object r3 = r17.getTag()
                    com.hl.android.HLLayoutActivity$13$ViewHolder r3 = (com.p000hl.android.HLLayoutActivity.C003613.ViewHolder) r3
                    goto L_0x00ee
                */
                throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.HLLayoutActivity.C003613.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public Object getItem(int position) {
                return null;
            }

            public int getCount() {
                BookController controller = BookController.getInstance();
                return ((SectionEntity) controller.getBook().getSections().get(controller.currendsectionindex)).getPages().size();
            }
        };
        this.listView4ShowSnapshots.setAdapter(this.basAdapter);
        this.listView4ShowSnapshots.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookController bookController = BookController.getInstance();
                String pageID = BookController.getInstance().getSectionPageIdByPosition(position);
                if (!pageID.equals(BookController.getInstance().mainPageID)) {
                    bookController.playPageById(pageID);
                }
                HLLayoutActivity.this.getUPNav().dismiss();
            }
        });
    }

    public IndesignUP getUPNav() {
        if (this.mUPNav == null) {
            this.mUPNav = new IndesignUP(this);
            this.coverLayout.addView(this.mUPNav);
            this.mUPNav.setVisibility(4);
        }
        return this.mUPNav;
    }

    public IndesignBottom getBottomNav() {
        if (this.mBottomNav == null) {
            this.mBottomNav = new IndesignBottom(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(12);
            this.coverLayout.addView(this.mBottomNav, layoutParams);
            this.mBottomNav.setVisibility(4);
        }
        return this.mBottomNav;
    }

    public IndesignMiddle getMiddleNav() {
        if (this.mMiddleNav == null) {
            this.mMiddleNav = new IndesignMiddle(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            if (this.mUPNav != null) {
                layoutParams.addRule(3, this.mUPNav.getId());
            }
            if (this.mBottomNav != null) {
                layoutParams.addRule(2, this.mBottomNav.getId());
            }
            layoutParams.bottomMargin = -2;
            layoutParams.topMargin = -2;
            this.coverLayout.addView(this.mMiddleNav, layoutParams);
            this.mMiddleNav.setVisibility(4);
        }
        return this.mMiddleNav;
    }

    public MarkView4NavMenu getMarkView4NavMenu() {
        return this.markView4NavMenu;
    }

    public void addGallery() {
        if (!BookSetting.IS_NO_NAVIGATION) {
            if (BookSetting.IS_HOR_VER) {
                setSnapshots();
            }
            this.gallery = GalleyHelper.getGalley(this);
            RelativeLayout.LayoutParams glp = new RelativeLayout.LayoutParams(-1, -2);
            glp.addRule(12);
            glp.addRule(14);
            glp.bottomMargin = this.gallery.getLayoutParams().height;
            this.gallery.setId(this.gallery_ID);
            BookController.getInstance().setGallery(this.gallery);
            this.gallery.hideGalleryInfor();
            RelativeLayout.LayoutParams imgBtnlp = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(this, 120.0f), ScreenUtils.dip2px(this, 48.0f));
            imgBtnlp.addRule(12);
            imgBtnlp.addRule(14);
            imgBtnlp.bottomMargin = this.gallery.getLayoutParams().height;
            this.coverLayout.addView(this.gallery.getHideImgButton(), imgBtnlp);
            this.coverLayout.addView(this.gallery.getPageTextView());
            this.coverLayout.addView(this.gallery);
        }
    }

    public void setSnapshots() {
        if (BookSetting.IS_HOR && BookSetting.SNAPSHOTS_WIDTH < BookSetting.SNAPSHOTS_HEIGHT) {
            int a = BookSetting.SNAPSHOTS_WIDTH;
            BookSetting.SNAPSHOTS_WIDTH = BookSetting.SNAPSHOTS_HEIGHT;
            BookSetting.SNAPSHOTS_HEIGHT = a;
        }
        if (!BookSetting.IS_HOR && BookSetting.SNAPSHOTS_WIDTH > BookSetting.SNAPSHOTS_HEIGHT) {
            int a2 = BookSetting.SNAPSHOTS_WIDTH;
            BookSetting.SNAPSHOTS_WIDTH = BookSetting.SNAPSHOTS_HEIGHT;
            BookSetting.SNAPSHOTS_HEIGHT = a2;
        }
    }

    public void hideGalley() {
        if (this.gallery != null) {
            this.gallery.hideGalleryInfor();
        }
    }

    public void setFlipView() {
        new LayoutParams(-2, -2).gravity = 17;
        switch (BookSetting.FLIPCODE) {
            case 0:
                this.absPageView = new PageWidgetNew(this);
                break;
            case 1:
                this.absPageView = new PageFlipView(this);
                break;
            case 2:
                this.absPageView = new PageFlipView(this);
                break;
        }
        this.mainLayout.addView(this.absPageView, this.contentLp);
        this.absPageView.setVisibility(8);
        BookController.getInstance().setFlipView(this.absPageView);
        AbstractPageFlipView apf2 = new PageFlipVerticleView(this);
        BookController.getInstance().setSubPageViewFlip(apf2);
        this.mainLayout.addView(apf2, this.contentLp);
        this.commonLayout.bringToFront();
    }
}
