package com.p000hl.android.view.component.moudle;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"DrawAllocation", "ViewConstructor"})
/* renamed from: com.hl.android.view.component.moudle.HLMouseVerInteractScrollUIComponent */
public class HLMouseVerInteractScrollUIComponent extends View implements Component {
    /* access modifiers changed from: private */
    public Animator animator;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    /* access modifiers changed from: private */
    public int bottomY = 0;
    GestureDetector detector = null;
    RectF dst = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), (float) getPaddingLeft(), (float) getPaddingTop());
    private float lastoffsetY = 0.0f;
    SimpleOnGestureListener listener = new SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
            float aa;
            if (HLMouseVerInteractScrollUIComponent.this.animator == null || !HLMouseVerInteractScrollUIComponent.this.animator.isRunning()) {
                if (HLMouseVerInteractScrollUIComponent.this.offsetY <= HLMouseVerInteractScrollUIComponent.this.mHeight - HLMouseVerInteractScrollUIComponent.this.bottomY || HLMouseVerInteractScrollUIComponent.this.offsetY >= 0) {
                    aa = arg3 / 2.0f;
                    if (HLMouseVerInteractScrollUIComponent.this.offsetY >= 0) {
                        if (arg3 > 0.0f) {
                            aa = arg3;
                        }
                    } else if (HLMouseVerInteractScrollUIComponent.this.offsetY <= HLMouseVerInteractScrollUIComponent.this.mHeight - HLMouseVerInteractScrollUIComponent.this.bottomY && arg3 < 0.0f) {
                        aa = arg3;
                    }
                } else {
                    aa = arg3;
                }
                HLMouseVerInteractScrollUIComponent.access$224(HLMouseVerInteractScrollUIComponent.this, aa);
                HLMouseVerInteractScrollUIComponent.this.invalidate();
            }
            return true;
        }
    };
    private Context mContext;
    private ComponentEntity mEntity;
    /* access modifiers changed from: private */
    public int mHeight;
    /* access modifiers changed from: private */
    public int offsetY = 0;
    private ArrayList<MyRect> rects;

    /* renamed from: com.hl.android.view.component.moudle.HLMouseVerInteractScrollUIComponent$MyRect */
    class MyRect {
        public Bitmap mDrawBitmap;
        public int[] mSize;

        public MyRect(Bitmap bitmap4draw, int[] size) {
            this.mDrawBitmap = bitmap4draw;
            this.mSize = size;
        }
    }

    static /* synthetic */ int access$224(HLMouseVerInteractScrollUIComponent x0, float x1) {
        int i = (int) (((float) x0.offsetY) - x1);
        x0.offsetY = i;
        return i;
    }

    public HLMouseVerInteractScrollUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
    }

    private void loadRects() {
        this.rects = new ArrayList<>();
        ArrayList<String> sourceIDS = ((MoudleComponentEntity) this.mEntity).getSourceIDList();
        if (sourceIDS != null) {
            for (int i = 0; i < sourceIDS.size(); i++) {
                int[] size = {getLayoutParams().width, 0};
                Bitmap bitmap4draw = BitmapUtils.getBitMap((String) sourceIDS.get(i), this.mContext, size);
                size[1] = (int) ((((float) size[1]) * ((float) getLayoutParams().width)) / ((float) size[0]));
                size[0] = getLayoutParams().width;
                this.rects.add(new MyRect(bitmap4draw, size));
                this.bottomY += size[1];
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            this.dst.top = (float) (this.offsetY + getPaddingTop());
            Iterator i$ = this.rects.iterator();
            while (i$.hasNext()) {
                MyRect myRect = (MyRect) i$.next();
                if (this.dst.top >= ((float) getLayoutParams().height)) {
                    break;
                }
                this.dst.bottom = this.dst.top + ((float) myRect.mSize[1]);
                if (this.dst.bottom <= 0.0f) {
                    this.dst.top = this.dst.bottom;
                } else if (myRect.mDrawBitmap == null || !myRect.mDrawBitmap.isRecycled()) {
                    canvas.drawBitmap(myRect.mDrawBitmap, null, this.dst, null);
                    this.dst.top = this.dst.bottom;
                }
            }
            new CountDownTimer(0, 0) {
                public void onTick(long arg0) {
                }

                public void onFinish() {
                    HLMouseVerInteractScrollUIComponent.this.doRectAction();
                }
            }.start();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void doRectAction() {
        boolean curIsIn;
        boolean lastIsIn;
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity beheavior = (BehaviorEntity) i$.next();
            if (beheavior.EventName.equals("BEHAVIOR_ON_TEMPLATE_SLIDER_IN") || beheavior.EventName.equals("BEHAVIOR_ON_TEMPLATE_SLIDER_OUT")) {
                String[] splits = beheavior.EventValue.split(",");
                float[] positions = {Float.parseFloat(splits[0]), Float.parseFloat(splits[1])};
                int top = (int) Math.min(positions[0], positions[1]);
                int bottom = (int) Math.max(positions[0], positions[1]);
                if ((-this.offsetY) <= top || (-this.offsetY) >= bottom) {
                    curIsIn = false;
                } else {
                    curIsIn = true;
                }
                if ((-this.lastoffsetY) <= ((float) top) || (-this.lastoffsetY) >= ((float) bottom)) {
                    lastIsIn = false;
                } else {
                    lastIsIn = true;
                }
                if (curIsIn ^ lastIsIn) {
                    if (!curIsIn && beheavior.EventName.equals("BEHAVIOR_ON_TEMPLATE_SLIDER_OUT")) {
                        BookController.getInstance().runBehavior(beheavior);
                    }
                    if (curIsIn && beheavior.EventName.equals("BEHAVIOR_ON_TEMPLATE_SLIDER_IN")) {
                        BookController.getInstance().runBehavior(beheavior);
                    }
                }
            }
        }
        this.lastoffsetY = (float) this.offsetY;
    }

    public void setOffsetY(int p) {
        this.offsetY = p;
        invalidate();
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        this.mHeight = getLayoutParams().height;
        this.dst.right = (float) getLayoutParams().width;
        loadRects();
        setClickable(true);
        this.detector = new GestureDetector(this.mContext, this.listener);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.detector.onTouchEvent(event);
        if (event.getAction() == 1) {
            if (this.offsetY > 0) {
                doGoBackAnimation(0);
            } else if (this.offsetY < this.mHeight - this.bottomY) {
                doGoBackAnimation(this.mHeight - this.bottomY);
            }
        }
        return true;
    }

    private void doGoBackAnimation(int endOffset) {
        if (this.animator != null && this.animator.isRunning()) {
            this.animator.cancel();
        }
        this.animator = ObjectAnimator.ofInt(this, "offsetY", new int[]{endOffset});
        this.animator.setDuration(500);
        this.animator.start();
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        if (this.rects != null) {
            for (int i = 0; i < this.rects.size(); i++) {
                BitmapUtils.recycleBitmap(((MyRect) this.rects.get(i)).mDrawBitmap);
            }
            this.rects.clear();
            this.rects = null;
        }
    }

    public void hide() {
        setVisibility(4);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
    }
}
