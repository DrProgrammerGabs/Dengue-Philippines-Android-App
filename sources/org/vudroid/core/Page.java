package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextPaint;
import java.util.concurrent.locks.ReentrantLock;

class Page {
    private float aspectRatio;
    private Bitmap bitmap;
    RectF bounds;
    private DocumentView documentView;
    private final Paint fillPaint = fillPaint();
    final int index;
    private ReentrantLock lock = new ReentrantLock();
    private PageTreeNode node;
    private final Paint strokePaint = strokePaint();

    Page(DocumentView documentView2, int index2) {
        this.documentView = documentView2;
        this.index = index2;
        this.node = new PageTreeNode(documentView2, new RectF(0.0f, 0.0f, 1.0f, 1.0f), this, 1, null);
    }

    public void dispose() {
        this.lock.lock();
        try {
            if (this.bitmap != null) {
                this.bitmap.recycle();
                this.bitmap = null;
                this.lock.unlock();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void setBitmap(Bitmap bitmap2) {
        this.lock.lock();
        try {
            if (this.bitmap != null) {
                this.bitmap.recycle();
            }
            this.bitmap = bitmap2;
        } finally {
            this.lock.unlock();
        }
    }

    public Bitmap getBitmap() {
        this.lock.lock();
        try {
            return this.bitmap;
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public float getPageHeight(int mainWidth, float zoom) {
        return (((float) mainWidth) / getAspectRatio()) * zoom;
    }

    public int getTop() {
        return Math.round(this.bounds.top);
    }

    public int getBottom() {
        return Math.round(this.bounds.bottom);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawRect(this.bounds, this.fillPaint);
            this.node.draw(canvas);
            canvas.drawLine(this.bounds.left, this.bounds.top, this.bounds.right, this.bounds.top, this.strokePaint);
            canvas.drawLine(this.bounds.left, this.bounds.bottom, this.bounds.right, this.bounds.bottom, this.strokePaint);
        }
    }

    private Paint strokePaint() {
        Paint strokePaint2 = new Paint();
        strokePaint2.setColor(-16777216);
        strokePaint2.setStyle(Style.STROKE);
        strokePaint2.setStrokeWidth(2.0f);
        return strokePaint2;
    }

    private Paint fillPaint() {
        Paint fillPaint2 = new Paint();
        fillPaint2.setColor(-1);
        fillPaint2.setStyle(Style.FILL);
        return fillPaint2;
    }

    /* access modifiers changed from: protected */
    public TextPaint textPaint() {
        TextPaint paint = new TextPaint();
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setTextSize(24.0f);
        paint.setTextAlign(Align.CENTER);
        return paint;
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(float aspectRatio2) {
        if (this.aspectRatio != aspectRatio2) {
            this.aspectRatio = aspectRatio2;
            this.documentView.invalidatePageSizes();
        }
    }

    public boolean isVisible() {
        return this.documentView.pageIndex == this.index;
    }

    public void setAspectRatio(int width, int height) {
        setAspectRatio((((float) width) * 1.0f) / ((float) height));
    }

    /* access modifiers changed from: 0000 */
    public void setBounds(RectF pageBounds) {
        this.bounds = pageBounds;
        this.node.invalidateNodeBounds();
    }

    public void updateVisibility() {
        this.node.updateVisibility();
    }

    public void invalidate() {
        this.node.invalidate();
    }
}
