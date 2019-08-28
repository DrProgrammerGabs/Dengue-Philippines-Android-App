package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;
import android.view.View;
import com.p000hl.android.C0048R;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.BitmapUtils;

/* renamed from: com.hl.android.view.component.WaterStain */
public class WaterStain extends View {
    private static float MAX_TEXT_SIZE = 50.0f;
    private Bitmap mBitmap;
    private Context mContext;
    private Paint mPaint4Line;
    private Paint mPaint4text;
    private String mText;
    private float textHeight;
    private float textSize;
    private float textWidth;

    public WaterStain(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        this.mBitmap = BitmapUtils.decodeResource(this.mContext, C0048R.drawable.water_stain);
        this.mText = HLSetting.BookMarkLabelText;
        Log.d("SunYongle", "水印值：" + this.mText);
        this.mPaint4Line = new Paint();
        this.mPaint4Line.setAntiAlias(true);
        this.mPaint4Line.setColor(-1);
        this.mPaint4text = new Paint(this.mPaint4Line);
        this.mPaint4Line.setAlpha(128);
        this.mPaint4text.setTextSize(this.textSize);
        this.mPaint4text.setAntiAlias(true);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        super.onDraw(canvas);
        if (HLSetting.IsShowBookMark) {
            int bitmapPositionX = (getWidth() - this.mBitmap.getWidth()) / 2;
            int bitmapPositionY = (getHeight() - this.mBitmap.getHeight()) / 2;
            canvas.drawBitmap(this.mBitmap, (float) bitmapPositionX, (float) bitmapPositionY, null);
            canvas.drawLine(0.0f, 0.0f, (float) bitmapPositionX, (float) bitmapPositionY, this.mPaint4Line);
            canvas.drawLine((float) getWidth(), 0.0f, (float) (this.mBitmap.getWidth() + bitmapPositionX), (float) bitmapPositionY, this.mPaint4Line);
            canvas.drawLine(0.0f, (float) getHeight(), (float) bitmapPositionX, (float) (this.mBitmap.getHeight() + bitmapPositionY), this.mPaint4Line);
            canvas.drawLine((float) getWidth(), (float) getHeight(), (float) (this.mBitmap.getWidth() + bitmapPositionX), (float) (this.mBitmap.getHeight() + bitmapPositionY), this.mPaint4Line);
        }
        this.textSize = (float) ((getWidth() * 6) / 320);
        this.mPaint4text.setTextSize(this.textSize);
        this.textWidth = this.mPaint4text.measureText(this.mText);
        String[] position = HLSetting.BookMarkLablePositon.split("\\|");
        for (int i = 0; i < position.length; i++) {
            Log.d("SunYongle", " position: " + position[i]);
        }
        String horPosition = position[0];
        String verPosition = position[1];
        Log.d("SunYongle", " xString: " + horPosition + "yString: " + verPosition);
        float horInt = 0.0f;
        float verInt = 0.0f;
        if (horPosition.equals("left")) {
            horInt = (float) HLSetting.BookMarkLabelHorGap;
        } else if (horPosition.equals("center")) {
            horInt = ((((float) getWidth()) - this.textWidth) / 2.0f) + ((float) HLSetting.BookMarkLabelHorGap);
        } else if (horPosition.equals("right")) {
            horInt = ((float) (getWidth() - HLSetting.BookMarkLabelHorGap)) - this.textWidth;
        }
        if (verPosition.equals("top")) {
            verInt = (float) HLSetting.BookMarkLabelVerGap;
        } else if (verPosition.equals("middle")) {
            verInt = (float) (((getHeight() - 8) / 2) + HLSetting.BookMarkLabelVerGap);
        } else if (verPosition.equals("bottom")) {
            verInt = (float) ((getHeight() - HLSetting.BookMarkLabelVerGap) - 8);
        }
        Log.d("SunYongle", "水印： " + HLSetting.BookMarkLabelText + " x : " + horInt + "y: " + verInt);
        canvas.drawText(this.mText, horInt, verInt, this.mPaint4text);
    }
}
