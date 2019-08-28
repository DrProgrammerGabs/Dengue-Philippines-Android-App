package com.p000hl.android.view.pageflip.animation;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

/* renamed from: com.hl.android.view.pageflip.animation.SwapViews */
public final class SwapViews implements Runnable {
    View image1;
    View image2;
    private boolean mIsFirstView;

    public SwapViews(boolean isFirstView, View image12, View image22) {
        this.mIsFirstView = isFirstView;
        this.image1 = image12;
        this.image2 = image22;
    }

    public void run() {
        Flip3dAnimation rotation;
        float centerX = ((float) this.image1.getWidth()) / 2.0f;
        float centerY = ((float) this.image1.getHeight()) / 2.0f;
        if (this.mIsFirstView) {
            this.image1.setVisibility(8);
            this.image2.setVisibility(0);
            this.image2.requestFocus();
            rotation = new Flip3dAnimation(-90.0f, 0.0f, centerX, centerY);
        } else {
            this.image2.setVisibility(8);
            this.image1.setVisibility(0);
            this.image1.requestFocus();
            rotation = new Flip3dAnimation(90.0f, 0.0f, centerX, centerY);
        }
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        if (this.mIsFirstView) {
            this.image2.startAnimation(rotation);
        } else {
            this.image1.startAnimation(rotation);
        }
    }
}
