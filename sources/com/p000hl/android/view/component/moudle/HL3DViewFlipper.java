package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.HL3DViewFlipper */
public class HL3DViewFlipper extends ImageView implements Component {
    int currentID = 0;
    MoudleComponentEntity entity;
    private long lastPlayTime = 0;
    public Handler loadHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (HL3DViewFlipper.this.rotationType.equalsIgnoreCase("clockwise")) {
                HL3DViewFlipper.this.loadNextBitmap(true);
            } else {
                HL3DViewFlipper.this.loadNextBitmap(false);
            }
        }
    };
    boolean mLeft = true;
    boolean mRunning = true;
    boolean mStart = false;
    boolean mUp = true;
    private float oldTouchValueX;
    private float oldTouchValueY;
    /* access modifiers changed from: private */
    public String rotationType = "clockwise";
    ArrayList<String> sourceIDS = null;

    public HL3DViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HL3DViewFlipper(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = (MoudleComponentEntity) entity2;
        setBackgroundColor(0);
        setScaleType(ScaleType.FIT_XY);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (MoudleComponentEntity) entity2;
    }

    public void load() {
        this.sourceIDS = this.entity.getSourceIDList();
        setImageBitmapOnly(0);
        this.rotationType = this.entity.rotationType;
    }

    public void load(InputStream is) {
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
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }

    public boolean onTouchEvent(MotionEvent touchevent) {
        if (touchevent.getPointerCount() == 1) {
            dotouchOnePointEvent(touchevent);
        } else if (touchevent.getPointerCount() == 2) {
            ((ViewCell) getParent()).doSomeThing(touchevent);
        }
        return true;
    }

    private void dotouchOnePointEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case 0:
                this.oldTouchValueX = touchevent.getX();
                this.oldTouchValueY = touchevent.getY();
                this.mRunning = false;
                return;
            case 1:
                this.mRunning = true;
                this.loadHandler.sendEmptyMessage(1);
                break;
            case 2:
                break;
            default:
                return;
        }
        float currentX = touchevent.getX();
        float distabcex = this.oldTouchValueX - currentX;
        float distabcey = this.oldTouchValueY - touchevent.getY();
        if (this.entity.isHorSlider) {
            if (distabcex < 0.0f) {
                this.mLeft = false;
                loadNextBitmap(this.mLeft);
                this.rotationType = "anticlosewise";
            } else if (distabcex > 0.0f) {
                this.mLeft = true;
                loadNextBitmap(this.mLeft);
                this.rotationType = "clockwise";
            }
        } else if (distabcey < 0.0f) {
            this.mUp = false;
            loadNextBitmap(this.mUp);
            this.rotationType = "anticlosewise";
        } else if (distabcey > 0.0f) {
            this.mUp = true;
            loadNextBitmap(this.mUp);
            this.rotationType = "clockwise";
        }
    }

    public void loadNextBitmap(boolean currentDirect) {
        if (!currentDirect) {
            if (this.currentID == 0) {
                this.currentID = this.sourceIDS.size() - 1;
            } else {
                this.currentID--;
            }
            doBehaiors(this.currentID);
            setImageBitmapOnly(this.currentID);
            return;
        }
        if (this.currentID < this.sourceIDS.size() - 1) {
            this.currentID++;
        } else {
            this.currentID = 0;
        }
        doBehaiors(this.currentID);
        setImageBitmapOnly(this.currentID);
    }

    public void setImageBitmapOnly(int index) {
        this.lastPlayTime = System.currentTimeMillis();
        Bitmap bitmap = BitmapManager.getBitmapFromCache((String) this.sourceIDS.get(index));
        if (bitmap == null) {
            bitmap = BitmapUtils.getBitMap((String) this.sourceIDS.get(index), getContext());
            BitmapManager.putBitmapCache((String) this.sourceIDS.get(index), bitmap);
        }
        setImageBitmap(bitmap);
        postInvalidate();
        long delay = this.entity.getTimerDelay() - (System.currentTimeMillis() - this.lastPlayTime);
        if (delay < 0) {
            delay = 0;
        }
        if (this.mRunning && this.entity.isAutoRotation) {
            this.loadHandler.sendEmptyMessageDelayed(1, delay);
        }
    }

    private void doBehaiors(int index) {
        Iterator i$ = this.entity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity beheavior = (BehaviorEntity) i$.next();
            BehaviorHelper.doBeheavorForList(beheavior, index, beheavior.triggerComponentID);
        }
    }
}
