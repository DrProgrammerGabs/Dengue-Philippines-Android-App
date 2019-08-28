package com.p000hl.android.core.helper.animation;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.SeniorAnimationEntity;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.bean.ViewRecord;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.core.helper.animation.AnimationFactory */
public class AnimationFactory {
    public static ObjectAnimator getAnimator(ViewCell v, AnimationEntity entity, ViewRecord record) {
        ObjectAnimator animator;
        float scale;
        String animationType = entity.AnimationType;
        AnimatorListener4Item listener = new AnimatorListener4Item(v, v.getEntity().getAnims().indexOf(entity));
        listener.setRecord(record);
        boolean isout = "OUT_FLAG".equals(entity.AnimationEnterOrQuit);
        boolean iskeep = Boolean.parseBoolean(entity.IsKeep);
        if (StringUtils.isEmpty(animationType)) {
            animationType = "";
        }
        int cnt = Integer.parseInt(entity.Repeat) - 1;
        long delay = Long.parseLong(entity.Delay);
        long duration = Long.parseLong(entity.Duration);
        if (animationType.equals("ANIMATION_FADEOUT")) {
            animator = ObjectAnimator.ofFloat(v, "alpha", new float[]{v.getEntity().alpha, 0.0f});
        } else if (animationType.equals("ANIMATION_FADEIN")) {
            animator = ObjectAnimator.ofFloat(v, "alpha", new float[]{0.0f, v.getEntity().alpha});
        } else if (animationType.equals("MOVE_UP")) {
            float dis = Float.valueOf(entity.CustomProperties).floatValue();
            animator = ObjectAnimator.ofFloat(v, "y", new float[]{record.f29mY, record.f29mY - ScreenUtils.getVerScreenValue(dis)});
            if (iskeep) {
                record.f29mY -= ScreenUtils.getVerScreenValue(dis);
            }
        } else if (animationType.equals("MOVE_DOWN")) {
            float dis2 = Float.valueOf(entity.CustomProperties).floatValue();
            animator = ObjectAnimator.ofFloat(v, "y", new float[]{record.f29mY, record.f29mY + ScreenUtils.getVerScreenValue(dis2)});
            if (iskeep) {
                record.f29mY += ScreenUtils.getVerScreenValue(dis2);
            }
        } else if (animationType.equals("MOVE_LEFT")) {
            float dis3 = Float.valueOf(entity.CustomProperties).floatValue();
            animator = ObjectAnimator.ofFloat(v, "x", new float[]{record.f28mX, record.f28mX - ScreenUtils.getHorScreenValue(dis3)});
            if (iskeep) {
                record.f28mX -= ScreenUtils.getHorScreenValue(dis3);
            }
        } else if (animationType.equals("MOVE_RIGHT")) {
            float dis4 = Float.valueOf(entity.CustomProperties).floatValue();
            animator = ObjectAnimator.ofFloat(v, "x", new float[]{record.f28mX, record.f28mX + ScreenUtils.getHorScreenValue(dis4)});
            if (iskeep) {
                record.f28mX += ScreenUtils.getHorScreenValue(dis4);
            }
        } else if (animationType.equals("ANIMATION_ZOOMOUT")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleX", new float[]{0.1f, record.mScaleX}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.1f, record.mScaleY}), PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, v.getEntity().alpha})});
        } else if (animationType.equals("ANIMATION_ZOOMIN")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleX", new float[]{record.mScaleX, 0.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{record.mScaleY, 0.0f}), PropertyValuesHolder.ofFloat("alpha", new float[]{v.getEntity().alpha, 0.0f})});
        } else if (animationType.equals("ANIMATION_SPIN")) {
            float rotate = Float.valueOf(entity.CustomProperties).floatValue();
            animator = ObjectAnimator.ofFloat(v, "rotation", new float[]{0.0f, rotate});
            if (iskeep) {
                record.mRotation = rotate % 360.0f;
            }
        } else if (animationType.equals("WIPEOUT_LEFT")) {
            if (isout) {
                animator = ObjectAnimator.ofInt(v, "leftPadding", new int[]{0, record.mWidth});
            } else {
                animator = ObjectAnimator.ofInt(v, "leftPadding", new int[]{record.mWidth, 0});
            }
        } else if (animationType.equals("WIPEOUT_UP")) {
            if (isout) {
                animator = ObjectAnimator.ofInt(v, "topPadding", new int[]{0, record.mHeight});
            } else {
                animator = ObjectAnimator.ofInt(v, "topPadding", new int[]{record.mHeight, 0});
            }
        } else if (animationType.equals("WIPEOUT_DOWN")) {
            if (isout) {
                animator = ObjectAnimator.ofInt(v, "bottomPadding", new int[]{0, record.mHeight});
            } else {
                animator = ObjectAnimator.ofInt(v, "bottomPadding", new int[]{record.mHeight, 0});
            }
        } else if (animationType.equals("WIPEOUT_RIGHT")) {
            if (isout) {
                animator = ObjectAnimator.ofInt(v, "rightPadding", new int[]{0, record.mWidth});
            } else {
                animator = ObjectAnimator.ofInt(v, "rightPadding", new int[]{record.mWidth, 0});
            }
        } else if (animationType.startsWith("FLOATIN")) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, v.getEntity().alpha});
            float value = Float.parseFloat(entity.CustomProperties);
            PropertyValuesHolder pos = null;
            if (animationType.contains("UP")) {
                pos = PropertyValuesHolder.ofFloat("y", new float[]{record.f29mY + ScreenUtils.getVerScreenValue(value), record.f29mY});
            } else if (animationType.contains("DOWN")) {
                pos = PropertyValuesHolder.ofFloat("y", new float[]{record.f29mY - ScreenUtils.getVerScreenValue(value), record.f29mY});
            } else if (animationType.contains("LEFT")) {
                pos = PropertyValuesHolder.ofFloat("x", new float[]{record.f28mX + ScreenUtils.getHorScreenValue(value), record.f28mX});
            } else if (animationType.contains("RIGHT")) {
                pos = PropertyValuesHolder.ofFloat("x", new float[]{record.f28mX - ScreenUtils.getHorScreenValue(value), record.f28mX});
            }
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{alpha, pos});
        } else if (animationType.startsWith("FLOATOUT")) {
            PropertyValuesHolder alpha2 = PropertyValuesHolder.ofFloat("alpha", new float[]{v.getEntity().alpha, 0.0f});
            float value2 = Float.parseFloat(entity.CustomProperties);
            PropertyValuesHolder pos2 = null;
            if (animationType.contains("UP")) {
                pos2 = PropertyValuesHolder.ofFloat("y", new float[]{record.f29mY, record.f29mY - ScreenUtils.getVerScreenValue(value2)});
                if (iskeep) {
                    record.f29mY -= ScreenUtils.getVerScreenValue(value2);
                }
            } else if (animationType.contains("DOWN")) {
                pos2 = PropertyValuesHolder.ofFloat("y", new float[]{record.f29mY, record.f29mY + ScreenUtils.getVerScreenValue(value2)});
                if (iskeep) {
                    record.f29mY += ScreenUtils.getVerScreenValue(value2);
                }
            } else if (animationType.contains("RIGHT")) {
                pos2 = PropertyValuesHolder.ofFloat("x", new float[]{record.f28mX, record.f28mX + ScreenUtils.getHorScreenValue(value2)});
                if (iskeep) {
                    record.f28mX += ScreenUtils.getHorScreenValue(value2);
                }
            } else if (animationType.contains("LEFT")) {
                pos2 = PropertyValuesHolder.ofFloat("x", new float[]{record.f28mX, record.f28mX - ScreenUtils.getHorScreenValue(value2)});
                if (iskeep) {
                    record.f28mX -= ScreenUtils.getHorScreenValue(value2);
                }
            }
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{alpha2, pos2});
        } else if (animationType.equals("ANIMATION_ROTATEIN")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, v.getEntity().alpha}), PropertyValuesHolder.ofFloat("myRotationY", new float[]{-180.0f, 360.0f})});
        } else if (animationType.equals("ANIMATION_ROTATEOUT")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{v.getEntity().alpha, 0.0f}), PropertyValuesHolder.ofFloat("myRotationY", new float[]{360.0f, -180.0f})});
        } else if (animationType.equals("ANIMATION_TURNIN")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, v.getEntity().alpha}), PropertyValuesHolder.ofFloat("rotation", new float[]{90.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.1f, record.mScaleX}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.1f, record.mScaleY})});
        } else if (animationType.equals("ANIMATION_TURNOUT")) {
            animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{v.getEntity().alpha, 0.0f}), PropertyValuesHolder.ofFloat("rotation", new float[]{0.0f, 90.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{record.mScaleX, 0.1f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{record.mScaleY, 0.1f})});
        } else if (animationType.equals("ANIMATION_SEESAW")) {
            animator = ObjectAnimator.ofFloat(v, "rotation", new float[]{0.0f, 15.0f, 0.0f, -15.0f, 0.0f, 15.0f, 0.0f, -15.0f, 0.0f});
        } else if (animationType.startsWith("SCALE")) {
            String properties = entity.CustomProperties;
            if (properties.equals("Min")) {
                scale = 0.3f;
            } else if (properties.equals("MinMax")) {
                scale = 0.16f;
            } else if (properties.equals("Max")) {
                scale = 3.0f;
            } else if (properties.equals("MaxMax")) {
                scale = 6.0f;
            } else {
                scale = Float.valueOf(properties).floatValue();
            }
            if (animationType.endsWith("ALL")) {
                PropertyValuesHolder scaleValueX = PropertyValuesHolder.ofFloat("scaleX", new float[]{record.mScaleX, record.mScaleX * scale});
                PropertyValuesHolder scaleValueY = PropertyValuesHolder.ofFloat("scaleY", new float[]{record.mScaleY, record.mScaleY * scale});
                if (iskeep) {
                    record.mScaleX *= scale;
                }
                if (iskeep) {
                    record.mScaleY *= scale;
                }
                animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{scaleValueX, scaleValueY});
            } else if (animationType.endsWith("HORZ")) {
                animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleX", new float[]{record.mScaleX, record.mScaleX * scale})});
                if (iskeep) {
                    record.mScaleX *= scale;
                }
            } else {
                animator = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleY", new float[]{record.mScaleY, record.mScaleY * scale})});
                if (iskeep) {
                    record.mScaleY *= scale;
                }
            }
        } else if (!animationType.equals("ANIMATION_SENIOR")) {
            return null;
        } else {
            animator = getSeniorAnimator(v, entity, record);
        }
        animator.setRepeatCount(cnt);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.addListener(listener);
        animator.setInterpolator(new HLInterpolator(entity.EaseType));
        if (v.getAnimatorUpdateListener() != null) {
            animator.addUpdateListener(v.getAnimatorUpdateListener());
        }
        return animator;
    }

    private static ObjectAnimator getSeniorAnimator(ViewCell v, AnimationEntity entity, ViewRecord record) {
        ViewRecord recordCopy = record.getClone();
        long duration = Long.parseLong(entity.Duration);
        Keyframe[] alphaList = new Keyframe[(entity.hEntitys.size() + 1)];
        Keyframe[] rotateList = (Keyframe[]) alphaList.clone();
        Keyframe[] xList = (Keyframe[]) alphaList.clone();
        Keyframe[] yList = (Keyframe[]) alphaList.clone();
        Keyframe[] widthList = (Keyframe[]) alphaList.clone();
        Keyframe[] heightList = (Keyframe[]) alphaList.clone();
        alphaList[0] = Keyframe.ofFloat(0.0f, v.getEntity().alpha);
        rotateList[0] = Keyframe.ofFloat(0.0f, recordCopy.mRotation);
        xList[0] = Keyframe.ofFloat(0.0f, recordCopy.f28mX);
        yList[0] = Keyframe.ofFloat(0.0f, recordCopy.f29mY);
        widthList[0] = Keyframe.ofFloat(0.0f, recordCopy.mScaleX);
        heightList[0] = Keyframe.ofFloat(0.0f, recordCopy.mScaleY);
        int time = 0;
        float lastCenterX = recordCopy.f28mX + ((float) (v.getLayoutParams().width / 2));
        float lastCenterY = recordCopy.f29mY + ((float) (v.getLayoutParams().height / 2));
        for (int index = 0; index < entity.hEntitys.size(); index++) {
            SeniorAnimationEntity animEntity = (SeniorAnimationEntity) entity.hEntitys.get(index);
            float ratioMX = ScreenUtils.getHorScreenValue(animEntity.f17mX);
            float ratioMY = ScreenUtils.getVerScreenValue(animEntity.f18mY);
            float ratioMWidth = ScreenUtils.getHorScreenValue(animEntity.mWidth);
            float ratioMHeight = ScreenUtils.getVerScreenValue(animEntity.mHeight);
            time = (int) (((float) time) + animEntity.mDuration);
            float framePoint = ((float) time) / ((float) duration);
            alphaList[index + 1] = Keyframe.ofFloat(framePoint, animEntity.mAlpha);
            Keyframe rotateFrame = Keyframe.ofFloat(framePoint, animEntity.mDegree);
            recordCopy.mRotation = animEntity.mDegree;
            rotateList[index + 1] = rotateFrame;
            float ratio = ratioMWidth / ((float) v.getLayoutParams().width);
            widthList[index + 1] = Keyframe.ofFloat(framePoint, ratio);
            recordCopy.mScaleX = ratio;
            float ratio2 = ratioMHeight / ((float) v.getLayoutParams().height);
            heightList[index + 1] = Keyframe.ofFloat(framePoint, ratio2);
            recordCopy.mScaleY = ratio2;
            float nX = (((float) (((((double) ratioMX) - (((double) (ratioMHeight / 2.0f)) * Math.sin((((double) animEntity.mDegree) * 3.141592653589793d) / 180.0d))) - ((double) (ratioMWidth / 2.0f))) + (((double) (ratioMWidth / 2.0f)) * Math.cos((((double) animEntity.mDegree) * 3.141592653589793d) / 180.0d)))) + (ratioMWidth / 2.0f)) - lastCenterX;
            float nY = (((float) (((((double) ratioMY) + (((double) (ratioMWidth / 2.0f)) * Math.sin((((double) animEntity.mDegree) * 3.141592653589793d) / 180.0d))) - ((double) (ratioMHeight / 2.0f))) + (((double) (ratioMHeight / 2.0f)) * Math.cos((((double) animEntity.mDegree) * 3.141592653589793d) / 180.0d)))) + (ratioMHeight / 2.0f)) - lastCenterY;
            Keyframe xFrame = Keyframe.ofFloat(framePoint, recordCopy.f28mX + nX);
            recordCopy.f28mX += nX;
            xList[index + 1] = xFrame;
            Keyframe yFrame = Keyframe.ofFloat(framePoint, recordCopy.f29mY + nY);
            recordCopy.f29mY += nY;
            yList[index + 1] = yFrame;
            lastCenterX += nX;
            lastCenterY += nY;
        }
        return ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofKeyframe("alpha", alphaList), PropertyValuesHolder.ofKeyframe("superRotation", rotateList), PropertyValuesHolder.ofKeyframe("x", xList), PropertyValuesHolder.ofKeyframe("y", yList), PropertyValuesHolder.ofKeyframe("scaleX", widthList), PropertyValuesHolder.ofKeyframe("scaleY", heightList)});
    }
}
