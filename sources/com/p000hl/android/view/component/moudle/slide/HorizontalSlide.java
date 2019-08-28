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

@SuppressLint({"ViewConstructor", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.slide.HorizontalSlide */
public class HorizontalSlide extends View implements Component {
    public static final int MOVE_TO_LEFT = 1000124;
    public static final int MOVE_TO_RIGHT = 1000123;
    public static final int NO_MOVE = 1000125;
    private int CLICKSIZELIMIT = 5;
    private boolean canmove = false;

    /* renamed from: dx */
    private float f42dx = 0.0f;

    /* renamed from: dy */
    private float f43dy = 0.0f;
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
    private ArrayList<HRect> rects;
    public long sleepTime = 5;
    private boolean start;
    private float totalAbsDx = 0.0f;
    private float totalAbsDy = 0.0f;
    private float totalDx = 0.0f;
    public float totalMoveLength = 0.0f;

    /* renamed from: com.hl.android.view.component.moudle.slide.HorizontalSlide$HRect */
    class HRect {
        public float mHeight = 0.0f;
        public Bitmap mImageBitmap;
        public int mIndex;
        public float mWidth = 0.0f;

        /* renamed from: mX */
        public float f44mX = 0.0f;

        /* renamed from: mY */
        public float f45mY = 0.0f;

        public HRect(float x, float y, float width, float height, Bitmap bitmap, int index) {
            this.f44mX = x;
            this.f45mY = y;
            this.mWidth = width;
            this.mHeight = height;
            this.mImageBitmap = bitmap;
            this.mIndex = index;
        }

        public void drawMe(Canvas canvas, Paint paint, int columnIndex) {
            if (this.mImageBitmap != null) {
                canvas.drawBitmap(this.mImageBitmap, null, new RectF(this.f44mX + ((float) HorizontalSlide.this.getPaddingLeft()), this.f45mY + ((float) HorizontalSlide.this.getPaddingTop()), this.f44mX + this.mWidth + ((float) HorizontalSlide.this.getPaddingLeft()), this.f45mY + this.mHeight + ((float) HorizontalSlide.this.getPaddingTop())), paint);
            }
        }
    }

    public HorizontalSlide(Context context, ComponentEntity entity) {
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

    private ArrayList<HRect> getDataSource(ArrayList<String> sourceIDS, LayoutParams lp) {
        setImageSpace(0.0f);
        ArrayList<HRect> souce = new ArrayList<>();
        float endPosition = 0.0f;
        if (sourceIDS != null && sourceIDS.size() > 0) {
            for (int i = 0; i < sourceIDS.size(); i++) {
                Bitmap bitmap = BitmapManager.getBitmapFromCache((String) sourceIDS.get(i));
                if (bitmap == null) {
                    bitmap = BitmapUtils.getBitMap((String) sourceIDS.get(i), this.mContext);
                    BitmapManager.putBitmapCache((String) sourceIDS.get(i), bitmap);
                }
                float ratio = (1.0f * ((float) lp.height)) / ((float) bitmap.getHeight());
                HRect rect = new HRect(endPosition, 0.0f, ((float) bitmap.getWidth()) * ratio, (float) lp.height, bitmap, i);
                endPosition += ((float) bitmap.getWidth()) * ratio;
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
            this.f42dx = event.getX() - this.oldEvent.getX();
            this.f43dy = event.getY() - this.oldEvent.getY();
            this.totalDx += this.f42dx;
            this.totalAbsDx += Math.abs(this.f42dx);
            this.totalAbsDy += Math.abs(this.f43dy);
        } else if (event.getAction() == 1) {
            BookController.getInstance().runBehavior(getEntity(), "BEHAVIOR_ON_SLIDER_UP");
            if (this.totalAbsDx <= ((float) this.CLICKSIZELIMIT) && this.totalAbsDy <= ((float) this.CLICKSIZELIMIT)) {
                int i = 0;
                while (true) {
                    if (i >= this.rects.size()) {
                        break;
                    }
                    HRect mCurrentHRect = (HRect) this.rects.get(i);
                    if (hasTouchedRect(event.getX(), event.getY(), mCurrentHRect)) {
                        doClickAction(mCurrentHRect);
                        break;
                    }
                    i++;
                }
            } else if (this.canmove && Math.abs(this.totalDx) > ((float) this.CLICKSIZELIMIT)) {
                if (this.totalDx > ((float) this.CLICKSIZELIMIT)) {
                    this.mMoveState = 1000123;
                } else {
                    this.mMoveState = 1000124;
                }
            }
            this.totalAbsDx = 0.0f;
            this.totalAbsDy = 0.0f;
            this.totalDx = 0.0f;
        }
        this.oldEvent = MotionEvent.obtain(event);
        return true;
    }

    private void doClickAction(HRect currentTouchRect) {
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

    private boolean hasTouchedRect(float touchX, float touchY, HRect touchedRect) {
        if (touchX <= touchedRect.f44mX || touchX >= touchedRect.f44mX + touchedRect.mWidth || touchY <= touchedRect.f45mY || touchY >= touchedRect.f45mY + touchedRect.mHeight) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        this.nextStep = getNextStep();
        this.hasMoveLength -= (float) this.nextStep;
        for (int i = 0; i < this.rects.size(); i++) {
            HRect mCurrentRect = (HRect) this.rects.get(i);
            if (this.start) {
                mCurrentRect.f44mX += (float) this.nextStep;
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
    }

    public void load(InputStream is) {
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
