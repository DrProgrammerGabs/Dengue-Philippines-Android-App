package com.p000hl.android.view.pageflip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;
import com.p000hl.android.C0048R;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.ScreenUtils;
import java.io.IOException;

/* renamed from: com.hl.android.view.pageflip.PageWidget */
public class PageWidget extends AbstractPageFlipView {
    boolean isRight;
    /* access modifiers changed from: private */
    public ActionOnEnd mAction;
    int[] mBackShadowColors;
    GradientDrawable mBackShadowDrawableLR;
    GradientDrawable mBackShadowDrawableRL;
    PointF mBezierControl1 = new PointF();
    PointF mBezierControl2 = new PointF();
    PointF mBezierEnd1 = new PointF();
    PointF mBezierEnd2 = new PointF();
    PointF mBezierStart1 = new PointF();
    PointF mBezierStart2 = new PointF();
    PointF mBeziervertex1 = new PointF();
    PointF mBeziervertex2 = new PointF();
    ColorMatrixColorFilter mColorMatrixFilter;
    Context mContext;
    private int mCornerX = 0;
    private int mCornerY = 0;
    float mDegrees;
    GradientDrawable mFolderShadowDrawableLR;
    GradientDrawable mFolderShadowDrawableRL;
    int[] mFrontShadowColors;
    GradientDrawable mFrontShadowDrawableHBT;
    GradientDrawable mFrontShadowDrawableHTB;
    GradientDrawable mFrontShadowDrawableVLR;
    GradientDrawable mFrontShadowDrawableVRL;
    private int mHeight = BookSetting.SCREEN_HEIGHT;
    boolean mIsRTandLB;
    Matrix mMatrix;
    float[] mMatrixArray = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    float mMaxLength = ((float) Math.hypot((double) this.mWidth, (double) this.mHeight));
    float mMiddleX;
    float mMiddleY;
    Paint mPaint;
    private Path mPath0;
    private Path mPath1;
    Scroller mScroller;
    PointF mTouch = new PointF();
    float mTouchToCornerDis;
    private int mWidth = BookSetting.SCREEN_WIDTH;

    /* renamed from: mc */
    MyCount f63mc;
    MediaPlayer media;

    /* renamed from: com.hl.android.view.pageflip.PageWidget$MyCount */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            try {
                PageWidget.this.setVisibility(8);
                if (!PageWidget.this._preload) {
                }
                if (!PageWidget.this._preload && PageWidget.this.mAction != null) {
                    PageWidget.this.mAction.doAction();
                }
                new CountDownTimer(1, 1) {
                    public void onFinish() {
                        BookController.getInstance().startPlay();
                        Log.d("hl", "hl finish " + System.currentTimeMillis());
                        PageWidget.this.mNextPageBitmap.recycle();
                        PageWidget.this.hide();
                    }

                    public void onTick(long arg0) {
                    }
                }.start();
            } catch (Exception ex) {
                Log.e("hl", "load error", ex);
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    @SuppressLint({"NewApi"})
    public PageWidget(Context context) {
        super(context);
        if (ScreenUtils.getAPILevel() > 11) {
            setLayerType(1, null);
        }
        this.mContext = context;
        this.mPath0 = new Path();
        this.mPath1 = new Path();
        createDrawable();
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.FILL);
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        this.mColorMatrixFilter = new ColorMatrixColorFilter(cm);
        this.mMatrix = new Matrix();
        this.mScroller = new Scroller(getContext());
        this.mTouch.x = 0.01f;
        this.mTouch.y = 0.01f;
        this.media = MediaPlayer.create(context, C0048R.raw.flip);
        try {
            this.media.prepare();
        } catch (IOException | IllegalStateException e) {
        }
    }

    public void calcCornerXY(float x, float y) {
        if (x <= ((float) (this.mWidth / 2))) {
            this.mCornerX = 0;
        } else {
            this.mCornerX = this.mWidth;
        }
        if (y <= ((float) (this.mHeight / 2))) {
            this.mCornerY = 0;
        } else {
            this.mCornerY = this.mHeight;
        }
        if ((this.mCornerX == 0 && this.mCornerY == this.mHeight) || (this.mCornerX == this.mWidth && this.mCornerY == 0)) {
            this.mIsRTandLB = true;
        } else {
            this.mIsRTandLB = false;
        }
    }

    public boolean doTouchEvent(MotionEvent event) {
        if (event.getAction() == 2) {
            this.mTouch.x = event.getX();
            this.mTouch.y = event.getY();
            postInvalidate();
        }
        if (event.getAction() == 0) {
            this.mTouch.x = event.getX();
            this.mTouch.y = event.getY();
        }
        if (event.getAction() == 1) {
            if (canDragOver()) {
                startAnimation(HLSetting.FlipTime);
            } else {
                this.mTouch.x = ((float) this.mCornerX) - 0.09f;
                this.mTouch.y = ((float) this.mCornerY) - 0.09f;
            }
            postInvalidate();
        }
        return true;
    }

    public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);
        CrossP.x = ((((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x)) - b1) / (a1 - ((P4.y - P3.y) / (P4.x - P3.x)));
        CrossP.y = (CrossP.x * a1) + b1;
        return CrossP;
    }

    private void calcPoints() {
        this.mMiddleX = (this.mTouch.x + ((float) this.mCornerX)) / 2.0f;
        this.mMiddleY = (this.mTouch.y + ((float) this.mCornerY)) / 2.0f;
        this.mBezierControl1.x = this.mMiddleX - (((((float) this.mCornerY) - this.mMiddleY) * (((float) this.mCornerY) - this.mMiddleY)) / (((float) this.mCornerX) - this.mMiddleX));
        this.mBezierControl1.y = (float) this.mCornerY;
        this.mBezierControl2.x = (float) this.mCornerX;
        this.mBezierControl2.y = this.mMiddleY - (((((float) this.mCornerX) - this.mMiddleX) * (((float) this.mCornerX) - this.mMiddleX)) / (((float) this.mCornerY) - this.mMiddleY));
        this.mBezierStart1.x = this.mBezierControl1.x - ((((float) this.mCornerX) - this.mBezierControl1.x) / 2.0f);
        this.mBezierStart1.y = (float) this.mCornerY;
        if (this.mTouch.x > 0.0f && this.mTouch.x < ((float) this.mWidth) && (this.mBezierStart1.x < 0.0f || this.mBezierStart1.x > ((float) this.mWidth))) {
            if (this.mBezierStart1.x < 0.0f) {
                this.mBezierStart1.x = ((float) this.mWidth) - this.mBezierStart1.x;
            }
            float f1 = Math.abs(((float) this.mCornerX) - this.mTouch.x);
            float f2 = (((float) this.mWidth) * f1) / this.mBezierStart1.x;
            this.mTouch.x = Math.abs(((float) this.mCornerX) - f2);
            float f3 = (Math.abs(((float) this.mCornerX) - this.mTouch.x) * Math.abs(((float) this.mCornerY) - this.mTouch.y)) / f1;
            this.mTouch.y = Math.abs(((float) this.mCornerY) - f3);
            this.mMiddleX = (this.mTouch.x + ((float) this.mCornerX)) / 2.0f;
            this.mMiddleY = (this.mTouch.y + ((float) this.mCornerY)) / 2.0f;
            this.mBezierControl1.x = this.mMiddleX - (((((float) this.mCornerY) - this.mMiddleY) * (((float) this.mCornerY) - this.mMiddleY)) / (((float) this.mCornerX) - this.mMiddleX));
            this.mBezierControl1.y = (float) this.mCornerY;
            this.mBezierControl2.x = (float) this.mCornerX;
            this.mBezierControl2.y = this.mMiddleY - (((((float) this.mCornerX) - this.mMiddleX) * (((float) this.mCornerX) - this.mMiddleX)) / (((float) this.mCornerY) - this.mMiddleY));
            this.mBezierStart1.x = this.mBezierControl1.x - ((((float) this.mCornerX) - this.mBezierControl1.x) / 2.0f);
        }
        this.mBezierStart2.x = (float) this.mCornerX;
        this.mBezierStart2.y = this.mBezierControl2.y - ((((float) this.mCornerY) - this.mBezierControl2.y) / 2.0f);
        this.mTouchToCornerDis = (float) Math.hypot((double) (this.mTouch.x - ((float) this.mCornerX)), (double) (this.mTouch.y - ((float) this.mCornerY)));
        this.mBezierEnd1 = getCross(this.mTouch, this.mBezierControl1, this.mBezierStart1, this.mBezierStart2);
        this.mBezierEnd2 = getCross(this.mTouch, this.mBezierControl2, this.mBezierStart1, this.mBezierStart2);
        this.mBeziervertex1.x = ((this.mBezierStart1.x + (this.mBezierControl1.x * 2.0f)) + this.mBezierEnd1.x) / 4.0f;
        this.mBeziervertex1.y = (((this.mBezierControl1.y * 2.0f) + this.mBezierStart1.y) + this.mBezierEnd1.y) / 4.0f;
        this.mBeziervertex2.x = ((this.mBezierStart2.x + (this.mBezierControl2.x * 2.0f)) + this.mBezierEnd2.x) / 4.0f;
        this.mBeziervertex2.y = (((this.mBezierControl2.y * 2.0f) + this.mBezierStart2.y) + this.mBezierEnd2.y) / 4.0f;
    }

    private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
        this.mPath0.reset();
        this.mPath0.moveTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath0.quadTo(this.mBezierControl1.x, this.mBezierControl1.y, this.mBezierEnd1.x, this.mBezierEnd1.y);
        this.mPath0.lineTo(this.mTouch.x, this.mTouch.y);
        this.mPath0.lineTo(this.mBezierEnd2.x, this.mBezierEnd2.y);
        this.mPath0.quadTo(this.mBezierControl2.x, this.mBezierControl2.y, this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath0.lineTo((float) this.mCornerX, (float) this.mCornerY);
        this.mPath0.close();
        canvas.save();
        canvas.clipPath(path, Op.XOR);
        canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) BookSetting.SCREEN_WIDTH, (float) BookSetting.SCREEN_HEIGHT), null);
        canvas.restore();
    }

    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        this.mPath1.reset();
        this.mPath1.moveTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath1.lineTo(this.mBeziervertex1.x, this.mBeziervertex1.y);
        this.mPath1.lineTo(this.mBeziervertex2.x, this.mBeziervertex2.y);
        this.mPath1.lineTo(this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath1.lineTo((float) this.mCornerX, (float) this.mCornerY);
        this.mPath1.close();
        this.mDegrees = (float) Math.toDegrees(Math.atan2((double) (this.mBezierControl1.x - ((float) this.mCornerX)), (double) (this.mBezierControl2.y - ((float) this.mCornerY))));
        if (this.mIsRTandLB) {
            leftx = (int) this.mBezierStart1.x;
            rightx = (int) (this.mBezierStart1.x + (this.mTouchToCornerDis / 4.0f));
            mBackShadowDrawable = this.mBackShadowDrawableLR;
        } else {
            leftx = (int) (this.mBezierStart1.x - (this.mTouchToCornerDis / 4.0f));
            rightx = (int) this.mBezierStart1.x;
            mBackShadowDrawable = this.mBackShadowDrawableRL;
        }
        canvas.save();
        canvas.clipPath(this.mPath0);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        canvas.drawColor(-1);
        canvas.rotate(this.mDegrees, this.mBezierStart1.x, this.mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx, (int) this.mBezierStart1.y, rightx, (int) (this.mMaxLength + this.mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawNextPageAreaAndShadow0(Canvas canvas, Bitmap bitmap) {
        this.mPath1.reset();
        this.mPath1.moveTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath1.lineTo(this.mBeziervertex1.x, this.mBeziervertex1.y);
        this.mPath1.lineTo(this.mBeziervertex2.x, this.mBeziervertex2.y);
        this.mPath1.lineTo(this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath1.lineTo((float) this.mCornerX, (float) this.mCornerY);
        this.mPath1.close();
        this.mDegrees = (float) Math.toDegrees(Math.atan2((double) (this.mBezierControl1.x - ((float) this.mCornerX)), (double) (this.mBezierControl2.y - ((float) this.mCornerY))));
        int leftx = (int) (this.mBezierStart1.x - (this.mTouchToCornerDis / 4.0f));
        int rightx = (int) this.mBezierStart1.x;
        GradientDrawable mBackShadowDrawable = this.mBackShadowDrawableRL;
        canvas.save();
        canvas.clipPath(this.mPath0);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) BookSetting.SCREEN_WIDTH, (float) BookSetting.SCREEN_HEIGHT), null);
        canvas.rotate(this.mDegrees, this.mBezierStart1.x, this.mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx, (int) this.mBezierStart1.y, rightx, (int) (this.mMaxLength + this.mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    public void setScreen(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
        this.mMaxLength = (float) Math.hypot((double) this.mWidth, (double) this.mHeight);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        try {
            if (this.isRight) {
                canvas.drawColor(-1);
                calcPoints();
                drawCurrentPageArea(canvas, this.mCurPageBitmap, this.mPath0);
                if (this._preload) {
                    this.mNextPageBitmap = BookController.getInstance().getSnapshot(BookController.getInstance().getViewPage());
                }
                if (this.mNextPageBitmap != null) {
                    drawNextPageAreaAndShadow(canvas, this.mNextPageBitmap);
                    drawNextPageAreaAndShadow0(canvas, this.mNextPageBitmap);
                }
                drawCurrentPageShadow0(canvas);
                return;
            }
            canvas.drawColor(-1);
            calcPoints();
            if (this._preload) {
                this.mNextPageBitmap = BookController.getInstance().getSnapshot(BookController.getInstance().getViewPage());
            }
            if (this.mNextPageBitmap != null) {
                drawCurrentPageArea(canvas, this.mNextPageBitmap, this.mPath0);
            }
            drawNextPageAreaAndShadow(canvas, this.mCurPageBitmap);
            drawNextPageAreaAndShadow0(canvas, this.mCurPageBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDrawable() {
        int[] color = {3355443, -1338821837};
        this.mFolderShadowDrawableRL = new GradientDrawable(Orientation.RIGHT_LEFT, color);
        this.mFolderShadowDrawableRL.setGradientType(0);
        this.mFolderShadowDrawableLR = new GradientDrawable(Orientation.LEFT_RIGHT, color);
        this.mFolderShadowDrawableLR.setGradientType(0);
        this.mBackShadowColors = new int[]{-15658735, 1118481};
        this.mBackShadowDrawableRL = new GradientDrawable(Orientation.RIGHT_LEFT, this.mBackShadowColors);
        this.mBackShadowDrawableRL.setGradientType(0);
        this.mBackShadowDrawableLR = new GradientDrawable(Orientation.LEFT_RIGHT, this.mBackShadowColors);
        this.mBackShadowDrawableLR.setGradientType(0);
        this.mFrontShadowColors = new int[]{-2146365167, 1118481};
        this.mFrontShadowDrawableVLR = new GradientDrawable(Orientation.LEFT_RIGHT, this.mFrontShadowColors);
        this.mFrontShadowDrawableVLR.setGradientType(0);
        this.mFrontShadowDrawableVRL = new GradientDrawable(Orientation.RIGHT_LEFT, this.mFrontShadowColors);
        this.mFrontShadowDrawableVRL.setGradientType(0);
        this.mFrontShadowDrawableHTB = new GradientDrawable(Orientation.TOP_BOTTOM, this.mFrontShadowColors);
        this.mFrontShadowDrawableHTB.setGradientType(0);
        this.mFrontShadowDrawableHBT = new GradientDrawable(Orientation.BOTTOM_TOP, this.mFrontShadowColors);
        this.mFrontShadowDrawableHBT.setGradientType(0);
    }

    public void drawCurrentPageShadow(Canvas canvas) {
        double degree;
        float y;
        int leftx;
        int rightx;
        GradientDrawable mCurrentPageShadow;
        int leftx2;
        int rightx2;
        GradientDrawable mCurrentPageShadow2;
        float temp;
        if (this.mIsRTandLB) {
            degree = 0.7853981633974483d - Math.atan2((double) (this.mBezierControl1.y - this.mTouch.y), (double) (this.mTouch.x - this.mBezierControl1.x));
        } else {
            degree = 0.7853981633974483d - Math.atan2((double) (this.mTouch.y - this.mBezierControl1.y), (double) (this.mTouch.x - this.mBezierControl1.x));
        }
        double d2 = 35.35d * Math.sin(degree);
        float x = (float) (((double) this.mTouch.x) + (35.35d * Math.cos(degree)));
        if (this.mIsRTandLB) {
            y = (float) (((double) this.mTouch.y) + d2);
        } else {
            y = (float) (((double) this.mTouch.y) - d2);
        }
        this.mPath1.reset();
        this.mPath1.moveTo(x, y);
        this.mPath1.lineTo(this.mTouch.x, this.mTouch.y);
        this.mPath1.lineTo(this.mBezierControl1.x, this.mBezierControl1.y);
        this.mPath1.lineTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath1.close();
        canvas.save();
        canvas.clipPath(this.mPath0, Op.XOR);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        if (this.mIsRTandLB) {
            leftx = (int) this.mBezierControl1.x;
            rightx = ((int) this.mBezierControl1.x) + 25;
            mCurrentPageShadow = this.mFrontShadowDrawableVLR;
        } else {
            leftx = (int) (this.mBezierControl1.x - 25.0f);
            rightx = ((int) this.mBezierControl1.x) + 1;
            mCurrentPageShadow = this.mFrontShadowDrawableVRL;
        }
        canvas.rotate((float) Math.toDegrees(Math.atan2((double) (this.mTouch.x - this.mBezierControl1.x), (double) (this.mBezierControl1.y - this.mTouch.y))), this.mBezierControl1.x, this.mBezierControl1.y);
        mCurrentPageShadow.setBounds(leftx, (int) (this.mBezierControl1.y - this.mMaxLength), rightx, (int) this.mBezierControl1.y);
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
        this.mPath1.reset();
        this.mPath1.moveTo(x, y);
        this.mPath1.lineTo(this.mTouch.x, this.mTouch.y);
        this.mPath1.lineTo(this.mBezierControl2.x, this.mBezierControl2.y);
        this.mPath1.lineTo(this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath1.close();
        canvas.save();
        canvas.clipPath(this.mPath0, Op.XOR);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        if (this.mIsRTandLB) {
            leftx2 = (int) this.mBezierControl2.y;
            rightx2 = (int) (this.mBezierControl2.y + 25.0f);
            mCurrentPageShadow2 = this.mFrontShadowDrawableHTB;
        } else {
            leftx2 = (int) (this.mBezierControl2.y - 25.0f);
            rightx2 = (int) (this.mBezierControl2.y + 1.0f);
            mCurrentPageShadow2 = this.mFrontShadowDrawableHBT;
        }
        canvas.rotate((float) Math.toDegrees(Math.atan2((double) (this.mBezierControl2.y - this.mTouch.y), (double) (this.mBezierControl2.x - this.mTouch.x))), this.mBezierControl2.x, this.mBezierControl2.y);
        if (this.mBezierControl2.y < 0.0f) {
            temp = this.mBezierControl2.y - ((float) this.mHeight);
        } else {
            temp = this.mBezierControl2.y;
        }
        int hmg = (int) Math.hypot((double) this.mBezierControl2.x, (double) temp);
        if (((float) hmg) > this.mMaxLength) {
            mCurrentPageShadow2.setBounds(((int) (this.mBezierControl2.x - 25.0f)) - hmg, leftx2, ((int) (this.mBezierControl2.x + this.mMaxLength)) - hmg, rightx2);
        } else {
            mCurrentPageShadow2.setBounds((int) (this.mBezierControl2.x - this.mMaxLength), leftx2, (int) this.mBezierControl2.x, rightx2);
        }
        mCurrentPageShadow2.draw(canvas);
        canvas.restore();
    }

    public void drawCurrentPageShadow0(Canvas canvas) {
        float temp;
        double degree = 0.7853981633974483d - Math.atan2((double) (this.mTouch.y - this.mBezierControl1.y), (double) (this.mTouch.x - this.mBezierControl1.x));
        float x = (float) (((double) this.mTouch.x) + (35.35d * Math.cos(degree)));
        float y = (float) (((double) this.mTouch.y) - (35.35d * Math.sin(degree)));
        this.mPath1.reset();
        this.mPath1.moveTo(x, y);
        this.mPath1.lineTo(this.mTouch.x, this.mTouch.y);
        this.mPath1.lineTo(this.mBezierControl1.x, this.mBezierControl1.y);
        this.mPath1.lineTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath1.close();
        canvas.save();
        canvas.clipPath(this.mPath0, Op.XOR);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        int leftx = (int) (this.mBezierControl1.x - 25.0f);
        int rightx = ((int) this.mBezierControl1.x) + 1;
        GradientDrawable mCurrentPageShadow = this.mFrontShadowDrawableVRL;
        canvas.rotate((float) Math.toDegrees(Math.atan2((double) (this.mTouch.x - this.mBezierControl1.x), (double) (this.mBezierControl1.y - this.mTouch.y))), this.mBezierControl1.x, this.mBezierControl1.y);
        mCurrentPageShadow.setBounds(leftx, (int) (this.mBezierControl1.y - this.mMaxLength), rightx, (int) this.mBezierControl1.y);
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
        this.mPath1.reset();
        this.mPath1.moveTo(x, y);
        this.mPath1.lineTo(this.mTouch.x, this.mTouch.y);
        this.mPath1.lineTo(this.mBezierControl2.x, this.mBezierControl2.y);
        this.mPath1.lineTo(this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath1.close();
        canvas.save();
        canvas.clipPath(this.mPath0, Op.XOR);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        int leftx2 = (int) (this.mBezierControl2.y - 25.0f);
        int rightx2 = (int) (this.mBezierControl2.y + 1.0f);
        GradientDrawable mCurrentPageShadow2 = this.mFrontShadowDrawableHBT;
        canvas.rotate((float) Math.toDegrees(Math.atan2((double) (this.mBezierControl2.y - this.mTouch.y), (double) (this.mBezierControl2.x - this.mTouch.x))), this.mBezierControl2.x, this.mBezierControl2.y);
        if (this.mBezierControl2.y < 0.0f) {
            temp = this.mBezierControl2.y - ((float) this.mHeight);
        } else {
            temp = this.mBezierControl2.y;
        }
        int hmg = (int) Math.hypot((double) this.mBezierControl2.x, (double) temp);
        if (((float) hmg) > this.mMaxLength) {
            mCurrentPageShadow2.setBounds(((int) (this.mBezierControl2.x - 25.0f)) - hmg, leftx2, ((int) (this.mBezierControl2.x + this.mMaxLength)) - hmg, rightx2);
        } else {
            mCurrentPageShadow2.setBounds((int) (this.mBezierControl2.x - this.mMaxLength), leftx2, (int) this.mBezierControl2.x, rightx2);
        }
        mCurrentPageShadow2.draw(canvas);
        canvas.restore();
    }

    public void computeScroll() {
        super.computeScroll();
        try {
            if (this.mScroller.computeScrollOffset()) {
                float y = (float) this.mScroller.getCurrY();
                this.mTouch.x = (float) this.mScroller.getCurrX();
                this.mTouch.y = y;
                postInvalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnimation(int delayMillis) {
        int dx;
        int dy;
        if (this.mCornerX > 0) {
            dx = -((int) (((float) this.mWidth) + this.mTouch.x));
        } else {
            dx = (int) ((((float) this.mWidth) - this.mTouch.x) + ((float) this.mWidth));
        }
        if (this.mCornerY > 0) {
            dy = (int) (((float) this.mHeight) - this.mTouch.y);
        } else {
            dy = (int) (1.0f - this.mTouch.y);
        }
        if (this.isRight) {
            this.mScroller.startScroll((int) this.mTouch.x, (int) this.mTouch.y, dx, dy, delayMillis);
            return;
        }
        this.mCornerX = BookSetting.SCREEN_WIDTH;
        this.mCornerY = BookSetting.SCREEN_HEIGHT;
        this.mScroller.startScroll((-BookSetting.SCREEN_WIDTH) + 10, BookSetting.SCREEN_HEIGHT - 1, BookSetting.SCREEN_WIDTH * 2, 0, delayMillis);
    }

    public void abortAnimation() {
        if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
    }

    public boolean canDragOver() {
        if (this.mTouchToCornerDis > ((float) (this.mWidth / 10))) {
            return true;
        }
        return false;
    }

    public boolean DragToRight() {
        return this.isRight;
    }

    public void play(int pageIndex, int newPageIndex, ActionOnEnd action) {
        Log.d("hl", "hl play " + System.currentTimeMillis());
        this.mWidth = BookSetting.SCREEN_WIDTH;
        this.mHeight = BookSetting.SCREEN_HEIGHT;
        try {
            this.media.seekTo(0);
            this.media.start();
        } catch (Exception e) {
        }
        this.mAction = action;
        setBackgroundColor(17170443);
        bringToFront();
        this.isRight = pageIndex < newPageIndex;
        if (this.isRight) {
            calcCornerXY((float) BookSetting.SCREEN_WIDTH, (float) BookSetting.SCREEN_HEIGHT);
            doTouchEvent(MotionEvent.obtain(100, 100, 0, (float) BookSetting.SCREEN_WIDTH, (float) BookSetting.SCREEN_HEIGHT, 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 2, (float) (BookSetting.SCREEN_WIDTH - 30), (float) (BookSetting.SCREEN_HEIGHT - 10), 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 2, (float) (BookSetting.SCREEN_WIDTH - 50), (float) (BookSetting.SCREEN_HEIGHT - 20), 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 1, (float) (BookSetting.SCREEN_WIDTH - 70), (float) (BookSetting.SCREEN_HEIGHT - 30), 0));
        } else {
            calcCornerXY(0.0f, (float) BookSetting.SCREEN_HEIGHT);
            doTouchEvent(MotionEvent.obtain(100, 100, 0, 0.0f, (float) BookSetting.SCREEN_HEIGHT, 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 2, 30.0f, (float) (BookSetting.SCREEN_HEIGHT - 10), 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 2, 50.0f, (float) (BookSetting.SCREEN_HEIGHT - 20), 0));
            doTouchEvent(MotionEvent.obtain(100, 100, 1, 70.0f, (float) (BookSetting.SCREEN_HEIGHT - 30), 0));
        }
        startAnimation(HLSetting.FlipTime * 2);
        setEnabled(false);
        setClickable(false);
        Log.d("hl", "MyCount play " + System.currentTimeMillis());
        this.f63mc = new MyCount((long) HLSetting.FlipTime, 100);
        this.f63mc.start();
    }

    public void hide() {
        setVisibility(8);
        setBackgroundColor(0);
        setEnabled(true);
        setClickable(true);
        if (this.f63mc != null) {
            this.f63mc.cancel();
        }
    }
}
