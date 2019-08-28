package com.p000hl.android.view.component;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.p000hl.android.C0048R;

/* renamed from: com.hl.android.view.component.IndesignBottom */
public class IndesignBottom extends RelativeLayout {
    private Animation anim4dismiss;
    private Animation anim4show;
    private Context mContext;
    /* access modifiers changed from: private */
    public BottomNavListenner mListenner = null;
    /* access modifiers changed from: private */
    public ImageButton mMoveBtn;
    /* access modifiers changed from: private */
    public MotionEvent oldEvent;
    public boolean tagggg = false;

    /* renamed from: com.hl.android.view.component.IndesignBottom$BottomNavListenner */
    public interface BottomNavListenner {
        void onDismiss();

        void onShow();

        void onSliderPositionChanged(float f, float f2);

        void onSliderTouchDown();
    }

    public IndesignBottom(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        setBackgroundResource(C0048R.drawable.indesign_bottomnav_bg);
        this.mMoveBtn = new ImageButton(this.mContext);
        this.mMoveBtn.setBackgroundResource(C0048R.drawable.btn_movebtn_selector);
        addView(this.mMoveBtn);
        this.anim4dismiss = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        this.anim4dismiss.setDuration(200);
        this.anim4dismiss.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                IndesignBottom.this.setVisibility(4);
            }
        });
        this.anim4show = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        this.anim4show.setDuration(200);
        this.mMoveBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (IndesignBottom.this.mListenner != null) {
                            IndesignBottom.this.mListenner.onSliderTouchDown();
                            break;
                        }
                        break;
                    case 2:
                        float resultX = IndesignBottom.this.mMoveBtn.getX() + (event.getRawX() - IndesignBottom.this.oldEvent.getRawX());
                        if (resultX <= 13.0f) {
                            resultX = 13.0f;
                        } else if (resultX >= ((float) ((IndesignBottom.this.getWidth() - 13) - IndesignBottom.this.mMoveBtn.getWidth()))) {
                            resultX = (float) ((IndesignBottom.this.getWidth() - 13) - IndesignBottom.this.mMoveBtn.getWidth());
                        }
                        if (resultX != IndesignBottom.this.mMoveBtn.getX()) {
                            IndesignBottom.this.mMoveBtn.setX(resultX);
                            if (IndesignBottom.this.mListenner != null) {
                                IndesignBottom.this.mListenner.onSliderPositionChanged(resultX - 13.0f, IndesignBottom.this.getTotalSlideLength());
                                break;
                            }
                        }
                        break;
                }
                IndesignBottom.this.oldEvent = MotionEvent.obtain(event);
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void setBottomNavListenner(BottomNavListenner listenner) {
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

    private void doAnim4Dismiss() {
        if (this.mListenner != null) {
            this.mListenner.onDismiss();
        }
        startAnimation(this.anim4dismiss);
    }

    private void doAnim4Show() {
        if (this.mListenner != null) {
            this.mListenner.onShow();
        }
        startAnimation(this.anim4show);
    }

    public float getTotalSlideLength() {
        return (float) ((getWidth() - 26) - this.mMoveBtn.getWidth());
    }

    public void seekTo(float percentX) {
        this.mMoveBtn.setX(13.0f + (getTotalSlideLength() * percentX));
    }
}
