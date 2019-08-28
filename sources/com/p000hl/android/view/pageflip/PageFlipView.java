package com.p000hl.android.view.pageflip;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.view.pageflip.PageFlipView */
public class PageFlipView extends AbstractPageFlipView {
    Context _context;
    int _index = 0;

    /* renamed from: an */
    ObjectAnimator f61an = null;
    ActionOnEnd mAction;

    /* renamed from: v */
    View f62v;

    public PageFlipView(Context context) {
        super(context);
        this._context = context;
        setBackgroundColor(0);
    }

    public void show() {
        setBackgroundDrawable(new BitmapDrawable(this.mCurPageBitmap));
        setVisibility(0);
        bringToFront();
        BookController.getInstance().hlActivity.commonLayout.bringToFront();
    }

    public void hide() {
        setVisibility(8);
    }

    public void play(int pageIndex, int newPaheIndex, ActionOnEnd action) {
        ObjectAnimator ans;
        this.mAction = action;
        try {
            Log.d("zhaoq", "mCurPageBitmap-----" + this.mCurPageBitmap);
            if (this.mCurPageBitmap != null) {
                View moveView = this.bookLayout;
                if (pageIndex < newPaheIndex) {
                    this.f61an = ObjectAnimator.ofFloat(moveView, "x", new float[]{(float) BookSetting.BOOK_WIDTH, 0.0f});
                } else {
                    this.f61an = ObjectAnimator.ofFloat(moveView, "x", new float[]{(float) (BookSetting.BOOK_WIDTH * -1), 0.0f});
                }
                this.f61an.setDuration((long) HLSetting.FlipTime);
                if (pageIndex < newPaheIndex) {
                    ans = ObjectAnimator.ofFloat(this, "x", new float[]{0.0f, (float) (BookSetting.BOOK_WIDTH * -1)});
                } else {
                    ans = ObjectAnimator.ofFloat(this, "x", new float[]{0.0f, (float) BookSetting.BOOK_WIDTH});
                }
                ans.setDuration((long) HLSetting.FlipTime);
                ans.setRepeatCount(0);
                this.f61an.addListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        Log.d("PageFlipVIew.flip", "flipAnimation end");
                        PageFlipView.this.hide();
                        if (!PageFlipView.this._preload && PageFlipView.this.mAction != null) {
                            PageFlipView.this.mAction.doAction();
                        }
                        BookController.getInstance().hlActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                BookController.getInstance().revokeCommonPage();
                                BookController.getInstance().removeNotShowViewPage();
                                BookController.getInstance().startPlay();
                            }
                        });
                    }

                    public void onAnimationCancel(Animator animation) {
                    }
                });
                ans.start();
                this.f61an.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            BookController.getInstance().startPlay();
        }
    }
}
