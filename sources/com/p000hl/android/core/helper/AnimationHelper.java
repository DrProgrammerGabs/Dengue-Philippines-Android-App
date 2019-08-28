package com.p000hl.android.core.helper;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.os.CountDownTimer;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.helper.animation.AnimationFactory;
import com.p000hl.android.core.helper.animation.AnimationKey;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.hl.android.core.helper.AnimationHelper */
public class AnimationHelper {
    public static HashMap<AnimationKey, Animator> animatiorMap = new HashMap<>();

    /* renamed from: com.hl.android.core.helper.AnimationHelper$AnimatorSetListener */
    public static class AnimatorSetListener implements AnimatorListener {
        boolean iscancel = false;
        public ViewCell mComponent;

        public AnimatorSetListener(ViewCell component) {
            this.mComponent = component;
            this.mComponent.getEntity().currentRepeat = this.mComponent.getEntity().animationRepeat;
        }

        public void onAnimationStart(Animator animation) {
            this.iscancel = false;
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (this.iscancel) {
                AnimationHelper.animatiorMap.remove(this.mComponent);
                return;
            }
            if (this.mComponent.getComponent() instanceof ComponentListener) {
                ((ComponentListener) this.mComponent.getComponent()).callBackListener();
            }
            if (this.mComponent.getEntity().currentRepeat != 1) {
                if (this.mComponent.getEntity().currentRepeat != 0) {
                    ComponentEntity entity = this.mComponent.getEntity();
                    entity.currentRepeat--;
                }
                new CountDownTimer(100, 100) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        AnimationHelper.playAnimation(AnimatorSetListener.this.mComponent);
                    }
                }.start();
            }
        }

        public void onAnimationCancel(Animator animation) {
            this.iscancel = true;
        }
    }

    public static void playAnimationAt(ViewCell v, int index) {
        Animator animator;
        if (BookSetting.IS_CLOSED) {
            animatiorMap.clear();
        } else if (index < v.getEntity().getAnims().size() && index >= 0) {
            v.getAnimatorUpdateListener().mStop = false;
            AnimationKey animationKey = new AnimationKey(v, index);
            Animator animator2 = (Animator) animatiorMap.get(animationKey);
            if (v.getAnimatorUpdateListener() == null || !v.getAnimatorUpdateListener().isPause()) {
                if (v.mAnimator != null && v.mAnimator.isRunning()) {
                    v.mAnimator.cancel();
                }
                AnimationEntity animationEntity = (AnimationEntity) v.getEntity().getAnims().get(index);
                if (!animatiorMap.containsKey(animationKey) || (animationEntity.isKeepEndStatus && Boolean.parseBoolean(animationEntity.IsKeep))) {
                    animator = AnimationFactory.getAnimator(v, (AnimationEntity) v.getEntity().getAnims().get(index), v.getViewRecord());
                    if (animator != null) {
                        animatiorMap.put(animationKey, animator);
                    } else {
                        return;
                    }
                } else {
                    animator = (Animator) animatiorMap.get(animationKey);
                }
                v.mAnimator = animator;
                animator.start();
            } else if (animator2 == v.mAnimator) {
                v.getAnimatorUpdateListener().play();
            }
        }
    }

    public static void stopAnimation(ViewCell viewCell) {
        if (viewCell.getAnimatorUpdateListener() != null) {
            viewCell.getAnimatorUpdateListener().play();
        }
        Animator animator = viewCell.mAnimator;
        if (animator != null) {
            animator.cancel();
            if (viewCell.getAnimatorUpdateListener() != null) {
                viewCell.getAnimatorUpdateListener().mStop = true;
            }
        }
        viewCell.resetViewCell();
    }

    public static void pauseAnimation(ViewCell component) {
        Animator animator = component.mAnimator;
        if (animator != null && animator.isRunning() && component.getAnimatorUpdateListener() != null) {
            component.getAnimatorUpdateListener().pause();
        }
    }

    public static void playAnimation(ViewCell viewCell) {
        Animator animator;
        if (BookSetting.IS_CLOSED) {
            animatiorMap.clear();
        } else if (viewCell.getAnimatorUpdateListener() != null) {
            viewCell.getAnimatorUpdateListener().mStop = false;
            AnimationKey animationKey = new AnimationKey(viewCell);
            Animator animator2 = (Animator) animatiorMap.get(animationKey);
            if (viewCell.getAnimatorUpdateListener() == null || !viewCell.getAnimatorUpdateListener().isPause() || animator2 != viewCell.mAnimator) {
                if (animator2 != null) {
                    if (viewCell.getAnimatorUpdateListener() != null) {
                        viewCell.getAnimatorUpdateListener().play();
                    }
                    Animator animator1 = viewCell.mAnimator;
                    if (animator1 != null) {
                        animator1.cancel();
                        viewCell.getAnimatorUpdateListener().mStop = true;
                    }
                    viewCell.resetViewCell();
                }
                if (animatiorMap.containsKey(animationKey)) {
                    animator = (Animator) animatiorMap.get(animationKey);
                } else {
                    AnimatorSet animatorSet = new AnimatorSet();
                    ArrayList<Animator> animatorList = new ArrayList<>();
                    for (int i = 0; i < viewCell.getEntity().getAnims().size(); i++) {
                        Animator aniator = AnimationFactory.getAnimator(viewCell, (AnimationEntity) viewCell.getEntity().getAnims().get(i), viewCell.getViewRecord());
                        if (aniator != null) {
                            if (animatorList != null) {
                                animatorList.add(aniator);
                            }
                            AnimationKey animationKeyat = new AnimationKey(viewCell, i);
                            if (!animatiorMap.containsKey(animationKeyat)) {
                                animatiorMap.put(animationKeyat, aniator);
                            }
                        }
                    }
                    animatorSet.addListener(new AnimatorSetListener(viewCell));
                    animatorSet.playSequentially(animatorList);
                    animatiorMap.put(animationKey, animatorSet);
                    animator = animatorSet;
                }
                viewCell.mAnimator = animator;
                animator.start();
                return;
            }
            viewCell.getAnimatorUpdateListener().play();
        }
    }
}
