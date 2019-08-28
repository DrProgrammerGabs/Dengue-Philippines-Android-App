package org.vudroid.core.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Scroller;
import com.p000hl.android.C0048R;
import org.vudroid.core.models.ZoomModel;

public class ZoomRoll extends View {
    private static final int MAX_VALUE = 1000;
    private static final float MULTIPLIER = 400.0f;
    private final Bitmap center;
    private float lastX;
    private final Bitmap left;
    private final Bitmap right;
    private Scroller scroller;
    private final Bitmap serifs;
    private VelocityTracker velocityTracker;
    private final ZoomModel zoomModel;

    public ZoomRoll(Context context, ZoomModel zoomModel2) {
        super(context);
        this.zoomModel = zoomModel2;
        this.left = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.left);
        this.right = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.right);
        this.center = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.center);
        this.serifs = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.serifs);
        this.scroller = new Scroller(context);
        setLayoutParams(new LayoutParams(-1, -2));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(this.left.getHeight(), this.right.getHeight()));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        canvas.drawBitmap(this.center, new Rect(0, 0, this.center.getWidth(), this.center.getHeight()), new Rect(0, 0, getWidth(), getHeight()), paint);
        float currentOffset = (-getCurrentValue()) % 40.0f;
        while (currentOffset < ((float) getWidth())) {
            canvas.drawBitmap(this.serifs, currentOffset, ((float) (getHeight() - this.serifs.getHeight())) / 2.0f, paint);
            currentOffset += (float) this.serifs.getWidth();
        }
        canvas.drawBitmap(this.left, 0.0f, 0.0f, paint);
        canvas.drawBitmap(this.right, (float) (getWidth() - this.right.getWidth()), (float) (getHeight() - this.right.getHeight()), paint);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case 0:
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                    this.zoomModel.commit();
                }
                this.lastX = ev.getX();
                break;
            case 1:
                this.velocityTracker.computeCurrentVelocity(MAX_VALUE);
                this.scroller.fling((int) getCurrentValue(), 0, (int) (-this.velocityTracker.getXVelocity()), 0, 0, MAX_VALUE, 0, 0);
                this.velocityTracker.recycle();
                this.velocityTracker = null;
                if (!this.scroller.computeScrollOffset()) {
                    this.zoomModel.commit();
                    break;
                }
                break;
            case 2:
                setCurrentValue(getCurrentValue() - (ev.getX() - this.lastX));
                this.lastX = ev.getX();
                break;
        }
        invalidate();
        return true;
    }

    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            setCurrentValue((float) this.scroller.getCurrX());
            invalidate();
            return;
        }
        this.zoomModel.commit();
    }

    public float getCurrentValue() {
        return (this.zoomModel.getZoom() - 1.0f) * MULTIPLIER;
    }

    public void setCurrentValue(float currentValue) {
        if (((double) currentValue) < 0.0d) {
            currentValue = 0.0f;
        }
        if (currentValue > 1000.0f) {
            currentValue = 1000.0f;
        }
        this.zoomModel.setZoom(1.0f + (currentValue / MULTIPLIER));
    }
}
