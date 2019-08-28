package com.p000hl.android.view.component.moudle.masksliderimag;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Toast;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MaskBean;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.ViewCell;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;

@SuppressLint({"DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.masksliderimag.MaskSliderImage */
public class MaskSliderImage extends View implements Component, AnimatorListener, OnScaleGestureListener {
    private static final int LIMIT_DIS = 50;
    private long DOWNTIME = 0;
    private float beforFullScreenX;
    private float beforFullScreenY;
    private float cImageDrawX;
    private float cImageDrawY;
    private float cImageHeight;
    private float cImageWidth;
    private ViewCell cell;
    private Bitmap closeBitmap;
    private RectF closeRect;
    MaskViewBean curMaskView;
    private int dotRadius = 8;
    private float downX = 0.0f;
    private int endPositionx;
    private int endPositiony;
    float eventX = 0.0f;
    float eventY = 0.0f;
    private int initalHeight;
    private int initalWidth;
    private boolean isAction = false;
    /* access modifiers changed from: private */
    public boolean isDoScaleToFullView = false;
    private boolean isDown = false;
    private boolean isPortlet = true;
    private boolean isdoAnimation;
    /* access modifiers changed from: private */
    public boolean isdoAnimation1;
    private Context mContext;
    private float mCurWidth;
    private MoudleComponentEntity mEntity;
    ScaleGestureDetector mScaleGestureDetector;
    private int mSelectIndex = 0;
    /* access modifiers changed from: private */
    public int marginLeft = 0;
    MediaPlayer media = new MediaPlayer();
    private PointF mid = new PointF();
    String moveDir = "next";
    private int navigationHeight;
    MaskViewBean nextMaskView;
    private float oldDistance;
    private Paint paint;
    private float parentHeight;
    private float parentWidth;
    MaskViewBean prevMaskView;
    MaskViewBean recyleMaskView = null;
    private int touchW;
    private float touchX;
    private float touchY;

    public MaskSliderImage(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = (MoudleComponentEntity) entity;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.rgb(192, 192, 192));
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public float getCImageDrawX() {
        return this.cImageDrawX;
    }

    public void setCImageDrawX(float cImageDrawX2) {
        this.cImageDrawX = cImageDrawX2;
    }

    public float getCImageDrawY() {
        return this.cImageDrawY;
    }

    public void setCImageDrawY(float cImageDrawY2) {
        this.cImageDrawY = cImageDrawY2;
    }

    public float getCImageWidth() {
        return this.cImageWidth;
    }

    public void setCImageWidth(float cImageWidth2) {
        this.cImageWidth = cImageWidth2;
    }

    public float getCImageHeight() {
        return this.cImageHeight;
    }

    public void setCImageHeight(float cImageHeight2) {
        this.cImageHeight = cImageHeight2;
    }

    public void setParentWidth(float parentWidth2) {
        this.parentWidth = parentWidth2;
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        this.cell.setLayoutParams(new MarginLayoutParams((int) parentWidth2, this.cell.getLayoutParams().height));
    }

    public float getParentWidth() {
        return this.parentWidth;
    }

    public void setParentHeight(float parentHeight2) {
        this.parentHeight = parentHeight2;
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        this.cell.setLayoutParams(new MarginLayoutParams(this.cell.getLayoutParams().width, (int) parentHeight2));
    }

    public float getParentHeight() {
        return this.parentHeight;
    }

    public void setParentX(float parentX) {
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        this.cell.setX(parentX);
    }

    public float getParentX() {
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        return this.cell.getX();
    }

    public void setParentY(float parentY) {
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        this.cell.setY(parentY);
    }

    public float getParentY() {
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        return this.cell.getY();
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.curMaskView = new MaskViewBean(this.mContext, (MaskBean) this.mEntity.maskBeanList.get(this.mSelectIndex));
        this.nextMaskView = new MaskViewBean(this.mContext, (MaskBean) this.mEntity.maskBeanList.get(this.mSelectIndex + 1));
        this.initalWidth = getLayoutParams().width;
        this.initalHeight = getLayoutParams().height;
        this.mCurWidth = (float) this.initalWidth;
        this.closeBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), C0048R.drawable.close_normal);
        int right = BookSetting.BOOK_WIDTH - ScreenUtils.dip2px(this.mContext, 20.0f);
        int top = ScreenUtils.dip2px(this.mContext, 20.0f);
        this.closeRect = new RectF((float) (right - 44), (float) top, (float) right, (float) (top + 44));
        this.beforFullScreenX = (float) this.mEntity.f7x;
        this.beforFullScreenY = (float) this.mEntity.f8y;
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        try {
            if (this.media != null && this.media.isPlaying()) {
                this.media.stop();
                this.media.release();
                this.media = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable th) {
            this.media = null;
            throw th;
        }
        this.media = null;
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
        try {
            this.media.start();
        } catch (Exception e) {
        }
    }

    public void pause() {
        try {
            this.media.pause();
        } catch (Exception e) {
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isdoAnimation && !this.isdoAnimation1) {
            this.mScaleGestureDetector.onTouchEvent(event);
            if (event.getPointerCount() <= 1) {
                switch (event.getAction()) {
                    case 0:
                        this.DOWNTIME = System.currentTimeMillis();
                        this.eventX = event.getX();
                        this.eventY = event.getY();
                        this.downX = this.eventX;
                        this.isDown = true;
                        break;
                    case 1:
                        if (this.isPortlet || !this.closeRect.contains(event.getX(), event.getY())) {
                            if (this.isDown) {
                                if (this.downX != 0.0f && System.currentTimeMillis() - this.DOWNTIME < 200 && Math.abs(this.downX - event.getX()) < ((float) ScreenUtils.dip2px(this.mContext, 5.0f))) {
                                    doMaskViewFullAction();
                                    this.isDown = false;
                                    this.downX = 0.0f;
                                    break;
                                } else {
                                    this.eventX = 0.0f;
                                    doWrapMaskView();
                                    break;
                                }
                            }
                        } else {
                            doCloseAction();
                            break;
                        }
                        break;
                    case 2:
                        if (this.isDown) {
                            if (this.eventX != 0.0f) {
                                this.marginLeft = (int) (((float) this.marginLeft) + (event.getX(0) - this.eventX));
                                if (this.mSelectIndex != 0 || this.marginLeft < 0) {
                                    if (this.mSelectIndex == this.mEntity.maskBeanList.size() - 1 && this.marginLeft <= 0) {
                                        this.marginLeft = 0;
                                        this.eventX = 0.0f;
                                        break;
                                    } else {
                                        postInvalidate();
                                    }
                                } else {
                                    this.marginLeft = 0;
                                    this.eventX = 0.0f;
                                    break;
                                }
                            }
                            this.eventX = event.getX(0);
                            this.eventY = event.getY(0);
                            break;
                        }
                        break;
                }
            } else {
                this.isDown = false;
                if (event.getPointerCount() == 2) {
                    switch (event.getAction() & 255) {
                        case 2:
                            float newDistance = (float) Math.sqrt((double) (((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1))) + ((event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1)))));
                            this.mCurWidth = (float) ((int) ((this.mCurWidth * newDistance) / this.oldDistance));
                            this.cell.setLayoutParams(new MarginLayoutParams((int) this.mCurWidth, (int) ((this.mCurWidth * ((float) this.initalHeight)) / ((float) this.initalWidth))));
                            this.endPositionx = (int) ((this.mid.x + this.touchX) - ((this.mid.x / ((float) this.touchW)) * this.mCurWidth));
                            this.endPositiony = (int) ((this.mid.y + this.touchY) - ((this.mid.y / ((float) this.touchW)) * this.mCurWidth));
                            this.cell.setX((float) this.endPositionx);
                            this.cell.setY((float) this.endPositiony);
                            this.oldDistance = newDistance;
                            break;
                        case 5:
                            this.oldDistance = (float) Math.sqrt((double) (((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1))) + ((event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1)))));
                            this.mid.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
                            this.touchX = this.cell.getX();
                            this.touchY = this.cell.getY();
                            this.touchW = this.cell.getWidth();
                            break;
                    }
                }
            }
        }
        return true;
    }

    private void doCloseAction() {
        this.isPortlet = true;
        postInvalidate();
        try {
            if (this.media != null && this.media.isPlaying()) {
                this.media.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doShareAction() {
        Toast.makeText(this.mContext, "no share sdk", 1).show();
    }

    private void doMaskViewFullAction() {
        if (this.isPortlet) {
            this.isPortlet = false;
            PropertyValuesHolder parentwidth = PropertyValuesHolder.ofFloat("parentWidth", new float[]{(float) BookSetting.BOOK_WIDTH});
            PropertyValuesHolder parentheight = PropertyValuesHolder.ofFloat("parentHeight", new float[]{(float) BookSetting.BOOK_HEIGHT});
            PropertyValuesHolder parentx = PropertyValuesHolder.ofFloat("parentX", new float[]{0.0f});
            PropertyValuesHolder parenty = PropertyValuesHolder.ofFloat("parentY", new float[]{0.0f});
            PropertyValuesHolder cimageWidth = PropertyValuesHolder.ofFloat("cImageWidth", new float[]{(float) BookSetting.BOOK_WIDTH});
            PropertyValuesHolder cimageHeight = PropertyValuesHolder.ofFloat("cImageHeight", new float[]{(float) (BookSetting.BOOK_HEIGHT - (ScreenUtils.dip2px(this.mContext, 60.0f) * 2))});
            PropertyValuesHolder cimageDrawX = PropertyValuesHolder.ofFloat("cImageDrawX", new float[]{0.0f});
            PropertyValuesHolder cimageDrawY = PropertyValuesHolder.ofFloat("cImageDrawY", new float[]{(float) ScreenUtils.dip2px(this.mContext, 60.0f)});
            this.moveDir = "none";
            this.parentWidth = (float) getLayoutParams().width;
            this.parentHeight = (float) getLayoutParams().height;
            RectF mcrectF = getMaskRect(this.curMaskView, 0, this.navigationHeight);
            this.cImageDrawX = mcrectF.left;
            this.cImageDrawY = mcrectF.top;
            this.cImageWidth = mcrectF.right - mcrectF.left;
            this.cImageHeight = mcrectF.bottom - mcrectF.top;
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{parentx, parenty, parentwidth, parentheight, cimageDrawX, cimageDrawY, cimageWidth, cimageHeight});
            animator.setDuration(600);
            animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    MaskSliderImage.this.isdoAnimation1 = true;
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    MaskSliderImage.this.isdoAnimation1 = false;
                    MaskSliderImage.this.marginLeft = 0;
                    MaskSliderImage.this.isDoScaleToFullView = false;
                    MaskSliderImage.this.postInvalidate();
                }

                public void onAnimationCancel(Animator animation) {
                    MaskSliderImage.this.isdoAnimation1 = false;
                }
            });
            animator.start();
            this.isDoScaleToFullView = true;
            this.curMaskView.playMedia(this.media);
        }
    }

    private void doWrapMaskView() {
        ObjectAnimator anm;
        if (this.marginLeft < -50) {
            this.moveDir = "next";
            anm = ObjectAnimator.ofInt(this, "marginLeft", new int[]{this.marginLeft, -getWidth()});
        } else if (this.marginLeft > 50) {
            this.moveDir = "prev";
            anm = ObjectAnimator.ofInt(this, "marginLeft", new int[]{this.marginLeft, getWidth()});
        } else {
            this.moveDir = "none";
            anm = ObjectAnimator.ofInt(this, "marginLeft", new int[]{0});
        }
        anm.addListener(this);
        anm.start();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        if (this.cell == null) {
            this.cell = (ViewCell) getParent();
        }
        if (this.isDoScaleToFullView) {
            canvas.save();
            canvas.clipRect(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()));
            canvas.drawBitmap(this.curMaskView.mBitmap, null, new RectF(this.cImageDrawX, this.cImageDrawY, this.cImageDrawX + this.cImageWidth, this.cImageDrawY + this.cImageHeight), null);
            canvas.restore();
        } else if (!this.isPortlet) {
            drawFullView(canvas);
        } else {
            drawIconView(canvas);
        }
    }

    private void drawIconView(Canvas canvas) {
        if (this.curMaskView != null) {
            int mcOffsetX = this.marginLeft;
            int mpreOffsetX = mcOffsetX - getWidth();
            int mnextOffsetX = mcOffsetX + getWidth();
            if (!this.isAction) {
                MarginLayoutParams marginLp = (MarginLayoutParams) this.cell.getLayoutParams();
                marginLp.width = this.initalWidth;
                marginLp.height = this.initalHeight;
                marginLp.leftMargin = 0;
                marginLp.topMargin = 0;
                this.cell.requestLayout();
                this.cell.setX(this.beforFullScreenX);
                this.cell.setY(this.beforFullScreenY);
                if (this.mEntity.isShowControllerPoint) {
                    drawNavigation(canvas);
                    this.navigationHeight = this.dotRadius * 4;
                }
                this.mCurWidth = (float) this.initalWidth;
            }
            canvas.save();
            canvas.clipRect(new RectF((float) mcOffsetX, 0.0f, (float) (getWidth() + mcOffsetX), (float) (getHeight() - this.navigationHeight)));
            this.curMaskView.drawMaskView(canvas, getMaskRect(this.curMaskView, mcOffsetX, this.navigationHeight), Boolean.valueOf(this.isPortlet), false);
            canvas.restore();
            if (this.prevMaskView != null) {
                canvas.save();
                canvas.clipRect(new RectF((float) mpreOffsetX, 0.0f, (float) (getWidth() + mpreOffsetX), (float) (getHeight() - this.navigationHeight)));
                this.prevMaskView.drawMaskView(canvas, getMaskRect(this.prevMaskView, mpreOffsetX, this.navigationHeight), Boolean.valueOf(this.isPortlet), false);
                canvas.restore();
            }
            if (this.nextMaskView != null) {
                canvas.save();
                canvas.clipRect(new RectF((float) mnextOffsetX, 0.0f, (float) (getWidth() + mnextOffsetX), (float) (getHeight() - this.navigationHeight)));
                this.nextMaskView.drawMaskView(canvas, getMaskRect(this.nextMaskView, mnextOffsetX, this.navigationHeight), Boolean.valueOf(this.isPortlet), false);
                canvas.restore();
            }
            if (this.recyleMaskView != null) {
                this.recyleMaskView.recyle();
            }
        }
    }

    private RectF getBigMaskRect(int px) {
        return new RectF((float) px, 0.0f, (float) (getWidth() + px), (float) getHeight());
    }

    private RectF getMaskRect(MaskViewBean maskView, int offsetX, int navigationHeight2) {
        float xcRatio = ((float) getWidth()) / maskView.mMaskBean.rectW;
        float ycRatio = ((float) (getHeight() - navigationHeight2)) / maskView.mMaskBean.rectH;
        RectF mcrectF = new RectF();
        mcrectF.left = ((-maskView.mMaskBean.rectX) * xcRatio) + ((float) offsetX);
        mcrectF.top = (-maskView.mMaskBean.rectY) * ycRatio;
        mcrectF.right = mcrectF.left + (((float) maskView.mBitmap.getWidth()) * xcRatio);
        mcrectF.bottom = mcrectF.top + (((float) maskView.mBitmap.getHeight()) * ycRatio);
        return mcrectF;
    }

    private void drawNavigation(Canvas canvas) {
        int y = getLayoutParams().height - (this.dotRadius * 3);
        int x = ((getLayoutParams().width / 2) - ((((this.mEntity.maskBeanList.size() * 3) - 1) * this.dotRadius) / 2)) + this.dotRadius;
        for (int i = 0; i < this.mEntity.maskBeanList.size(); i++) {
            this.paint.setColor(Color.rgb(144, 144, 144));
            canvas.drawCircle((float) x, (float) y, (float) this.dotRadius, this.paint);
            canvas.save();
            this.paint.setColor(-1);
            canvas.drawCircle((float) x, (float) y, (float) (this.dotRadius - 1), this.paint);
            canvas.save();
            if (this.mSelectIndex == i) {
                this.paint.setColor(Color.rgb(144, 144, 144));
                canvas.drawCircle((float) x, (float) y, (float) (this.dotRadius - 3), this.paint);
                canvas.save();
            }
            x += this.dotRadius * 3;
        }
    }

    private void drawFullView(Canvas canvas) {
        this.cell.setScaleX(1.0f);
        this.cell.setScaleY(1.0f);
        if (this.curMaskView != null) {
            if (this.isAction) {
                canvas.drawColor(-1);
                RectF rectF = getBigMaskRect(0);
                rectF.top += ((((float) ScreenUtils.dip2px(this.mContext, 60.0f)) * 1.0f) / ((float) BookSetting.BOOK_HEIGHT)) * ((float) getHeight());
                rectF.bottom -= ((((float) ScreenUtils.dip2px(this.mContext, 60.0f)) * 1.0f) / ((float) BookSetting.BOOK_HEIGHT)) * ((float) getHeight());
                canvas.drawBitmap(this.curMaskView.mBitmap, null, rectF, this.paint);
                return;
            }
            canvas.drawColor(-16777216);
            canvas.drawBitmap(this.closeBitmap, null, this.closeRect, null);
            ViewCell cell2 = (ViewCell) getParent();
            MarginLayoutParams marginLp = (MarginLayoutParams) cell2.getLayoutParams();
            marginLp.width = BookSetting.BOOK_WIDTH;
            marginLp.height = BookSetting.BOOK_HEIGHT;
            cell2.setLayoutParams(marginLp);
            cell2.setX(0.0f);
            cell2.setY(0.0f);
            this.mCurWidth = (float) BookSetting.BOOK_WIDTH;
            this.curMaskView.drawMaskView(canvas, getBigMaskRect(this.marginLeft), Boolean.valueOf(this.isPortlet), true);
            if (this.prevMaskView != null) {
                this.prevMaskView.drawMaskView(canvas, getBigMaskRect(this.marginLeft - getWidth()), Boolean.valueOf(this.isPortlet), false);
            }
            if (this.nextMaskView != null) {
                this.nextMaskView.drawMaskView(canvas, getBigMaskRect(this.marginLeft + getWidth()), Boolean.valueOf(this.isPortlet), false);
            }
            if (this.recyleMaskView != null) {
                this.recyleMaskView.recyle();
            }
        }
    }

    public void setMarginLeft(int x) {
        this.marginLeft = x;
        postInvalidate();
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void onAnimationCancel(Animator animation) {
        this.isdoAnimation = false;
    }

    public void onAnimationEnd(Animator animation) {
        this.isdoAnimation = false;
        if ("next".equals(this.moveDir)) {
            if (this.mSelectIndex != this.mEntity.maskBeanList.size() - 1) {
                this.recyleMaskView = this.prevMaskView;
                this.mSelectIndex++;
                this.prevMaskView = this.curMaskView;
                this.curMaskView = this.nextMaskView;
                if (this.mSelectIndex < this.mEntity.maskBeanList.size() - 1) {
                    this.nextMaskView = new MaskViewBean(this.mContext, (MaskBean) this.mEntity.maskBeanList.get(this.mSelectIndex + 1));
                } else {
                    this.nextMaskView = null;
                }
                if (!this.isPortlet) {
                    this.curMaskView.playMedia(this.media);
                }
            } else {
                return;
            }
        } else if ("prev".equals(this.moveDir)) {
            if (this.mSelectIndex != 0) {
                this.recyleMaskView = this.nextMaskView;
                this.mSelectIndex--;
                this.nextMaskView = this.curMaskView;
                this.curMaskView = this.prevMaskView;
                if (this.mSelectIndex != 0) {
                    this.prevMaskView = new MaskViewBean(this.mContext, (MaskBean) this.mEntity.maskBeanList.get(this.mSelectIndex - 1));
                } else {
                    this.prevMaskView = null;
                }
                if (!this.isPortlet) {
                    this.curMaskView.playMedia(this.media);
                }
            } else {
                return;
            }
        }
        this.marginLeft = 0;
        Log.d("hl", "anima end");
        this.isDoScaleToFullView = false;
        postInvalidate();
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
        this.isdoAnimation = true;
    }

    public boolean onScale(ScaleGestureDetector arg0) {
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector detector) {
        this.isAction = true;
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        if (this.isPortlet) {
            if (this.cell.getWidth() > this.initalWidth) {
                this.isPortlet = false;
                this.curMaskView.playMedia(this.media);
            }
        } else if (this.cell.getWidth() < BookSetting.BOOK_WIDTH) {
            this.isPortlet = true;
            pause();
        }
        this.marginLeft = 0;
        this.isAction = false;
        Log.d("hl", " onScaleEnd");
        postInvalidate();
    }
}
