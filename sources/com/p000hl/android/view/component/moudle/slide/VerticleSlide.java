package com.p000hl.android.view.component.moudle.slide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.helper.BehaviorHelper;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"ViewConstructor"})
/* renamed from: com.hl.android.view.component.moudle.slide.VerticleSlide */
public class VerticleSlide extends View implements Component {
    public static final int MOVE_TO_DOWN = 1000123;
    public static final int MOVE_TO_UP = 1000124;
    public static final int NO_MOVE = 1000125;
    private int CLICKSIZELIMIT = 5;
    private boolean canmove = false;

    /* renamed from: dx */
    private float f47dx = 0.0f;

    /* renamed from: dy */
    private float f48dy = 0.0f;
    private float hasMoveLength = 0.0f;
    private long lastTime = 0;
    private Context mContext;
    private ComponentEntity mEntity = null;
    private float mImageHeight;
    private float mImageSpace;
    private float mImageWith;
    public int mMoveState;
    private int nextStep;
    private MotionEvent oldEvent = null;
    private Paint paint;
    private ArrayList<VRect> rects;
    public long sleepTime = 5;
    private boolean start;
    private float totalAbsDx = 0.0f;
    private float totalAbsDy = 0.0f;
    private float totalDy = 0.0f;
    public float totalMoveLength = 0.0f;

    /* renamed from: com.hl.android.view.component.moudle.slide.VerticleSlide$VRect */
    class VRect {
        public float mHeight = 0.0f;
        public Bitmap mImageBitmap;
        public int mIndex;
        public float mWidth = 0.0f;

        /* renamed from: mX */
        public float f49mX = 0.0f;

        /* renamed from: mY */
        public float f50mY = 0.0f;

        public VRect(float x, float y, float width, float height, Bitmap bitmap, int index) {
            this.f49mX = x;
            this.f50mY = y;
            this.mWidth = width;
            this.mHeight = height;
            this.mImageBitmap = bitmap;
            this.mIndex = index;
        }

        public void drawMe(Canvas canvas, Paint paint, int columnIndex) {
            if (this.mImageBitmap != null) {
                canvas.drawBitmap(this.mImageBitmap, null, new RectF(this.f49mX + ((float) VerticleSlide.this.getPaddingLeft()), this.f50mY + ((float) VerticleSlide.this.getPaddingTop()), this.f49mX + ((float) VerticleSlide.this.getPaddingLeft()) + this.mWidth, this.f50mY + ((float) VerticleSlide.this.getPaddingTop()) + this.mHeight), paint);
            }
        }
    }

    public VerticleSlide(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
        this.paint = new Paint(4);
        this.paint.setStyle(Style.STROKE);
        this.paint.setAntiAlias(true);
        setBackgroundColor(0);
    }

    public float getImageWith() {
        return this.mImageWith;
    }

    public void setImageWith(float mImageWith2) {
        this.mImageWith = mImageWith2;
    }

    public float getImageHeight() {
        return this.mImageHeight;
    }

    public void setImageHeight(float mImageHeight2) {
        this.mImageHeight = mImageHeight2;
    }

    public void setImageSpace(float mImageSpace2) {
        this.mImageSpace = mImageSpace2;
    }

    public float getImageSpace() {
        return this.mImageSpace;
    }

    private ArrayList<VRect> getDataSource(ArrayList<String> sourceIDS, LayoutParams lp) {
        setImageSpace(0.0f);
        ArrayList<VRect> souce = new ArrayList<>();
        float endPosition = 0.0f;
        if (sourceIDS != null && sourceIDS.size() > 0) {
            for (int i = 0; i < sourceIDS.size(); i++) {
                Bitmap bitmap = BitmapManager.getBitmapFromCache((String) sourceIDS.get(i));
                if (bitmap == null) {
                    bitmap = BitmapUtils.getBitMap((String) sourceIDS.get(i), this.mContext);
                    BitmapManager.putBitmapCache((String) sourceIDS.get(i), bitmap);
                }
                float ratio = (1.0f * ((float) lp.width)) / ((float) bitmap.getWidth());
                VRect rect = new VRect(0.0f, endPosition, (float) lp.width, ((float) bitmap.getHeight()) * ratio, bitmap, i);
                endPosition += ((float) bitmap.getHeight()) * ratio;
                souce.add(rect);
            }
            this.totalMoveLength = endPosition - ((float) lp.width);
            if (endPosition > ((float) lp.width)) {
                this.mMoveState = 1000124;
                this.canmove = true;
            } else {
                this.mMoveState = 1000125;
                this.canmove = false;
            }
        }
        return souce;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            BookController.getInstance().runBehavior(getEntity(), Behavior.BEHAVIOR_ON_CLICK);
        } else if (event.getAction() == 2) {
            this.f47dx = event.getX() - this.oldEvent.getX();
            this.f48dy = event.getY() - this.oldEvent.getY();
            this.totalDy += this.f48dy;
            this.totalAbsDx += Math.abs(this.f47dx);
            this.totalAbsDy += Math.abs(this.f48dy);
        } else if (event.getAction() == 1) {
            BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_SLIDER_UP");
            if (this.totalAbsDx <= ((float) this.CLICKSIZELIMIT) && this.totalAbsDy <= ((float) this.CLICKSIZELIMIT)) {
                int i = 0;
                while (true) {
                    if (i >= this.rects.size()) {
                        break;
                    }
                    VRect mCurrentVRect = (VRect) this.rects.get(i);
                    if (hasTouchedRect(event.getX(), event.getY(), mCurrentVRect)) {
                        doClickAction(mCurrentVRect);
                        break;
                    }
                    i++;
                }
            } else if (this.canmove && Math.abs(this.totalDy) > ((float) this.CLICKSIZELIMIT)) {
                if (this.totalDy > ((float) this.CLICKSIZELIMIT)) {
                    this.mMoveState = 1000123;
                } else {
                    this.mMoveState = 1000124;
                }
            }
            this.totalAbsDx = 0.0f;
            this.totalAbsDy = 0.0f;
            this.totalDy = 0.0f;
        }
        this.oldEvent = MotionEvent.obtain(event);
        return true;
    }

    private void doClickAction(VRect currentTouchRect) {
        if (currentTouchRect != null) {
            Iterator i$ = this.mEntity.behaviors.iterator();
            while (i$.hasNext()) {
                BehaviorEntity behavior = (BehaviorEntity) i$.next();
                if (behavior.EventName.equals(Behavior.BEHAVIOR_ON_TEMPLATE_ITEM_CLICK)) {
                    BehaviorHelper.doBeheavorForList(behavior, currentTouchRect.mIndex, this.mEntity.componentId);
                }
            }
        }
    }

    private boolean hasTouchedRect(float touchX, float touchY, VRect touchedRect) {
        if (touchX <= touchedRect.f49mX || touchX >= touchedRect.f49mX + touchedRect.mWidth || touchY <= touchedRect.f50mY || touchY >= touchedRect.f50mY + touchedRect.mHeight) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        this.nextStep = getNextStep();
        this.hasMoveLength -= (float) this.nextStep;
        for (int i = 0; i < this.rects.size(); i++) {
            VRect mCurrentRect = (VRect) this.rects.get(i);
            if (this.start) {
                mCurrentRect.f50mY += (float) this.nextStep;
            }
            mCurrentRect.drawMe(canvas, this.paint, i);
        }
        if (this.mMoveState != 1000125) {
            if (this.hasMoveLength >= this.totalMoveLength) {
                this.mMoveState = 1000123;
            } else if (this.hasMoveLength <= 0.0f) {
                this.mMoveState = 1000124;
            }
        }
        postInvalidate();
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = entity;
    }

    public void load() {
        this.rects = getDataSource(((MoudleComponentEntity) this.mEntity).getSourceIDList(), getLayoutParams());
        this.sleepTime = ((MoudleComponentEntity) this.mEntity).getTimerDelay();
        setLayerType(1, null);
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
        this.start = true;
    }

    public void stop() {
        this.start = false;
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

    private int getNextStep() {
        if (this.lastTime == 0) {
            this.lastTime = System.currentTimeMillis();
            return 0;
        } else if (this.mMoveState == 1000125) {
            return 0;
        } else {
            long duration = System.currentTimeMillis() - this.lastTime;
            if (duration < this.sleepTime) {
                return 0;
            }
            int result = (int) (duration / this.sleepTime);
            if (this.mMoveState == 1000124) {
                result = -result;
            }
            this.lastTime = System.currentTimeMillis();
            return result;
        }
    }
}
