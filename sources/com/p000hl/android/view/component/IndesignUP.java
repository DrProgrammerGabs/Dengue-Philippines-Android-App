package com.p000hl.android.view.component;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.C0048R;

/* renamed from: com.hl.android.view.component.IndesignUP */
public class IndesignUP extends RelativeLayout implements OnClickListener {
    private static final int BTN_NAV_ITEM1_ID = 65552;
    private static final int BTN_NAV_ITEM2_ID = 65553;
    private static final int BTN_NAV_ITEM3_ID = 65554;
    private static final int BTN_NAV_ITEM4_ID = 65555;
    private static final int BTN_NAV_ITEM6_ID = 65557;
    private Animation anim4dismiss;
    private Animation anim4show;
    private ImageButton btnItem1;
    private ImageButton btnItem2;
    private ImageButton btnItem3;
    private ImageButton btnItem4;
    private ImageButton btnItem6;
    private Context mContext;
    private NavMenuListenner mListenner = null;

    /* renamed from: com.hl.android.view.component.IndesignUP$NavMenuListenner */
    public interface NavMenuListenner {
        void onDismiss();

        void onItem1Click(View view);

        void onItem2Click(View view);

        void onItem3Click(View view);

        void onItem4Click(View view);

        void onItem5Click(View view);

        void onItem6Click(View view);

        void onShow();
    }

    public IndesignUP(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        setBackgroundResource(C0048R.drawable.indesign_topnavbgimg);
        this.btnItem1 = new ImageButton(this.mContext);
        this.btnItem1.setScaleType(ScaleType.FIT_XY);
        this.btnItem1.setBackgroundResource(C0048R.drawable.btn_gohome_selector);
        this.btnItem1.setId(BTN_NAV_ITEM1_ID);
        addView(this.btnItem1, new LayoutParams(-2, -2));
        this.btnItem2 = new ImageButton(this.mContext);
        this.btnItem2.setScaleType(ScaleType.FIT_XY);
        this.btnItem2.setBackgroundResource(C0048R.drawable.btn_golastpage_selector);
        this.btnItem2.setId(BTN_NAV_ITEM2_ID);
        LayoutParams params4btnGoLastPage = new LayoutParams(-2, -2);
        params4btnGoLastPage.addRule(1, BTN_NAV_ITEM1_ID);
        addView(this.btnItem2, params4btnGoLastPage);
        this.btnItem3 = new ImageButton(this.mContext);
        this.btnItem3.setScaleType(ScaleType.FIT_XY);
        this.btnItem3.setBackgroundResource(C0048R.drawable.btn_cata_selector);
        this.btnItem3.setId(BTN_NAV_ITEM3_ID);
        LayoutParams params4btnShowCata = new LayoutParams(-2, -2);
        params4btnShowCata.addRule(1, BTN_NAV_ITEM2_ID);
        addView(this.btnItem3, params4btnShowCata);
        this.btnItem6 = new ImageButton(this.mContext);
        this.btnItem6.setScaleType(ScaleType.FIT_XY);
        this.btnItem6.setBackgroundResource(C0048R.drawable.btn_showconor_selecor);
        this.btnItem6.setId(BTN_NAV_ITEM6_ID);
        LayoutParams params4btnShowConor = new LayoutParams(-2, -2);
        params4btnShowConor.addRule(11);
        addView(this.btnItem6, params4btnShowConor);
        this.btnItem4 = new ImageButton(this.mContext);
        this.btnItem4.setScaleType(ScaleType.FIT_XY);
        this.btnItem4.setBackgroundResource(C0048R.drawable.btn_goback_selector);
        this.btnItem4.setId(BTN_NAV_ITEM4_ID);
        LayoutParams params4btnGoback = new LayoutParams(-2, -2);
        params4btnGoback.addRule(0, BTN_NAV_ITEM6_ID);
        addView(this.btnItem4, params4btnGoback);
        this.btnItem1.setOnClickListener(this);
        this.btnItem2.setOnClickListener(this);
        this.btnItem3.setOnClickListener(this);
        this.btnItem4.setOnClickListener(this);
        this.btnItem6.setOnClickListener(this);
        this.anim4dismiss = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
        this.anim4dismiss.setDuration(200);
        this.anim4dismiss.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                IndesignUP.this.setVisibility(4);
            }
        });
        this.anim4show = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
        this.anim4show.setDuration(200);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void setNavMenuListenner(NavMenuListenner listenner) {
        this.mListenner = listenner;
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void show() {
        if (!isShowing()) {
            setVisibility(0);
            doAnim4Show();
        }
    }

    public void dismiss() {
        bringToFront();
        if (isShowing()) {
            doAnim4Dismiss();
        }
    }

    public View getItem(int position) {
        switch (position) {
            case 0:
                return this.btnItem1;
            case 1:
                return this.btnItem2;
            case 2:
                return this.btnItem3;
            case 3:
                return this.btnItem4;
            case 5:
                return this.btnItem6;
            default:
                return null;
        }
    }

    private void doAnim4Dismiss() {
        if (this.mListenner != null) {
            this.mListenner.onDismiss();
        }
        startAnimation(this.anim4dismiss);
        this.btnItem3.setBackgroundResource(C0048R.drawable.btn_cata_selector);
        this.btnItem6.setBackgroundResource(C0048R.drawable.btn_showconor_selecor);
        ((InputMethodManager) this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void doAnim4Show() {
        if (this.mListenner != null) {
            this.mListenner.onShow();
        }
        startAnimation(this.anim4show);
    }

    public void onClick(View v) {
        if (this.mListenner != null) {
            switch (v.getId()) {
                case BTN_NAV_ITEM1_ID /*65552*/:
                    this.mListenner.onItem1Click(this.btnItem1);
                    return;
                case BTN_NAV_ITEM2_ID /*65553*/:
                    this.mListenner.onItem2Click(this.btnItem2);
                    return;
                case BTN_NAV_ITEM3_ID /*65554*/:
                    this.mListenner.onItem3Click(this.btnItem3);
                    return;
                case BTN_NAV_ITEM4_ID /*65555*/:
                    this.mListenner.onItem4Click(this.btnItem4);
                    return;
                case BTN_NAV_ITEM6_ID /*65557*/:
                    this.mListenner.onItem6Click(this.btnItem6);
                    return;
                default:
                    return;
            }
        }
    }
}
