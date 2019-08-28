package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.CounterEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"NewApi", "ViewConstructor"})
/* renamed from: com.hl.android.view.component.HLCounterComponent */
public class HLCounterComponent extends LinearLayout implements Component {
    public AnimationSet animationset = null;
    MyCount1 count = null;
    int countValue = 0;
    CounterEntity entity;
    public ViewRecord initRecord;
    String scope = "";
    TextView textView;

    /* renamed from: com.hl.android.view.component.HLCounterComponent$MyCount1 */
    public class MyCount1 extends CountDownTimer {
        public MyCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            HLCounterComponent.this.setVisibility(0);
            HLCounterComponent.this.startAnimation(HLCounterComponent.this.animationset);
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    public HLCounterComponent(Context context, ComponentEntity entity2) {
        super(context);
        this.textView = new TextView(context);
        addView(this.textView, -1, -1);
        setEntity(entity2);
        if (entity2.isHideAtBegining) {
            setVisibility(4);
        }
    }

    public void setCounterText() {
        this.countValue = this.entity.minValue;
        this.scope = this.entity.scope;
        if (StringUtils.isEmpty(this.scope) || !this.scope.equals("global")) {
            this.textView.setText(Integer.toString(this.countValue));
            return;
        }
        if (BookController.getInstance().count < 0) {
            BookController.getInstance().count = this.countValue;
        }
        this.textView.setText(Integer.toString(BookController.getInstance().count));
        Log.d("marsor", "global timer changed" + BookController.getInstance().count + this.entity.componentId + this);
        postInvalidate();
    }

    public CounterEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (CounterEntity) entity2;
        this.countValue = ((CounterEntity) entity2).minValue;
    }

    public void reset() {
        this.countValue = this.entity.minValue;
        this.scope = this.entity.scope;
        if (StringUtils.isEmpty(this.scope) || !this.scope.equals("global")) {
            this.textView.setText(Integer.toString(this.countValue));
        } else {
            BookController.getInstance().count = this.countValue;
            this.textView.setText(Integer.toString(BookController.getInstance().count));
        }
        if (this.entity.isHideAtBegining) {
            setVisibility(4);
        }
    }

    public void minus(int value) {
        int eqvalue;
        int targetValue;
        ArrayList<BehaviorEntity> behaviors = this.entity.behaviors;
        if (StringUtils.isEmpty(this.scope) || !this.scope.equals("global")) {
            int eqvalue2 = this.countValue;
            if (eqvalue2 != this.entity.minValue) {
                int eqvalue3 = eqvalue2 - value;
                if (eqvalue3 < this.entity.minValue) {
                    eqvalue3 = this.entity.minValue;
                }
                this.countValue = eqvalue;
            } else {
                return;
            }
        } else if (BookController.getInstance().count != this.entity.minValue) {
            eqvalue = BookController.getInstance().count - value;
            if (eqvalue < this.entity.minValue) {
                eqvalue = this.entity.minValue;
            }
            BookController.getInstance().count = eqvalue;
        } else {
            return;
        }
        this.textView.setText(Integer.toString(eqvalue));
        Iterator i$ = behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (behavior.EventName.equals("BEHAVIOR_ON_COUNTER_NUMBER")) {
                if (StringUtils.isEmpty(behavior.EventValue)) {
                    targetValue = 0;
                } else {
                    targetValue = Integer.valueOf(behavior.EventValue).intValue();
                }
                if (eqvalue == targetValue) {
                    BookController.getInstance().runBehavior(behavior);
                }
            }
        }
    }

    public void plus(int value) {
        int eqvalue;
        ArrayList<BehaviorEntity> behaviors = this.entity.behaviors;
        if (StringUtils.isEmpty(this.scope) || !this.scope.equals("global")) {
            int eqvalue2 = this.countValue;
            if (eqvalue2 != this.entity.maxValue) {
                int eqvalue3 = eqvalue2 + value;
                if (eqvalue3 > this.entity.maxValue) {
                    eqvalue3 = this.entity.maxValue;
                }
                this.countValue = eqvalue;
            } else {
                return;
            }
        } else if (BookController.getInstance().count != this.entity.maxValue) {
            eqvalue = BookController.getInstance().count + value;
            if (eqvalue > this.entity.maxValue) {
                eqvalue = this.entity.maxValue;
            }
            BookController.getInstance().count = eqvalue;
        } else {
            return;
        }
        this.textView.setText(Integer.toString(eqvalue));
        Iterator i$ = behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (behavior.EventName.equals("BEHAVIOR_ON_COUNTER_NUMBER") && eqvalue == Integer.valueOf(behavior.EventValue).intValue()) {
                BookController.getInstance().runBehavior(behavior);
            }
        }
    }

    public void load() {
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
            this.textView.setTextColor(color);
        } catch (Exception e) {
            this.textView.setTextColor(-16777216);
        }
        float fontSize = ScreenUtils.getVerScreenValue(Float.parseFloat(this.entity.fontSize));
        this.textView.setSingleLine(true);
        this.textView.setTextSize(fontSize / 2.0f);
        setGravity(17);
        setCounterText();
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
        setCounterText();
    }

    public void stop() {
    }

    public void hide() {
        setVisibility(4);
    }

    public void show() {
        setVisibility(0);
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
