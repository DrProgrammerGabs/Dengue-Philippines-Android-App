package com.p000hl.android.view.pageflip.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* renamed from: com.hl.android.view.pageflip.animation.Rotate3dAnimation */
public class Rotate3dAnimation extends Animation {
    private Camera mCamera;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final float mFromDegrees;
    private final boolean mReverse;
    private final float mToDegrees;

    public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, boolean reverse) {
        this.mFromDegrees = fromDegrees;
        this.mToDegrees = toDegrees;
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mDepthZ = depthZ;
        this.mReverse = reverse;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mCamera = new Camera();
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees = this.mFromDegrees;
        float degrees = fromDegrees + ((this.mToDegrees - fromDegrees) * interpolatedTime);
        float centerX = this.mCenterX;
        float centerY = this.mCenterY;
        Camera camera = this.mCamera;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (this.mReverse) {
            camera.translate(0.0f, 0.0f, 0.0f);
        } else {
            camera.translate(0.0f, 0.0f, 0.0f);
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
