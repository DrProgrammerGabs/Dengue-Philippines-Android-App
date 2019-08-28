package com.p000hl.android.view.component.moudle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.HLViewFlipperVerticle */
public class HLViewFlipperVerticle extends FrameLayout implements Component, ComponentPost {
    private static final int DEFAULT_INTERVAL = 3000;
    private static final boolean LOGD = false;
    private final int FLIP_MSG = 1;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    ComponentEntity entity;
    boolean mAnimateFirstTime = true;
    private boolean mAutoStart = false;
    boolean mFirstTime = true;
    /* access modifiers changed from: private */
    public int mFlipInterval = DEFAULT_INTERVAL;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1 && HLViewFlipperVerticle.this.mRunning) {
                HLViewFlipperVerticle.this.showNext();
                sendMessageDelayed(obtainMessage(1), (long) HLViewFlipperVerticle.this.mFlipInterval);
            }
        }
    };
    Animation mInAnimation;
    Animation mOutAnimation;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                HLViewFlipperVerticle.this.mUserPresent = false;
                HLViewFlipperVerticle.this.updateRunning();
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                HLViewFlipperVerticle.this.mUserPresent = true;
                HLViewFlipperVerticle.this.updateRunning();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mRunning = false;
    private boolean mStarted = false;
    /* access modifiers changed from: private */
    public boolean mUserPresent = true;
    private boolean mVisible = false;
    int mWhichChild = 0;
    private float oldTouchValue;

    public HLViewFlipperVerticle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HLViewFlipperVerticle(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        setBackgroundColor(0);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
        LayoutParams lp = getLayoutParams();
        ArrayList<String> sourceIDS = ((MoudleComponentEntity) this.entity).getSourceIDList();
        if (sourceIDS != null && sourceIDS.size() > 0) {
            for (int i = 0; i < sourceIDS.size(); i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.measure(MeasureSpec.makeMeasureSpec(lp.width, 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
                imageView.setImageBitmap(getBitMap((String) sourceIDS.get(i)));
                addView(imageView);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        showOnly(0);
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public Bitmap getBitMap(String localSourceID) {
        return BitmapUtils.getBitMap(localSourceID, getContext(), getLayoutParams().width, getLayoutParams().height);
    }

    public void stop() {
    }

    public void hide() {
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

    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case 0:
                this.oldTouchValue = touchevent.getY();
                break;
            case 1:
                float currentX = touchevent.getY();
                if (this.oldTouchValue - currentX >= -5.0f) {
                    if (this.oldTouchValue - currentX <= 5.0f) {
                        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_CLICK);
                        break;
                    } else {
                        setInAnimation(inFromRightAnimation());
                        setOutAnimation(outToLeftAnimation());
                        showNext();
                        break;
                    }
                } else {
                    setInAnimation(inFromLeftAnimation());
                    setOutAnimation(outToRightAnimation());
                    showPrevious();
                    break;
                }
        }
        return true;
    }

    public static Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 1.0f, 2, 0.0f);
        inFromRight.setDuration(350);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, -1.0f);
        outtoLeft.setDuration(350);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, -1.0f, 2, 0.0f);
        inFromLeft.setDuration(350);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, 1.0f);
        outtoRight.setDuration(350);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    public void setDisplayedChild(int whichChild) {
        boolean hasFocus = false;
        this.mWhichChild = whichChild;
        if (whichChild >= getChildCount()) {
            this.mWhichChild = 0;
        } else if (whichChild < 0) {
            this.mWhichChild = getChildCount() - 1;
        }
        if (getFocusedChild() != null) {
            hasFocus = true;
        }
        showOnly(this.mWhichChild);
        if (hasFocus) {
            requestFocus(2);
        }
    }

    public int getDisplayedChild() {
        return this.mWhichChild;
    }

    public void showNext() {
        setDisplayedChild(this.mWhichChild + 1);
    }

    public void showPrevious() {
        setDisplayedChild(this.mWhichChild - 1);
    }

    /* access modifiers changed from: 0000 */
    public void showOnly(int childIndex) {
        boolean checkForFirst;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!this.mFirstTime || this.mAnimateFirstTime) {
                checkForFirst = true;
            } else {
                checkForFirst = false;
            }
            if (i == childIndex) {
                if (checkForFirst && this.mInAnimation != null) {
                    child.startAnimation(this.mInAnimation);
                }
                child.setVisibility(0);
                this.mFirstTime = false;
            } else {
                if (checkForFirst && this.mOutAnimation != null && child.getVisibility() == 0) {
                    child.startAnimation(this.mOutAnimation);
                } else if (child.getAnimation() == this.mInAnimation) {
                    child.clearAnimation();
                }
                child.setVisibility(8);
            }
        }
    }

    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        child.setVisibility(0);
    }

    public void removeAllViews() {
        super.removeAllViews();
        this.mWhichChild = 0;
        this.mFirstTime = true;
    }

    public void removeView(View view) {
        int index = indexOfChild(view);
        if (index >= 0) {
            removeViewAt(index);
        }
    }

    public void removeViewAt(int index) {
        super.removeViewAt(index);
        int childCount = getChildCount();
        if (childCount == 0) {
            this.mWhichChild = 0;
            this.mFirstTime = true;
        } else if (this.mWhichChild >= childCount) {
            setDisplayedChild(childCount - 1);
        } else if (this.mWhichChild == index) {
            setDisplayedChild(this.mWhichChild);
        }
    }

    public void removeViewInLayout(View view) {
        removeView(view);
    }

    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        if (getChildCount() == 0) {
            this.mWhichChild = 0;
            this.mFirstTime = true;
        } else if (this.mWhichChild >= start && this.mWhichChild < start + count) {
            setDisplayedChild(this.mWhichChild);
        }
    }

    public void removeViewsInLayout(int start, int count) {
        removeViews(start, count);
    }

    public View getCurrentView() {
        return getChildAt(this.mWhichChild);
    }

    public Animation getInAnimation() {
        return this.mInAnimation;
    }

    public void setInAnimation(Animation inAnimation) {
        this.mInAnimation = inAnimation;
    }

    public Animation getOutAnimation() {
        return this.mOutAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.mOutAnimation = outAnimation;
    }

    public void setInAnimation(Context context, int resourceID) {
        setInAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    public void setOutAnimation(Context context, int resourceID) {
        setOutAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    public void setAnimateFirstView(boolean animate) {
        this.mAnimateFirstTime = animate;
    }

    public int getBaseline() {
        return getCurrentView() != null ? getCurrentView().getBaseline() : super.getBaseline();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        getContext().registerReceiver(this.mReceiver, filter);
        if (this.mAutoStart) {
            startFlipping();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        getContext().unregisterReceiver(this.mReceiver);
        updateRunning();
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mVisible = visibility == 0;
        updateRunning();
    }

    public void setFlipInterval(int milliseconds) {
        this.mFlipInterval = milliseconds;
    }

    public void startFlipping() {
        this.mStarted = true;
        updateRunning();
    }

    public void stopFlipping() {
        this.mStarted = false;
        updateRunning();
    }

    /* access modifiers changed from: private */
    public void updateRunning() {
        boolean running = this.mVisible && this.mStarted && this.mUserPresent;
        if (running != this.mRunning) {
            if (running) {
                showOnly(this.mWhichChild);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) this.mFlipInterval);
            } else {
                this.mHandler.removeMessages(1);
            }
            this.mRunning = running;
        }
    }

    public boolean isFlipping() {
        return this.mStarted;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    public boolean isAutoStart() {
        return this.mAutoStart;
    }

    public void recyle() {
        BitmapUtils.recycleBitmaps(this.bitmapList);
        for (int i = 0; i < getChildCount(); i++) {
            ((ImageView) getChildAt(i)).setImageBitmap(null);
        }
    }
}
