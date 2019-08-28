package com.p000hl.android.view.component;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.widget.Button;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.HLLocalButtonComponent */
public class HLLocalButtonComponent extends Button implements Component, ComponentListener, ComponentPost {
    AnimatorSet animsationSet;
    Bitmap bitmap = null;
    public ComponentEntity entity = null;
    int initHeight = 0;
    int initWidth = 0;
    int initX = 0;
    int initY = 0;
    private boolean isSendAutoPage = false;
    private Context mContext;
    BitmapDrawable normalD;
    private OnComponentCallbackListener onComponentCallbackListener;
    BitmapDrawable selectD;
    Bitmap selectbitmap = null;

    public HLLocalButtonComponent(Context context) {
        super(context);
    }

    public HLLocalButtonComponent(Context context, ComponentEntity entity2) {
        super(context);
        setEntity(entity2);
        this.mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                setBackgroundDrawable(this.selectD);
                break;
            case 1:
                setBackgroundDrawable(this.normalD);
                break;
        }
        return false;
    }

    public void load() {
        this.initX = getEntity().f7x;
        this.initY = getEntity().f8y;
        loadBitmap();
        if (this.entity.isHideAtBegining) {
            setVisibility(8);
        }
    }

    public void loadBitmap() {
        int width = getLayoutParams().width;
        int height = getLayoutParams().height;
        this.initWidth = width;
        this.initHeight = height;
        int[] size = {width, height};
        this.bitmap = BitmapUtils.getBitMap(getEntity().localSourceId, getContext(), size);
        this.selectbitmap = BitmapUtils.getBitMap(getEntity().downSourceID, getContext(), size);
        this.selectD = new BitmapDrawable(this.mContext.getResources(), this.selectbitmap);
        this.normalD = new BitmapDrawable(this.mContext.getResources(), this.bitmap);
        setBackgroundDrawable(this.normalD);
    }

    public void load(InputStream is) {
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void play() {
    }

    public void stop() {
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
            this.bitmap = null;
            System.gc();
        }
    }

    public void hide() {
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }

    public void show() {
        if (this.bitmap == null) {
            loadBitmap();
        }
        if (getVisibility() != 0) {
            setVisibility(0);
        }
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void recyle() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }

    public void callBackListener() {
        if (!this.isSendAutoPage) {
            this.onComponentCallbackListener.setPlayComplete();
            this.isSendAutoPage = true;
        }
    }
}
