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
import java.util.LinkedList;

@SuppressLint({"ViewConstructor"})
/* renamed from: com.hl.android.view.component.moudle.SubCatalogVScrollUIComponent */
public class SubCatalogVScrollUIComponent extends View {
    private int AGroupRectsLengthY;
    private float curFlingSpeed;
    private int hasStartCount;
    private int lastPosition;
    private ArrayList<Bitmap> mBitmaps;
    private float mHeight;
    private ArrayList<Integer> mIndexs;
    public boolean mIsMoveAuto;
    /* access modifiers changed from: private */
    public Paint mPaint = new Paint();
    /* access modifiers changed from: private */
    public float mWidth;
    private LinkedList<MyRect> rects;
    public boolean startBeginAnim;

    /* renamed from: com.hl.android.view.component.moudle.SubCatalogVScrollUIComponent$MyRect */
    class MyRect {
        private RectF dstRect;
        public Bitmap mDrawBitmap;
        public int mInHeight;
        public float mInWidth;
        public int mIndex;
        public int mPositionY = 0;
        private Rect srcRect;

        public MyRect(Bitmap bitmap4draw, int index, int positionY) {
            this.mDrawBitmap = bitmap4draw;
            this.mIndex = index;
            this.mPositionY = positionY;
            this.mInWidth = SubCatalogVScrollUIComponent.this.mWidth;
            this.mInHeight = (int) (((SubCatalogVScrollUIComponent.this.mWidth * 1.0f) / ((float) this.mDrawBitmap.getWidth())) * ((float) this.mDrawBitmap.getHeight()));
        }

        /* access modifiers changed from: private */
        public void drawMe(Canvas canvas) {
            this.srcRect = new Rect(0, 0, this.mDrawBitmap.getWidth(), this.mDrawBitmap.getHeight());
            this.dstRect = new RectF((float) SubCatalogVScrollUIComponent.this.getPaddingLeft(), (float) (SubCatalogVScrollUIComponent.this.getPaddingTop() + this.mPositionY), ((float) SubCatalogVScrollUIComponent.this.getPaddingLeft()) + this.mInWidth, (float) (SubCatalogVScrollUIComponent.this.getPaddingTop() + this.mPositionY + this.mInHeight));
            canvas.drawBitmap(this.mDrawBitmap, this.srcRect, this.dstRect, SubCatalogVScrollUIComponent.this.mPaint);
        }
    }

    public SubCatalogVScrollUIComponent(Context context, ArrayList<Bitmap> bitmaps, ArrayList<Integer> indexs, float width, float height) {
        super(context);
        this.mBitmaps = bitmaps;
        this.mIndexs = indexs;
        this.mWidth = width;
        this.mHeight = height;
        loadRects();
    }

    private void loadRects() {
        this.rects = new LinkedList<>();
        boolean hasLoadRects = false;
        while (!hasLoadRects) {
            loadAGroupRects();
            if (((float) this.lastPosition) >= this.mHeight + ((float) (this.AGroupRectsLengthY * 4))) {
                hasLoadRects = true;
            }
        }
    }

    private void loadAGroupRects() {
        MyRect myRect;
        for (int i = 0; i < this.mBitmaps.size(); i++) {
            if (i == 0) {
                myRect = new MyRect((Bitmap) this.mBitmaps.get(0), ((Integer) this.mIndexs.get(0)).intValue(), this.lastPosition);
            } else {
                myRect = new MyRect((Bitmap) this.mBitmaps.get(i), ((Integer) this.mIndexs.get(i)).intValue(), ((MyRect) this.rects.get(this.rects.size() - 1)).mInHeight + ((MyRect) this.rects.get(this.rects.size() - 1)).mPositionY);
            }
            this.rects.add(myRect);
        }
        if (this.lastPosition == 0) {
            this.lastPosition = ((MyRect) this.rects.get(this.rects.size() - 1)).mInHeight + ((MyRect) this.rects.get(this.rects.size() - 1)).mPositionY;
            this.AGroupRectsLengthY = this.lastPosition;
            return;
        }
        this.lastPosition = ((MyRect) this.rects.get(this.rects.size() - 1)).mInHeight + ((MyRect) this.rects.get(this.rects.size() - 1)).mPositionY;
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
        while (((MyRect) this.rects.getFirst()).mPositionY <= (-this.AGroupRectsLengthY)) {
            ((MyRect) this.rects.getFirst()).mPositionY = ((MyRect) this.rects.getLast()).mInHeight + ((MyRect) this.rects.getLast()).mPositionY;
            this.rects.addLast(this.rects.getFirst());
            this.rects.removeFirst();
        }
        while (true) {
            if (((float) (((MyRect) this.rects.getLast()).mInHeight + ((MyRect) this.rects.getLast()).mPositionY)) < this.mHeight + ((float) (this.AGroupRectsLengthY * 2))) {
                break;
            }
            ((MyRect) this.rects.getLast()).mPositionY = ((MyRect) this.rects.getFirst()).mPositionY - ((MyRect) this.rects.getLast()).mInHeight;
            this.rects.addFirst(this.rects.getLast());
            this.rects.removeLast();
        }
        if (this.startBeginAnim) {
            for (int i = 0; i < this.rects.size(); i++) {
                MyRect myRect = (MyRect) this.rects.get(i);
                myRect.mPositionY += this.AGroupRectsLengthY / 30;
                if (this.hasStartCount < this.AGroupRectsLengthY % 30) {
                    MyRect myRect2 = (MyRect) this.rects.get(i);
                    myRect2.mPositionY++;
                }
            }
            this.hasStartCount++;
            if (this.hasStartCount >= 30) {
                this.startBeginAnim = false;
                this.hasStartCount = 0;
            }
        }
        if (!this.mIsMoveAuto) {
            return;
        }
        if (this.curFlingSpeed > 0.0f) {
            int speed = (int) this.curFlingSpeed;
            for (int i2 = 0; i2 < this.rects.size(); i2++) {
                MyRect myRect3 = (MyRect) this.rects.get(i2);
                myRect3.mPositionY += speed;
            }
            this.curFlingSpeed *= 0.97f;
            if (((float) speed) <= 1.0f) {
                this.mIsMoveAuto = false;
            }
        } else if (this.curFlingSpeed < 0.0f) {
            int speed2 = (int) this.curFlingSpeed;
            for (int i3 = 0; i3 < this.rects.size(); i3++) {
                MyRect myRect4 = (MyRect) this.rects.get(i3);
                myRect4.mPositionY += speed2;
            }
            this.curFlingSpeed *= 0.97f;
            if (((float) speed2) >= -1.0f) {
                this.mIsMoveAuto = false;
            }
        }
    }

    public LinkedList<MyRect> getRects() {
        return this.rects;
    }

    public void moveAutoWidthSpeed(float flingSpeed) {
        this.curFlingSpeed = flingSpeed;
        this.mIsMoveAuto = true;
    }

    public void doBeginAnim() {
        this.startBeginAnim = true;
    }
}
