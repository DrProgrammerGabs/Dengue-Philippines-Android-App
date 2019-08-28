package com.p000hl.android.view.component.moudle;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
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
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.inter.ComponentPost;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.view.component.moudle.HLViewFlipperInter */
public class HLViewFlipperInter extends RelativeLayout implements Component, ComponentPost {
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> bitmaps;
    /* access modifiers changed from: private */
    public int curShowIndex;
    /* access modifiers changed from: private */

    /* renamed from: dx */
    public float f38dx = 0.0f;
    /* access modifiers changed from: private */

    /* renamed from: dy */
    public float f39dy = 0.0f;
    private LayoutParams imageLayoutParams;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public MoudleComponentEntity mEntity;
    /* access modifiers changed from: private */
    public int mImageHeight;
    /* access modifiers changed from: private */
    public int mImageWidth;
    private View myImageView;
    /* access modifiers changed from: private */
    public NaviView naviView;
    private LayoutParams naviViewLayoutParams;
    /* access modifiers changed from: private */
    public MotionEvent oldEvent = null;
    /* access modifiers changed from: private */
    public float totalAbsDx = 0.0f;
    /* access modifiers changed from: private */
    public float totalAbsDy = 0.0f;

    /* renamed from: com.hl.android.view.component.moudle.HLViewFlipperInter$MyImageView */
    class MyImageView extends View {
        /* access modifiers changed from: private */
        public boolean hasFlingAni;
        /* access modifiers changed from: private */
        public float leftDrawPositon;
        /* access modifiers changed from: private */
        public ArrayList<Bitmap> mBitmaps;
        private RectF rectF;

        static /* synthetic */ float access$1216(MyImageView x0, float x1) {
            float f = x0.leftDrawPositon + x1;
            x0.leftDrawPositon = f;
            return f;
        }

        public MyImageView(Context context, ArrayList<Bitmap> bitmaps) {
            super(context);
            this.mBitmaps = bitmaps;
            this.rectF = new RectF(this.leftDrawPositon, 0.0f, this.leftDrawPositon + ((float) HLViewFlipperInter.this.mImageWidth), (float) HLViewFlipperInter.this.mImageHeight);
            final GestureDetector detector = new GestureDetector(HLViewFlipperInter.this.mContext, new SimpleOnGestureListener(HLViewFlipperInter.this) {
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX > 0.0f) {
                        if (HLViewFlipperInter.this.curShowIndex - 1 >= 0) {
                            HLViewFlipperInter.this.doClickCircle(HLViewFlipperInter.this.curShowIndex - 1);
                            MyImageView.this.hasFlingAni = true;
                        }
                    } else if (HLViewFlipperInter.this.curShowIndex + 1 <= MyImageView.this.mBitmaps.size() - 1) {
                        HLViewFlipperInter.this.doClickCircle(HLViewFlipperInter.this.curShowIndex + 1);
                        MyImageView.this.hasFlingAni = true;
                    }
                    return false;
                }

                public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
                    return false;
                }

                public void onLongPress(MotionEvent event) {
                }
            });
            setOnTouchListener(new OnTouchListener(HLViewFlipperInter.this) {
                public boolean onTouch(View v, MotionEvent event) {
                    detector.onTouchEvent(event);
                    if (event.getAction() != 0) {
                        if (event.getAction() == 2) {
                            if (!MyImageView.this.hasFlingAni) {
                                HLViewFlipperInter.this.f38dx = event.getX() - HLViewFlipperInter.this.oldEvent.getX();
                                HLViewFlipperInter.this.f39dy = event.getY() - HLViewFlipperInter.this.oldEvent.getY();
                                MyImageView.access$1216(MyImageView.this, HLViewFlipperInter.this.f38dx);
                                if (MyImageView.this.leftDrawPositon > 0.0f) {
                                    MyImageView.this.leftDrawPositon = 0.0f;
                                } else if (MyImageView.this.leftDrawPositon + ((float) (MyImageView.this.mBitmaps.size() * HLViewFlipperInter.this.mImageWidth)) < ((float) HLViewFlipperInter.this.mImageWidth)) {
                                    MyImageView.this.leftDrawPositon = (float) (HLViewFlipperInter.this.mImageWidth - (MyImageView.this.mBitmaps.size() * HLViewFlipperInter.this.mImageWidth));
                                }
                                HLViewFlipperInter.access$1316(HLViewFlipperInter.this, Math.abs(HLViewFlipperInter.this.f38dx));
                                HLViewFlipperInter.access$1416(HLViewFlipperInter.this, Math.abs(HLViewFlipperInter.this.f39dy));
                                MyImageView.this.invalidate();
                            }
                            return true;
                        } else if (event.getAction() == 1) {
                            if (HLViewFlipperInter.this.totalAbsDx < 2.0f && HLViewFlipperInter.this.totalAbsDy < 2.0f) {
                                HLViewFlipperInter.this.doClickAction(HLViewFlipperInter.this.curShowIndex);
                            } else if (MyImageView.this.mBitmaps.size() >= 1) {
                                if (MyImageView.this.hasFlingAni) {
                                    MyImageView.this.hasFlingAni = false;
                                } else if (MyImageView.this.leftDrawPositon + ((float) (HLViewFlipperInter.this.curShowIndex * HLViewFlipperInter.this.mImageWidth)) >= ((float) (HLViewFlipperInter.this.mImageWidth / 2))) {
                                    HLViewFlipperInter.this.doClickCircle(HLViewFlipperInter.this.curShowIndex - 1);
                                } else if (MyImageView.this.leftDrawPositon + ((float) (HLViewFlipperInter.this.curShowIndex * HLViewFlipperInter.this.mImageWidth)) <= ((float) ((-HLViewFlipperInter.this.mImageWidth) / 2))) {
                                    HLViewFlipperInter.this.doClickCircle(HLViewFlipperInter.this.curShowIndex + 1);
                                } else {
                                    HLViewFlipperInter.this.doClickCircle(HLViewFlipperInter.this.curShowIndex);
                                }
                            }
                            HLViewFlipperInter.this.totalAbsDx = 0.0f;
                            HLViewFlipperInter.this.totalAbsDy = 0.0f;
                        }
                    }
                    HLViewFlipperInter.this.oldEvent = MotionEvent.obtain(event);
                    return true;
                }
            });
        }

        public void setLeftDrawPositon(float leftDrawPositon2) {
            this.leftDrawPositon = leftDrawPositon2;
            invalidate();
        }

        public float getLeftDrawPositon() {
            return this.leftDrawPositon;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            for (int i = 0; i < this.mBitmaps.size(); i++) {
                this.rectF.left = ((float) (HLViewFlipperInter.this.mImageWidth * i)) + this.leftDrawPositon;
                this.rectF.right = this.rectF.left + ((float) HLViewFlipperInter.this.mImageWidth);
                if (this.mBitmaps.get(i) != null && !((Bitmap) this.mBitmaps.get(i)).isRecycled()) {
                    canvas.drawBitmap((Bitmap) this.mBitmaps.get(i), new Rect(0, 0, ((Bitmap) this.mBitmaps.get(i)).getWidth(), ((Bitmap) this.mBitmaps.get(i)).getHeight()), this.rectF, null);
                }
            }
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.HLViewFlipperInter$NaviView */
    class NaviView extends View {
        private Paint paint = new Paint(4);

        public NaviView(Context context) {
            super(context);
            this.paint.setStyle(Style.STROKE);
            this.paint.setTextSize(15.0f);
            this.paint.setAntiAlias(true);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            String des = (String) HLViewFlipperInter.this.mEntity.renderDes.get(HLViewFlipperInter.this.curShowIndex);
            canvas.drawText(des, ((((float) HLViewFlipperInter.this.mImageWidth) * 1.0f) / 2.0f) - (this.paint.measureText(des) / 2.0f), 20.0f, this.paint);
            for (int i = 0; i < HLViewFlipperInter.this.bitmaps.size(); i++) {
                this.paint.setStyle(Style.STROKE);
                canvas.drawCircle((((((float) HLViewFlipperInter.this.mImageWidth) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30)), 40.0f, 10.0f, this.paint);
                if (i == HLViewFlipperInter.this.curShowIndex) {
                    this.paint.setStyle(Style.FILL);
                    canvas.drawCircle((((((float) HLViewFlipperInter.this.mImageWidth) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30)), 40.0f, 6.0f, this.paint);
                }
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == 1) {
                int i = 0;
                while (true) {
                    if (i >= HLViewFlipperInter.this.bitmaps.size()) {
                        break;
                    } else if (event.getY() <= 25.0f || event.getX() <= ((((((float) HLViewFlipperInter.this.mImageWidth) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30))) - 15.0f || event.getY() >= 55.0f || event.getX() >= (((((float) HLViewFlipperInter.this.mImageWidth) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30)) + 15.0f) {
                        i++;
                    } else if (HLViewFlipperInter.this.curShowIndex != i) {
                        HLViewFlipperInter.this.doClickCircle(i);
                    }
                }
            }
            return true;
        }
    }

    static /* synthetic */ float access$1316(HLViewFlipperInter x0, float x1) {
        float f = x0.totalAbsDx + x1;
        x0.totalAbsDx = f;
        return f;
    }

    static /* synthetic */ float access$1416(HLViewFlipperInter x0, float x1) {
        float f = x0.totalAbsDy + x1;
        x0.totalAbsDy = f;
        return f;
    }

    public HLViewFlipperInter(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (MoudleComponentEntity) entity;
        setBackgroundColor(0);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        ArrayList<String> sourceIDS = this.mEntity.getSourceIDList();
        this.bitmaps = new ArrayList<>();
        Iterator i$ = sourceIDS.iterator();
        while (i$.hasNext()) {
            String curSourceID = (String) i$.next();
            Bitmap bitmap = BitmapManager.getBitmapFromCache(curSourceID);
            if (bitmap == null) {
                bitmap = BitmapUtils.getBitMap(curSourceID, this.mContext);
                BitmapManager.putBitmapCache(curSourceID, bitmap);
            }
            this.bitmaps.add(bitmap);
        }
        this.curShowIndex = 0;
        this.mImageHeight = getLayoutParams().height;
        this.mImageWidth = getLayoutParams().width;
        if (this.mEntity.isShowNavi) {
            this.mImageHeight -= 60;
            this.naviView = new NaviView(this.mContext);
        }
        this.myImageView = new MyImageView(this.mContext, this.bitmaps);
        this.imageLayoutParams = new LayoutParams(this.mImageWidth * this.bitmaps.size(), this.mImageHeight);
        this.imageLayoutParams.addRule(10);
        addView(this.myImageView, this.imageLayoutParams);
        if (this.mEntity.isShowNavi) {
            this.naviViewLayoutParams = new LayoutParams(this.mImageWidth, 60);
            this.naviViewLayoutParams.addRule(12);
            addView(this.naviView, this.naviViewLayoutParams);
        }
    }

    public void load(InputStream is) {
    }

    public void doClickCircle(int i) {
        if (this.myImageView.getAnimation() == null || this.myImageView.getAnimation().hasEnded()) {
            playChangeImageAnim(this.myImageView, i, 300);
        }
    }

    /* access modifiers changed from: private */
    public void doClickAction(int index) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                BehaviorHelper.doBeheavorForList(behavior, index, this.mEntity.componentId);
            }
        }
    }

    private void playChangeImageAnim(View v, final int endIndex, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "leftDrawPositon", new float[]{(float) ((-endIndex) * this.mImageWidth)});
        animator.setDuration(duration);
        animator.addListener(new AnimatorListener() {
            private int triggerIndex = 0;

            public void onAnimationStart(Animator arg0) {
                this.triggerIndex = endIndex;
                HLViewFlipperInter.this.doChangeStart(this.triggerIndex);
                HLViewFlipperInter.this.curShowIndex = endIndex;
                if (HLViewFlipperInter.this.naviView != null) {
                    HLViewFlipperInter.this.naviView.postInvalidate();
                }
            }

            public void onAnimationRepeat(Animator arg0) {
            }

            public void onAnimationEnd(Animator arg0) {
                HLViewFlipperInter.this.doChangeEnd(this.triggerIndex);
            }

            public void onAnimationCancel(Animator arg0) {
            }
        });
        animator.start();
    }

    public void recyle() {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
    }

    public void stop() {
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

    /* access modifiers changed from: private */
    public void doChangeStart(int index) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CHANGE_BEGIN.equals(behavior.EventName)) {
                BehaviorHelper.doBeheavorForList(behavior, index, this.mEntity.componentId);
            }
        }
    }

    /* access modifiers changed from: private */
    public void doChangeEnd(int index) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CHANGE_COMPLETE.equals(behavior.EventName)) {
                BehaviorHelper.doBeheavorForList(behavior, index, this.mEntity.componentId);
            }
        }
    }
}
