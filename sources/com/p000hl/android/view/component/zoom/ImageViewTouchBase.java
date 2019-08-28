package com.p000hl.android.view.component.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.view.component.zoom.easing.Cubic;

/* renamed from: com.hl.android.view.component.zoom.ImageViewTouchBase */
public class ImageViewTouchBase extends ImageView implements IDisposable {
    public static final String LOG_TAG = "image";
    protected final float MAX_ZOOM = 2.0f;
    protected Matrix mBaseMatrix = new Matrix();
    protected final RotateBitmap mBitmapDisplayed = new RotateBitmap(null, 0);
    protected final Matrix mDisplayMatrix = new Matrix();
    protected Handler mHandler = new Handler();
    private OnBitmapChangedListener mListener;
    protected final float[] mMatrixValues = new float[9];
    protected float mMaxZoom;
    protected Runnable mOnLayoutRunnable = null;
    protected Matrix mSuppMatrix = new Matrix();
    protected int mThisHeight = -1;
    protected int mThisWidth = -1;

    /* renamed from: com.hl.android.view.component.zoom.ImageViewTouchBase$Command */
    protected enum Command {
        Center,
        Move,
        Zoom,
        Layout,
        Reset
    }

    /* renamed from: com.hl.android.view.component.zoom.ImageViewTouchBase$OnBitmapChangedListener */
    public interface OnBitmapChangedListener {
        void onBitmapChanged(Bitmap bitmap);
    }

    public ImageViewTouchBase(Context context) {
        super(context);
        init();
    }

    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnBitmapChangedListener(OnBitmapChangedListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public void init() {
        setScaleType(ScaleType.MATRIX);
    }

    public void clear() {
        setImageBitmapReset(null, true);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mThisWidth = right - left;
        this.mThisHeight = bottom - top;
        Runnable r = this.mOnLayoutRunnable;
        if (r != null) {
            this.mOnLayoutRunnable = null;
            r.run();
        }
        if (this.mBitmapDisplayed.getBitmap() != null) {
            getProperBaseMatrix(this.mBitmapDisplayed, this.mBaseMatrix);
            setImageMatrix(Command.Layout, getImageViewMatrix());
        }
    }

    public void setImageBitmapReset(Bitmap bitmap, boolean reset) {
        setImageRotateBitmapReset(new RotateBitmap(bitmap, 0), reset);
    }

    public void setImageBitmapReset(Bitmap bitmap, int rotation, boolean reset) {
        setImageRotateBitmapReset(new RotateBitmap(bitmap, rotation), reset);
    }

    public void setImageRotateBitmapReset(final RotateBitmap bitmap, final boolean reset) {
        Log.d(LOG_TAG, "setImageRotateBitmapReset");
        if (getWidth() <= 0) {
            this.mOnLayoutRunnable = new Runnable() {
                public void run() {
                    ImageViewTouchBase.this.setImageBitmapReset(bitmap.getBitmap(), bitmap.getRotation(), reset);
                }
            };
            return;
        }
        if (bitmap.getBitmap() != null) {
            getProperBaseMatrix(bitmap, this.mBaseMatrix);
            setImageBitmap(bitmap.getBitmap(), bitmap.getRotation());
        } else {
            this.mBaseMatrix.reset();
            setImageBitmap(null);
        }
        if (reset) {
            this.mSuppMatrix.reset();
        }
        setImageMatrix(Command.Reset, getImageViewMatrix());
        this.mMaxZoom = maxZoom();
        if (this.mListener != null) {
            this.mListener.onBitmapChanged(bitmap.getBitmap());
        }
    }

    /* access modifiers changed from: protected */
    public float maxZoom() {
        if (this.mBitmapDisplayed.getBitmap() == null) {
            return 1.0f;
        }
        return Math.max(((float) this.mBitmapDisplayed.getWidth()) / ((float) this.mThisWidth), ((float) this.mBitmapDisplayed.getHeight()) / ((float) this.mThisHeight)) * 4.0f;
    }

    public RotateBitmap getDisplayBitmap() {
        return this.mBitmapDisplayed;
    }

    public float getMaxZoom() {
        return this.mMaxZoom;
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, 0);
    }

    /* access modifiers changed from: protected */
    public void setImageBitmap(Bitmap bitmap, int rotation) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
        this.mBitmapDisplayed.setBitmap(bitmap);
        this.mBitmapDisplayed.setRotation(rotation);
    }

    /* access modifiers changed from: protected */
    public Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    /* access modifiers changed from: protected */
    public void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
        float viewWidth = (float) getWidth();
        float viewHeight = (float) getHeight();
        float w = (float) bitmap.getWidth();
        float h = (float) bitmap.getHeight();
        matrix.reset();
        float scale = Math.min(Math.min(viewWidth / w, 2.0f), Math.min(viewHeight / h, 2.0f));
        matrix.postConcat(bitmap.getRotateMatrix());
        matrix.postScale(scale, scale);
        matrix.postTranslate((viewWidth - (w * scale)) / 2.0f, (viewHeight - (h * scale)) / 2.0f);
    }

    /* access modifiers changed from: protected */
    public float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[whichValue];
    }

    /* access modifiers changed from: protected */
    public RectF getBitmapRect() {
        if (this.mBitmapDisplayed.getBitmap() == null) {
            return null;
        }
        Matrix m = getImageViewMatrix();
        RectF rect = new RectF(0.0f, 0.0f, (float) this.mBitmapDisplayed.getBitmap().getWidth(), (float) this.mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        return rect;
    }

    /* access modifiers changed from: protected */
    public float getScale(Matrix matrix) {
        return getValue(matrix, 0);
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    /* access modifiers changed from: protected */
    public void center(boolean horizontal, boolean vertical) {
        if (this.mBitmapDisplayed.getBitmap() != null) {
            RectF rect = getCenter(horizontal, vertical);
            if (rect.left != 0.0f || rect.top != 0.0f) {
                postTranslate(rect.left, rect.top);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setImageMatrix(Command command, Matrix matrix) {
        setImageMatrix(matrix);
    }

    /* access modifiers changed from: protected */
    public RectF getCenter(boolean horizontal, boolean vertical) {
        if (this.mBitmapDisplayed.getBitmap() == null) {
            return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        }
        RectF rect = getBitmapRect();
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        if (vertical) {
            int viewHeight = getHeight();
            if (height < ((float) viewHeight)) {
                deltaY = ((((float) viewHeight) - height) / 2.0f) - rect.top;
            } else if (rect.top > 0.0f) {
                deltaY = -rect.top;
            } else if (rect.bottom < ((float) viewHeight)) {
                deltaY = ((float) getHeight()) - rect.bottom;
            }
        }
        if (horizontal) {
            int viewWidth = getWidth();
            if (width < ((float) viewWidth)) {
                deltaX = ((((float) viewWidth) - width) / 2.0f) - rect.left;
            } else if (rect.left > 0.0f) {
                deltaX = -rect.left;
            } else if (rect.right < ((float) viewWidth)) {
                deltaX = ((float) viewWidth) - rect.right;
            }
        }
        return new RectF(deltaX, deltaY, 0.0f, 0.0f);
    }

    /* access modifiers changed from: protected */
    public void postTranslate(float deltaX, float deltaY) {
        this.mSuppMatrix.postTranslate(deltaX, deltaY);
        setImageMatrix(Command.Move, getImageViewMatrix());
    }

    /* access modifiers changed from: protected */
    public void postScale(float scale, float centerX, float centerY) {
        this.mSuppMatrix.postScale(scale, scale, centerX, centerY);
        setImageMatrix(Command.Zoom, getImageViewMatrix());
    }

    /* access modifiers changed from: protected */
    public void zoomTo(float scale) {
        zoomTo(scale, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
    }

    public void zoomTo(float scale, float durationMs) {
        zoomTo(scale, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, durationMs);
    }

    /* access modifiers changed from: protected */
    public void zoomTo(float scale, float centerX, float centerY) {
        if (scale > this.mMaxZoom) {
            scale = this.mMaxZoom;
        }
        postScale(scale / getScale(), centerX, centerY);
        onZoom(getScale());
        center(true, true);
    }

    /* access modifiers changed from: protected */
    public void onZoom(float scale) {
    }

    public void scrollBy(float x, float y) {
        panBy(x, y);
    }

    /* access modifiers changed from: protected */
    public void panBy(float dx, float dy) {
        RectF rect = getBitmapRect();
        RectF srect = new RectF(dx, dy, 0.0f, 0.0f);
        updateRect(rect, srect);
        postTranslate(srect.left, srect.top);
        center(true, true);
    }

    /* access modifiers changed from: protected */
    public void updateRect(RectF bitmapRect, RectF scrollRect) {
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (bitmapRect != null && scrollRect != null) {
            if (bitmapRect.top >= 0.0f && bitmapRect.bottom <= height) {
                scrollRect.top = 0.0f;
            }
            if (bitmapRect.left >= 0.0f && bitmapRect.right <= width) {
                scrollRect.left = 0.0f;
            }
            if (bitmapRect.top + scrollRect.top >= 0.0f && bitmapRect.bottom > height) {
                scrollRect.top = (float) ((int) (0.0f - bitmapRect.top));
            }
            if (bitmapRect.bottom + scrollRect.top <= height - 0.0f && bitmapRect.top < 0.0f) {
                scrollRect.top = (float) ((int) ((height - 0.0f) - bitmapRect.bottom));
            }
            if (bitmapRect.left + scrollRect.left >= 0.0f) {
                scrollRect.left = (float) ((int) (0.0f - bitmapRect.left));
            }
            if (bitmapRect.right + scrollRect.left <= width - 0.0f) {
                scrollRect.left = (float) ((int) ((width - 0.0f) - bitmapRect.right));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void scrollBy(float distanceX, float distanceY, float durationMs) {
        final float dx = distanceX;
        final float dy = distanceY;
        final long startTime = System.currentTimeMillis();
        final float f = durationMs;
        this.mHandler.post(new Runnable() {
            float old_x = 0.0f;
            float old_y = 0.0f;

            public void run() {
                float currentMs = Math.min(f, (float) (System.currentTimeMillis() - startTime));
                float x = Cubic.easeOut(currentMs, 0.0f, dx, f);
                float y = Cubic.easeOut(currentMs, 0.0f, dy, f);
                ImageViewTouchBase.this.panBy(x - this.old_x, y - this.old_y);
                this.old_x = x;
                this.old_y = y;
                if (currentMs < f) {
                    ImageViewTouchBase.this.mHandler.post(this);
                    return;
                }
                RectF centerRect = ImageViewTouchBase.this.getCenter(true, true);
                if (centerRect.left != 0.0f || centerRect.top != 0.0f) {
                    ImageViewTouchBase.this.scrollBy(centerRect.left, centerRect.top);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void zoomTo(float scale, float centerX, float centerY, float durationMs) {
        final long startTime = System.currentTimeMillis();
        final float incrementPerMs = (scale - getScale()) / durationMs;
        final float oldScale = getScale();
        final float f = durationMs;
        final float f2 = centerX;
        final float f3 = centerY;
        this.mHandler.post(new Runnable() {
            public void run() {
                float currentMs = Math.min(f, (float) (System.currentTimeMillis() - startTime));
                ImageViewTouchBase.this.zoomTo(oldScale + (incrementPerMs * currentMs), f2, f3);
                if (currentMs < f) {
                    ImageViewTouchBase.this.mHandler.post(this);
                }
            }
        });
    }

    public void dispose() {
        if (this.mBitmapDisplayed.getBitmap() != null && !this.mBitmapDisplayed.getBitmap().isRecycled()) {
            this.mBitmapDisplayed.getBitmap().recycle();
        }
        clear();
    }
}
