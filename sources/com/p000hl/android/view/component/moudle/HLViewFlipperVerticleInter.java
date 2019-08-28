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

/* renamed from: com.hl.android.view.component.moudle.HLViewFlipperVerticleInter */
public class HLViewFlipperVerticleInter extends RelativeLayout implements Component, ComponentPost {
    /* access modifiers changed from: private */
    public ArrayList<Bitmap> bitmaps;
    /* access modifiers changed from: private */
    public int curShowIndex;
    /* access modifiers changed from: private */

    /* renamed from: dx */
    public float f40dx = 0.0f;
    /* access modifiers changed from: private */

    /* renamed from: dy */
    public float f41dy = 0.0f;
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

    /* renamed from: com.hl.android.view.component.moudle.HLViewFlipperVerticleInter$MyImageView */
    class MyImageView extends View {
        /* access modifiers changed from: private */
        public boolean hasFlingAni;
        /* access modifiers changed from: private */
        public ArrayList<Bitmap> mBitmaps;
        private RectF rectF;
        /* access modifiers changed from: private */
        public float topDrawPositon;

        static /* synthetic */ float access$1216(MyImageView x0, float x1) {
            float f = x0.topDrawPositon + x1;
            x0.topDrawPositon = f;
            return f;
        }

        public MyImageView(Context context, ArrayList<Bitmap> bitmaps) {
            super(context);
            this.mBitmaps = bitmaps;
            this.rectF = new RectF(0.0f, this.topDrawPositon, (float) HLViewFlipperVerticleInter.this.mImageWidth, this.topDrawPositon + ((float) HLViewFlipperVerticleInter.this.mImageHeight));
            final GestureDetector detector = new GestureDetector(HLViewFlipperVerticleInter.this.mContext, new SimpleOnGestureListener(HLViewFlipperVerticleInter.this) {
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityY > 0.0f) {
                        if (HLViewFlipperVerticleInter.this.curShowIndex - 1 >= 0) {
                            HLViewFlipperVerticleInter.this.doClickCircle(HLViewFlipperVerticleInter.this.curShowIndex - 1);
                            MyImageView.this.hasFlingAni = true;
                        }
                    } else if (HLViewFlipperVerticleInter.this.curShowIndex + 1 <= MyImageView.this.mBitmaps.size() - 1) {
                        HLViewFlipperVerticleInter.this.doClickCircle(HLViewFlipperVerticleInter.this.curShowIndex + 1);
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
            setOnTouchListener(new OnTouchListener(HLViewFlipperVerticleInter.this) {
                public boolean onTouch(View v, MotionEvent event) {
                    detector.onTouchEvent(event);
                    if (event.getAction() != 0) {
                        if (event.getAction() == 2) {
                            if (!MyImageView.this.hasFlingAni) {
                                HLViewFlipperVerticleInter.this.f40dx = event.getX() - HLViewFlipperVerticleInter.this.oldEvent.getX();
                                HLViewFlipperVerticleInter.this.f41dy = event.getY() - HLViewFlipperVerticleInter.this.oldEvent.getY();
                                MyImageView.access$1216(MyImageView.this, HLViewFlipperVerticleInter.this.f41dy);
                                if (MyImageView.this.topDrawPositon > 0.0f) {
                                    MyImageView.this.topDrawPositon = 0.0f;
                                } else if (MyImageView.this.topDrawPositon + ((float) (MyImageView.this.mBitmaps.size() * HLViewFlipperVerticleInter.this.mImageHeight)) < ((float) HLViewFlipperVerticleInter.this.mImageHeight)) {
                                    MyImageView.this.topDrawPositon = (float) (HLViewFlipperVerticleInter.this.mImageHeight - (MyImageView.this.mBitmaps.size() * HLViewFlipperVerticleInter.this.mImageHeight));
                                }
                                HLViewFlipperVerticleInter.access$1316(HLViewFlipperVerticleInter.this, Math.abs(HLViewFlipperVerticleInter.this.f40dx));
                                HLViewFlipperVerticleInter.access$1416(HLViewFlipperVerticleInter.this, Math.abs(HLViewFlipperVerticleInter.this.f41dy));
                                MyImageView.this.invalidate();
                            }
                            return true;
                        } else if (event.getAction() == 1) {
                            if (HLViewFlipperVerticleInter.this.totalAbsDx < 2.0f && HLViewFlipperVerticleInter.this.totalAbsDy < 2.0f) {
                                HLViewFlipperVerticleInter.this.doClickAction(HLViewFlipperVerticleInter.this.curShowIndex);
                            } else if (MyImageView.this.mBitmaps.size() >= 1) {
                                if (MyImageView.this.hasFlingAni) {
                                    MyImageView.this.hasFlingAni = false;
                                } else if (MyImageView.this.topDrawPositon + ((float) (HLViewFlipperVerticleInter.this.curShowIndex * HLViewFlipperVerticleInter.this.mImageHeight)) >= ((float) (HLViewFlipperVerticleInter.this.mImageHeight / 2))) {
                                    HLViewFlipperVerticleInter.this.doClickCircle(HLViewFlipperVerticleInter.this.curShowIndex - 1);
                                } else if (MyImageView.this.topDrawPositon + ((float) (HLViewFlipperVerticleInter.this.curShowIndex * HLViewFlipperVerticleInter.this.mImageHeight)) <= ((float) ((-HLViewFlipperVerticleInter.this.mImageHeight) / 2))) {
                                    HLViewFlipperVerticleInter.this.doClickCircle(HLViewFlipperVerticleInter.this.curShowIndex + 1);
                                } else {
                                    HLViewFlipperVerticleInter.this.doClickCircle(HLViewFlipperVerticleInter.this.curShowIndex);
                                }
                            }
                            HLViewFlipperVerticleInter.this.totalAbsDx = 0.0f;
                            HLViewFlipperVerticleInter.this.totalAbsDy = 0.0f;
                        }
                    }
                    HLViewFlipperVerticleInter.this.oldEvent = MotionEvent.obtain(event);
                    return true;
                }
            });
        }

        public void setTopDrawPositon(float topDrawPositon2) {
            this.topDrawPositon = topDrawPositon2;
            invalidate();
        }

        public float getTopDrawPositon() {
            return this.topDrawPositon;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            for (int i = 0; i < this.mBitmaps.size(); i++) {
                this.rectF.top = ((float) (HLViewFlipperVerticleInter.this.mImageHeight * i)) + this.topDrawPositon;
                this.rectF.bottom = this.rectF.top + ((float) HLViewFlipperVerticleInter.this.mImageHeight);
                if (this.mBitmaps.get(i) != null && !((Bitmap) this.mBitmaps.get(i)).isRecycled()) {
                    canvas.drawBitmap((Bitmap) this.mBitmaps.get(i), null, this.rectF, null);
                }
            }
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.HLViewFlipperVerticleInter$NaviView */
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
            String des = (String) HLViewFlipperVerticleInter.this.mEntity.renderDes.get(HLViewFlipperVerticleInter.this.curShowIndex);
            for (int i = 0; i < des.length(); i++) {
                canvas.drawText(des.charAt(i) + "", 5.0f, ((((((float) HLViewFlipperVerticleInter.this.mImageHeight) * 1.0f) / 2.0f) + 7.0f) - ((((float) des.length()) * this.paint.getTextSize()) / 2.0f)) + (((float) i) * this.paint.getTextSize()), this.paint);
            }
            for (int i2 = 0; i2 < HLViewFlipperVerticleInter.this.bitmaps.size(); i2++) {
                this.paint.setStyle(Style.STROKE);
                canvas.drawCircle(40.0f, (((((float) HLViewFlipperVerticleInter.this.mImageHeight) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperVerticleInter.this.bitmaps.size() - 1) * 15))) + ((float) (i2 * 30)), 10.0f, this.paint);
                if (i2 == HLViewFlipperVerticleInter.this.curShowIndex) {
                    this.paint.setStyle(Style.FILL);
                    canvas.drawCircle(40.0f, (((((float) HLViewFlipperVerticleInter.this.mImageHeight) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperVerticleInter.this.bitmaps.size() - 1) * 15))) + ((float) (i2 * 30)), 6.0f, this.paint);
                }
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == 1) {
                int i = 0;
                while (true) {
                    if (i >= HLViewFlipperVerticleInter.this.bitmaps.size()) {
                        break;
                    } else if (event.getX() <= 25.0f || event.getY() <= ((((((float) HLViewFlipperVerticleInter.this.mImageHeight) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperVerticleInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30))) - 15.0f || event.getX() >= 55.0f || event.getY() >= (((((float) HLViewFlipperVerticleInter.this.mImageHeight) * 1.0f) / 2.0f) - ((float) ((HLViewFlipperVerticleInter.this.bitmaps.size() - 1) * 15))) + ((float) (i * 30)) + 15.0f) {
                        i++;
                    } else if (HLViewFlipperVerticleInter.this.curShowIndex != i) {
                        HLViewFlipperVerticleInter.this.doClickCircle(i);
                    }
                }
            }
            return true;
        }
    }

    static /* synthetic */ float access$1316(HLViewFlipperVerticleInter x0, float x1) {
        float f = x0.totalAbsDx + x1;
        x0.totalAbsDx = f;
        return f;
    }

    static /* synthetic */ float access$1416(HLViewFlipperVerticleInter x0, float x1) {
        float f = x0.totalAbsDy + x1;
        x0.totalAbsDy = f;
        return f;
    }

    public HLViewFlipperVerticleInter(Context context, ComponentEntity entity) {
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
            this.mImageWidth -= 60;
            this.naviView = new NaviView(this.mContext);
        }
        this.myImageView = new MyImageView(this.mContext, this.bitmaps);
        this.imageLayoutParams = new LayoutParams(this.mImageWidth, this.mImageHeight * this.bitmaps.size());
        this.imageLayoutParams.addRule(9);
        addView(this.myImageView, this.imageLayoutParams);
        if (this.mEntity.isShowNavi) {
            this.naviViewLayoutParams = new LayoutParams(60, this.mImageHeight);
            this.naviViewLayoutParams.addRule(11);
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "topDrawPositon", new float[]{(float) ((-endIndex) * this.mImageHeight)});
        animator.setDuration(duration);
        animator.addListener(new AnimatorListener() {
            private int triggerIndex = 0;

            public void onAnimationStart(Animator arg0) {
                this.triggerIndex = endIndex;
                HLViewFlipperVerticleInter.this.doChangeStart(this.triggerIndex);
                HLViewFlipperVerticleInter.this.curShowIndex = endIndex;
                if (HLViewFlipperVerticleInter.this.naviView != null) {
                    HLViewFlipperVerticleInter.this.naviView.postInvalidate();
                }
            }

            public void onAnimationRepeat(Animator arg0) {
            }

            public void onAnimationEnd(Animator arg0) {
                HLViewFlipperVerticleInter.this.doChangeEnd(this.triggerIndex);
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
