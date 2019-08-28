package com.p000hl.android.view.component.moudle.bookshelf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.View;
import com.p000hl.android.view.component.Button4Play;

@SuppressLint({"DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.bookshelf.HorizontalProgressBar */
public class HorizontalProgressBar extends View {
    private Paint bgPaint = new Paint();
    private Paint fgPaint = new Paint();
    private float max = 1000.0f;
    private float progress = 0.0f;

    public HorizontalProgressBar(Context context) {
        super(context);
        this.bgPaint.setColor(Button4Play.BTN_TOUCH_COLOR);
        this.bgPaint.setStyle(Style.FILL);
        this.fgPaint.setColor(-256);
        this.fgPaint.setStyle(Style.FILL);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        RectF rect = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
        canvas.drawRoundRect(rect, 6.0f, 6.0f, this.bgPaint);
        canvas.clipRect(new RectF(0.0f, 0.0f, ((float) getWidth()) * (this.progress / this.max), (float) getHeight()));
        canvas.drawRoundRect(rect, 6.0f, 6.0f, this.fgPaint);
        super.onDraw(canvas);
    }

    public void setBackgroundColor(int color) {
        this.bgPaint.setColor(color);
    }

    public void setProgressColor(int color) {
        this.fgPaint.setColor(color);
    }

    public int getMax() {
        return (int) this.max;
    }

    public void setMax(int max2) {
        this.max = (float) max2;
    }

    public int getProgress() {
        return (int) this.progress;
    }

    public void setProgress(int progress2) {
        this.progress = (float) progress2;
        invalidate();
    }
}
