package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* renamed from: com.hl.android.view.component.Button4Play */
public class Button4Play extends View {
    public static final int BTN_NOMAL_COLOR = Color.rgb(218, 218, 218);
    public static final int BTN_STATE_PLAY_NOMAL = 4097;
    public static final int BTN_STATE_PLAY_TOUCH = 4098;
    public static final int BTN_STATE_STOP_NOMAL = 4099;
    public static final int BTN_STATE_STOP_TOUCH = 4100;
    public static final int BTN_TOUCH_COLOR = -7829368;
    /* access modifiers changed from: private */
    public int currentState = BTN_STATE_PLAY_NOMAL;
    private PaintFlagsDrawFilter drawFilter = new PaintFlagsDrawFilter(0, 3);
    private Context mContext;
    private ActionListener mListener;
    private Paint mPaint;
    private Path path;

    /* renamed from: com.hl.android.view.component.Button4Play$ActionListener */
    interface ActionListener {
        void onDoPlay();

        void onDoStop();
    }

    public Button4Play(Context context) {
        super(context);
        this.mContext = context;
        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth(4.0f);
        this.mPaint.setStyle(Style.FILL);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (Button4Play.this.touchInTheRect(event, 0.0f, 0.0f, (float) Button4Play.this.getLayoutParams().width, (float) Button4Play.this.getLayoutParams().height)) {
                    switch (event.getAction()) {
                        case 0:
                        case 2:
                            if (Button4Play.this.currentState == 4097) {
                                Button4Play.this.currentState = Button4Play.BTN_STATE_PLAY_TOUCH;
                            } else if (Button4Play.this.currentState == 4099) {
                                Button4Play.this.currentState = Button4Play.BTN_STATE_STOP_TOUCH;
                            }
                            Button4Play.this.postInvalidate();
                            break;
                        case 1:
                            if (Button4Play.this.currentState == 4098) {
                                Button4Play.this.doPlayAction();
                            } else if (Button4Play.this.currentState == 4100) {
                                Button4Play.this.doStopAction();
                            }
                            Button4Play.this.postInvalidate();
                            break;
                    }
                } else {
                    if (Button4Play.this.currentState == 4098) {
                        Button4Play.this.currentState = Button4Play.BTN_STATE_PLAY_NOMAL;
                    } else if (Button4Play.this.currentState == 4100) {
                        Button4Play.this.currentState = Button4Play.BTN_STATE_STOP_NOMAL;
                    }
                    Button4Play.this.postInvalidate();
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean touchInTheRect(MotionEvent event, float x, float y, float width, float height) {
        float tx = event.getX();
        float ty = event.getY();
        if (tx <= x || tx >= x + width || ty <= y || ty >= y + height) {
            return false;
        }
        return true;
    }

    public ActionListener getActionListener() {
        return this.mListener;
    }

    public void setActionListener(ActionListener mListener2) {
        this.mListener = mListener2;
    }

    public void change2ShowStop() {
        this.currentState = BTN_STATE_STOP_NOMAL;
        postInvalidate();
    }

    public void change2ShowPlay() {
        this.currentState = BTN_STATE_PLAY_NOMAL;
        postInvalidate();
    }

    public void doPlayAction() {
        this.currentState = BTN_STATE_STOP_NOMAL;
        if (this.mListener != null) {
            this.mListener.onDoPlay();
        }
        postInvalidate();
    }

    public void doStopAction() {
        this.currentState = BTN_STATE_PLAY_NOMAL;
        if (this.mListener != null) {
            this.mListener.onDoStop();
        }
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(this.drawFilter);
        if (this.path == null) {
            this.path = new Path();
            this.path.moveTo(((float) getLayoutParams().width) / 3.0f, ((float) getLayoutParams().height) / 3.0f);
            this.path.lineTo(((float) (getLayoutParams().width * 2)) / 3.0f, ((float) getLayoutParams().height) / 2.0f);
            this.path.lineTo(((float) getLayoutParams().width) / 3.0f, ((float) (getLayoutParams().height * 2)) / 3.0f);
        }
        switch (this.currentState) {
            case BTN_STATE_PLAY_NOMAL /*4097*/:
                this.mPaint.setColor(BTN_NOMAL_COLOR);
                canvas.drawPath(this.path, this.mPaint);
                return;
            case BTN_STATE_PLAY_TOUCH /*4098*/:
                this.mPaint.setColor(BTN_TOUCH_COLOR);
                canvas.drawPath(this.path, this.mPaint);
                return;
            case BTN_STATE_STOP_NOMAL /*4099*/:
                this.mPaint.setColor(BTN_NOMAL_COLOR);
                canvas.drawLine((((float) getLayoutParams().width) / 3.0f) - 2.0f, ((float) getLayoutParams().height) / 3.0f, (((float) getLayoutParams().width) / 3.0f) - 2.0f, ((float) (getLayoutParams().height * 2)) / 3.0f, this.mPaint);
                canvas.drawLine((((float) (getLayoutParams().width * 2)) / 3.0f) - 2.0f, ((float) getLayoutParams().height) / 3.0f, (((float) (getLayoutParams().width * 2)) / 3.0f) - 2.0f, ((float) (getLayoutParams().height * 2)) / 3.0f, this.mPaint);
                return;
            case BTN_STATE_STOP_TOUCH /*4100*/:
                this.mPaint.setColor(BTN_TOUCH_COLOR);
                canvas.drawLine((((float) getLayoutParams().width) / 3.0f) - 2.0f, ((float) getLayoutParams().height) / 3.0f, (((float) getLayoutParams().width) / 3.0f) - 2.0f, ((float) (getLayoutParams().height * 2)) / 3.0f, this.mPaint);
                canvas.drawLine((((float) (getLayoutParams().width * 2)) / 3.0f) - 2.0f, ((float) getLayoutParams().height) / 3.0f, (((float) (getLayoutParams().width * 2)) / 3.0f) - 2.0f, ((float) (getLayoutParams().height * 2)) / 3.0f, this.mPaint);
                return;
            default:
                return;
        }
    }
}
