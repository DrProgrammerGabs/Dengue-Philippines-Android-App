package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.SliderEffectComponentEntity;
import com.p000hl.android.book.entity.SubImageItem;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.helper.animation.MyAnimation4CubeEffect;
import com.p000hl.android.core.helper.animation.MyAnimation4FlipEffect;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.Iterator;

@SuppressLint({"ViewConstructor", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.HLSliderEffectComponent */
public class HLSliderEffectComponent extends RelativeLayout implements Component {
    private static final float CLICKSIZELIMIT = 30.0f;
    /* access modifiers changed from: private */
    public boolean autoPlay = false;
    /* access modifiers changed from: private */
    public int curShowIndex = 0;
    private int currentAlpha = 255;
    private RectF dst;

    /* renamed from: dx */
    private float f23dx;

    /* renamed from: dy */
    private float f24dy;
    /* access modifiers changed from: private */
    public boolean hasAutoPlay;
    private boolean hasMovePlay;
    private int hasPlayCount = 1;
    private boolean isStop;
    private Context mContext;
    /* access modifiers changed from: private */
    public SliderEffectComponentEntity mEntity;
    private Paint mPaint;
    private MotionEvent oldEvent;
    private Paint paint;
    private float totalDx;
    private float totalDy;
    /* access modifiers changed from: private */
    public View view;

    /* renamed from: com.hl.android.view.component.HLSliderEffectComponent$MyView */
    public class MyView extends View {
        private int index = 0;

        public MyView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            HLSliderEffectComponent.this.drawCurrentImage(canvas);
            if (HLSliderEffectComponent.this.hasAutoPlay) {
                String aniType = ((SubImageItem) HLSliderEffectComponent.this.mEntity.subItems.get(HLSliderEffectComponent.this.curShowIndex)).aniType;
                String direction = ((SubImageItem) HLSliderEffectComponent.this.mEntity.subItems.get(HLSliderEffectComponent.this.curShowIndex)).aniProperty;
                if (aniType.equals("transitionFade")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("fade", true, direction);
                } else if (aniType.equals("cubeEffect")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionCubeEffect(true, direction);
                } else if (aniType.equals("flipEffect")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionFlipEffect(true, direction);
                } else if (aniType.equals("transitionMoveIn")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("moveIn", true, direction);
                } else if (aniType.equals("transitionPush")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("push", true, direction);
                } else if (aniType.equals("transitionReveal")) {
                    HLSliderEffectComponent.this.drawCurrentImage(canvas);
                    HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("reveal", true, direction);
                } else {
                    canvas.save();
                    canvas.translate(10.0f, 10.0f);
                    drawScene(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(160.0f, 10.0f);
                    canvas.clipRect(10, 10, 90, 90);
                    canvas.clipRect(HLSliderEffectComponent.CLICKSIZELIMIT, HLSliderEffectComponent.CLICKSIZELIMIT, 70.0f, 70.0f, Op.DIFFERENCE);
                    drawScene(canvas);
                    canvas.restore();
                }
            } else if (HLSliderEffectComponent.this.autoPlay) {
                HLSliderEffectComponent.this.hasAutoPlay = true;
                postInvalidateDelayed(((SubImageItem) HLSliderEffectComponent.this.mEntity.subItems.get(HLSliderEffectComponent.this.curShowIndex)).delay);
            }
        }

        private void drawScene(Canvas canvas) {
            canvas.drawColor(-1);
            Paint mPaint = new Paint();
            mPaint.setColor(-65536);
            canvas.drawLine(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), mPaint);
            mPaint.setColor(-16711936);
            canvas.drawCircle(50.0f, 50.0f, 50.0f, mPaint);
            mPaint.setColor(-16777216);
            canvas.drawText("Clipping", 100.0f, HLSliderEffectComponent.CLICKSIZELIMIT, mPaint);
        }
    }

    public HLSliderEffectComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (SliderEffectComponentEntity) entity;
        this.mPaint = new Paint(4);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setAntiAlias(true);
        this.paint = new Paint(4);
        this.paint.setStyle(Style.STROKE);
        this.paint.setAntiAlias(true);
        setBackgroundColor(0);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (SliderEffectComponentEntity) entity;
    }

    public void load() {
        this.dst = new RectF(0.0f, 0.0f, (float) getLayoutParams().width, (float) getLayoutParams().height);
        for (int i = 0; i < this.mEntity.subItems.size(); i++) {
            SubImageItem curSubImageItem = (SubImageItem) this.mEntity.subItems.get(i);
            curSubImageItem.changeSourceID2Bitmap(this.mContext);
            curSubImageItem.mIndex = i;
        }
        this.view = new MyView(this.mContext);
        addView(this.view, new LayoutParams(-1, -1));
    }

    public void load(InputStream is) {
    }

    /* access modifiers changed from: private */
    public void doPlayWithTypeTransitionFlipEffect(final boolean isNext, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        final SubImageItem nextSubItem = getNextSubItem(isNext);
        final ImageView imageView = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        imageView.setImageBitmap(curBitmap1);
        imageView.setScaleType(ScaleType.FIT_XY);
        addView(imageView, new LayoutParams(-1, -1));
        Animation curImageAnim = new MyAnimation4FlipEffect(0.0f, 180.0f, imageView, nextBitmap, direction);
        curImageAnim.setDuration(curSubItem.duration);
        curImageAnim.setAnimationListener(new AnimationListener() {
            ViewCell cell;
            float scalex;
            float scaley;

            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(nextSubItem.mIndex);
                this.cell = (ViewCell) HLSliderEffectComponent.this.getParent();
                this.scalex = this.cell.getScaleX();
                this.scaley = this.cell.getScaleY();
                this.cell.setScaleX(this.scalex * 2.0f);
                this.cell.setScaleY(this.scaley * 2.0f);
                imageView.setScaleX(0.5f);
                imageView.setScaleY(0.5f);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                this.cell.setScaleX(this.scalex);
                this.cell.setScaleY(this.scaley);
                if (VERSION.SDK_INT > 15) {
                    imageView.setScaleX(1.0f);
                    imageView.setScaleY(1.0f);
                }
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(isNext);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        imageView.startAnimation(curImageAnim);
    }

    /* access modifiers changed from: private */
    public void doPlayWithTypeTransitionCubeEffect(final boolean isNext, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        final SubImageItem nextSubItem = getNextSubItem(isNext);
        ImageView curShowImageView = new ImageView(this.mContext);
        ImageView nextShowImageView = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        curShowImageView.setImageBitmap(curBitmap1);
        curShowImageView.setScaleType(ScaleType.FIT_XY);
        nextShowImageView.setImageBitmap(nextBitmap);
        nextShowImageView.setScaleType(ScaleType.FIT_XY);
        LayoutParams params = new LayoutParams(-1, -1);
        addView(curShowImageView, params);
        addView(nextShowImageView, params);
        Animation curImageAnim = null;
        Animation nextImageAnim = null;
        if (direction.equals("left") || direction.equals("up")) {
            curImageAnim = new MyAnimation4CubeEffect(0.0f, -90.0f, direction);
            nextImageAnim = new MyAnimation4CubeEffect(90.0f, 0.0f, direction);
        } else if (direction.equals("right") || direction.equals("down")) {
            curImageAnim = new MyAnimation4CubeEffect(0.0f, 90.0f, direction);
            nextImageAnim = new MyAnimation4CubeEffect(-90.0f, 0.0f, direction);
        }
        curImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(nextSubItem.mIndex);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(isNext);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        curShowImageView.startAnimation(curImageAnim);
        nextShowImageView.startAnimation(nextImageAnim);
    }

    /* access modifiers changed from: private */
    public void doPlayWithTypeTransitionNomal(String type, boolean isNext, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        SubImageItem nextSubItem = getNextSubItem(isNext);
        ImageView imageView = new ImageView(this.mContext);
        ImageView imageView2 = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        imageView.setImageBitmap(curBitmap1);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView2.setImageBitmap(nextBitmap);
        imageView2.setScaleType(ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        addView(imageView, layoutParams);
        addView(imageView2, layoutParams);
        AnimationSet curImageAnim = null;
        AnimationSet nextImageAnim = null;
        if (type.equals("fade")) {
            curImageAnim = new AnimationSet(true);
            curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
            nextImageAnim = new AnimationSet(true);
            nextImageAnim.addAnimation(new AlphaAnimation(0.0f, 1.0f));
            curImageAnim.setZAdjustment(1);
            nextImageAnim.setZAdjustment(-1);
        } else {
            if (type.equals("moveIn")) {
                curImageAnim = new AnimationSet(true);
                curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                nextImageAnim = new AnimationSet(true);
                nextImageAnim.addAnimation(new AlphaAnimation(0.5f, 1.0f));
                TranslateAnimation transAni = null;
                if (direction.equals("left")) {
                    transAni = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                } else {
                    if (direction.equals("right")) {
                        transAni = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else {
                        if (direction.equals("up")) {
                            transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("down")) {
                                transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                            }
                        }
                    }
                }
                nextImageAnim.addAnimation(transAni);
            } else {
                if (type.equals("reveal")) {
                    curImageAnim = new AnimationSet(true);
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    nextImageAnim = new AnimationSet(true);
                    nextImageAnim.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    TranslateAnimation transAni2 = null;
                    if (direction.equals("left")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                    } else {
                        if (direction.equals("right")) {
                            transAni2 = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("up")) {
                                transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                            } else {
                                if (direction.equals("down")) {
                                    transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                                }
                            }
                        }
                    }
                    curImageAnim.addAnimation(transAni2);
                    curImageAnim.setZAdjustment(1);
                    nextImageAnim.setZAdjustment(-1);
                } else {
                    if (type.equals("push")) {
                        curImageAnim = new AnimationSet(true);
                        curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                        nextImageAnim = new AnimationSet(true);
                        nextImageAnim.addAnimation(new AlphaAnimation(0.5f, 1.0f));
                        TranslateAnimation transAni4curImage = null;
                        TranslateAnimation transAni4nextImage = null;
                        if (direction.equals("left")) {
                            transAni4curImage = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                            transAni4nextImage = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("right")) {
                                transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                                transAni4nextImage = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                            } else {
                                if (direction.equals("up")) {
                                    transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                                    transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                                } else {
                                    if (direction.equals("down")) {
                                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                                        transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                                    }
                                }
                            }
                        }
                        curImageAnim.addAnimation(transAni4curImage);
                        nextImageAnim.addAnimation(transAni4nextImage);
                    }
                }
            }
        }
        curImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setDuration(curSubItem.duration);
        final SubImageItem subImageItem = nextSubItem;
        final boolean z = isNext;
        nextImageAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(subImageItem.mIndex);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(z);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        imageView.startAnimation(curImageAnim);
        imageView2.startAnimation(nextImageAnim);
    }

    private SubImageItem getNextSubItem(boolean isNext) {
        SubImageItem nextSubItem = (SubImageItem) this.mEntity.subItems.get((this.curShowIndex + 1) % this.mEntity.subItems.size());
        if (isNext) {
            return nextSubItem;
        }
        if (this.curShowIndex != 0) {
            return (SubImageItem) this.mEntity.subItems.get(this.curShowIndex - 1);
        }
        if (this.mEntity.isEndToStart) {
            return (SubImageItem) this.mEntity.subItems.get(this.mEntity.subItems.size() - 1);
        }
        return nextSubItem;
    }

    /* access modifiers changed from: private */
    public void doChangeEndAction(boolean isNext) {
        this.hasAutoPlay = false;
        if (isNext) {
            this.curShowIndex++;
        } else {
            this.curShowIndex--;
        }
        if (!this.mEntity.isLoop || this.hasPlayCount == this.mEntity.repeat) {
            if (this.curShowIndex >= this.mEntity.subItems.size()) {
                this.curShowIndex = 0;
            } else if (this.curShowIndex >= this.mEntity.subItems.size() - 1) {
                BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
                this.curShowIndex %= this.mEntity.subItems.size();
                this.autoPlay = false;
            } else if (this.curShowIndex < 0) {
                this.curShowIndex = this.mEntity.subItems.size() - 1;
                this.autoPlay = false;
            }
        } else if (this.curShowIndex >= this.mEntity.subItems.size()) {
            if (this.hasPlayCount < this.mEntity.repeat) {
                this.hasPlayCount++;
            }
            this.curShowIndex = 0;
        } else if (this.curShowIndex >= this.mEntity.subItems.size() - 1) {
            BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
        }
        doChangeEnd(this.curShowIndex);
    }

    /* access modifiers changed from: private */
    public void doChangeEndAction(int position) {
        this.hasAutoPlay = false;
        this.curShowIndex = position;
        doChangeEnd(this.curShowIndex);
    }

    /* access modifiers changed from: private */
    public void drawCurrentImage(Canvas canvas) {
        this.currentAlpha = 255;
        this.mPaint.setAlpha(this.currentAlpha);
        this.paint.setAlpha(this.currentAlpha);
        if (this.isStop) {
            this.curShowIndex = 0;
        }
        canvas.drawBitmap(((SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size())).getBitmap(this.mContext), null, this.dst, this.mPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.autoPlay) {
            switch (event.getAction()) {
                case 0:
                    BookController.getInstance().runBehavior(getEntity(), Behavior.BEHAVIOR_ON_CLICK);
                    break;
                case 1:
                    BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_MOUSE_UP");
                    if (!this.hasMovePlay && (this.mEntity.switchType.equals("click") || this.mEntity.switchType.equals("clickAndSlide"))) {
                        changeNext(((SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size())).aniProperty);
                    }
                    this.f23dx = 0.0f;
                    this.f24dy = 0.0f;
                    this.totalDx = 0.0f;
                    this.totalDy = 0.0f;
                    this.hasMovePlay = false;
                    break;
                case 2:
                    if (!this.hasMovePlay) {
                        this.f23dx = event.getX() - this.oldEvent.getX();
                        this.f24dy = event.getY() - this.oldEvent.getY();
                        this.totalDx += this.f23dx;
                        this.totalDy += this.f24dy;
                        if (Math.abs(this.totalDx) >= CLICKSIZELIMIT || Math.abs(this.totalDy) >= CLICKSIZELIMIT) {
                            this.hasMovePlay = true;
                            if (!this.mEntity.switchType.equals("click")) {
                                if (Math.abs(this.totalDx) < Math.abs(this.totalDy)) {
                                    if (this.totalDy <= 0.0f) {
                                        changeNext("up");
                                        break;
                                    } else {
                                        changePre("down");
                                        break;
                                    }
                                } else if (this.totalDx <= 0.0f) {
                                    changeNext("left");
                                    break;
                                } else {
                                    changePre("right");
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
            this.oldEvent = MotionEvent.obtain(event);
        }
        return true;
    }

    private void changePre(String direction) {
        this.isStop = false;
        String aniType = ((SubImageItem) this.mEntity.subItems.get(this.curShowIndex)).aniType;
        if ((this.curShowIndex != 0 || this.mEntity.isEndToStart) && !this.hasAutoPlay) {
            this.hasAutoPlay = true;
            if (aniType.equals("cubeEffect")) {
                doPlayWithTypeTransitionCubeEffect(false, direction);
            } else if (aniType.equals("flipEffect")) {
                doPlayWithTypeTransitionFlipEffect(false, direction);
            } else if (aniType.equals("transitionFade")) {
                doPlayWithTypeTransitionNomal("fade", false, direction);
            } else if (aniType.equals("transitionMoveIn")) {
                doPlayWithTypeTransitionNomal("moveIn", false, direction);
            } else if (aniType.equals("transitionPush")) {
                doPlayWithTypeTransitionNomal("push", false, direction);
            } else if (aniType.equals("transitionReveal")) {
                doPlayWithTypeTransitionNomal("reveal", false, direction);
            } else {
                this.view.postInvalidate();
            }
        }
    }

    private void changeTo(int position) {
        this.isStop = false;
        String aniType = ((SubImageItem) this.mEntity.subItems.get(this.curShowIndex)).aniType;
        String direction = ((SubImageItem) this.mEntity.subItems.get(this.curShowIndex)).aniProperty;
        if (!this.hasAutoPlay) {
            this.hasAutoPlay = true;
            if (aniType.equals("cubeEffect")) {
                doPlayWithTypeTransitionCubeEffect(position, direction);
            } else if (aniType.equals("flipEffect")) {
                doPlayWithTypeTransitionFlipEffect(position, direction);
            } else if (aniType.equals("transitionFade")) {
                doPlayWithTypeTransitionNomal("fade", position, direction);
            } else if (aniType.equals("transitionMoveIn")) {
                doPlayWithTypeTransitionNomal("moveIn", position, direction);
            } else if (aniType.equals("transitionPush")) {
                doPlayWithTypeTransitionNomal("push", position, direction);
            } else if (aniType.equals("transitionReveal")) {
                doPlayWithTypeTransitionNomal("reveal", position, direction);
            } else {
                this.view.postInvalidate();
            }
        }
    }

    private void doPlayWithTypeTransitionNomal(String type, int position, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        SubImageItem nextSubItem = getNextSubItem(position);
        ImageView imageView = new ImageView(this.mContext);
        ImageView imageView2 = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        imageView.setImageBitmap(curBitmap1);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView2.setImageBitmap(nextBitmap);
        imageView2.setScaleType(ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        addView(imageView, layoutParams);
        addView(imageView2, layoutParams);
        AnimationSet curImageAnim = null;
        AnimationSet nextImageAnim = null;
        if (type.equals("fade")) {
            curImageAnim = new AnimationSet(true);
            curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
            nextImageAnim = new AnimationSet(true);
            nextImageAnim.addAnimation(new AlphaAnimation(0.0f, 1.0f));
            curImageAnim.setZAdjustment(1);
            nextImageAnim.setZAdjustment(-1);
        } else {
            if (type.equals("moveIn")) {
                curImageAnim = new AnimationSet(true);
                curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                nextImageAnim = new AnimationSet(true);
                nextImageAnim.addAnimation(new AlphaAnimation(0.5f, 1.0f));
                TranslateAnimation transAni = null;
                if (direction.equals("left")) {
                    transAni = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                } else {
                    if (direction.equals("right")) {
                        transAni = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                    } else {
                        if (direction.equals("up")) {
                            transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("down")) {
                                transAni = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                            }
                        }
                    }
                }
                nextImageAnim.addAnimation(transAni);
            } else {
                if (type.equals("reveal")) {
                    curImageAnim = new AnimationSet(true);
                    curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                    nextImageAnim = new AnimationSet(true);
                    nextImageAnim.addAnimation(new AlphaAnimation(0.0f, 1.0f));
                    TranslateAnimation transAni2 = null;
                    if (direction.equals("left")) {
                        transAni2 = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                    } else {
                        if (direction.equals("right")) {
                            transAni2 = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("up")) {
                                transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                            } else {
                                if (direction.equals("down")) {
                                    transAni2 = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                                }
                            }
                        }
                    }
                    curImageAnim.addAnimation(transAni2);
                    curImageAnim.setZAdjustment(1);
                    nextImageAnim.setZAdjustment(-1);
                } else {
                    if (type.equals("push")) {
                        curImageAnim = new AnimationSet(true);
                        curImageAnim.addAnimation(new AlphaAnimation(1.0f, 0.0f));
                        nextImageAnim = new AnimationSet(true);
                        nextImageAnim.addAnimation(new AlphaAnimation(0.5f, 1.0f));
                        TranslateAnimation transAni4curImage = null;
                        TranslateAnimation transAni4nextImage = null;
                        if (direction.equals("left")) {
                            transAni4curImage = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                            transAni4nextImage = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                        } else {
                            if (direction.equals("right")) {
                                transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                                transAni4nextImage = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                            } else {
                                if (direction.equals("up")) {
                                    transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                                    transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                                } else {
                                    if (direction.equals("down")) {
                                        transAni4curImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                                        transAni4nextImage = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                                    }
                                }
                            }
                        }
                        curImageAnim.addAnimation(transAni4curImage);
                        nextImageAnim.addAnimation(transAni4nextImage);
                    }
                }
            }
        }
        curImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setDuration(curSubItem.duration);
        final SubImageItem subImageItem = nextSubItem;
        final int i = position;
        nextImageAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(subImageItem.mIndex);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(i);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        imageView.startAnimation(curImageAnim);
        imageView2.startAnimation(nextImageAnim);
    }

    private void doPlayWithTypeTransitionFlipEffect(final int position, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        final SubImageItem nextSubItem = getNextSubItem(position);
        final ImageView imageView = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        imageView.setImageBitmap(curBitmap1);
        imageView.setScaleType(ScaleType.FIT_XY);
        addView(imageView, new LayoutParams(-1, -1));
        Animation curImageAnim = new MyAnimation4FlipEffect(0.0f, 180.0f, imageView, nextBitmap, direction);
        curImageAnim.setDuration(curSubItem.duration);
        curImageAnim.setAnimationListener(new AnimationListener() {
            ViewCell cell;
            float scalex;
            float scaley;

            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(nextSubItem.mIndex);
                this.cell = (ViewCell) HLSliderEffectComponent.this.getParent();
                this.scalex = this.cell.getScaleX();
                this.scaley = this.cell.getScaleY();
                this.cell.setScaleX(this.scalex * 2.0f);
                this.cell.setScaleY(this.scaley * 2.0f);
                imageView.setScaleX(0.5f);
                imageView.setScaleY(0.5f);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                this.cell.setScaleX(this.scalex);
                this.cell.setScaleY(this.scaley);
                if (VERSION.SDK_INT > 15) {
                    imageView.setScaleX(1.0f);
                    imageView.setScaleY(1.0f);
                }
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(position);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        imageView.startAnimation(curImageAnim);
    }

    private void doPlayWithTypeTransitionCubeEffect(final int position, String direction) {
        removeAllViews();
        SubImageItem curSubItem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
        final SubImageItem nextSubItem = getNextSubItem(position);
        ImageView curShowImageView = new ImageView(this.mContext);
        ImageView nextShowImageView = new ImageView(this.mContext);
        Bitmap curBitmap1 = curSubItem.getBitmap(this.mContext);
        Bitmap nextBitmap = nextSubItem.getBitmap(this.mContext);
        curShowImageView.setImageBitmap(curBitmap1);
        curShowImageView.setScaleType(ScaleType.FIT_XY);
        nextShowImageView.setImageBitmap(nextBitmap);
        nextShowImageView.setScaleType(ScaleType.FIT_XY);
        LayoutParams params = new LayoutParams(-1, -1);
        addView(curShowImageView, params);
        addView(nextShowImageView, params);
        Animation curImageAnim = null;
        Animation nextImageAnim = null;
        if (direction.equals("left") || direction.equals("up")) {
            curImageAnim = new MyAnimation4CubeEffect(0.0f, -90.0f, direction);
            nextImageAnim = new MyAnimation4CubeEffect(90.0f, 0.0f, direction);
        } else if (direction.equals("right") || direction.equals("down")) {
            curImageAnim = new MyAnimation4CubeEffect(0.0f, 90.0f, direction);
            nextImageAnim = new MyAnimation4CubeEffect(-90.0f, 0.0f, direction);
        }
        curImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setDuration(curSubItem.duration);
        nextImageAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation arg0) {
                HLSliderEffectComponent.this.doChangeStart(nextSubItem.mIndex);
            }

            public void onAnimationRepeat(Animation arg0) {
            }

            public void onAnimationEnd(Animation arg0) {
                HLSliderEffectComponent.this.removeAllViews();
                HLSliderEffectComponent.this.doChangeEndAction(position);
                HLSliderEffectComponent.this.addView(HLSliderEffectComponent.this.view);
            }
        });
        curShowImageView.startAnimation(curImageAnim);
        nextShowImageView.startAnimation(nextImageAnim);
    }

    private SubImageItem getNextSubItem(int position) {
        return (SubImageItem) this.mEntity.subItems.get(position);
    }

    private void changeNext(String direction) {
        this.isStop = false;
        String aniType = ((SubImageItem) this.mEntity.subItems.get(this.curShowIndex)).aniType;
        if ((this.curShowIndex != this.mEntity.subItems.size() - 1 || this.mEntity.isEndToStart) && !this.hasAutoPlay) {
            this.hasAutoPlay = true;
            if (aniType.equals("cubeEffect")) {
                doPlayWithTypeTransitionCubeEffect(true, direction);
            } else if (aniType.equals("flipEffect")) {
                doPlayWithTypeTransitionFlipEffect(true, direction);
            } else if (aniType.equals("transitionFade")) {
                doPlayWithTypeTransitionNomal("fade", true, direction);
            } else if (aniType.equals("transitionMoveIn")) {
                doPlayWithTypeTransitionNomal("moveIn", true, direction);
            } else if (aniType.equals("transitionPush")) {
                doPlayWithTypeTransitionNomal("push", true, direction);
            } else if (aniType.equals("transitionReveal")) {
                doPlayWithTypeTransitionNomal("reveal", true, direction);
            } else {
                this.view.postInvalidate();
            }
        }
    }

    public void play() {
        if (!this.autoPlay) {
            this.isStop = false;
            final SubImageItem curImageitem = (SubImageItem) this.mEntity.subItems.get(this.curShowIndex % this.mEntity.subItems.size());
            this.hasMovePlay = false;
            this.autoPlay = true;
            new CountDownTimer(curImageitem.delay, curImageitem.delay) {
                public void onTick(long arg0) {
                }

                public void onFinish() {
                    BookController.getInstance().runBehavior(HLSliderEffectComponent.this.mEntity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
                    HLSliderEffectComponent.this.hasAutoPlay = true;
                    if (curImageitem.aniType.equals("cubeEffect")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionCubeEffect(true, curImageitem.aniProperty);
                    } else if (curImageitem.aniType.equals("flipEffect")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionFlipEffect(true, curImageitem.aniProperty);
                    } else if (curImageitem.aniType.equals("transitionFade")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("fade", true, curImageitem.aniProperty);
                    } else if (curImageitem.aniType.equals("transitionMoveIn")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("moveIn", true, curImageitem.aniProperty);
                    } else if (curImageitem.aniType.equals("transitionPush")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("push", true, curImageitem.aniProperty);
                    } else if (curImageitem.aniType.equals("transitionReveal")) {
                        HLSliderEffectComponent.this.doPlayWithTypeTransitionNomal("reveal", true, curImageitem.aniProperty);
                    } else {
                        HLSliderEffectComponent.this.view.postInvalidate();
                    }
                }
            }.start();
        }
    }

    public void stop() {
        for (int i = 0; i < getChildCount(); i++) {
            View subView = getChildAt(i);
            try {
                if (!subView.getAnimation().hasEnded()) {
                    subView.getAnimation().cancel();
                    subView.clearAnimation();
                }
            } catch (Exception e) {
            }
        }
        this.isStop = true;
        this.hasAutoPlay = false;
        this.curShowIndex = 0;
        this.autoPlay = false;
        removeAllViews();
        addView(this.view);
        this.view.postInvalidate();
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

    private void doChangeEnd(int index) {
        Iterator i$ = this.mEntity.behaviors.iterator();
        while (i$.hasNext()) {
            BehaviorEntity behavior = (BehaviorEntity) i$.next();
            if (Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CHANGE_COMPLETE.equals(behavior.EventName)) {
                BehaviorHelper.doBeheavorForList(behavior, index, this.mEntity.componentId);
            }
        }
    }

    public void hide() {
        setVisibility(8);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.mEntity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public void pause() {
        if (this.autoPlay) {
            this.autoPlay = false;
        }
    }

    public void doChangeToAction(int position) {
        changeTo(position);
    }
}
