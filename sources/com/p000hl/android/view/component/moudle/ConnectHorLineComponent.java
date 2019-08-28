package com.p000hl.android.view.component.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.Cell;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.view.component.Behavior;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;

@SuppressLint({"ViewConstructor", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.ConnectHorLineComponent */
public class ConnectHorLineComponent extends View implements Component {
    private static String BEHAVIOR_ON_CONNECT_ALL = "BEHAVIOR_ON_CONNECT_ALL";
    private static String BEHAVIOR_ON_CONNECT_SIGLE = "BEHAVIOR_ON_CONNECT_SIGLE";
    private static String BEHAVIOR_ON_CONNECT_SIGLE_ERROR = "BEHAVIOR_ON_CONNECT_SIGLE_ERROR";
    private boolean canDrawLine;
    private int cellHeight = 0;
    private int cellWidth = 0;
    private int columnGap = 0;
    private Rect currentBigRect;
    private boolean hasInRect = false;
    private int hasLinkedCount = 0;
    private int lineEndX = -100;
    private int lineEndY = -100;
    /* access modifiers changed from: private */
    public int lineGap = 0;
    private ArrayList<Line> lineList = new ArrayList<>();
    private int lineStartX = -100;
    private int lineStartY = -100;
    private Context mContext;
    private MoudleComponentEntity mEntity;
    /* access modifiers changed from: private */
    public Paint paint;
    private ArrayList<Rect> rectList = new ArrayList<>();
    private Rect waitToLine1;
    private Rect waitToLine2;

    /* renamed from: com.hl.android.view.component.moudle.ConnectHorLineComponent$Line */
    class Line {
        public int mEndX = 0;
        public int mEndY = 0;
        public int mStartX = 0;
        public int mStartY = 0;

        public Line(int startx, int starty, int endx, int endy) {
            this.mStartX = startx;
            this.mStartY = starty;
            this.mEndX = endx;
            this.mEndY = endy;
        }
    }

    /* renamed from: com.hl.android.view.component.moudle.ConnectHorLineComponent$Rect */
    class Rect {
        public static final int LINKPOINT_FADE = 16777233;
        public static final int LINKPOINT_LIGHT = 16777232;
        public boolean canBig = true;
        private int count = 0;
        private Bitmap currentShowLinkPoint;
        private long currentTime;
        private boolean mDoAnimation = false;
        private boolean mHasLinked = false;
        public int mHeight = 0;
        public Bitmap mImageBitmap;
        public int mIndex = -1;
        public Bitmap mLinkPointFade;
        /* access modifiers changed from: private */
        public boolean mLinkPointIsUp;
        public Bitmap mLinkPointLight;
        public boolean mShouldBig = false;
        public int mWidth = 0;

        /* renamed from: mX */
        public int f30mX = 0;

        /* renamed from: mY */
        public int f31mY = 0;
        /* access modifiers changed from: private */
        public String myID;
        private long oldTime = 0;
        private int rotate = 5;
        /* access modifiers changed from: private */
        public String shouldID;

        public Rect(Context context, int x, int y, int width, int height, Bitmap imagebitmap, boolean linkPointIsUp) {
            this.f30mX = x;
            this.f31mY = y;
            this.mWidth = width;
            this.mHeight = height;
            this.mImageBitmap = imagebitmap;
            this.mLinkPointIsUp = linkPointIsUp;
            this.mLinkPointFade = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.dian01);
            this.mLinkPointLight = BitmapFactory.decodeResource(context.getResources(), C0048R.drawable.dian02);
            this.currentShowLinkPoint = this.mLinkPointLight;
        }

        public void setAnimationState(boolean doAnimation) {
            this.mDoAnimation = doAnimation;
        }

        public boolean isPlayingAnimation() {
            return this.mDoAnimation;
        }

        public void drawWrapBitmap(Canvas canvas) {
            int resultWidth = this.mWidth;
            int resultHeight = this.mHeight;
            if ((((float) this.mImageBitmap.getWidth()) * 1.0f) / ((float) this.mImageBitmap.getHeight()) >= (((float) resultWidth) * 1.0f) / ((float) resultHeight)) {
                resultHeight = (this.mWidth * this.mImageBitmap.getHeight()) / this.mImageBitmap.getWidth();
            } else {
                resultWidth = (this.mHeight * this.mImageBitmap.getWidth()) / this.mImageBitmap.getHeight();
            }
            canvas.drawBitmap(this.mImageBitmap, null, new RectF((float) (this.f30mX + ((this.mWidth - resultWidth) / 2) + ConnectHorLineComponent.this.getPaddingLeft()), (float) (this.f31mY + ConnectHorLineComponent.this.getPaddingTop() + ((this.mHeight - resultHeight) / 2)), (float) (this.f30mX + ConnectHorLineComponent.this.getPaddingLeft() + ((this.mWidth - resultWidth) / 2) + resultWidth), (float) (this.f31mY + ConnectHorLineComponent.this.getPaddingTop() + ((this.mHeight - resultHeight) / 2) + resultHeight)), ConnectHorLineComponent.this.paint);
        }

        public void drawMe(Canvas canvas, Paint paint) {
            if (this.mImageBitmap != null) {
                if (this.mShouldBig && this.canBig) {
                    canvas.save();
                    canvas.scale(1.1f, 1.1f, (float) (this.f30mX + (this.mWidth / 2)), (float) (this.f31mY + (this.mHeight / 2)));
                    drawWrapBitmap(canvas);
                    canvas.restore();
                } else if (this.mDoAnimation) {
                    this.currentTime = System.currentTimeMillis();
                    if (this.currentTime - this.oldTime > 30) {
                        this.rotate = -this.rotate;
                        canvas.save();
                        canvas.rotate((float) this.rotate, (float) (this.f30mX + (this.mWidth / 2)), (float) (this.f31mY + (this.mHeight / 2)));
                        drawWrapBitmap(canvas);
                        canvas.restore();
                        this.count++;
                        this.oldTime = this.currentTime;
                    }
                    if (this.count >= 6) {
                        this.mDoAnimation = false;
                        this.count = 0;
                        this.oldTime = 0;
                    }
                } else {
                    drawWrapBitmap(canvas);
                }
            }
            if (this.mLinkPointIsUp) {
                canvas.drawBitmap(this.currentShowLinkPoint, (float) (((this.f30mX + ConnectHorLineComponent.this.getPaddingLeft()) + (this.mWidth / 2)) - (this.currentShowLinkPoint.getWidth() / 2)), ((float) ((ConnectHorLineComponent.this.getLayoutParams().height - ConnectHorLineComponent.this.lineGap) / 2)) - 7.5f, paint);
            } else {
                canvas.drawBitmap(this.currentShowLinkPoint, (float) (((this.f30mX + ConnectHorLineComponent.this.getPaddingLeft()) + (this.mWidth / 2)) - (this.currentShowLinkPoint.getWidth() / 2)), ((float) ((ConnectHorLineComponent.this.getLayoutParams().height + ConnectHorLineComponent.this.lineGap) / 2)) - 7.5f, paint);
            }
        }

        public void setCurrentShowLinkPoint(int currentShowLinkPoint2) {
            if (currentShowLinkPoint2 == 16777233) {
                this.currentShowLinkPoint = this.mLinkPointFade;
            } else if (currentShowLinkPoint2 == 16777232) {
                this.currentShowLinkPoint = this.mLinkPointLight;
            }
        }

        public Bitmap getCurrentShowLinkPoint() {
            return this.currentShowLinkPoint;
        }

        public void setHasLined(boolean haslinked) {
            this.mHasLinked = haslinked;
        }

        public boolean hasLinked() {
            return this.mHasLinked;
        }

        public void shouldBig() {
            this.mShouldBig = true;
        }

        public void shouldGoback() {
            this.mShouldBig = false;
        }

        public int getLinkPointCenterY() {
            if (this.mLinkPointIsUp) {
                return this.f31mY + this.mHeight;
            }
            return this.f31mY;
        }

        public int getLinkPointCenterX() {
            return this.f30mX + (this.mWidth / 2);
        }
    }

    public ConnectHorLineComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        this.mContext = context;
        this.paint = new Paint(4);
        this.paint.setStyle(Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        setBackgroundColor(0);
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (MoudleComponentEntity) entity;
    }

    public void load() {
        this.lineGap = (int) ScreenUtils.getVerScreenValue((float) this.mEntity.mLineGap);
        this.columnGap = (int) ScreenUtils.getHorScreenValue((float) this.mEntity.mRowOrColumnGap);
        this.cellHeight = getLayoutParams().height - this.lineGap;
        this.cellHeight /= 2;
        int cellColumns = this.mEntity.cellList.size() / 2;
        this.cellWidth = getLayoutParams().width - (this.columnGap * cellColumns);
        this.cellWidth /= cellColumns;
        this.rectList = getDataSource();
        this.paint.setStrokeWidth((float) this.mEntity.lineThick);
        this.paint.setColor(this.mEntity.lineColor);
        this.paint.setAlpha((int) (this.mEntity.lineAlpha * 255.0f));
    }

    private ArrayList<Rect> getDataSource() {
        ArrayList<Rect> source = new ArrayList<>();
        for (int index = 0; index < this.mEntity.cellList.size(); index++) {
            int rectIndex = -1;
            Cell cell = (Cell) this.mEntity.cellList.get(index);
            int y = 0;
            if ("DOWN_CELL".equals(cell.mCellType)) {
                y = this.cellHeight + this.lineGap;
            } else {
                rectIndex = index / 2;
            }
            Rect rect = new Rect(this.mContext, (this.cellWidth + this.columnGap) * (index / 2), y, this.cellWidth, this.cellHeight, BitmapUtils.getBitMap(cell.mSourceID, this.mContext), y == 0);
            rect.mIndex = rectIndex;
            rect.myID = cell.mCellID;
            rect.shouldID = cell.mLinkID;
            source.add(rect);
        }
        return source;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.save();
        canvas.scale(0.9090909f, 0.9090909f, ((float) getLayoutParams().width) / 2.0f, ((float) getLayoutParams().height) / 2.0f);
        for (int i = 0; i < this.rectList.size(); i++) {
            ((Rect) this.rectList.get(i)).drawMe(canvas, this.paint);
        }
        for (int i2 = 0; i2 < this.lineList.size(); i2++) {
            Line currentLine = (Line) this.lineList.get(i2);
            canvas.drawLine((float) (currentLine.mStartX + getPaddingLeft()), (float) (currentLine.mStartY + getPaddingTop()), (float) (currentLine.mEndX + getPaddingLeft()), (float) (currentLine.mEndY + getPaddingTop()), this.paint);
            canvas.drawCircle((float) (currentLine.mStartX + getPaddingLeft()), (float) (currentLine.mStartY + getPaddingTop()), ((float) this.mEntity.lineThick) / 2.0f, this.paint);
            canvas.drawCircle((float) (currentLine.mEndX + getPaddingLeft()), (float) (currentLine.mEndY + getPaddingTop()), ((float) this.mEntity.lineThick) / 2.0f, this.paint);
        }
        if (this.canDrawLine) {
            if (this.lineEndX != -100) {
                canvas.drawLine((float) (this.lineStartX + getPaddingLeft()), (float) (this.lineStartY + getPaddingTop()), (float) (this.lineEndX + getPaddingLeft()), (float) (this.lineEndY + getPaddingTop()), this.paint);
            }
            canvas.drawCircle((float) (this.lineStartX + getPaddingLeft()), (float) (this.lineStartY + getPaddingTop()), ((float) this.mEntity.lineThick) / 2.0f, this.paint);
            canvas.drawCircle((float) (this.lineEndX + getPaddingLeft()), (float) (this.lineEndY + getPaddingTop()), ((float) this.mEntity.lineThick) / 2.0f, this.paint);
        }
        canvas.restore();
        postInvalidateDelayed(50);
    }

    public void load(InputStream is) {
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

    private void doLinkFailureAnimation(Rect waitToLine22) {
        if (!waitToLine22.isPlayingAnimation()) {
            waitToLine22.setAnimationState(true);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z;
        if (event.getAction() == 0) {
            this.lineStartX = (int) event.getX();
            this.lineStartY = (int) event.getY();
            int i = 0;
            while (true) {
                if (i >= this.rectList.size()) {
                    break;
                }
                Rect currentRect = (Rect) this.rectList.get(i);
                if (this.lineStartX <= currentRect.f30mX || this.lineStartX >= currentRect.f30mX + currentRect.mWidth || this.lineStartY <= currentRect.f31mY || this.lineStartY >= currentRect.f31mY + currentRect.mHeight) {
                    i++;
                } else {
                    if (!currentRect.hasLinked()) {
                        z = true;
                    } else {
                        z = false;
                    }
                    this.canDrawLine = z;
                    if (this.canDrawLine) {
                        this.waitToLine1 = currentRect;
                        this.waitToLine1.setCurrentShowLinkPoint(16777233);
                        this.lineStartX = this.waitToLine1.getLinkPointCenterX();
                        this.lineStartY = this.waitToLine1.getLinkPointCenterY();
                        this.waitToLine1.shouldBig();
                    }
                }
            }
        } else if (event.getAction() == 2) {
            if (this.canDrawLine) {
                this.lineEndX = (int) event.getX();
                this.lineEndY = (int) event.getY();
                this.hasInRect = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= this.rectList.size()) {
                        break;
                    }
                    Rect currentRect2 = (Rect) this.rectList.get(i2);
                    if (currentRect2.mLinkPointIsUp != this.waitToLine1.mLinkPointIsUp && this.lineEndX > currentRect2.f30mX && this.lineEndX < currentRect2.f30mX + currentRect2.mWidth && this.lineEndY > currentRect2.f31mY && this.lineEndY < currentRect2.f31mY + currentRect2.mHeight) {
                        this.hasInRect = true;
                        if (!(this.currentBigRect == null || this.currentBigRect == currentRect2)) {
                            this.currentBigRect.shouldGoback();
                            if (!this.currentBigRect.hasLinked()) {
                                this.currentBigRect.setCurrentShowLinkPoint(16777232);
                            }
                        }
                        this.currentBigRect = currentRect2;
                        this.lineEndX = currentRect2.getLinkPointCenterX();
                        this.lineEndY = currentRect2.getLinkPointCenterY();
                        this.waitToLine2 = currentRect2;
                        this.waitToLine2.shouldBig();
                        this.waitToLine2.setCurrentShowLinkPoint(16777233);
                    } else {
                        i2++;
                    }
                }
                if (!this.hasInRect) {
                    if (this.currentBigRect != null) {
                        if (!this.currentBigRect.hasLinked()) {
                            this.currentBigRect.setCurrentShowLinkPoint(16777232);
                        }
                        this.currentBigRect.shouldGoback();
                    }
                    this.waitToLine2 = null;
                }
            }
        } else if (event.getAction() == 1) {
            if (this.waitToLine1 != null) {
                if (this.waitToLine2 == null || !this.waitToLine1.shouldID.equals(this.waitToLine2.myID)) {
                    if (!this.waitToLine1.hasLinked()) {
                        this.waitToLine1.setCurrentShowLinkPoint(16777232);
                    }
                    if (this.waitToLine2 != null) {
                        if (!this.waitToLine2.hasLinked()) {
                            this.waitToLine2.setCurrentShowLinkPoint(16777232);
                        }
                        this.waitToLine2.shouldGoback();
                        doLinkFailureAnimation(this.waitToLine2);
                        doLinkFailureAction(this.waitToLine1, this.waitToLine2);
                    }
                } else {
                    this.lineStartX = this.waitToLine1.getLinkPointCenterX();
                    this.lineStartY = this.waitToLine1.getLinkPointCenterY();
                    this.lineEndX = this.waitToLine2.getLinkPointCenterX();
                    this.lineEndY = this.waitToLine2.getLinkPointCenterY();
                    this.lineList.add(new Line(this.lineStartX, this.lineStartY, this.lineEndX, this.lineEndY));
                    this.waitToLine1.setHasLined(true);
                    this.waitToLine2.setHasLined(true);
                    this.waitToLine1.canBig = false;
                    this.waitToLine2.canBig = false;
                    this.waitToLine1.setCurrentShowLinkPoint(16777233);
                    this.waitToLine2.setCurrentShowLinkPoint(16777233);
                    this.hasLinkedCount++;
                    doLinkedAction(this.waitToLine1, this.waitToLine2);
                    if (this.hasLinkedCount * 2 == this.mEntity.cellList.size()) {
                        doAllLinkedAction();
                    }
                }
            }
            if (this.waitToLine1 != null) {
                this.waitToLine1.shouldGoback();
            }
            this.waitToLine1 = null;
            this.waitToLine2 = null;
            this.lineEndX = -100;
            this.lineEndY = -100;
            this.lineStartX = -100;
            this.lineStartY = -100;
            this.hasInRect = false;
            this.canDrawLine = false;
        }
        return true;
    }

    private void doLinkedAction(Rect waitToLine12, Rect waitToLine22) {
        String eventValue;
        String str = "-1";
        if (waitToLine12.mIndex >= 0) {
            eventValue = Integer.toString(waitToLine12.mIndex);
        } else {
            eventValue = Integer.toString(waitToLine22.mIndex);
        }
        BookController.getInstance().runBehavior(getEntity(), BEHAVIOR_ON_CONNECT_SIGLE, eventValue);
    }

    private void doLinkFailureAction(Rect waitToLine12, Rect waitToLine22) {
        BookController.getInstance().runBehavior(getEntity(), BEHAVIOR_ON_CONNECT_SIGLE_ERROR);
    }

    private void doAllLinkedAction() {
        BookController.getInstance().runBehavior(getEntity(), BEHAVIOR_ON_CONNECT_ALL);
    }
}
