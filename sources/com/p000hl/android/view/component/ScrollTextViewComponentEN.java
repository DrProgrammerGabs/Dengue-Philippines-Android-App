package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.textview.TextViewComponent;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.ScrollTextViewComponentEN */
public class ScrollTextViewComponentEN extends ScrollView implements Component {
    public AnimationSet animationset = null;
    MyThread autoThread = null;
    public ComponentEntity entity = null;
    public ViewRecord initRecord;
    /* access modifiers changed from: private */
    public boolean isAutoScroll = false;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ScrollTextViewComponentEN.this.scrollText();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    TextViewComponent textView = null;

    /* renamed from: com.hl.android.view.component.ScrollTextViewComponentEN$MyThread */
    class MyThread extends Thread implements Runnable {
        MyThread() {
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = 1;
                    if (ScrollTextViewComponentEN.this.isAutoScroll) {
                        ScrollTextViewComponentEN.this.myHandler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ScrollTextViewComponentEN(Context context) {
        super(context);
    }

    public ScrollTextViewComponentEN(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        setFadingEdgeLength(0);
        setVerticalFadingEdgeEnabled(false);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void load() {
        setScrollContainer(true);
        setFocusable(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        this.textView = new TextViewComponent(getContext(), this.entity);
        this.textView.setLayoutParams(new LayoutParams(getLayoutParams().width, getLayoutParams().height));
        this.textView.loadText();
        addView(this.textView);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.textView != null) {
            int height = this.textView.getTextHeight();
            if (height > b - t) {
                this.textView.layout(0, 0, r - l, height);
            } else {
                this.textView.layout(0, 0, r - l, b - t);
            }
        }
    }

    /* access modifiers changed from: private */
    public void scrollText() {
        scrollBy(0, 1);
    }

    private void autoScroll() {
        this.autoThread = new MyThread();
        this.autoThread.start();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public void load(InputStream is) {
    }

    public void play() {
        invalidate();
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
