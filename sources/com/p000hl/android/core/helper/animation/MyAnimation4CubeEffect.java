package com.p000hl.android.core.helper.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* renamed from: com.hl.android.core.helper.animation.MyAnimation4CubeEffect */
public class MyAnimation4CubeEffect extends Animation {
    private Camera mCamera = new Camera();
    private String mDdirection;
    private final float mFromDegree;
    private int mHalfHeight;
    private int mHalfWidth;
    private final float mToDegree;

    public MyAnimation4CubeEffect(float fromDegree, float toDegree, String direction) {
        this.mFromDegree = fromDegree;
        this.mToDegree = toDegree;
        this.mDdirection = direction;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mHalfWidth = width / 2;
        this.mHalfHeight = height / 2;
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();
        float degree = this.mFromDegree + ((this.mToDegree - this.mFromDegree) * interpolatedTime);
        this.mCamera.save();
        if (degree >= 82.0f) {
            if (this.mDdirection.equals("left") || this.mDdirection.equals("right")) {
                this.mCamera.rotateY(90.0f);
            } else {
                this.mCamera.rotateX(-90.0f);
            }
        } else if (degree <= -82.0f) {
            if (this.mDdirection.equals("left") || this.mDdirection.equals("right")) {
                this.mCamera.rotateY(90.0f);
            } else {
                this.mCamera.rotateX(-90.0f);
            }
        } else if (this.mDdirection.equals("left") || this.mDdirection.equals("right")) {
            this.mCamera.translate(0.0f, 0.0f, (float) this.mHalfWidth);
            this.mCamera.rotateY(degree);
            this.mCamera.translate(0.0f, 0.0f, (float) (-this.mHalfWidth));
        } else {
            this.mCamera.translate(0.0f, 0.0f, (float) this.mHalfHeight);
            this.mCamera.rotateX(-degree);
            this.mCamera.translate(0.0f, 0.0f, (float) (-this.mHalfHeight));
        }
        this.mCamera.getMatrix(matrix);
        this.mCamera.restore();
        matrix.preTranslate((float) (-this.mHalfWidth), (float) (-this.mHalfHeight));
        matrix.postTranslate((float) this.mHalfWidth, (float) this.mHalfHeight);
    }
}
