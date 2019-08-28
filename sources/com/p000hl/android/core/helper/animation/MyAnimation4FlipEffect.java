package com.p000hl.android.core.helper.animation;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/* renamed from: com.hl.android.core.helper.animation.MyAnimation4FlipEffect */
public class MyAnimation4FlipEffect extends Animation {
    private boolean changeBitmap;
    private Camera mCamera = new Camera();
    private String mDdirection;
    private final float mFromDegree;
    private int mHeight;
    private ImageView mImageView;
    private Bitmap mNextBitmap;
    private final float mToDegree;
    private int mWidth;

    public MyAnimation4FlipEffect(float fromDegree, float toDegree, ImageView imageView, Bitmap nextBitmap, String direction) {
        this.mFromDegree = fromDegree;
        this.mToDegree = toDegree;
        this.mImageView = imageView;
        this.mNextBitmap = nextBitmap;
        this.mDdirection = direction;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mWidth = width;
        this.mHeight = height;
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();
        float cameraty = 200.0f * interpolatedTime;
        float degree = this.mFromDegree + ((this.mToDegree - this.mFromDegree) * interpolatedTime);
        if (interpolatedTime >= 0.5f) {
            if (!this.changeBitmap) {
                this.changeBitmap = true;
                this.mImageView.setImageBitmap(this.mNextBitmap);
                this.mImageView.setScaleType(ScaleType.FIT_XY);
            }
            degree -= 180.0f;
            cameraty = 200.0f * (1.0f - interpolatedTime);
        }
        Log.d("ww", "degree:" + degree);
        this.mCamera.save();
        this.mCamera.translate(0.0f, 0.0f, cameraty);
        if (this.mDdirection.equals("left")) {
            this.mCamera.rotateY(-degree);
        } else if (this.mDdirection.equals("right")) {
            this.mCamera.rotateY(degree);
        } else if (this.mDdirection.equals("up")) {
            this.mCamera.rotateX(degree);
        } else if (this.mDdirection.equals("down")) {
            this.mCamera.rotateX(-degree);
        }
        this.mCamera.getMatrix(matrix);
        this.mCamera.restore();
        matrix.preTranslate(((float) (-this.mWidth)) / 2.0f, ((float) (-this.mHeight)) / 2.0f);
        matrix.postTranslate(((float) this.mWidth) / 2.0f, ((float) this.mHeight) / 2.0f);
    }
}
