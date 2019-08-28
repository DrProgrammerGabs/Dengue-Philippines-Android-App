package com.p000hl.android.core.helper.animation;

import android.view.View;

/* renamed from: com.hl.android.core.helper.animation.AnimationKey */
public class AnimationKey {
    public int mIndex = 9999;
    public View mView;

    public AnimationKey(View view) {
        this.mView = view;
    }

    public AnimationKey(View view, int index) {
        this.mView = view;
        this.mIndex = index;
    }

    public boolean equals(Object o) {
        if (!(o instanceof AnimationKey)) {
            return false;
        }
        AnimationKey target = (AnimationKey) o;
        if (target.mView == this.mView && target.mIndex == this.mIndex) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.mView.hashCode() * 100) + this.mIndex;
    }
}
