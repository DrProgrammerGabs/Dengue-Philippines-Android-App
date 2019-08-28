package com.p000hl.android.core.helper.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.view.View;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.bean.ViewRecord;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.core.helper.animation.AnimatorListener4Item */
public class AnimatorListener4Item implements AnimatorListener {
    private AnimationEntity animationEntity;
    private AnimationKey animationKey;
    private ComponentEntity entity;
    private boolean isCancel = false;
    private int mIndex = 0;
    private ViewRecord mRecord;
    private ViewCell mView;

    public void setRecord(ViewRecord record) {
        this.mRecord = record.getClone();
    }

    public AnimatorListener4Item(ViewCell v, int index) {
        this.mView = v;
        this.mIndex = index;
        this.entity = this.mView.getEntity();
        this.animationEntity = (AnimationEntity) this.entity.getAnims().get(this.mIndex);
        this.animationKey = new AnimationKey(v, index);
    }

    public void onAnimationCancel(Animator animation) {
        this.isCancel = true;
    }

    public void onAnimationEnd(Animator animation) {
        if (this.isCancel) {
            this.isCancel = false;
            return;
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END_AT, Integer.toString(this.mIndex));
        if (this.animationEntity.AnimationType.contains("WIPEOUT")) {
            this.mView.setPadding(0, 0, 0, 0);
            if (Boolean.parseBoolean(this.animationEntity.IsKeep) && "OUT_FLAG".equals(this.animationEntity.AnimationEnterOrQuit)) {
                this.mView.setVisibility(4);
            }
        } else if (!Boolean.parseBoolean(this.animationEntity.IsKeep)) {
            this.mView.setPadding(0, 0, 0, 0);
            this.mView.setX(this.mRecord.f28mX);
            this.mView.setY(this.mRecord.f29mY);
            this.mView.setAlpha(this.mView.getEntity().alpha);
            this.mView.setSuperRotation(this.mRecord.mRotation);
            this.mView.setScaleX(this.mRecord.mScaleX);
            this.mView.setScaleY(this.mRecord.mScaleY);
        }
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
        if (this.mView.getVisibility() != 0) {
            this.mView.show();
        }
        if (this.animationEntity.AnimationType.equals("ANIMATION_ROTATEIN") || this.animationEntity.AnimationType.equals("ANIMATION_ROTATEOUT")) {
            this.mView.setScaleX(this.mRecord.mScaleX * 1.1f);
            this.mView.setScaleY(this.mRecord.mScaleY * 1.1f);
            ((View) this.mView.getComponent()).setScaleX(0.909f);
            ((View) this.mView.getComponent()).setScaleY(0.909f);
        }
        if (((View) this.mView.getComponent()).getVisibility() != 0) {
            this.mView.show();
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_PLAY);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_PLAY_AT, Integer.toString(this.mIndex));
    }
}
