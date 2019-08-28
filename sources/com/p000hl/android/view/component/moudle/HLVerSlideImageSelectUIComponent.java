package com.p000hl.android.view.component.moudle;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent */
public class HLVerSlideImageSelectUIComponent extends RelativeLayout implements Component {
    /* access modifiers changed from: private */
    public int curSelectIndex = 0;
    /* access modifiers changed from: private */
    public int curWaitToSelectIndex = -1;
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> defBimaps;
    /* access modifiers changed from: private */
    public RectF dst;
    /* access modifiers changed from: private */
    public int longPressIndex = -1;
    /* access modifiers changed from: private */
    public RectF longPressRectf;
    /* access modifiers changed from: private */
    public Context mContext;
    private ComponentEntity mEntity;
    /* access modifiers changed from: private */
    public Paint mPaint;
    /* access modifiers changed from: private */
    public float mTopOffset = 0.0f;
    /* access modifiers changed from: private */
    public MyImagView myImageView;
    protected boolean playMoveAni;
    public ArrayList<PositionAndHeight> positionAndHeights;
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> selectBitmaps;
    /* access modifiers changed from: private */
    public float totalHeight = 0.0f;

    /* renamed from: com.hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent$MyImagView */
    class MyImagView extends View {
        public MyImagView(Context context) {
            super(context);
            HLVerSlideImageSelectUIComponent.this.longPressRectf = new RectF(-1.0f, -1.0f, -1.0f, -1.0f);
            setClickable(true);
            final GestureDetector detector = new GestureDetector(HLVerSlideImageSelectUIComponent.this.mContext, new SimpleOnGestureListener(HLVerSlideImageSelectUIComponent.this) {
                public boolean onSingleTapUp(MotionEvent e) {
                    if (!HLVerSlideImageSelectUIComponent.this.playMoveAni) {
                        int i = 0;
                        while (true) {
                            if (i >= HLVerSlideImageSelectUIComponent.this.defBimaps.size()) {
                                break;
                            }
                            if (HLVerSlideImageSelectUIComponent.this.touchInTheRect(e, 0.0f, HLVerSlideImageSelectUIComponent.this.mTopOffset + ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition, (float) MyImagView.this.getLayoutParams().width, ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mHeight)) {
                                HLVerSlideImageSelectUIComponent.this.curSelectIndex = i;
                                MyImagView.this.playMoveAnim(-((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition, 200);
                                HLVerSlideImageSelectUIComponent.this.doClickItemEvent(i);
                                break;
                            }
                            i++;
                        }
                    }
                    return false;
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }

                public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
                    if (!HLVerSlideImageSelectUIComponent.this.playMoveAni && HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex == -1) {
                        HLVerSlideImageSelectUIComponent.access$224(HLVerSlideImageSelectUIComponent.this, arg3);
                        MyImagView.this.invalidate();
                    }
                    return false;
                }

                public void onLongPress(MotionEvent event) {
                    super.onLongPress(event);
                    if (!HLVerSlideImageSelectUIComponent.this.playMoveAni) {
                        for (int i = 0; i < HLVerSlideImageSelectUIComponent.this.defBimaps.size(); i++) {
                            if (HLVerSlideImageSelectUIComponent.this.touchInTheRect(event, 0.0f, HLVerSlideImageSelectUIComponent.this.mTopOffset + ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition, (float) MyImagView.this.getLayoutParams().width, ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mHeight)) {
                                HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex = i;
                                HLVerSlideImageSelectUIComponent.this.longPressRectf.left = 0.0f;
                                HLVerSlideImageSelectUIComponent.this.longPressRectf.top = ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition + HLVerSlideImageSelectUIComponent.this.mTopOffset;
                                HLVerSlideImageSelectUIComponent.this.longPressRectf.right = (float) MyImagView.this.getLayoutParams().width;
                                HLVerSlideImageSelectUIComponent.this.longPressRectf.bottom = ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mHeight + HLVerSlideImageSelectUIComponent.this.mTopOffset + ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition;
                                MyImagView.this.invalidate();
                                return;
                            }
                        }
                    }
                }
            });
            setOnTouchListener(new OnTouchListener(HLVerSlideImageSelectUIComponent.this) {
                public boolean onTouch(View v, MotionEvent event) {
                    if (!HLVerSlideImageSelectUIComponent.this.playMoveAni) {
                        detector.onTouchEvent(event);
                        if (event.getAction() == 1) {
                            if (HLVerSlideImageSelectUIComponent.this.mTopOffset > 0.0f) {
                                MyImagView.this.playMoveAnim(0.0f, 200);
                            }
                            if (HLVerSlideImageSelectUIComponent.this.totalHeight + HLVerSlideImageSelectUIComponent.this.mTopOffset < ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(HLVerSlideImageSelectUIComponent.this.positionAndHeights.size() - 1)).mHeight) {
                                if (HLVerSlideImageSelectUIComponent.this.playMoveAni) {
                                    MyImagView.this.getAnimation().cancel();
                                }
                                MyImagView.this.playMoveAnim(((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(HLVerSlideImageSelectUIComponent.this.positionAndHeights.size() - 1)).mHeight - HLVerSlideImageSelectUIComponent.this.totalHeight, 200);
                            }
                            if (HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex != -1 && HLVerSlideImageSelectUIComponent.this.longPressRectf.contains(event.getX(), event.getY())) {
                                HLVerSlideImageSelectUIComponent.this.curSelectIndex = HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex;
                                HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex = -1;
                                MyImagView.this.playMoveAnim(HLVerSlideImageSelectUIComponent.this.mTopOffset - HLVerSlideImageSelectUIComponent.this.longPressRectf.top, 200);
                                HLVerSlideImageSelectUIComponent.this.doClickItemEvent(HLVerSlideImageSelectUIComponent.this.curSelectIndex);
                            }
                            HLVerSlideImageSelectUIComponent.this.longPressRectf.left = -1.0f;
                            HLVerSlideImageSelectUIComponent.this.longPressRectf.right = -1.0f;
                            HLVerSlideImageSelectUIComponent.this.longPressRectf.top = -1.0f;
                            HLVerSlideImageSelectUIComponent.this.longPressRectf.bottom = -1.0f;
                        } else if (HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex != -1) {
                            if (!HLVerSlideImageSelectUIComponent.this.longPressRectf.contains(event.getX(), event.getY())) {
                                HLVerSlideImageSelectUIComponent.this.longPressIndex = HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex;
                                HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex = -1;
                                MyImagView.this.invalidate();
                            }
                        } else if (HLVerSlideImageSelectUIComponent.this.longPressRectf.contains(event.getX(), event.getY())) {
                            HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex = HLVerSlideImageSelectUIComponent.this.longPressIndex;
                            MyImagView.this.invalidate();
                        }
                    }
                    return true;
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            for (int i = 0; i < HLVerSlideImageSelectUIComponent.this.defBimaps.size(); i++) {
                Bitmap drawBitmap = (Bitmap) HLVerSlideImageSelectUIComponent.this.defBimaps.get(i);
                if (HLVerSlideImageSelectUIComponent.this.curSelectIndex == i || HLVerSlideImageSelectUIComponent.this.curWaitToSelectIndex == i) {
                    drawBitmap = (Bitmap) HLVerSlideImageSelectUIComponent.this.selectBitmaps.get(i);
                }
                HLVerSlideImageSelectUIComponent.this.dst = new RectF(0.0f, HLVerSlideImageSelectUIComponent.this.mTopOffset + ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition, (float) getLayoutParams().width, ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mHeight + HLVerSlideImageSelectUIComponent.this.mTopOffset + ((PositionAndHeight) HLVerSlideImageSelectUIComponent.this.positionAndHeights.get(i)).mPosition);
                canvas.drawBitmap(drawBitmap, null, HLVerSlideImageSelectUIComponent.this.dst, HLVerSlideImageSelectUIComponent.this.mPaint);
            }
        }

        /* access modifiers changed from: private */
        public void playMoveAnim(float endPosition, long duration) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "mTopOffset", new float[]{endPosition});
            animator.setDuration(duration);
            animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator arg0) {
                    HLVerSlideImageSelectUIComponent.this.playMoveAni = true;
                    HLVerSlideImageSelectUIComponent.this.myImageView.postInvalidate();
                }

                public void onAnimationRepeat(Animator arg0) {
                }

                public void onAnimationEnd(Animator arg0) {
                    HLVerSlideImageSelectUIComponent.this.playMoveAni = false;
                }

                public void onAnimationCancel(Animator arg0) {
                }
            });
            animator.start();
        }

        public void setMTopOffset(float topOffset) {
            HLVerSlideImageSelectUIComponent.this.mTopOffset = topOffset;
            invalidate();
        }

        public float getMTopOffset() {
            return HLVerSlideImageSelectUIComponent.this.mTopOffset;
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.HLVerSlideImageSelectUIComponent$PositionAndHeight */
    class PositionAndHeight {
        float mHeight;
        float mPosition;

        public PositionAndHeight(float position, float height) {
            this.mPosition = position;
            this.mHeight = height;
        }
    }

    static /* synthetic */ float access$224(HLVerSlideImageSelectUIComponent x0, float x1) {
        float f = x0.mTopOffset - x1;
        x0.mTopOffset = f;
        return f;
    }

    public HLVerSlideImageSelectUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
        this.mPaint = new Paint();
        this.defBimaps = new ArrayList<>();
        this.selectBitmaps = new ArrayList<>();
        this.positionAndHeights = new ArrayList<>();
    }

    private void loadBitmaps() {
        ArrayList<String> sourceIDS = ((MoudleComponentEntity) this.mEntity).getSourceIDList();
        ArrayList<String> selectSourceIDS = ((MoudleComponentEntity) this.mEntity).getSelectSourceIDList();
        if (!(sourceIDS == null || selectSourceIDS == null)) {
            for (int i = 0; i < sourceIDS.size(); i++) {
                Bitmap defBimap = BitmapUtils.getBitMap((String) sourceIDS.get(i), this.mContext);
                Bitmap selectBitmap = BitmapUtils.getBitMap((String) selectSourceIDS.get(i), this.mContext);
                this.defBimaps.add(defBimap);
                this.selectBitmaps.add(selectBitmap);
                float curHeight = ((float) defBimap.getHeight()) * ((((float) getLayoutParams().width) * 1.0f) / ((float) defBimap.getWidth()));
                this.positionAndHeights.add(new PositionAndHeight(this.totalHeight, curHeight));
                this.totalHeight += curHeight;
            }
        }
        this.mTopOffset = 0.0f;
    }

    /* access modifiers changed from: private */
    public boolean touchInTheRect(MotionEvent event, float x, float y, float width, float height) {
        if (event.getX() <= x || event.getX() >= x + width || event.getY() <= y || event.getY() >= y + height) {
            return false;
        }
        return true;
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        loadBitmaps();
        this.myImageView = new MyImagView(this.mContext);
        addView(this.myImageView, new LayoutParams(getLayoutParams().width, (int) this.totalHeight));
    }

    public void doSelectItemEvent(int index) {
        this.myImageView.playMoveAnim(-((PositionAndHeight) this.positionAndHeights.get(index)).mPosition, 300);
        this.curSelectIndex = index;
        this.curWaitToSelectIndex = -1;
    }

    /* access modifiers changed from: private */
    public void doClickItemEvent(int i) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                BehaviorHelper.doBeheavorForList(behavior, i, this.mEntity.componentId);
            }
        }
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        BitmapUtils.recycleBitmaps(this.defBimaps);
        BitmapUtils.recycleBitmaps(this.selectBitmaps);
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
