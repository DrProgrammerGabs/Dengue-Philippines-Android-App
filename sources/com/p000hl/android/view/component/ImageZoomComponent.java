package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationSet;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import com.p000hl.android.view.component.zoom.ImageViewTouch;
import java.io.IOException;
import java.io.InputStream;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.ImageZoomComponent */
public class ImageZoomComponent extends ImageViewTouch implements Component, ComponentListener, ComponentPost {
    public AnimationSet animationset = null;
    Bitmap bitmap = null;

    /* renamed from: bp */
    Bitmap f25bp = null;
    MyCount1 count = null;
    public ComponentEntity entity = null;
    Bitmap resizeBmp = null;

    /* renamed from: com.hl.android.view.component.ImageZoomComponent$MyCount1 */
    public class MyCount1 extends CountDownTimer {
        public MyCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            ImageZoomComponent.this.setVisibility(0);
            ImageZoomComponent.this.startAnimation(ImageZoomComponent.this.animationset);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public ImageZoomComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageZoomComponent(Context context, ComponentEntity entity2) {
        super(context, null);
        setEntity(entity2);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
        try {
            if (HLSetting.IsResourceSD) {
                load(FileUtils.getInstance().getFileInputStream(getEntity().localSourceId.trim()));
            } else {
                load(FileUtils.getInstance().getFileInputStream(getContext(), getEntity().localSourceId.trim()));
            }
        } catch (OutOfMemoryError e) {
            Log.e("hl", "load error", e);
        }
    }

    public void load(InputStream is) {
        Options _option = new Options();
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[512000];
        try {
            this.bitmap = BitmapFactory.decodeStream(is, null, _option);
        } catch (Exception e) {
        } catch (OutOfMemoryError e2) {
            _option.inSampleSize = 2;
            this.bitmap = BitmapFactory.decodeStream(is, null, _option);
        }
        try {
            is.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        int aa = getLayoutParams().width;
        int bb = getLayoutParams().height;
        if (this.entity.getRotation() != 0.0f) {
            this.resizeBmp = Bitmap.createScaledBitmap(this.bitmap, (int) getEntity().oldWidth, (int) getEntity().oldHeight, true);
            this.bitmap.recycle();
            Matrix mx = new Matrix();
            mx.setRotate(this.entity.getRotation());
            this.f25bp = Bitmap.createBitmap(this.resizeBmp, 0, 0, (int) getEntity().oldWidth, (int) getEntity().oldHeight, mx, true);
            this.resizeBmp.recycle();
            setImageBitmapReset(this.f25bp, true);
        } else if (this.bitmap != null) {
            this.resizeBmp = Bitmap.createScaledBitmap(this.bitmap, aa, bb, true);
            this.bitmap.recycle();
            setImageBitmapReset(this.resizeBmp, true);
            BitmapUtils.recycleBitmap(this.bitmap);
        }
    }

    public void setRotation(float rotation) {
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
        clearAnimation();
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
    }

    public void callBackListener() {
    }

    public void recyle() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        if (this.resizeBmp != null) {
            this.resizeBmp.recycle();
            this.resizeBmp = null;
        }
        if (this.f25bp != null) {
            BitmapUtils.recycleBitmap(this.f25bp);
        }
        setImageBitmap(null);
    }
}
