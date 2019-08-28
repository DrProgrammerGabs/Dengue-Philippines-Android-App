package com.p000hl.android.view.pageflip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.pageflip.animation.Rotate3dAnimation;

/* renamed from: com.hl.android.view.pageflip.TurnPageFlipView */
public class TurnPageFlipView extends AbstractPageFlipView {
    /* access modifiers changed from: private */
    public int curHeight = BookSetting.SCREEN_HEIGHT;
    /* access modifiers changed from: private */
    public int curWidth = BookSetting.SCREEN_WIDTH;
    private Bitmap currentS;
    /* access modifiers changed from: private */
    public ImageView imageViewLeft;
    /* access modifiers changed from: private */
    public ImageView imageViewRight;
    ActionOnEnd mAction;

    public TurnPageFlipView(Context context) {
        super(context);
    }

    public void show() {
        this.imageViewRight = new ImageView(getContext());
        this.imageViewRight.setBackgroundDrawable(new BitmapDrawable(Bitmap.createBitmap(this.mCurPageBitmap, this.curWidth / 2, 0, this.curWidth / 2, this.curHeight)));
        LayoutParams lp = new LayoutParams(this.curWidth / 2, this.curHeight);
        lp.addRule(7);
        lp.addRule(11);
        this.bookLayout.addView(this.imageViewRight, lp);
        this.imageViewLeft = new ImageView(getContext());
        this.imageViewLeft.setBackgroundColor(0);
        this.imageViewLeft.setBackgroundDrawable(new BitmapDrawable(Bitmap.createBitmap(this.mCurPageBitmap, 0, 0, this.curWidth / 2, this.curHeight)));
        LayoutParams lp1 = new LayoutParams(this.curWidth / 2, this.curHeight);
        lp.addRule(5);
        this.bookLayout.addView(this.imageViewLeft, lp1);
        setVisibility(0);
        BookController.getInstance().hlActivity.commonLayout.bringToFront();
    }

    private void setimg() {
        this.imageViewRight = new ImageView(getContext());
        this.imageViewRight.setBackgroundDrawable(new BitmapDrawable(Bitmap.createBitmap(this.mCurPageBitmap, this.curWidth / 2, 0, this.curWidth / 2, this.curHeight)));
        LayoutParams lp = new LayoutParams(this.curWidth / 2, this.curHeight);
        lp.addRule(7);
        lp.addRule(11);
        this.bookLayout.addView(this.imageViewRight, lp);
        this.imageViewLeft = new ImageView(getContext());
        this.imageViewLeft.setBackgroundColor(0);
        this.imageViewLeft.setBackgroundDrawable(new BitmapDrawable(Bitmap.createBitmap(this.mCurPageBitmap, 0, 0, this.curWidth / 2, this.curHeight)));
        LayoutParams lp1 = new LayoutParams(this.curWidth / 2, this.curHeight);
        lp.addRule(5);
        this.bookLayout.addView(this.imageViewLeft, lp1);
        setVisibility(0);
    }

    public void hide() {
        this.imageViewLeft.setVisibility(8);
        this.imageViewRight.setVisibility(8);
        this.bookLayout.removeView(this.imageViewLeft);
        this.bookLayout.removeView(this.imageViewRight);
        setBackgroundColor(0);
        setVisibility(8);
    }

    public void play(int pageIndex, int newPageIndex, ActionOnEnd action) {
        this.mAction = action;
        this.currentS = BookController.getInstance().getCurrentSnapShotCashImage();
        setimg();
        this.imageViewLeft.bringToFront();
        this.imageViewRight.bringToFront();
        if (pageIndex < newPageIndex) {
            applyRotationnext(this.imageViewRight, 0.0f, -180.0f);
        } else {
            applyRotationpre(this.imageViewLeft, 0.0f, 180.0f);
        }
    }

    private void applyRotationpre(View view, float start, float end) {
        final float centerX = ((float) this.curWidth) / 2.0f;
        final float centerY = ((float) this.curHeight) / 2.0f;
        this.imageViewLeft.bringToFront();
        Rotate3dAnimation rotationl = new Rotate3dAnimation(0.0f, 90.0f, centerX, centerY, 310.0f, true);
        rotationl.setDuration((long) (HLSetting.FlipTime / 2));
        rotationl.setFillAfter(true);
        rotationl.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                TurnPageFlipView.this.imageViewRight.setVisibility(8);
                Matrix mx = new Matrix();
                mx.setScale(1.0f, -1.0f);
                Bitmap bprightb1 = BookController.getInstance().getCurrentSnapShotCashImage();
                mx.setRotate(180.0f);
                Bitmap bprightb = Bitmap.createBitmap(bprightb1, 0, 0, TurnPageFlipView.this.curWidth / 2, TurnPageFlipView.this.curHeight, mx, true);
                TurnPageFlipView.this.imageViewRight.setVisibility(0);
                TurnPageFlipView.this.imageViewLeft.setBackgroundDrawable(new BitmapDrawable(bprightb));
                Rotate3dAnimation _rotationl = new Rotate3dAnimation(91.0f, 180.0f, centerX, centerY, 310.0f, true);
                _rotationl.setDuration((long) (HLSetting.FlipTime / 2));
                _rotationl.setFillAfter(true);
                _rotationl.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        BookController.getInstance().hlActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                BookController.getInstance().revokeCommonPage();
                                TurnPageFlipView.this.hide();
                                BookController.getInstance().startPlay();
                            }
                        });
                    }
                });
                TurnPageFlipView.this.imageViewLeft.startAnimation(_rotationl);
            }
        });
        this.imageViewLeft.startAnimation(rotationl);
    }

    private void applyRotationnext(View view, float start, float end) {
        Bitmap drawingCache = getRootView().getDrawingCache();
        float f = ((float) this.curWidth) / 2.0f;
        final float centerY = ((float) this.curHeight) / 2.0f;
        Rotate3dAnimation rotation = new Rotate3dAnimation(0.0f, -90.0f, 0.0f, centerY, 310.0f, true);
        rotation.setDuration((long) (HLSetting.FlipTime / 2));
        rotation.setFillAfter(true);
        rotation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (TurnPageFlipView.this.mAction != null) {
                    TurnPageFlipView.this.mAction.doAction();
                }
                TurnPageFlipView.this.imageViewLeft.setVisibility(8);
                Matrix mx = new Matrix();
                mx.setScale(-1.0f, 1.0f);
                Bitmap bprightb1 = BookController.getInstance().getCurrentSnapShotCashImage();
                mx.setRotate(180.0f);
                Bitmap createBitmap = Bitmap.createBitmap(bprightb1, 0, 0, TurnPageFlipView.this.curWidth / 2, TurnPageFlipView.this.curHeight, mx, true);
                TurnPageFlipView.this.imageViewLeft.setVisibility(0);
                TurnPageFlipView.this.imageViewRight.setBackgroundDrawable(new BitmapDrawable(bprightb1));
                Rotate3dAnimation _rotation = new Rotate3dAnimation(-90.0f, -180.0f, 0.0f, centerY, 310.0f, true);
                _rotation.setDuration((long) (HLSetting.FlipTime / 2));
                _rotation.setFillAfter(true);
                _rotation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        TurnPageFlipView.this.hide();
                        BookController.getInstance().startPlay();
                    }
                });
                TurnPageFlipView.this.imageViewRight.startAnimation(_rotation);
            }
        });
        this.imageViewRight.startAnimation(rotation);
    }
}
