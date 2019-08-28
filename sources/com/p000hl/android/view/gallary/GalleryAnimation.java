package com.p000hl.android.view.gallary;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import com.p000hl.android.view.gallary.base.AbstractGalley;

/* renamed from: com.hl.android.view.gallary.GalleryAnimation */
public class GalleryAnimation implements AnimationListener {

    /* renamed from: an */
    Animation f58an = null;
    AbstractGalley gallery;

    public void playGallery(boolean isDown, AbstractGalley gallery2) {
        this.gallery = gallery2;
        if (!isDown) {
            this.f58an = new TranslateAnimation(0.0f, 0.0f, -300.0f, 3000.0f);
            gallery2.showGalleryInfor();
        } else {
            this.f58an = new TranslateAnimation(0.0f, 0.0f, 300.0f, -300.0f);
        }
        this.f58an.setDuration(5500);
        this.f58an.setRepeatCount(0);
        this.f58an.setStartOffset(0);
        this.f58an.initialize(1, 1, 5, 5);
        this.f58an.setAnimationListener(this);
        gallery2.startAnimation(this.f58an);
    }

    public void onAnimationEnd(Animation animation) {
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}
