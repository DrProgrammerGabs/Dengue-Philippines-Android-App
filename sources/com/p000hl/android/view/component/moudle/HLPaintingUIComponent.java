package com.p000hl.android.view.component.moudle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.http.protocol.HTTP;

@SuppressLint({"ViewConstructor", "HandlerLeak", "DrawAllocation"})
/* renamed from: com.hl.android.view.component.moudle.HLPaintingUIComponent */
public class HLPaintingUIComponent extends View implements Component {
    public static int targetHeight;
    public static int targetWidth;
    /* access modifiers changed from: private */
    public int COLOR_4_ERASER = -65552;
    /* access modifiers changed from: private */
    public int SHOW_MESSAGE_SUCCESSED_TO_SAVE_BITMAP = 65553;
    private Bitmap backGroundBitmap;
    private RectF backGroundRectf;
    private Bitmap bitmap;
    private Canvas canvas4path;
    private Bitmap cleanBitmap;
    private RectF clearRectf;
    private MyPaint curSelectPaint;
    private int currentSelectPaintWidth = 5;
    private MyPaint eraser;
    private Bitmap eraserBitmap;
    private boolean hansTouchInPathRect;
    private boolean hasSetConfig;
    private MyPaint lastSelectPaint;
    private Canvas mCanvas;
    /* access modifiers changed from: private */
    public Context mContext;
    private ComponentEntity mEntity;
    private Handler mHandler;
    private Paint mPaint;
    private Paint paint4eraser;
    private MyPaint[] paints;
    private Path path;
    private Bitmap pathBitmap;
    private float preX;
    private float preY;
    private Bitmap saveBitmap;
    private String savePath;
    private RectF saveRectf;
    private float scalingH;
    private float scalingW;
    private String sdCardPath;
    private boolean shouldClipSaveBitmap;
    private Bitmap testBitmap;
    private RectF testRectf;
    private int totalMove;

    /* renamed from: com.hl.android.view.component.moudle.HLPaintingUIComponent$MyPaint */
    public class MyPaint {
        /* access modifiers changed from: private */
        public Bitmap mBitmap;
        /* access modifiers changed from: private */
        public int mClor;
        /* access modifiers changed from: private */
        public float mPositionX;
        /* access modifiers changed from: private */
        public float mPositionY;
        private RectF mrectf;

        static /* synthetic */ float access$316(MyPaint x0, float x1) {
            float f = x0.mPositionX + x1;
            x0.mPositionX = f;
            return f;
        }

        static /* synthetic */ float access$324(MyPaint x0, float x1) {
            float f = x0.mPositionX - x1;
            x0.mPositionX = f;
            return f;
        }

        public MyPaint(Context context, Bitmap bitmap, float x, float y) {
            HLPaintingUIComponent.this.mContext = context;
            this.mBitmap = bitmap;
            this.mPositionX = x;
            this.mPositionY = y;
        }

        /* access modifiers changed from: private */
        public void setColor(int color) {
            this.mClor = color;
        }

        /* access modifiers changed from: private */
        public void drawMe(Canvas canvas, Paint paint) {
            if (this.mBitmap != null) {
                this.mrectf = new RectF(this.mPositionX, this.mPositionY, this.mPositionX + 96.0f, this.mPositionY + 29.0f);
                if (this.mClor == HLPaintingUIComponent.this.COLOR_4_ERASER) {
                    this.mrectf.bottom = this.mPositionY + 79.0f;
                }
                canvas.drawBitmap(this.mBitmap, null, this.mrectf, paint);
            }
        }
    }

    public HLPaintingUIComponent(Context context, ComponentEntity entity) {
        super(context);
        this.mContext = context;
        this.mEntity = entity;
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.testRectf = new RectF(192.0f, 147.0f, 844.0f, 639.0f);
        this.backGroundRectf = new RectF(0.0f, 0.0f, 1027.0f, 768.0f);
        this.clearRectf = new RectF(90.0f, 680.0f, 160.0f, 747.0f);
        this.saveRectf = new RectF(875.0f, 680.0f, 945.0f, 747.0f);
    }

    private void setConfig() {
        targetWidth = getLayoutParams().width;
        targetHeight = getLayoutParams().height;
        this.scalingW = (((float) targetWidth) * 1.0f) / 1027.0f;
        this.scalingH = (((float) targetHeight) * 1.0f) / 768.0f;
        if (targetWidth < targetHeight) {
            this.scalingW = (((float) targetHeight) * 1.0f) / 1027.0f;
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

    private void init() {
        float x;
        int i;
        this.bitmap = Bitmap.createBitmap(1027, 768, Config.ARGB_8888);
        this.pathBitmap = Bitmap.createBitmap(652, 492, Config.ARGB_8888);
        this.canvas4path = new Canvas(this.pathBitmap);
        this.mCanvas = new Canvas(this.bitmap);
        this.backGroundBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.background);
        this.cleanBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.clean);
        this.saveBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.paintsave);
        this.eraserBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.eraser);
        this.testBitmap = getTestBitmap();
        if (this.testBitmap == null) {
            this.testBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.test);
        }
        this.paints = new MyPaint[16];
        for (int i2 = 0; i2 < this.paints.length; i2++) {
            Bitmap curBitmap = BitmapFactory.decodeResource(getResources(), C0048R.drawable.paint_01 + i2);
            if (i2 < 8) {
                if (i2 == 0) {
                    x = 936.0f;
                } else {
                    x = 961.0f;
                }
                i = i2 * 40;
            } else {
                x = -30.0f;
                i = (i2 - 8) * 40;
            }
            this.paints[i2] = new MyPaint(this.mContext, curBitmap, x, (float) (i + 145));
            setColor4Paint(i2);
        }
        this.eraser = new MyPaint(this.mContext, this.eraserBitmap, 956.0f, 580.0f);
        this.eraser.setColor(this.COLOR_4_ERASER);
        this.curSelectPaint = this.paints[0];
        this.lastSelectPaint = this.curSelectPaint;
        if (this.path == null) {
            this.path = new Path();
        }
        this.sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.savePath = this.sdCardPath + File.separator + this.mContext.getPackageName() + File.separator + "HLPicture";
        File mfilepath = new File(this.savePath);
        if (!mfilepath.isDirectory() || !mfilepath.exists()) {
            mfilepath.mkdirs();
        }
    }

    private void setColor4Paint(int i) {
        int color = 0;
        switch (i) {
            case 0:
                color = -16777216;
                break;
            case 1:
                color = -7133696;
                break;
            case 2:
                color = -256;
                break;
            case 3:
                color = -13041920;
                break;
            case 4:
                color = -16744320;
                break;
            case 5:
                color = -3932141;
                break;
            case 6:
                color = -15231465;
                break;
            case SimpleLog.LOG_LEVEL_OFF /*7*/:
                color = -12372837;
                break;
            case 8:
                color = -4980481;
                break;
            case HTTP.f67HT /*9*/:
                color = -18751;
                break;
            case HTTP.f68LF /*10*/:
                color = -8355712;
                break;
            case 11:
                color = -2915328;
                break;
            case 12:
                color = -4343957;
                break;
            case HTTP.f66CR /*13*/:
                color = -16745729;
                break;
            case 14:
                color = -11461882;
                break;
            case 15:
                color = -16711688;
                break;
        }
        this.paints[i].setColor(color);
    }

    private Bitmap getTestBitmap() {
        return BitmapUtils.getBitMap((String) ((MoudleComponentEntity) this.mEntity).getSourceIDList().get(0), this.mContext);
    }

    private void recycleBitmap(Bitmap bitmap2) {
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        if (!this.hasSetConfig) {
            setConfig();
            this.hasSetConfig = true;
        }
        myDraw(canvas);
        logic();
        invalidate();
    }

    private void myDraw(Canvas canvas) {
        if (canvas != null) {
            try {
                canvas.save();
                if (targetWidth < targetHeight) {
                    canvas.rotate(90.0f, ((float) targetWidth) / 2.0f, ((float) targetHeight) / 2.0f);
                }
                canvas.drawColor(-1);
                drawTheOriginalBitmap();
                canvas.drawBitmap(this.bitmap, null, new RectF((((float) targetWidth) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f, (((float) targetHeight) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f, (((float) targetWidth) + (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f, (((float) targetHeight) + (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f), this.mPaint);
                canvas.restore();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                }
            }
        }
    }

    private void drawTheOriginalBitmap() {
        this.mCanvas.drawColor(-1);
        this.mCanvas.drawBitmap(this.backGroundBitmap, null, this.backGroundRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.cleanBitmap, null, this.clearRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.saveBitmap, null, this.saveRectf, this.mPaint);
        for (MyPaint access$100 : this.paints) {
            access$100.drawMe(this.mCanvas, this.mPaint);
        }
        this.eraser.drawMe(this.mCanvas, this.mPaint);
        if (this.path != null && !this.path.isEmpty()) {
            this.mPaint.setColor(this.curSelectPaint.mClor);
            if (this.curSelectPaint != this.eraser) {
                this.canvas4path.drawPath(this.path, this.mPaint);
            } else {
                this.canvas4path.drawPath(this.path, this.paint4eraser);
            }
        }
        this.mCanvas.drawBitmap(this.pathBitmap, null, this.testRectf, this.mPaint);
        this.mCanvas.drawBitmap(this.testBitmap, null, this.testRectf, this.mPaint);
    }

    private void logic() {
        if (this.shouldClipSaveBitmap) {
            Bitmap clipSaveBitmap = Bitmap.createBitmap(this.bitmap, 192, 147, 652, 492);
            if (saveBitmap2file(clipSaveBitmap, this.savePath)) {
                this.mHandler.sendEmptyMessage(this.SHOW_MESSAGE_SUCCESSED_TO_SAVE_BITMAP);
                this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + this.savePath)));
            }
            this.shouldClipSaveBitmap = false;
            recycleBitmap(clipSaveBitmap);
        }
        if (this.lastSelectPaint != this.curSelectPaint) {
            if (this.lastSelectPaint.mPositionX <= 0.0f) {
                MyPaint.access$324(this.lastSelectPaint, 5.0f);
            } else {
                MyPaint.access$316(this.lastSelectPaint, 5.0f);
            }
            if (this.curSelectPaint.mPositionX <= 0.0f) {
                MyPaint.access$316(this.curSelectPaint, 5.0f);
            } else {
                MyPaint.access$324(this.curSelectPaint, 5.0f);
            }
            this.totalMove += 5;
            if (this.totalMove >= 25) {
                this.totalMove = 0;
                this.lastSelectPaint = this.curSelectPaint;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean cutEvent = false;
        float x = (event.getX() / this.scalingW) - ((((float) targetWidth) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f);
        float y = (event.getY() / this.scalingH) - ((((float) targetHeight) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f);
        if (targetWidth < targetHeight) {
            x = (event.getY() / this.scalingW) - ((((float) targetHeight) - (((float) this.bitmap.getWidth()) * this.scalingW)) / 2.0f);
            y = ((((float) targetWidth) - event.getX()) / this.scalingH) - ((((float) targetWidth) - (((float) this.bitmap.getHeight()) * this.scalingH)) / 2.0f);
        }
        float x2 = x - 192.0f;
        float y2 = y - 147.0f;
        if (touchInTheRect(event, 875.0f, 680.0f, 65.0f, 69.0f)) {
            if (event.getAction() == 1) {
                this.shouldClipSaveBitmap = true;
            }
        } else if (touchInTheRect(event, 90.0f, 680.0f, 70.0f, 67.0f)) {
            if (event.getAction() == 1) {
                this.path.reset();
                this.canvas4path.drawColor(-1, Mode.DST_OUT);
            }
        } else if (touchInTheRect(event, 192.0f, 147.0f, 649.0f, 489.0f)) {
            this.hansTouchInPathRect = true;
            switch (event.getAction()) {
                case 0:
                    cutEvent = true;
                    this.path.reset();
                    if (canLineTo(x2, y2)) {
                        this.path.moveTo(x2, y2);
                    }
                    this.path.quadTo(x2, y2, x2, y2);
                    this.preX = x2;
                    this.preY = y2;
                    break;
                case 1:
                    if (canLineTo(x2, y2)) {
                        this.path.quadTo(this.preX, this.preY, x2, y2);
                        break;
                    }
                    break;
                case 2:
                    if (this.path.isEmpty()) {
                        if (canLineTo(x2, y2)) {
                            this.path.moveTo(x2, y2);
                        }
                        this.path.quadTo(x2, y2, x2, y2);
                    } else if (canLineTo(x2, y2)) {
                        this.path.quadTo(this.preX, this.preY, (this.preX + x2) / 2.0f, (this.preY + y2) / 2.0f);
                    }
                    this.preX = x2;
                    this.preY = y2;
                    break;
            }
        } else {
            if (!this.path.isEmpty()) {
                if (this.hansTouchInPathRect) {
                    if (canLineTo(x2, y2)) {
                        this.path.quadTo(this.preX, this.preY, x2, y2);
                    }
                    this.hansTouchInPathRect = false;
                } else {
                    this.path.reset();
                }
            }
            if (this.totalMove == 0) {
                if (touchInTheRect(event, this.eraser.mPositionX, this.eraser.mPositionY, 1027.0f - this.eraser.mPositionX, 79.0f)) {
                    if (event.getAction() == 1) {
                        this.curSelectPaint = this.eraser;
                    }
                } else if (event.getAction() == 1) {
                    int i = 0;
                    while (true) {
                        if (i >= this.paints.length) {
                            break;
                        }
                        if (i < 8) {
                            if (touchInTheRect(event, this.paints[i].mPositionX, this.paints[i].mPositionY, 1027.0f - this.paints[i].mPositionX, 29.0f)) {
                                this.curSelectPaint = this.paints[i];
                                this.path.reset();
                                break;
                            }
                        } else {
                            if (touchInTheRect(event, 0.0f, this.paints[i].mPositionY, this.paints[i].mPositionX + 96.0f, 29.0f)) {
                                this.curSelectPaint = this.paints[i];
                                this.path.reset();
                                break;
                            }
                        }
                        i++;
                    }
                }
            }
        }
        return cutEvent;
    }

    private boolean canLineTo(float x, float y) {
        if (x == 0.0f && y == 0.0f) {
            return false;
        }
        return true;
    }

    static boolean saveBitmap2file(Bitmap bmp, String filePath) {
        CompressFormat format = CompressFormat.JPEG;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(filePath + File.separator + "画图" + System.currentTimeMillis() + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp.compress(format, 100, stream);
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
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.currentSelectPaintWidth = ((MoudleComponentEntity) this.mEntity).lineThick;
        if (this.currentSelectPaintWidth <= 5) {
            this.currentSelectPaintWidth = 5;
        }
        this.mPaint.setStrokeWidth((float) this.currentSelectPaintWidth);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.paint4eraser = new Paint();
        this.paint4eraser.setAntiAlias(true);
        this.paint4eraser.setDither(true);
        this.paint4eraser.setStrokeWidth(10.0f);
        this.paint4eraser.setStyle(Style.STROKE);
        this.paint4eraser.setStrokeJoin(Join.ROUND);
        this.paint4eraser.setStrokeCap(Cap.ROUND);
        this.paint4eraser.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.mHandler = new Handler() {
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                if (msg.what == HLPaintingUIComponent.this.SHOW_MESSAGE_SUCCESSED_TO_SAVE_BITMAP) {
                    Toast.makeText(HLPaintingUIComponent.this.mContext, "Save picture success", 0).show();
                }
            }
        };
        init();
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        recycleBitmap(this.backGroundBitmap);
        recycleBitmap(this.bitmap);
        recycleBitmap(this.testBitmap);
        recycleBitmap(this.cleanBitmap);
        recycleBitmap(this.eraserBitmap);
        recycleBitmap(this.saveBitmap);
        for (MyPaint access$800 : this.paints) {
            recycleBitmap(access$800.mBitmap);
        }
        recycleBitmap(this.eraser.mBitmap);
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
