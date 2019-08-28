package com.p000hl.android.view.pageflip;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.view.pageflip.PageFlipVerticleView */
public class PageFlipVerticleView extends AbstractPageFlipView {
    Context _context;
    int _index = 0;

    /* renamed from: an */
    Animation f60an = null;
    /* access modifiers changed from: private */
    public ActionOnEnd mAction;

    public PageFlipVerticleView(Context context) {
        super(context);
        this._context = context;
    }

    public void show() {
        setBackgroundDrawable(new BitmapDrawable(this.mCurPageBitmap));
        setVisibility(0);
        bringToFront();
        BookController.getInstance().hlActivity.commonLayout.bringToFront();
    }

    public void hide() {
        setVisibility(8);
        setBackgroundColor(0);
    }

    public void play(int pageIndex, int newPageIndex, ActionOnEnd action) {
        Animation ans;
        this.mAction = action;
        try {
            if (this.mCurPageBitmap == null) {
                BookController.getInstance().startPlay();
                return;
            }
            if (pageIndex < newPageIndex) {
                this.f60an = new TranslateAnimation(0.0f, 0.0f, (float) BookSetting.SCREEN_HEIGHT, 0.0f);
            } else {
                this.f60an = new TranslateAnimation(0.0f, 0.0f, (float) (BookSetting.SCREEN_HEIGHT * -1), 0.0f);
            }
            this.bookLayout.setAnimation(this.f60an);
            this.f60an.setDuration((long) HLSetting.FlipTime);
            this.f60an.setRepeatCount(0);
            this.f60an.setStartOffset(0);
            this.f60an.initialize(1, 1, 5, 5);
            if (pageIndex > newPageIndex) {
                ans = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) BookSetting.SCREEN_HEIGHT);
            } else {
                ans = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (BookSetting.SCREEN_HEIGHT * -1));
            }
            ans.setDuration((long) HLSetting.FlipTime);
            ans.setRepeatCount(0);
            ans.setStartOffset(0);
            ans.initialize(1, 1, 5, 5);
            setAnimation(ans);
            if (this.mCurPageBitmap == null) {
                hide();
                BookController.getInstance().startPlay();
                return;
            }
            this.f60an.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    Log.d("PageFlipVIew.flip", "flipAnimation end");
                    if (!PageFlipVerticleView.this._preload && PageFlipVerticleView.this.mAction != null) {
                        PageFlipVerticleView.this.mAction.doAction();
                    }
                    PageFlipVerticleView.this.hide();
                    new CountDownTimer(500, 500) {
                        public void onFinish() {
                            BookController.getInstance().doFlipSubPage = false;
                            BookController.getInstance().startPlay();
                        }

                        public void onTick(long arg0) {
                        }
                    }.start();
                }
            });
            ans.startNow();
            this.f60an.startNow();
        } catch (Exception e) {
            BookController.getInstance().startPlay();
        }
    }
}
