package com.p000hl.android.core.helper.animation;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import com.p000hl.android.common.BookSetting;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.core.helper.animation.HLAnimatorUpdateListener */
public class HLAnimatorUpdateListener implements AnimatorUpdateListener {
    /* access modifiers changed from: private */
    public float fraction = 0.0f;
    private HLInterpolator initInterInterpolator;
    private boolean isElapsed = true;
    /* access modifiers changed from: private */
    public long mCurrentPlayTime = 0;
    /* access modifiers changed from: private */
    public boolean mPause = false;
    public boolean mStop = false;
    private TimeInterpolator pauseTimer = new TimeInterpolator() {
        public float getInterpolation(float input) {
            return HLAnimatorUpdateListener.this.fraction;
        }
    };

    public boolean isPause() {
        return this.mPause;
    }

    public void pause() {
        this.mPause = true;
    }

    public void play() {
        this.mPause = false;
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        if (!BookSetting.IS_CLOSED) {
            if (this.mPause) {
                if (this.isElapsed) {
                    if (this.initInterInterpolator == null) {
                        this.initInterInterpolator = (HLInterpolator) ((HLInterpolator) animation.getInterpolator()).clone();
                    }
                    animation.setInterpolator(this.pauseTimer);
                } else {
                    this.isElapsed = false;
                }
                final ValueAnimator valueAnimator = animation;
                new CountDownTimer(ValueAnimator.getFrameDelay(), ValueAnimator.getFrameDelay()) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if (HLAnimatorUpdateListener.this.mPause) {
                            valueAnimator.setCurrentPlayTime(HLAnimatorUpdateListener.this.mCurrentPlayTime);
                        }
                    }
                }.start();
                return;
            }
            this.mCurrentPlayTime = animation.getCurrentPlayTime();
            this.fraction = animation.getAnimatedFraction();
            if (animation.getInterpolator() == this.pauseTimer) {
                this.isElapsed = true;
                animation.setInterpolator(this.initInterInterpolator);
            }
        }
    }
}
