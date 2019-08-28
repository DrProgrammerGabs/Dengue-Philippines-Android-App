package com.p000hl.android.view.gallary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.C0048R;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.gallary.base.AbstractGalley;
import com.p000hl.android.view.gallary.base.ImageManager;

/* renamed from: com.hl.android.view.gallary.Galley3D */
public class Galley3D extends AbstractGalley {
    int height;
    private Camera mCamera = new Camera();
    private int mCoveflowCenter;
    private int mMaxRotationAngle = 60;
    private int mMaxZoom = -300;
    int width;

    public Galley3D(Context context) {
        super(context);
        setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
        return this.mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
        this.mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
        return this.mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
        return (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    /* access modifiers changed from: protected */
    public boolean getChildStaticTransformation(View child, Transformation t) {
        int childCenter = getCenterOfView(child);
        int childWidth = child.getWidth();
        t.clear();
        t.setTransformationType(2);
        if (childCenter == this.mCoveflowCenter) {
            transformImageBitmap((ImageView) child, t, 0);
        } else {
            int rotationAngle = (int) ((((float) (this.mCoveflowCenter - childCenter)) / ((float) childWidth)) * ((float) this.mMaxRotationAngle));
            if (Math.abs(rotationAngle) > this.mMaxRotationAngle) {
                rotationAngle = rotationAngle < 0 ? -this.mMaxRotationAngle : this.mMaxRotationAngle;
            }
            transformImageBitmap((ImageView) child, t, rotationAngle);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
        this.mCamera.save();
        Matrix imageMatrix = t.getMatrix();
        int imageHeight = child.getLayoutParams().height;
        int imageWidth = child.getLayoutParams().width;
        int rotation = Math.abs(rotationAngle);
        this.mCamera.translate(0.0f, 0.0f, 100.0f);
        if (rotation < this.mMaxRotationAngle) {
            this.mCamera.translate(0.0f, 0.0f, (float) (((double) this.mMaxZoom) + (((double) rotation) * 1.5d)));
        }
        this.mCamera.rotateY((float) rotationAngle);
        this.mCamera.getMatrix(imageMatrix);
        imageMatrix.preTranslate((float) (-(imageWidth / 2)), (float) (-(imageHeight / 2)));
        imageMatrix.postTranslate((float) (imageWidth / 2), (float) (imageHeight / 2));
        this.mCamera.restore();
    }

    /* access modifiers changed from: protected */
    public Bitmap getBitmap(String resourceID, int width2, int height2) {
        return ImageManager.createReflectionImageWithOrigin(BitmapUtils.getBitMap(resourceID, this.mContext, width2, height2));
    }

    public LayoutParams getGalleryLp() {
        LayoutParams galleryLp = new LayoutParams(BookSetting.SCREEN_WIDTH, BookSetting.SNAPSHOTS_HEIGHT);
        galleryLp.addRule(14);
        galleryLp.addRule(12);
        return galleryLp;
    }

    public void playAnimation() {
    }

    /* access modifiers changed from: protected */
    public void setWaitLoad(ImageView img) {
        img.setImageResource(C0048R.drawable.scene_ic_loading_invert_3d);
    }

    /* access modifiers changed from: protected */
    public float getSizeRatio() {
        return 0.5f;
    }
}
