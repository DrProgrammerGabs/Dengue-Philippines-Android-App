package com.p000hl.android.view.component.moudle.imageseq;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.imageseq.HL3DViewVerFlipper */
public class HL3DViewVerFlipper extends ImageView implements Component, ComponentPost {
    private ArrayList<Bitmap> bitMapList;
    Bitmap bitmap = null;
    private Bitmap currentBitmap = null;
    ComponentEntity entity;
    private int mCount;
    int mCurrentIndex = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            HL3DViewVerFlipper.this.setImageBitmapOnly(msg.what);
            HL3DViewVerFlipper.this.postInvalidate();
        }
    };
    private float oldTouchValue;
    ArrayList<String> sourceIDS = null;

    public HL3DViewVerFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HL3DViewVerFlipper(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        setBackgroundColor(0);
        setScaleType(ScaleType.FIT_XY);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
        this.sourceIDS = ((MoudleComponentEntity) this.entity).getSourceIDList();
        this.mCount = this.sourceIDS.size();
        this.mCurrentIndex = 0;
        setImageBitmapOnly(0);
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
            case 2:
                float currentX = touchevent.getY();
                if (this.oldTouchValue >= currentX) {
                    if (this.oldTouchValue > currentX) {
                        loadNextBitmap();
                        break;
                    }
                } else {
                    loadPreBitmap();
                    break;
                }
                break;
        }
        return true;
    }

    public void loadNextBitmap() {
        if (this.mCurrentIndex == 19) {
            System.out.println();
        }
        if (this.mCurrentIndex != this.mCount - 1) {
            Message msg = this.mHandler.obtainMessage();
            this.mCurrentIndex++;
            msg.what = this.mCurrentIndex;
            this.mHandler.sendMessage(msg);
        }
    }

    public void loadPreBitmap() {
        if (this.mCurrentIndex != 0) {
            Message msg = this.mHandler.obtainMessage();
            int i = this.mCurrentIndex;
            this.mCurrentIndex = i - 1;
            msg.what = i;
            this.mHandler.sendMessage(msg);
        }
    }

    public void setImageBitmapOnly(int index) {
        if (index >= 0 && index <= this.mCount - 1) {
            BitmapUtils.recycleBitmap(this.currentBitmap);
            this.currentBitmap = BitmapUtils.getBitMap((String) this.sourceIDS.get(index), getContext(), getLayoutParams().width, getLayoutParams().height);
            if (this.currentBitmap != null) {
                setImageBitmap(this.currentBitmap);
            }
        }
    }

    public ArrayList<Bitmap> getBitMapList() {
        return this.bitMapList;
    }

    public void setBitMapList(ArrayList<Bitmap> bitMapList2) {
        this.bitMapList = bitMapList2;
    }

    public void recyle() {
        setImageBitmap(null);
        BitmapUtils.recycleBitmap(this.currentBitmap);
        BitmapUtils.recycleBitmap(this.bitmap);
    }
}
