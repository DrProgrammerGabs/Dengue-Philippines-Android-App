package com.p000hl.android.view.pageflip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region.Op;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLActivity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;

/* renamed from: com.hl.android.view.pageflip.PageWidgetNew */
public class PageWidgetNew extends AbstractPageFlipView {
    private int index = 1000;
    ActionOnEnd mAction;
    int[] mBackShadowColors;
    GradientDrawable mBackShadowDrawableLR;
    PointF mBezierControl1 = new PointF();
    PointF mBezierControl2 = new PointF();
    PointF mBezierEnd1 = new PointF();
    PointF mBezierEnd2 = new PointF();
    PointF mBezierStart1 = new PointF();
    PointF mBezierStart2 = new PointF();
    PointF mBeziervertex1 = new PointF();
    PointF mBeziervertex2 = new PointF();
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private int mCornerX = 0;
    private int mCornerY = 0;
    float mDegrees;
    GradientDrawable mFolderShadowDrawableLR;
    int[] mFrontShadowColors;
    GradientDrawable mFrontShadowDrawableHBT;
    GradientDrawable mFrontShadowDrawableHTB;
    GradientDrawable mFrontShadowDrawableVRL;
    float mMiddleX;
    float mMiddleY;
    private Path mPath0;
    private Path mPath1;
    PointF mTouch = new PointF();
    float mTouchToCornerDis;
    MediaPlayer media;
    Paint paint;
    private int step = 1;

    public PageWidgetNew(Context context) {
        super(context);
        this.media = MediaPlayer.create(context, C0048R.raw.flip);
        try {
            this.media.prepare();
        } catch (Exception e) {
        }
    }

    public void playNext() {
        this.mCornerX = getWidth();
        this.mCornerY = getHeight();
        this.mTouch.x = (float) (((double) this.mCornerX) * 0.9d);
        this.mTouch.y = (float) (((double) this.mCornerY) * 0.6d);
        this.index = 5;
        this.step = 10;
        postInvalidate();
    }

    public void playPrev() {
        this.mCornerX = getWidth();
        this.mCornerY = getHeight();
        if (getWidth() < getHeight()) {
            this.mTouch.x = (float) (-getWidth());
            this.mTouch.y = (float) (((double) this.mCornerY) * 0.8d);
        } else {
            this.mTouch.x = (float) (((double) this.mCornerX) * 0.9d);
            this.mTouch.y = (float) (-getHeight());
        }
        this.index = 0;
        this.step = -10;
        postInvalidate();
    }

    public void show() {
        this.index = 0;
        this.mTouch.y = (float) getHeight();
        this.mTouch.x = (float) getWidth();
        bringToFront();
        setVisibility(0);
        BookController.getInstance().hlActivity.commonLayout.bringToFront();
    }

    private void initDraw() {
        if (this.mPath0 == null) {
            this.mPath0 = new Path();
        }
        if (this.mPath1 == null) {
            this.mPath1 = new Path();
        }
        if (this.mFolderShadowDrawableLR == null) {
            createDrawable();
        }
        if (this.mBitmap == null) {
            try {
                this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                try {
                    BitmapUtils.recycleBitmap(this.mBitmap);
                    this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
                } catch (OutOfMemoryError e2) {
                    BitmapUtils.recycleBitmap(this.mBitmap);
                    this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ALPHA_8);
                }
            }
        }
        if (this.mBitmapPaint == null) {
            this.mBitmapPaint = new Paint(4);
        }
        if (this.mCanvas == null) {
            this.mCanvas = new Canvas(this.mBitmap);
        }
    }

    private PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
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
        try {
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
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        this.mPath1.reset();
        this.mPath1.moveTo(this.mBezierStart1.x, this.mBezierStart1.y);
        this.mPath1.lineTo(this.mBeziervertex1.x, this.mBeziervertex1.y);
        this.mPath1.lineTo(this.mBeziervertex2.x, this.mBeziervertex2.y);
        this.mPath1.lineTo(this.mBezierStart2.x, this.mBezierStart2.y);
        this.mPath1.lineTo((float) this.mCornerX, (float) this.mCornerY);
        this.mPath1.close();
        this.mDegrees = (float) Math.toDegrees(Math.atan2((double) (this.mBezierControl1.x - ((float) this.mCornerX)), (double) (this.mBezierControl2.y - ((float) this.mCornerY))));
        canvas.save();
        canvas.clipPath(this.mPath0);
        canvas.clipPath(this.mPath1, Op.INTERSECT);
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        }
        canvas.rotate(this.mDegrees, this.mBezierStart1.x, this.mBezierStart1.y);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        initDraw();
        this.mCanvas.drawColor(-5592406);
        canvas.drawColor(-5592406);
        calcPoints();
        drawCurrentPageArea(this.mCanvas, this.mCurPageBitmap, this.mPath0);
        if (this.mNextPageBitmap != null && !this.mNextPageBitmap.isRecycled()) {
            drawNextPageAreaAndShadow(this.mCanvas, this.mNextPageBitmap);
        }
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mBitmapPaint);
        }
        if (this.index != 1000) {
            if (this.index >= 100 || this.mTouch.y <= ((float) (-getWidth())) || this.mTouch.x <= ((float) (-getHeight()))) {
                Log.d("hl", "pageflip onDraw");
                this.mAction.doAction();
                BookController.getInstance().revokeCommonPage();
                Log.d("hl", "pageflip doAction");
                BookController.getInstance().startPlay();
                Log.d("hl", "pageflip startPlay");
                hide();
                return;
            }
            if (getWidth() > getHeight()) {
                this.mTouch.y -= (float) (this.index * this.step);
            } else {
                this.mTouch.x -= (float) (this.index * this.step);
            }
            postInvalidateDelayed(50);
            this.index += Math.abs(this.step);
        }
    }

    private void createDrawable() {
        this.mFolderShadowDrawableLR = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{3355443, -1338821837});
        this.mFolderShadowDrawableLR.setGradientType(0);
        this.mBackShadowColors = new int[]{-15658735, 1118481};
        this.mBackShadowDrawableLR = new GradientDrawable(Orientation.LEFT_RIGHT, this.mBackShadowColors);
        this.mBackShadowDrawableLR.setGradientType(0);
        this.mFrontShadowColors = new int[]{-2138535800, 8947848};
        this.mFrontShadowDrawableVRL = new GradientDrawable(Orientation.RIGHT_LEFT, this.mFrontShadowColors);
        this.mFrontShadowDrawableVRL.setGradientType(0);
        this.mFrontShadowDrawableHTB = new GradientDrawable(Orientation.TOP_BOTTOM, this.mFrontShadowColors);
        this.mFrontShadowDrawableHTB.setGradientType(0);
        this.mFrontShadowDrawableHBT = new GradientDrawable(Orientation.BOTTOM_TOP, this.mFrontShadowColors);
        this.mFrontShadowDrawableHBT.setGradientType(0);
    }

    public void play(int pageIndex, int newPageindex, ActionOnEnd action) {
        try {
            this.media.seekTo(0);
            this.media.start();
        } catch (Exception e) {
        }
        this.mAction = action;
        if (pageIndex < newPageindex) {
            new CountDownTimer(1, 1) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    BitmapUtils.recycleBitmap(PageWidgetNew.this.mNextPageBitmap);
                    PageWidgetNew.this.mNextPageBitmap = BookController.getInstance().getCurrentBookSnap();
                    PageWidgetNew.this.playNext();
                }
            }.start();
        } else {
            new CountDownTimer(1, 1) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    BitmapUtils.recycleBitmap(PageWidgetNew.this.mNextPageBitmap);
                    PageWidgetNew.this.mNextPageBitmap = BookController.getInstance().getCurrentBookSnap();
                    Bitmap tmp = PageWidgetNew.this.mCurPageBitmap;
                    PageWidgetNew.this.mCurPageBitmap = PageWidgetNew.this.mNextPageBitmap;
                    PageWidgetNew.this.mNextPageBitmap = tmp;
                    PageWidgetNew.this.playPrev();
                }
            }.start();
        }
    }

    public void hide() {
        setVisibility(8);
        setBackgroundColor(0);
        setEnabled(true);
        setClickable(true);
        BitmapUtils.recycleBitmap(this.mNextPageBitmap);
        BitmapUtils.recycleBitmap(this.mCurPageBitmap);
        HLActivity hLActivity = (HLActivity) getContext();
    }
}
