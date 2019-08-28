package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.VerticalImageComponent */
public class VerticalImageComponent extends View implements Component, ComponentListener {
    public ViewRecord initRecord;
    private boolean isSendAutoPage = false;
    private Bitmap mBitmap;
    private Context mContext;
    private ComponentEntity mEntity;
    private Paint mPaint;
    private MotionEvent oldEvent;
    private OnComponentCallbackListener onComponentCallbackListener;
    private RectF rectF;
    private int targetHeight;
    private float topOffset = 0.0f;

    public VerticalImageComponent(Context context) {
        super(context);
    }

    public VerticalImageComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        setEntity(entity);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
    }

    public void load() {
        this.mBitmap = BitmapManager.getBitmapFromCache(this.mEntity.getLocalSourceId());
        if (this.mBitmap == null) {
            this.mBitmap = BitmapUtils.getBitMap(this.mEntity.getLocalSourceId(), this.mContext);
            BitmapManager.putBitmapCache(this.mEntity.getLocalSourceId(), this.mBitmap);
        }
        this.targetHeight = (this.mBitmap.getHeight() * getLayoutParams().width) / this.mBitmap.getWidth();
    }

    public void load(InputStream is) {
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        this.rectF = new RectF((float) getPaddingLeft(), this.topOffset + ((float) getPaddingTop()), (float) (getLayoutParams().width + getPaddingLeft()), this.topOffset + ((float) this.targetHeight) + ((float) getPaddingTop()));
        if (this.rectF.top > ((float) getPaddingTop())) {
            this.rectF.top = (float) getPaddingTop();
        } else if (this.rectF.top < ((float) ((getLayoutParams().height - this.targetHeight) + getPaddingTop()))) {
            this.rectF.top = (float) ((getLayoutParams().height - this.targetHeight) + getPaddingTop());
        }
        this.topOffset = this.rectF.top - ((float) getPaddingTop());
        this.rectF.bottom = this.rectF.top + ((float) this.targetHeight);
        if (this.mBitmap != null) {
            canvas.drawBitmap(this.mBitmap, new Rect(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight()), this.rectF, this.mPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 2) {
            this.topOffset += (float) ((int) (event.getY() - this.oldEvent.getY()));
            invalidate();
        }
        this.oldEvent = MotionEvent.obtain(event);
        return true;
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void play() {
    }

    public void setRotation(float rotation) {
    }

    public void stop() {
    }

    public void hide() {
        clearAnimation();
        setVisibility(8);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
        }
    }

    public void resume() {
    }

    public void pause() {
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void callBackListener() {
        if (!this.isSendAutoPage) {
            this.onComponentCallbackListener.setPlayComplete();
            this.isSendAutoPage = true;
        }
    }

    @SuppressLint({"NewApi"})
    public ViewRecord getCurrentRecord() {
        ViewRecord curRecord = new ViewRecord();
        curRecord.mHeight = getLayoutParams().width;
        curRecord.mWidth = getLayoutParams().height;
        curRecord.f28mX = getX();
        curRecord.f29mY = getY();
        curRecord.mRotation = getRotation();
        return curRecord;
    }
}
