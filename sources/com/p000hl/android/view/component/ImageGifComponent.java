package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.GifComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;

@SuppressLint({"NewApi", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.ImageGifComponent */
public class ImageGifComponent extends View implements Component, ComponentPost, ComponentListener, AnimationListener {
    public AnimationSet animationset = null;
    Component component = null;
    MyCount1 count = null;
    private int currentIndex = 0;
    private float deltaX;
    private float deltaY;
    private int duration = 0;
    public GifComponentEntity entity = null;
    int height = 0;
    private boolean isEnd = false;
    private boolean isEndHide = false;
    boolean isInExcute = false;
    boolean isOutExcute = false;
    private long lastChangeTime;
    int lastX;
    int lastY;
    float last_x = 0.0f;
    float last_y = 0.0f;
    private boolean mPausing = false;
    private boolean mRunning = false;
    private OnComponentCallbackListener onComponentCallbackListener;
    Paint paint = null;
    RectF rectf = null;
    int repeat = 0;
    private int repeatCount = 0;
    int width = 0;

    /* renamed from: com.hl.android.view.component.ImageGifComponent$MyCount1 */
    public class MyCount1 extends CountDownTimer {
        public MyCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            ImageGifComponent.this.setVisibility(0);
            ImageGifComponent.this.startAnimation(ImageGifComponent.this.animationset);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public ImageGifComponent(Context context) {
        super(context);
    }

    @SuppressLint({"NewApi"})
    public ImageGifComponent(Context context, ComponentEntity entity2) {
        super(context);
        setEntity(entity2);
        this.paint = new Paint(4);
        this.paint.setStyle(Style.STROKE);
        this.paint.setAntiAlias(true);
    }

    @SuppressLint({"NewApi"})
    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (GifComponentEntity) entity2;
    }

    public void load() {
        this.width = getLayoutParams().width;
        this.height = getLayoutParams().height;
        this.rectf = new RectF(0.0f, 0.0f, (float) this.width, (float) this.height);
        loadGifFrame();
    }

    private void loadGifFrame() {
        this.duration = (int) this.entity.getGifDuration();
        if (this.duration > 0) {
            this.duration /= this.entity.getFrameList().size() - 1;
        }
    }

    private Bitmap getBitmap() {
        String bitMapResource = (String) this.entity.getFrameList().get(this.currentIndex);
        Bitmap bitmap = BitmapManager.getBitmapFromCache(bitMapResource);
        if (bitmap != null) {
            return bitmap;
        }
        Bitmap bitmap2 = BitmapUtils.getBitMap(bitMapResource, getContext(), this.width, this.height);
        BitmapManager.putBitmapCache(bitMapResource, bitmap2);
        return bitmap2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Bitmap currentBitmap = getBitmap();
        if (currentBitmap != null) {
            canvas.drawBitmap(currentBitmap, new Rect(0, 0, currentBitmap.getWidth(), currentBitmap.getHeight()), this.rectf, this.paint);
        }
        if (!this.isEnd && !this.mPausing) {
            if (((long) this.duration) - (System.currentTimeMillis() - this.lastChangeTime) > 0) {
                invalidate();
                return;
            }
            if (this.currentIndex >= this.entity.getFrameList().size() - 1) {
                if (this.entity.isIsPlayOnetime()) {
                    this.isEnd = true;
                    this.mRunning = false;
                    this.onComponentCallbackListener.setPlayComplete();
                    BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
                    BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END);
                    return;
                }
                this.currentIndex = 0;
                this.lastChangeTime = System.currentTimeMillis();
            } else if (this.mRunning) {
                if (this.lastChangeTime != 0) {
                    this.currentIndex++;
                }
                this.lastChangeTime = System.currentTimeMillis();
            }
            invalidate();
        }
    }

    public void load(InputStream is) {
    }

    public void play() {
        if (!this.mRunning) {
            this.isEnd = false;
            this.currentIndex = 0;
            this.lastChangeTime = 0;
            this.mRunning = true;
            BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
        } else if (this.isEnd) {
            return;
        }
        this.mPausing = false;
        invalidate();
    }

    public void stop() {
        this.mPausing = true;
        this.mRunning = false;
        this.currentIndex = 0;
        if (this.count != null) {
            this.count.cancel();
        }
        invalidate();
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
        this.mPausing = false;
        postInvalidate();
    }

    public void pause() {
        this.mPausing = true;
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void recyle() {
        this.mRunning = false;
    }

    public void onAnimationStart(Animation animation) {
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_PLAY_AT, Integer.toString(this.animationset.getAnimations().indexOf(animation)));
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_PLAY);
    }

    public void onAnimationEnd(Animation animation) {
        this.repeatCount++;
        if (this.repeat == 0) {
            this.count = new MyCount1(0, 100);
            this.count.start();
            return;
        }
        if (this.repeat != 1 && this.repeatCount < this.repeat) {
            if (this.repeatCount == this.repeat - 1) {
                this.onComponentCallbackListener.setPlayComplete();
                BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END);
                return;
            }
            this.count = new MyCount1(0, 100);
            this.count.start();
        }
        if (this.isEndHide) {
            setVisibility(8);
        }
        if (!(this.deltaX == 0.0f && this.deltaY == 0.0f)) {
            Log.i("ImageGifComponent onAnimationEnd", "Location translate.........");
            setAnimation(new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f));
            float left = ((float) this.entity.f7x) + this.deltaX;
            float top = ((float) this.entity.f8y) + this.deltaY;
            float right = ((float) this.entity.f7x) + this.deltaX + ((float) getLayoutParams().width);
            float bottom = ((float) this.entity.f8y) + this.deltaY + ((float) getLayoutParams().height);
            this.entity.f7x = (int) left;
            this.entity.f8y = (int) top;
            layout((int) left, (int) top, (int) right, (int) bottom);
        }
        this.onComponentCallbackListener.setPlayComplete();
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END_AT, Integer.toString(this.animationset.getAnimations().indexOf(animation)));
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_ANIMATION_END);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void callBackListener() {
        this.onComponentCallbackListener.setPlayComplete();
    }

    public void setAlpha(float alpha) {
        if (alpha < 0.0f) {
            alpha = 0.0f;
        } else if (alpha > 1.0f) {
            alpha = 1.0f;
        }
        super.setAlpha(alpha);
    }
}
