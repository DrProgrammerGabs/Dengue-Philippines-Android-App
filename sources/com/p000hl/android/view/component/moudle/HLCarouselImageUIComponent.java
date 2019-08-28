package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Gallery;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.moudle.carouseimg.GalleryImageAdapter;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.moudle.HLCarouselImageUIComponent */
public class HLCarouselImageUIComponent extends Gallery implements Component {
    private Camera mCamera = new Camera();
    private Context mContext;
    private int mCoveflowCenter;
    private MoudleComponentEntity mEntity;
    private int mHeight;
    private Matrix mMatrix = new Matrix();
    private float mMaxRotationAngle = 30.0f;
    private int mWidth;

    public HLCarouselImageUIComponent(Context context) {
        super(context);
        this.mContext = context;
        setStaticTransformationsEnabled(true);
        setChildrenDrawingOrderEnabled(true);
    }

    public HLCarouselImageUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (MoudleComponentEntity) entity;
        setStaticTransformationsEnabled(true);
        setChildrenDrawingOrderEnabled(true);
    }

    /* access modifiers changed from: protected */
    public int getCenterOfCoverflow() {
        return (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2) + getPaddingLeft();
    }

    /* access modifiers changed from: protected */
    public int getCenterOfView(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = getSelectedItemPosition() - getFirstVisiblePosition();
        if (selectedIndex >= 0 && i >= selectedIndex && i >= selectedIndex) {
            return ((childCount - 1) - i) + selectedIndex;
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public float calculateOffsetOfCenter(View view) {
        int pCenter = getCenterOfCoverflow();
        return Math.max(Math.min(((float) (getCenterOfView(view) - pCenter)) / (((float) pCenter) * 1.0f), 1.0f), -1.0f);
    }

    /* access modifiers changed from: protected */
    public boolean getChildStaticTransformation(View child, Transformation t) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        getTransformationMatrix(child, calculateOffsetOfCenter(child));
        int saveCount = canvas.save();
        canvas.concat(this.mMatrix);
        boolean ret = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(saveCount);
        return ret;
    }

    /* access modifiers changed from: 0000 */
    public void getTransformationMatrix(View child, float offset) {
        float rotationAngle;
        int halfWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);
        int halfHeight = child.getMeasuredHeight() >> 1;
        int childCenter = getCenterOfView(child);
        int childWidth = child.getWidth();
        if (childCenter == this.mCoveflowCenter) {
            rotationAngle = 0.0f;
        } else {
            rotationAngle = (float) ((int) ((((float) (this.mCoveflowCenter - childCenter)) / ((float) childWidth)) * this.mMaxRotationAngle));
            if (Math.abs(rotationAngle) > this.mMaxRotationAngle) {
                rotationAngle = rotationAngle < 0.0f ? -this.mMaxRotationAngle : this.mMaxRotationAngle;
            }
        }
        this.mCamera.save();
        this.mCamera.translate(((-offset) * ((float) this.mWidth)) / 5.0f, 0.0f, (Math.abs(offset) * ((float) this.mWidth)) / 5.0f);
        this.mCamera.rotateY(rotationAngle);
        this.mCamera.getMatrix(this.mMatrix);
        this.mCamera.restore();
        this.mMatrix.preTranslate((float) (-halfWidth), (float) (-halfHeight));
        this.mMatrix.postTranslate((float) halfWidth, (float) halfHeight);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.mWidth = this.mEntity.getItemWidth();
        this.mHeight = this.mEntity.getItemHeight();
        this.mWidth = (int) ScreenUtils.getHorScreenValue((float) this.mWidth);
        this.mHeight = (int) ScreenUtils.getVerScreenValue((float) this.mHeight);
        setAdapter(new GalleryImageAdapter(this.mContext, this.mEntity.getSourceIDList(), new LayoutParams(this.mWidth, this.mHeight)));
        int selection = 1073741823;
        while (selection % this.mEntity.getSourceIDList().size() != 0) {
            selection++;
        }
        setSelection(selection);
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }
}
