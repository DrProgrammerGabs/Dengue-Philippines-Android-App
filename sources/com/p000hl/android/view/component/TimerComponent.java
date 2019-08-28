package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.TimerEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.component.bean.TimerShowBean;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Iterator;

@SuppressLint({"HandlerLeak", "ViewConstructor"})
/* renamed from: com.hl.android.view.component.TimerComponent */
public class TimerComponent extends TextView implements Component, ComponentListener {
    TimerEntity entity;
    public ViewRecord initRecord;
    /* access modifiers changed from: private */
    public OnComponentCallbackListener onComponentCallbackListener;
    public TimerShowBean showBean;
    /* access modifiers changed from: private */
    public Handler timeMsg = new Handler() {
        public void handleMessage(Message msg) {
            long showValue = System.currentTimeMillis() - TimerComponent.this.showBean.startTime;
            if (!TimerComponent.this.showBean.isStop && !TimerComponent.this.showBean.isEnd && !TimerComponent.this.showBean.isPause) {
                if (showValue < 0 || showValue > ((long) (TimerComponent.this.entity.getMaxTimer() * 1000))) {
                    TimerComponent.this.onComponentCallbackListener.setPlayComplete();
                    if (TimerComponent.this.entity.isPlayOrderbyDesc) {
                        TimerComponent.this.setShowValue(0);
                    } else {
                        TimerComponent.this.setShowValue((long) (TimerComponent.this.entity.getMaxTimer() * 1000));
                    }
                    TimerComponent.this.showBean.isEnd = true;
                    TimerComponent.this.showBean.startTime = -1;
                    TimerComponent.this.showBean.pauseTime = 0;
                    Iterator i$ = TimerComponent.this.entity.behaviors.iterator();
                    while (i$.hasNext()) {
                        BehaviorEntity behavior = (BehaviorEntity) i$.next();
                        if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END)) {
                            BookController.getInstance().runBehavior(behavior);
                        }
                    }
                    return;
                }
                if (TimerComponent.this.entity.isPlayOrderbyDesc) {
                    showValue = ((long) (TimerComponent.this.entity.getMaxTimer() * 1000)) - showValue;
                }
                TimerComponent.this.setShowValue(showValue);
                if (TimerComponent.this.entity.isPlayMillisecond) {
                    TimerComponent.this.timeMsg.sendEmptyMessageDelayed(1, 12);
                } else {
                    TimerComponent.this.timeMsg.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    public TimerComponent(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = (TimerEntity) entity2;
        if (this.entity.isStaticType) {
            dsyncStatic();
        } else {
            this.showBean = new TimerShowBean();
        }
        if (this.showBean.showValue != -1) {
            setShowValue(this.showBean.showValue);
        } else if (this.entity.isPlayOrderbyDesc) {
            setShowValue((long) (this.entity.getMaxTimer() * 1000));
        } else {
            setShowValue(0);
        }
        if (entity2.isHideAtBegining) {
            setVisibility(4);
        }
        setGravity(3);
    }

    private void dsyncStatic() {
        if (this.entity.isPlayOrderbyDesc) {
            this.showBean = BookController.getInstance().descShow;
        } else {
            this.showBean = BookController.getInstance().ascShow;
        }
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (TimerEntity) entity2;
    }

    public void load() {
        setGravity(17);
        setSingleLine(true);
        setPadding(0, 0, 0, 0);
        try {
            String textColor = URLDecoder.decode(this.entity.fontColor);
            int color = -16777216;
            if (!StringUtils.isEmpty(textColor)) {
                if (!textColor.startsWith("0x")) {
                    String[] a = textColor.split(";");
                    color = Color.rgb(Integer.valueOf(a[0]).intValue(), Integer.valueOf(a[1]).intValue(), Integer.valueOf(a[2]).intValue());
                } else if (textColor.length() == 8) {
                    color = Color.rgb(Integer.parseInt(textColor.substring(2, 4), 16), Integer.parseInt(textColor.substring(4, 6), 16), Integer.parseInt(textColor.substring(6, 8), 16));
                }
            }
            setTextColor(color);
        } catch (Exception e) {
            setTextColor(-16777216);
        }
        setTextSize(ScreenUtils.getVerScreenValue(Float.parseFloat(this.entity.fontSize)) / 2.0f);
        if (!this.entity.isPlayVideoOrAudioAtBegining) {
            if (!this.showBean.isStop && !this.showBean.isPause && !this.showBean.isEnd && this.showBean.startTime != -1) {
                playTimer();
            } else if (!this.showBean.isEnd) {
            } else {
                if (this.entity.isPlayOrderbyDesc) {
                    setShowValue((long) (this.entity.getMaxTimer() * 1000));
                } else {
                    setShowValue(0);
                }
            }
        }
    }

    public void syncBean() {
        if (this.entity.isStaticType) {
            dsyncStatic();
            setShowValue(this.showBean.showValue);
            if (!this.showBean.isStop && !this.showBean.isPause && !this.showBean.isEnd && this.showBean.startTime != -1) {
                playTimer();
            } else if (!this.showBean.isEnd) {
            } else {
                if (this.entity.isPlayOrderbyDesc) {
                    setShowValue((long) (this.entity.getMaxTimer() * 1000));
                } else {
                    setShowValue(0);
                }
            }
        }
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    /* access modifiers changed from: private */
    public void setShowValue(long showValue) {
        String showInfor = Long.toString(showValue / 1000);
        if (this.entity.isPlayMillisecond) {
            showInfor = showInfor + ":" + String.format("%02d", new Object[]{Long.valueOf(showValue % 100)});
        }
        setText(showInfor);
        this.showBean.showValue = showValue;
    }

    public void playTimer() {
        long showValue = System.currentTimeMillis() - this.showBean.startTime;
        if (showValue < 0 || showValue > ((long) (this.entity.getMaxTimer() * 1000))) {
            this.showBean.isEnd = true;
        }
        if (this.showBean.isEnd) {
            this.showBean.isStop = false;
            this.showBean.isPause = false;
            this.showBean.isEnd = false;
            this.showBean.startTime = -1;
            this.showBean.pauseTime = 0;
        }
        this.showBean.isStop = false;
        this.showBean.isPause = false;
        long curPlayTime = System.currentTimeMillis();
        if (this.showBean.startTime == -1) {
            this.showBean.startTime = curPlayTime;
            Iterator i$ = this.entity.behaviors.iterator();
            while (i$.hasNext()) {
                BehaviorEntity behavior = (BehaviorEntity) i$.next();
                if (behavior.EventName.equals(Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY)) {
                    BookController.getInstance().runBehavior(behavior);
                }
            }
        }
        if (this.showBean.pauseTime > 0) {
            this.showBean.startTime = (this.showBean.startTime + curPlayTime) - this.showBean.pauseTime;
            this.showBean.pauseTime = 0;
        }
        this.timeMsg.sendEmptyMessage(1);
    }

    public void play() {
    }

    public void stop() {
        this.timeMsg.removeMessages(1);
    }

    public void hide() {
        this.timeMsg.removeMessages(1);
        setVisibility(4);
    }

    public void show() {
        setVisibility(0);
    }

    public void resume() {
    }

    public void pauseTimer() {
        if (!this.showBean.isStop && !this.showBean.isEnd) {
            this.showBean.isPause = true;
            this.showBean.pauseTime = System.currentTimeMillis();
        }
    }

    public void pause() {
    }

    public void stopTimer() {
        this.showBean.isStop = true;
        this.showBean.isPause = false;
        this.showBean.isEnd = false;
        this.showBean.startTime = -1;
        this.showBean.pauseTime = 0;
        this.timeMsg.removeMessages(1);
        if (this.entity.isPlayOrderbyDesc) {
            setShowValue((long) (this.entity.getMaxTimer() * 1000));
        } else {
            setShowValue(0);
        }
    }

    public void resumeTimer() {
        this.showBean.isStop = false;
        long curPlayTime = System.currentTimeMillis();
        this.showBean.startTime = (this.showBean.startTime + curPlayTime) - this.showBean.pauseTime;
        this.showBean.pauseTime = 0;
        this.timeMsg.sendEmptyMessage(1);
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

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void callBackListener() {
        this.onComponentCallbackListener.setPlayComplete();
    }

    public void setCounterText() {
    }
}
