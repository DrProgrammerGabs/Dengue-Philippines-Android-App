package com.p000hl.android.view.component.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.Button4Play;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.HttpStatus;

@SuppressLint({"ViewConstructor", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.HLPuzzleGameUIComponent */
public class HLPuzzleGameUIComponent extends View implements Component {
    private static int LINE_TYPE_FS = Button4Play.BTN_STATE_STOP_NOMAL;
    private static int LINE_TYPE_TF = Button4Play.BTN_STATE_PLAY_TOUCH;
    public static int LINE_TYPE_TT = Button4Play.BTN_STATE_PLAY_NOMAL;
    private static int rectHeight = 100;
    private static int rectSpace = 10;
    private static int rectWidth;
    public static int targetHeight;
    public static int targetWidth;
    private Bitmap backGroundBitmap;
    private RectF backGroundRectf;
    private long bestTime = 0;
    private Bitmap bitmap;
    private float canScrollLength = 0.0f;
    private Bitmap cancelBitmap;
    private RectF cancelRectf;
    private float clipHeight;
    private float clipWidth;
    private int currentAlpha = 255;
    /* access modifiers changed from: private */
    public MRect currentCloneRect = null;
    private Bitmap currentGoBitmap;
    private RectF currentGoRectf;
    private Bitmap currentOpBitmap;
    private RectF currentOpRectf;
    private Bitmap currentPpBitmap;
    private RectF currentPpRectf;
    private Bitmap currentRpBitmap;
    private RectF currentRpRectf;
    private int currentSelectLineType = LINE_TYPE_TT;
    private String currentShowStr;
    private Bitmap currentStarBitmap;
    private RectF currentStarRectf;
    private float currentStrWidth;
    private int currentTouchImageIndex = -1;
    private float ddx;
    private float ddy;

    /* renamed from: dx */
    private float f34dx;

    /* renamed from: dy */
    private float f35dy;
    private Editor editor;
    private Bitmap finishBitmap;
    private RectF finishRectf;
    private Bitmap goBitmap1;
    private Bitmap goBitmap2;
    private boolean hasCloneRect;
    private int hasComplete = 0;
    private boolean hasSetConfig;
    private boolean hasStartGame = false;
    private boolean hasWin;
    private int horCount;
    private Bitmap lineHBitmap;
    private Bitmap lineVBitmap;
    private Canvas mCanvas;
    private Context mContext;
    private ComponentEntity mEntity;
    private Paint mPaint;
    private ArrayList<MRect> mRectList;
    private ArrayList<MRect> mRightPositionRects;
    private MotionEvent oldEvent = null;
    private Bitmap opBitmap1;
    private Bitmap opBitmap2;
    private Bitmap popupBitmap;
    private RectF popupRectf;
    private Bitmap ppBitmap1;
    private Bitmap ppBitmap2;
    private SharedPreferences preferences;
    private int rectTopPadding = 650;
    private Bitmap rpBitmap1;
    private Bitmap rpBitmap2;
    private float scalingH;
    private float scalingW;
    private boolean showMessage = false;
    private boolean showTestBitmap = false;
    private Bitmap star1Bitmap1;
    private Bitmap star2Bitmap2;
    private Bitmap sureBitmap;
    private RectF sureRectf;
    private RectF sureRectf1;
    private Bitmap testBitmap;
    private long timeHasGo = 0;
    private int timerCount;
    private float totalDx;
    private float totalDy;
    private int verCount;
    private int waitToChangeLineType = this.currentSelectLineType;

    /* renamed from: com.hl.android.view.component.moudle.HLPuzzleGameUIComponent$MRect */
    class MRect {
        /* access modifiers changed from: private */
        public MRect cloneOfwhom;
        public float mHeight = 0.0f;
        public Bitmap mImageBitmap;
        public int mIndexX;
        public int mIndexY;
        public float mWidth = 0.0f;

        /* renamed from: mX */
        public float f36mX = 0.0f;

        /* renamed from: mY */
        public float f37mY = 0.0f;

        public MRect(Bitmap bitmap, int indexX, int indexY, float width, float height) {
            this.mImageBitmap = bitmap;
            this.mIndexX = indexX;
            this.mIndexY = indexY;
            this.mWidth = width;
            this.mHeight = height;
        }

        public MRect cloneMe() {
            MRect rect = new MRect(this.mImageBitmap, this.mIndexX, this.mIndexY, this.mWidth, this.mHeight);
            rect.f36mX = this.f36mX;
            rect.f37mY = this.f37mY;
            rect.cloneOfwhom = this;
            return rect;
        }

        public void setXY(float x, float y) {
            this.f36mX = x;
            this.f37mY = y;
        }

        public void drawMe(Canvas canvas, Paint paint) {
            if (this.mImageBitmap == null) {
                return;
            }
            if (HLPuzzleGameUIComponent.this.currentCloneRect == null || HLPuzzleGameUIComponent.this.currentCloneRect.cloneOfwhom != this) {
                canvas.drawBitmap(this.mImageBitmap, null, new RectF(this.f36mX, this.f37mY, this.f36mX + this.mWidth, this.f37mY + this.mHeight), paint);
            }
        }
    }

    public HLPuzzleGameUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.backGroundRectf = new RectF(0.0f, 0.0f, 1024.0f, 768.0f);
        this.currentGoRectf = new RectF(850.0f, 132.0f, 992.0f, 274.0f);
        this.currentRpRectf = new RectF(865.0f, 310.0f, 977.0f, 356.0f);
        this.currentOpRectf = new RectF(865.0f, 370.0f, 977.0f, 416.0f);
        this.currentPpRectf = new RectF(865.0f, 430.0f, 977.0f, 476.0f);
        this.currentStarRectf = new RectF(858.0f, 525.0f, 989.0f, 558.0f);
        this.popupRectf = new RectF(284.0f, 220.0f, 739.0f, 448.0f);
        this.sureRectf = new RectF(341.0f, 370.0f, 481.0f, 406.0f);
        this.cancelRectf = new RectF(541.0f, 370.0f, 681.0f, 406.0f);
        this.finishRectf = new RectF(284.0f, 220.0f, 741.0f, 545.0f);
        this.sureRectf1 = new RectF(442.0f, 467.0f, 582.0f, 503.0f);
    }

    private void init() {
        this.bitmap = Bitmap.createBitmap(1024, 768, Config.ARGB_8888);
        this.mCanvas = new Canvas(this.bitmap);
        this.backGroundBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlebg);
        this.testBitmap = BitmapUtils.getBitMap((String) ((MoudleComponentEntity) this.mEntity).getSourceIDList().get(0), this.mContext);
        if (this.testBitmap == null) {
            this.testBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzletest);
        }
        this.goBitmap1 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlestart);
        this.goBitmap2 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlepress);
        this.finishBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.finishtip);
        this.opBitmap1 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.op_1);
        this.opBitmap2 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.op_2);
        this.ppBitmap1 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.pp_1);
        this.ppBitmap2 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.pp_2);
        this.rpBitmap1 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.rp_1);
        this.rpBitmap2 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.rp_2);
        this.star1Bitmap1 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlestar1);
        this.star2Bitmap2 = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlestar2);
        this.sureBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzleok);
        this.cancelBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlecancel);
        this.lineHBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlelineh);
        this.lineVBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlelinev);
        this.popupBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.puzzlepopup);
        this.currentGoBitmap = this.goBitmap1;
        this.currentOpBitmap = this.opBitmap1;
        this.currentPpBitmap = this.ppBitmap1;
        this.currentRpBitmap = this.rpBitmap1;
        this.currentStarBitmap = this.star1Bitmap1;
        this.hasWin = false;
        this.preferences = this.mContext.getSharedPreferences("StoreBestTime", 0);
        this.editor = this.preferences.edit();
        initRects();
    }

    private void initRects() {
        recycleBitmaps(this.mRectList);
        recycleBitmaps(this.mRightPositionRects);
        this.mRightPositionRects = new ArrayList<>();
        this.mRectList = clipBitMapWith(this.currentSelectLineType, this.testBitmap);
        Collections.shuffle(this.mRectList);
        setRectsDrawPosition(this.mRectList);
        this.hasComplete = 0;
        this.timeHasGo = 0;
        this.timerCount = 0;
        this.bestTime = getBestTime();
    }

    private void recycleBitmap(Bitmap bitmap2) {
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
    }

    private void recycleBitmaps(ArrayList<MRect> rectList) {
        if (rectList != null && rectList.size() != 0) {
            Iterator i$ = rectList.iterator();
            while (i$.hasNext()) {
                recycleBitmap(((MRect) i$.next()).mImageBitmap);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        long startTime = System.currentTimeMillis();
        if (!this.hasSetConfig) {
            setConfig();
            this.hasSetConfig = true;
        }
        super.onDraw(canvas);
        myDraw(canvas);
        logic();
        postInvalidateDelayed((100 + startTime) - System.currentTimeMillis());
    }

    private void setConfig() {
        targetWidth = getLayoutParams().width;
        targetHeight = getLayoutParams().height;
        this.scalingW = (((float) targetWidth) * 1.0f) / 1024.0f;
        this.scalingH = (((float) targetHeight) * 1.0f) / 768.0f;
        if (targetWidth < targetHeight) {
            this.scalingW = (((float) targetHeight) * 1.0f) / 1024.0f;
            this.scalingH = (((float) targetWidth) * 1.0f) / 768.0f;
        }
        if (HLSetting.FitScreen) {
            return;
        }
        if (this.scalingW > this.scalingH) {
            this.scalingW = this.scalingH;
        } else {
            this.scalingH = this.scalingW;
        }
    }

    private void myDraw(Canvas canvas) {
        if (canvas != null) {
            try {
                canvas.drawColor(-1);
                canvas.save();
                drawTheOriginalBitmap();
                RectF rectF = new RectF((((float) targetWidth) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f, (((float) targetHeight) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f, (((float) targetWidth) + (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f, (((float) targetHeight) + (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f);
                if (targetWidth < targetHeight) {
                    canvas.rotate(90.0f, ((float) targetWidth) / 2.0f, ((float) targetHeight) / 2.0f);
                }
                canvas.drawBitmap(this.bitmap, null, rectF, this.mPaint);
                canvas.restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void drawTheOriginalBitmap() {
        this.mCanvas.drawBitmap(this.backGroundBitmap, null, this.backGroundRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.currentGoBitmap, null, this.currentGoRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.currentRpBitmap, null, this.currentRpRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.currentOpBitmap, null, this.currentOpRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.currentPpBitmap, null, this.currentPpRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.currentStarBitmap, null, this.currentStarRectf, this.mPaint);
        this.currentShowStr = String.format("%02d:%02d", new Object[]{Long.valueOf(this.timeHasGo / 60), Long.valueOf(this.timeHasGo % 60)});
        this.currentStrWidth = this.mPaint.measureText(this.currentShowStr);
        this.mCanvas.drawText(this.currentShowStr, 102.0f - (this.currentStrWidth / 2.0f), 230.0f, this.mPaint);
        this.currentShowStr = String.format("%d", new Object[]{Integer.valueOf((int) ((((float) this.hasComplete) * 100.0f) / ((float) (this.horCount * this.verCount))))});
        this.currentShowStr += "%";
        this.currentStrWidth = this.mPaint.measureText(this.currentShowStr);
        this.mCanvas.drawText(this.currentShowStr, 102.0f - (this.currentStrWidth / 2.0f), 388.0f, this.mPaint);
        this.currentShowStr = String.format("%02d:%02d", new Object[]{Long.valueOf(this.bestTime / 60), Long.valueOf(this.bestTime % 60)});
        this.currentStrWidth = this.mPaint.measureText(this.currentShowStr);
        this.mCanvas.drawText(this.currentShowStr, 102.0f - (this.currentStrWidth / 2.0f), 520.0f, this.mPaint);
        if (!this.hasStartGame) {
            this.mPaint.setAlpha(255);
            this.mCanvas.drawBitmap(this.testBitmap, null, new RectF(213.0f, 125.0f, 810.0f, 562.0f), this.mPaint);
        } else {
            if (this.showTestBitmap) {
                this.mPaint.setAlpha(this.currentAlpha);
                this.mCanvas.drawBitmap(this.testBitmap, null, new RectF(213.0f, 125.0f, 810.0f, 562.0f), this.mPaint);
                this.mPaint.setAlpha(255);
            }
            drawRectList(this.mRectList, this.mCanvas);
        }
        drawRightRects(this.mRightPositionRects, this.mCanvas);
        drawLine(this.mCanvas);
        if (this.showMessage) {
            drawARGB(this.mCanvas);
            this.mCanvas.drawBitmap(this.popupBitmap, null, this.popupRectf, this.mPaint);
            this.mCanvas.drawBitmap(this.sureBitmap, null, this.sureRectf, this.mPaint);
            this.mCanvas.drawBitmap(this.cancelBitmap, null, this.cancelRectf, this.mPaint);
        } else if (this.hasWin) {
            drawARGB(this.mCanvas);
            this.mCanvas.drawBitmap(this.finishBitmap, null, this.finishRectf, this.mPaint);
            this.mCanvas.drawBitmap(this.sureBitmap, null, this.sureRectf1, this.mPaint);
            this.currentShowStr = String.format("%02d:%02d", new Object[]{Long.valueOf(this.timeHasGo / 60), Long.valueOf(this.timeHasGo % 60)});
            this.currentStrWidth = this.mPaint.measureText(this.currentShowStr);
            this.mCanvas.drawText(this.currentShowStr, 599.0f - (this.currentStrWidth / 2.0f), 370.0f, this.mPaint);
            this.currentShowStr = String.format("%02d:%02d", new Object[]{Long.valueOf(getBestTime() / 60), Long.valueOf(getBestTime() % 60)});
            this.currentStrWidth = this.mPaint.measureText(this.currentShowStr);
            this.mCanvas.drawText(this.currentShowStr, 599.0f - (this.currentStrWidth / 2.0f), 430.0f, this.mPaint);
        } else if (this.currentCloneRect != null) {
            this.mPaint.setAlpha(HttpStatus.SC_OK);
            this.currentCloneRect.drawMe(this.mCanvas, this.mPaint);
            this.mPaint.setAlpha(255);
        }
    }

    private long getBestTime() {
        return this.preferences.getLong("" + this.currentSelectLineType, 0);
    }

    private void drawARGB(Canvas canvas) {
        canvas.drawARGB(120, 255, 255, 255);
    }

    private void drawRightRects(ArrayList<MRect> rightPositionRects, Canvas canvas) {
        if (rightPositionRects != null && rightPositionRects.size() != 0) {
            for (int i = 0; i < rightPositionRects.size(); i++) {
                MRect curRect = (MRect) rightPositionRects.get(i);
                curRect.mWidth = this.clipWidth;
                curRect.mHeight = this.clipHeight;
                curRect.setXY(213.0f + (((float) curRect.mIndexX) * this.clipWidth), 125.0f + (((float) curRect.mIndexY) * this.clipHeight));
                curRect.drawMe(this.mCanvas, this.mPaint);
            }
        }
    }

    private void drawRectList(ArrayList<MRect> rectList, Canvas canvas) {
        if (rectList != null && rectList.size() > 0) {
            for (int i = 0; i < rectList.size(); i++) {
                ((MRect) rectList.get(i)).drawMe(this.mCanvas, this.mPaint);
            }
        }
    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i < this.horCount - 1; i++) {
            canvas.drawBitmap(this.lineVBitmap, null, new RectF((((float) (i + 1)) * this.clipWidth) + 213.0f, 125.0f, (((float) (i + 1)) * this.clipWidth) + 213.0f + 2.0f, 564.0f), this.mPaint);
        }
        for (int i2 = 0; i2 < this.verCount - 1; i2++) {
            canvas.drawBitmap(this.lineHBitmap, null, new RectF(213.0f, (((float) (i2 + 1)) * this.clipHeight) + 125.0f, 809.0f, (((float) (i2 + 1)) * this.clipHeight) + 125.0f + 2.0f), this.mPaint);
        }
    }

    private void logic() {
        if (this.showTestBitmap) {
            this.currentAlpha -= 6;
            if (this.currentAlpha <= 0) {
                this.showTestBitmap = false;
            }
        }
        if (this.hasStartGame && !this.showMessage && !this.hasWin) {
            this.timerCount++;
            if (this.timerCount >= 10) {
                this.timeHasGo++;
                this.timerCount = 0;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean cutTheEvent = false;
        if (this.hasWin) {
            if (event.getAction() == 1 && touchInTheRect(event, 442.0f, 467.0f, 140.0f, 36.0f)) {
                this.hasWin = false;
                this.hasStartGame = false;
                initRects();
            }
        } else if (this.showMessage && event.getAction() == 1) {
            if (touchInTheRect(event, 341.0f, 370.0f, 140.0f, 36.0f)) {
                if (this.currentSelectLineType == this.waitToChangeLineType) {
                    this.hasStartGame = true;
                    initRects();
                } else {
                    this.hasStartGame = false;
                    this.currentSelectLineType = this.waitToChangeLineType;
                    initRects();
                }
                this.showMessage = false;
            } else if (touchInTheRect(event, 541.0f, 370.0f, 140.0f, 36.0f)) {
                this.showMessage = false;
                this.waitToChangeLineType = this.currentSelectLineType;
            }
        }
        if (touchInTheRect(event, 850.0f, 132.0f, 142.0f, 142.0f)) {
            if (!this.showMessage && !this.hasWin) {
                if (event.getAction() == 0 || event.getAction() == 2) {
                    this.currentGoBitmap = this.goBitmap2;
                } else {
                    if (event.getAction() == 1) {
                        if (!this.hasStartGame) {
                            this.hasStartGame = true;
                        } else {
                            this.showMessage = true;
                        }
                    }
                    this.currentGoBitmap = this.goBitmap1;
                }
            }
        } else if (touchInTheRect(event, 865.0f, 310.0f, 112.0f, 46.0f)) {
            if (!this.showMessage && !this.hasWin) {
                if (event.getAction() == 0 || event.getAction() == 2) {
                    this.currentRpBitmap = this.rpBitmap2;
                } else {
                    doTouchAction(event, LINE_TYPE_TT);
                    this.currentRpBitmap = this.rpBitmap1;
                }
            }
        } else if (touchInTheRect(event, 865.0f, 370.0f, 112.0f, 46.0f)) {
            if (!this.showMessage && !this.hasWin) {
                if (event.getAction() == 0 || event.getAction() == 2) {
                    this.currentOpBitmap = this.opBitmap2;
                } else {
                    doTouchAction(event, LINE_TYPE_TF);
                    this.currentOpBitmap = this.opBitmap1;
                }
            }
        } else if (touchInTheRect(event, 865.0f, 430.0f, 112.0f, 46.0f)) {
            if (!this.showMessage && !this.hasWin) {
                if (event.getAction() == 0 || event.getAction() == 2) {
                    this.currentPpBitmap = this.ppBitmap2;
                } else {
                    doTouchAction(event, LINE_TYPE_FS);
                    this.currentPpBitmap = this.ppBitmap1;
                }
            }
        } else if (!touchInTheRect(event, 860.0f, 525.0f, 131.0f, 33.0f)) {
            if (touchInTheRect(event, 0.0f, (float) this.rectTopPadding, 1024.0f, (float) rectHeight)) {
                if (event.getAction() == 0) {
                    cutTheEvent = true;
                }
                if (!this.showMessage && !this.hasWin && this.hasStartGame) {
                    this.currentTouchImageIndex = getTouchImageIndex(event);
                    if (!this.hasCloneRect && this.mRectList.size() != 0 && this.canScrollLength > 0.0f) {
                        cutTheEvent = true;
                        if (event.getAction() == 2) {
                            this.f34dx = event.getX() - this.oldEvent.getX();
                            this.f35dy = event.getY() - this.oldEvent.getY();
                            this.f34dx /= this.scalingW;
                            this.f35dy /= this.scalingW;
                            this.totalDx += this.f34dx;
                            this.totalDy += this.f35dy;
                            if (targetWidth < targetHeight) {
                                moveRects(this.totalDy, this.f35dy);
                            } else {
                                moveRects(this.totalDx, this.f34dx);
                            }
                        }
                    }
                }
            } else {
                if (this.currentTouchImageIndex != -1 && !this.hasCloneRect) {
                    this.currentCloneRect = ((MRect) this.mRectList.get(this.currentTouchImageIndex)).cloneMe();
                    this.hasCloneRect = true;
                }
                this.currentGoBitmap = this.goBitmap1;
                this.currentRpBitmap = this.rpBitmap1;
                this.currentOpBitmap = this.opBitmap1;
                this.currentPpBitmap = this.ppBitmap1;
                this.currentStarBitmap = this.star1Bitmap1;
            }
        } else if (!this.showMessage && !this.hasWin && this.hasStartGame) {
            if (event.getAction() == 0 || event.getAction() == 2) {
                this.currentStarBitmap = this.star2Bitmap2;
            } else {
                if (event.getAction() == 1 && this.hasStartGame) {
                    this.currentAlpha = 255;
                    this.showTestBitmap = true;
                }
                this.currentStarBitmap = this.star1Bitmap1;
            }
        }
        if (event.getAction() == 1) {
            if (this.hasCloneRect) {
                if (touchInTheRect(event, 213.0f + (((float) this.currentCloneRect.mIndexX) * this.clipWidth), 125.0f + (((float) this.currentCloneRect.mIndexY) * this.clipHeight), this.clipWidth, this.clipHeight)) {
                    this.mRightPositionRects.add(this.currentCloneRect);
                    this.mRectList.remove(this.currentCloneRect.cloneOfwhom);
                    this.hasComplete++;
                    setRectsDrawPosition(this.mRectList);
                    checkWinAndDoSomeThing(this.mRectList);
                }
            }
            if (this.currentTouchImageIndex != -1) {
                cutTheEvent = true;
            }
            this.currentTouchImageIndex = -1;
            this.currentCloneRect = null;
            this.hasCloneRect = false;
        } else if (this.hasCloneRect) {
            cutTheEvent = true;
            if (event.getAction() == 2) {
                this.ddx = event.getX() - this.oldEvent.getX();
                this.ddy = event.getY() - this.oldEvent.getY();
                if (targetWidth < targetHeight) {
                    this.ddx /= this.scalingH;
                    this.ddy /= this.scalingW;
                    this.currentCloneRect.f36mX += this.ddy;
                    this.currentCloneRect.f37mY -= this.ddx;
                } else {
                    this.ddx /= this.scalingW;
                    this.ddy /= this.scalingH;
                    this.currentCloneRect.f36mX += this.ddx;
                    this.currentCloneRect.f37mY += this.ddy;
                }
            }
        }
        this.oldEvent = MotionEvent.obtain(event);
        return cutTheEvent;
    }

    private void doTouchAction(MotionEvent event, int lineType) {
        if (event.getAction() != 1) {
            return;
        }
        if (this.currentSelectLineType != lineType) {
            if (!this.hasStartGame) {
                this.currentSelectLineType = lineType;
                this.waitToChangeLineType = this.currentSelectLineType;
                initRects();
                return;
            }
            this.showMessage = true;
            this.waitToChangeLineType = lineType;
        } else if (this.hasStartGame) {
            this.showMessage = true;
            this.waitToChangeLineType = this.currentSelectLineType;
        }
    }

    private void checkWinAndDoSomeThing(ArrayList<MRect> rectList) {
        if (rectList != null && rectList.size() == 0) {
            this.hasWin = true;
            if (this.bestTime == 0) {
                this.editor.putLong("" + this.currentSelectLineType, this.timeHasGo);
            } else if (this.timeHasGo < this.bestTime) {
                this.editor.putLong("" + this.currentSelectLineType, this.timeHasGo);
            }
            this.editor.commit();
        }
    }

    private int getTouchImageIndex(MotionEvent event) {
        for (int i = 0; i < this.mRectList.size(); i++) {
            MRect curRect = (MRect) this.mRectList.get(i);
            if (touchInTheRect(event, curRect.f36mX, curRect.f37mY, curRect.mWidth, curRect.mHeight)) {
                return i;
            }
        }
        return -1;
    }

    private void moveRects(float totalDxOrDy, float dxOrdy) {
        int i = 0;
        while (i < this.mRectList.size()) {
            if (Math.abs(totalDxOrDy) < this.canScrollLength) {
                MRect mRect = (MRect) this.mRectList.get(i);
                mRect.f36mX += dxOrdy;
                i++;
            } else if (totalDxOrDy >= 0.0f) {
                moveToStart();
                return;
            } else {
                moveToEnd();
                return;
            }
        }
    }

    private void moveToEnd() {
        if (targetWidth < targetHeight) {
            this.totalDy = -this.canScrollLength;
        } else {
            this.totalDx = -this.canScrollLength;
        }
        for (int i = 0; i < this.mRectList.size(); i++) {
            ((MRect) this.mRectList.get((this.mRectList.size() - i) - 1)).setXY((float) (1024 - ((i + 1) * (rectWidth + rectSpace))), (float) this.rectTopPadding);
        }
    }

    private void moveToStart() {
        if (targetWidth < targetHeight) {
            this.totalDy = this.canScrollLength;
        } else {
            this.totalDx = this.canScrollLength;
        }
        for (int i = 0; i < this.mRectList.size(); i++) {
            ((MRect) this.mRectList.get(i)).setXY((float) ((rectWidth + rectSpace) * i), (float) this.rectTopPadding);
        }
    }

    private void setRectsDrawPosition(ArrayList<MRect> rectList) {
        if (rectList != null && rectList.size() != 0) {
            for (int i = 0; i < rectList.size(); i++) {
                ((MRect) rectList.get(i)).setXY((float) ((((1024 - (rectWidth * rectList.size())) - (rectSpace * (rectList.size() - 1))) / 2) + ((rectWidth + rectSpace) * i)), (float) this.rectTopPadding);
            }
            if (((MRect) rectList.get(0)).f36mX < 0.0f) {
                this.canScrollLength = -((MRect) rectList.get(0)).f36mX;
            } else {
                this.canScrollLength = 0.0f;
            }
        }
    }

    private ArrayList<MRect> clipBitMapWith(int currentSelectLineType2, Bitmap testBitmap2) {
        ArrayList<MRect> rects = new ArrayList<>();
        switch (currentSelectLineType2) {
            case Button4Play.BTN_STATE_PLAY_NOMAL /*4097*/:
                this.horCount = 3;
                this.verCount = 2;
                break;
            case Button4Play.BTN_STATE_PLAY_TOUCH /*4098*/:
                this.horCount = 4;
                this.verCount = 3;
                break;
            case Button4Play.BTN_STATE_STOP_NOMAL /*4099*/:
                this.horCount = 6;
                this.verCount = 4;
                break;
        }
        this.clipWidth = 597.0f / ((float) this.horCount);
        this.clipHeight = 437.0f / ((float) this.verCount);
        float subBitmapWidth = (float) (testBitmap2.getWidth() / this.horCount);
        float subBitmapHeight = (float) (testBitmap2.getHeight() / this.verCount);
        rectWidth = (int) ((((float) rectHeight) * this.clipWidth) / this.clipHeight);
        for (int i = 0; i < this.horCount; i++) {
            for (int j = 0; j < this.verCount; j++) {
                rects.add(new MRect(Bitmap.createBitmap(testBitmap2, (int) (((float) i) * subBitmapWidth), (int) (((float) j) * subBitmapHeight), (int) subBitmapWidth, (int) subBitmapHeight), i, j, (float) rectWidth, (float) rectHeight));
            }
        }
        return rects;
    }

    private boolean touchInTheRect(MotionEvent event, float x, float y, float width, float height) {
        float tx = (event.getX() / this.scalingW) - ((((float) targetWidth) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f);
        float ty = (event.getY() / this.scalingH) - ((((float) targetHeight) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f);
        if (targetWidth < targetHeight) {
            tx = (event.getY() / this.scalingW) - ((((float) targetHeight) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f);
            ty = ((((float) targetWidth) - event.getX()) / this.scalingH) - ((((float) targetWidth) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f);
        }
        if (tx <= x || tx >= x + width || ty <= y || ty >= y + height) {
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
        init();
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeWidth(1.0f);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize(40.0f);
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        recycleBitmaps(this.mRectList);
        recycleBitmaps(this.mRightPositionRects);
        recycleBitmap(this.backGroundBitmap);
        recycleBitmap(this.bitmap);
        recycleBitmap(this.cancelBitmap);
        recycleBitmap(this.sureBitmap);
        recycleBitmap(this.currentGoBitmap);
        recycleBitmap(this.currentOpBitmap);
        recycleBitmap(this.currentPpBitmap);
        recycleBitmap(this.currentRpBitmap);
        recycleBitmap(this.currentStarBitmap);
        recycleBitmap(this.finishBitmap);
        recycleBitmap(this.goBitmap1);
        recycleBitmap(this.goBitmap2);
        recycleBitmap(this.lineHBitmap);
        recycleBitmap(this.lineVBitmap);
        recycleBitmap(this.testBitmap);
        recycleBitmap(this.opBitmap1);
        recycleBitmap(this.opBitmap2);
        recycleBitmap(this.popupBitmap);
        recycleBitmap(this.ppBitmap1);
        recycleBitmap(this.ppBitmap2);
        recycleBitmap(this.rpBitmap1);
        recycleBitmap(this.rpBitmap2);
        recycleBitmap(this.star1Bitmap1);
        recycleBitmap(this.star2Bitmap2);
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }
}
