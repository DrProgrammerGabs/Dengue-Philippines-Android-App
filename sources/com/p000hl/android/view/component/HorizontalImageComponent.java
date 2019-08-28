package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.HorizontalImageComponent */
public class HorizontalImageComponent extends HorizontalScrollView implements Component, ComponentListener {
    private boolean isSendAutoPage = false;
    private float leftOffset = 0.0f;
    private Bitmap mBitmap;
    private Context mContext;
    private ComponentEntity mEntity;
    private Paint mPaint;
    private MotionEvent oldEvent;
    private OnComponentCallbackListener onComponentCallbackListener;
    private RectF rectF;
    private int targetWidth;

    public HorizontalImageComponent(Context context) {
        super(context);
    }

    public HorizontalImageComponent(Context context, ComponentEntity entity) {
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
        this.targetWidth = (this.mBitmap.getWidth() * getLayoutParams().height) / this.mBitmap.getHeight();
    }

    public void load(InputStream is) {
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        this.rectF = new RectF(this.leftOffset + ((float) getPaddingLeft()), (float) getPaddingTop(), this.leftOffset + ((float) this.targetWidth) + ((float) getPaddingLeft()), (float) (getLayoutParams().height + getPaddingTop()));
        if (this.rectF.left > ((float) getPaddingLeft())) {
            this.rectF.left = (float) getPaddingLeft();
        } else if (this.rectF.left < ((float) ((getLayoutParams().width - this.targetWidth) + getPaddingLeft()))) {
            this.rectF.left = (float) ((getLayoutParams().width - this.targetWidth) + getPaddingLeft());
        }
        this.leftOffset = this.rectF.left - ((float) getPaddingLeft());
        this.rectF.right = this.rectF.left + ((float) this.targetWidth);
        canvas.drawBitmap(this.mBitmap, null, this.rectF, this.mPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 2) {
            this.leftOffset += (float) ((int) (event.getX() - this.oldEvent.getX()));
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
}
