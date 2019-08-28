package com.p000hl.android.view.component.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import java.util.ArrayList;

@SuppressLint({"ViewConstructor"})
/* renamed from: com.hl.android.view.component.moudle.SubSliderSliceComponent */
public class SubSliderSliceComponent extends View {
    private float halfDistanceTomove;
    public boolean isMoveToDown;
    public boolean isMoveToUp;
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> mBitmaps;
    private float mDistanceTomove;
    private boolean mIsRight;
    /* access modifiers changed from: private */
    public Paint mPaint;
    /* access modifiers changed from: private */
    public float mPosition;
    private boolean mShouldInitNext;
    /* access modifiers changed from: private */
    public float mSingleHeight;
    /* access modifiers changed from: private */
    public float mSingleWidth;
    private float moveSpeed = 1.0f;
    public MyRect myRect1;
    public MyRect myRect2;
    public MyRect myRect3;
    public ArrayList<MyRect> rects;

    /* renamed from: com.hl.android.view.component.moudle.SubSliderSliceComponent$MyRect */
    class MyRect {
        private RectF dstRect;
        public Bitmap mDrawBitmap;
        public int mIndexOfBitmaps;
        public int mIntdexOfPositon;
        private Rect srcRect;

        public MyRect(int indexOfBitmaps, int intdexOfPositon) {
            this.mIndexOfBitmaps = indexOfBitmaps;
            this.mIntdexOfPositon = intdexOfPositon;
        }

        /* access modifiers changed from: private */
        public void drawMe(Canvas canvas) {
            this.mDrawBitmap = (Bitmap) SubSliderSliceComponent.this.mBitmaps.get(this.mIndexOfBitmaps);
            this.srcRect = new Rect(0, 0, this.mDrawBitmap.getWidth(), this.mDrawBitmap.getHeight());
            this.dstRect = new RectF((float) SubSliderSliceComponent.this.getPaddingLeft(), ((float) SubSliderSliceComponent.this.getPaddingTop()) + SubSliderSliceComponent.this.mPosition + (SubSliderSliceComponent.this.mSingleHeight * ((float) this.mIntdexOfPositon)), ((float) SubSliderSliceComponent.this.getPaddingLeft()) + SubSliderSliceComponent.this.mSingleWidth, ((float) SubSliderSliceComponent.this.getPaddingTop()) + SubSliderSliceComponent.this.mPosition + (SubSliderSliceComponent.this.mSingleHeight * ((float) this.mIntdexOfPositon)) + SubSliderSliceComponent.this.mSingleHeight);
            canvas.drawBitmap(this.mDrawBitmap, this.srcRect, this.dstRect, SubSliderSliceComponent.this.mPaint);
        }
    }

    public SubSliderSliceComponent(Context context, ArrayList<Bitmap> bitmaps, float width, float height, boolean isRight) {
        super(context);
        this.mBitmaps = bitmaps;
        this.mSingleWidth = width;
        this.mSingleHeight = height;
        this.mPosition = -this.mSingleHeight;
        this.mIsRight = isRight;
        this.rects = new ArrayList<>();
        if (isRight) {
            this.myRect1 = new MyRect(1, 0);
            this.myRect2 = new MyRect(0, 1);
            this.myRect3 = new MyRect(bitmaps.size() - 1, 2);
        } else {
            this.myRect1 = new MyRect(bitmaps.size() - 1, 0);
            this.myRect2 = new MyRect(0, 1);
            this.myRect3 = new MyRect(1, 2);
        }
        this.rects.add(this.myRect1);
        this.rects.add(this.myRect2);
        this.rects.add(this.myRect3);
        this.mPaint = new Paint();
    }

    public void setposition(float position) {
        this.mPosition = position;
    }

    public float getPosition() {
        return this.mPosition;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        for (int i = 0; i < this.rects.size(); i++) {
            ((MyRect) this.rects.get(i)).drawMe(canvas);
        }
        logic();
        postInvalidate();
    }

    private void logic() {
        if (this.isMoveToUp) {
            if (this.mDistanceTomove <= 0.0f || Math.abs(this.mDistanceTomove - this.moveSpeed) < this.moveSpeed) {
                this.moveSpeed = 30.0f;
                this.isMoveToUp = false;
                this.mDistanceTomove = 0.0f;
                setposition(-this.mSingleHeight);
                if (this.mShouldInitNext) {
                    initBitmap(true);
                    return;
                }
                return;
            }
            this.mPosition -= this.moveSpeed;
            this.mDistanceTomove -= this.moveSpeed;
            if (this.mDistanceTomove >= this.halfDistanceTomove) {
                this.moveSpeed += 2.0f;
            } else {
                this.moveSpeed -= 1.0f;
            }
        } else if (!this.isMoveToDown) {
        } else {
            if (this.mDistanceTomove <= 0.0f || Math.abs(this.mDistanceTomove - this.moveSpeed) < this.moveSpeed) {
                this.moveSpeed = 30.0f;
                this.isMoveToDown = false;
                this.mDistanceTomove = 0.0f;
                setposition(-this.mSingleHeight);
                if (this.mShouldInitNext) {
                    initBitmap(false);
                    return;
                }
                return;
            }
            this.mPosition += this.moveSpeed;
            this.mDistanceTomove -= this.moveSpeed;
            if (this.mDistanceTomove >= this.halfDistanceTomove) {
                this.moveSpeed += 2.0f;
            } else {
                this.moveSpeed -= 1.0f;
            }
        }
    }

    private void initBitmap(boolean isMoveToUp2) {
        if (isMoveToUp2) {
            this.myRect1.mIndexOfBitmaps = this.myRect2.mIndexOfBitmaps;
            this.myRect2.mIndexOfBitmaps = this.myRect3.mIndexOfBitmaps;
            if (!this.mIsRight) {
                this.myRect3.mIndexOfBitmaps++;
                if (this.myRect3.mIndexOfBitmaps >= this.mBitmaps.size()) {
                    this.myRect3.mIndexOfBitmaps = 0;
                    return;
                }
                return;
            }
            this.myRect3.mIndexOfBitmaps--;
            if (this.myRect3.mIndexOfBitmaps < 0) {
                this.myRect3.mIndexOfBitmaps = this.mBitmaps.size() - 1;
                return;
            }
            return;
        }
        this.myRect3.mIndexOfBitmaps = this.myRect2.mIndexOfBitmaps;
        this.myRect2.mIndexOfBitmaps = this.myRect1.mIndexOfBitmaps;
        if (this.mIsRight) {
            this.myRect1.mIndexOfBitmaps++;
            if (this.myRect1.mIndexOfBitmaps >= this.mBitmaps.size()) {
                this.myRect1.mIndexOfBitmaps = 0;
                return;
            }
            return;
        }
        this.myRect1.mIndexOfBitmaps--;
        if (this.myRect1.mIndexOfBitmaps < 0) {
            this.myRect1.mIndexOfBitmaps = this.mBitmaps.size() - 1;
        }
    }

    public void MoveToUp(float distance, boolean shouldInitNext) {
        this.mDistanceTomove = distance;
        this.halfDistanceTomove = this.mDistanceTomove / 2.0f;
        this.mShouldInitNext = shouldInitNext;
        this.isMoveToUp = true;
    }

    public void MoveToDown(float distance, boolean shouldInitNext) {
        this.mDistanceTomove = distance;
        this.halfDistanceTomove = this.mDistanceTomove / 2.0f;
        this.mShouldInitNext = shouldInitNext;
        this.isMoveToDown = true;
    }
}
