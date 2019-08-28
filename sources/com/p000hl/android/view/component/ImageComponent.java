package com.p000hl.android.view.component;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.ImageComponent */
public class ImageComponent extends ImageView implements Component, ComponentListener, ComponentPost {
    AnimatorSet animsationSet;
    Bitmap bitmap = null;
    Bitmap[][] bitmaps;
    public ComponentEntity entity = null;
    int initHeight = 0;
    int initWidth = 0;
    int initX = 0;
    int initY = 0;
    private boolean isSendAutoPage = false;
    private OnComponentCallbackListener onComponentCallbackListener;

    public ImageComponent(Context context) {
        super(context);
    }

    public ImageComponent(Context context, ComponentEntity entity2) {
        super(context);
        setEntity(entity2);
    }

    public void load() {
        this.initX = getEntity().f7x;
        this.initY = getEntity().f8y;
        loadBitmap();
        setScaleType(ScaleType.FIT_XY);
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
        this.bitmap = BitmapManager.getBitmapFromCache(getEntity().localSourceId);
        if (this.bitmap == null || this.bitmap.isRecycled()) {
            this.bitmap = BitmapUtils.getBitMap(getEntity().localSourceId, getContext(), size);
            BitmapManager.putBitmapCache(getEntity().localSourceId, this.bitmap);
        }
        setImageBitmap(this.bitmap);
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
    }

    public void hide() {
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
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
    }

    public void callBackListener() {
        if (!this.isSendAutoPage) {
            this.onComponentCallbackListener.setPlayComplete();
            this.isSendAutoPage = true;
        }
    }
}
