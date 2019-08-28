package com.p000hl.android.view.component.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.TextPaint;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.TextComponentEntity;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Vector;

/* renamed from: com.hl.android.view.component.textview.TextViewComponent */
public class TextViewComponent extends TextView implements Component {
    public AnimationSet animationset = null;
    public TextComponentEntity mEntity = null;
    private TextPaint mPaint = null;
    Vector<Float> m_LineWidth = new Vector<>();
    Vector<String> m_String = new Vector<>();
    float m_iFontHeight;
    int m_iRealLine = 0;
    Paint paint = new Paint();
    private int textHeight;

    /* renamed from: x */
    float f53x = 0.0f;

    /* renamed from: y */
    float f54y = 0.0f;

    public TextViewComponent(Context context) {
        super(context);
    }

    @SuppressLint({"NewApi"})
    public TextViewComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        this.mEntity = (TextComponentEntity) entity;
        setBGAlphaAndColor((int) (Float.valueOf(this.mEntity.getBgalpha()).floatValue() * 255.0f), this.mEntity.getBgcolor());
        if (ScreenUtils.getAPILevel() > 8) {
            setOverScrollMode(2);
        }
    }

    public void loadText() {
        this.mPaint = new TextPaint();
        setFontSize(this.mEntity.getFontSize());
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0266  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawView(float r33) {
        /*
            r32 = this;
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r28 = r28.getFontFamily()
            java.lang.String r12 = java.net.URLDecoder.decode(r28)
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r28 = r28.getFontWeight()
            java.lang.String r13 = java.net.URLDecoder.decode(r28)
            r25 = 0
            java.lang.String r28 = "Times New Roman"
            r0 = r28
            boolean r28 = r12.equals(r0)
            if (r28 == 0) goto L_0x021d
            android.content.Context r28 = r32.getContext()
            android.content.res.AssetManager r18 = r28.getAssets()
            java.lang.String r28 = "fonts/times.ttf"
            r0 = r18
            r1 = r28
            android.graphics.Typeface r25 = android.graphics.Typeface.createFromAsset(r0, r1)
            java.lang.String r28 = "normal"
            r0 = r28
            boolean r28 = r13.equals(r0)
            if (r28 == 0) goto L_0x0206
            r28 = 0
            r0 = r32
            r1 = r25
            r2 = r28
            r0.setTypeface(r1, r2)
        L_0x004f:
            r26 = 0
            r15 = 0
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            r29 = 1
            r28.setAntiAlias(r29)
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            java.lang.String r29 = "H"
            r28.measureText(r29)
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity     // Catch:{ Exception -> 0x0258 }
            r28 = r0
            java.lang.String r23 = r28.getTextColor()     // Catch:{ Exception -> 0x0258 }
            java.lang.String r23 = java.net.URLDecoder.decode(r23)     // Catch:{ Exception -> 0x0258 }
            java.lang.String r28 = ";"
            r0 = r23
            r1 = r28
            java.lang.String[] r3 = r0.split(r1)     // Catch:{ Exception -> 0x0258 }
            r28 = 0
            r28 = r3[r28]     // Catch:{ Exception -> 0x0258 }
            java.lang.Integer r28 = java.lang.Integer.valueOf(r28)     // Catch:{ Exception -> 0x0258 }
            int r28 = r28.intValue()     // Catch:{ Exception -> 0x0258 }
            r29 = 1
            r29 = r3[r29]     // Catch:{ Exception -> 0x0258 }
            java.lang.Integer r29 = java.lang.Integer.valueOf(r29)     // Catch:{ Exception -> 0x0258 }
            int r29 = r29.intValue()     // Catch:{ Exception -> 0x0258 }
            r30 = 2
            r30 = r3[r30]     // Catch:{ Exception -> 0x0258 }
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)     // Catch:{ Exception -> 0x0258 }
            int r30 = r30.intValue()     // Catch:{ Exception -> 0x0258 }
            int r7 = android.graphics.Color.rgb(r28, r29, r30)     // Catch:{ Exception -> 0x0258 }
            if (r23 == 0) goto L_0x00b6
            java.lang.String r28 = ""
            r0 = r23
            r1 = r28
            boolean r28 = r0.equals(r1)     // Catch:{ Exception -> 0x0258 }
            if (r28 == 0) goto L_0x024b
        L_0x00b6:
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint     // Catch:{ Exception -> 0x0258 }
            r28 = r0
            r29 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r28.setColor(r29)     // Catch:{ Exception -> 0x0258 }
        L_0x00c1:
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            r0 = r28
            r1 = r25
            r0.setTypeface(r1)
            android.content.Context r5 = r32.getContext()
            if (r5 != 0) goto L_0x0266
            android.content.res.Resources r19 = android.content.res.Resources.getSystem()
        L_0x00d8:
            float r33 = com.p000hl.android.core.utils.ScreenUtils.getHorScreenValue(r33)
            r28 = 2
            android.util.DisplayMetrics r29 = r19.getDisplayMetrics()
            r0 = r28
            r1 = r33
            r2 = r29
            float r10 = android.util.TypedValue.applyDimension(r0, r1, r2)
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            r0 = r28
            r0.setTextSize(r10)
            android.view.ViewGroup$LayoutParams r28 = r32.getLayoutParams()
            r0 = r28
            int r0 = r0.width
            r17 = r0
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            android.graphics.Paint$FontMetrics r11 = r28.getFontMetrics()
            float r4 = r11.leading
            float r0 = r11.bottom
            r28 = r0
            float r0 = r11.top
            r29 = r0
            float r28 = r28 - r29
            r0 = r28
            double r0 = (double) r0
            r28 = r0
            double r28 = java.lang.Math.ceil(r28)
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r30 = r0
            java.lang.String r30 = r30.getLineHeight()
            java.lang.Float r30 = java.lang.Float.valueOf(r30)
            float r30 = r30.floatValue()
            float r30 = r30 * r4
            r0 = r30
            double r0 = (double) r0
            r30 = r0
            double r28 = r28 + r30
            r0 = r28
            float r0 = (float) r0
            r28 = r0
            r0 = r28
            r1 = r32
            r1.m_iFontHeight = r0
            r28 = 0
            r0 = r28
            r1 = r32
            r1.f53x = r0
            float r0 = r11.descent
            r28 = r0
            float r0 = r11.ascent
            r29 = r0
            float r28 = r28 - r29
            r0 = r28
            r1 = r32
            r1.f54y = r0
            r16 = 0
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r24 = r28.getTotalParaTextContent()
            r21 = r24
            java.lang.String r21 = java.net.URLDecoder.decode(r24)     // Catch:{ Exception -> 0x026c }
        L_0x0170:
            r28 = 0
            r0 = r28
            r1 = r32
            r1.m_iRealLine = r0
            r14 = 0
        L_0x0179:
            int r28 = r21.length()
            r0 = r28
            if (r14 >= r0) goto L_0x0357
            r0 = r21
            char r6 = r0.charAt(r14)
            r28 = 1
            r0 = r28
            float[] r0 = new float[r0]
            r27 = r0
            java.lang.String r20 = java.lang.String.valueOf(r6)
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            r0 = r28
            r1 = r20
            r2 = r27
            r0.getTextWidths(r1, r2)
            r28 = 10
            r0 = r28
            if (r6 != r0) goto L_0x0289
            r0 = r32
            int r0 = r0.m_iRealLine
            r28 = r0
            int r28 = r28 + 1
            r0 = r28
            r1 = r32
            r1.m_iRealLine = r0
            r0 = r32
            java.util.Vector<java.lang.String> r0 = r0.m_String
            r28 = r0
            r0 = r21
            java.lang.String r29 = r0.substring(r15, r14)
            r28.addElement(r29)
            int r15 = r14 + 1
            r26 = 0
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r22 = r28.getTextAlign()
            boolean r28 = com.p000hl.android.core.utils.StringUtils.isEmpty(r22)
            if (r28 != 0) goto L_0x0276
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r28 = r28.getTextAlign()
            java.lang.String r22 = java.net.URLDecoder.decode(r28)
            java.lang.String r28 = "center"
            r0 = r28
            r1 = r22
            boolean r28 = r0.equals(r1)
            if (r28 == 0) goto L_0x0276
            r0 = r32
            java.util.Vector<java.lang.Float> r0 = r0.m_LineWidth
            r28 = r0
            java.lang.Float r29 = java.lang.Float.valueOf(r16)
            r28.add(r29)
            r16 = 0
        L_0x0202:
            int r14 = r14 + 1
            goto L_0x0179
        L_0x0206:
            java.lang.String r28 = "bold"
            r0 = r28
            boolean r28 = r13.equals(r0)
            if (r28 == 0) goto L_0x004f
            r28 = 1
            r0 = r32
            r1 = r25
            r2 = r28
            r0.setTypeface(r1, r2)
            goto L_0x004f
        L_0x021d:
            java.lang.String r28 = "normal"
            r0 = r28
            boolean r28 = r13.equals(r0)
            if (r28 == 0) goto L_0x0238
            r28 = 0
            r0 = r28
            android.graphics.Typeface r25 = android.graphics.Typeface.create(r12, r0)
        L_0x022f:
            r0 = r32
            r1 = r25
            r0.setTypeface(r1)
            goto L_0x004f
        L_0x0238:
            java.lang.String r28 = "bold"
            r0 = r28
            boolean r28 = r13.equals(r0)
            if (r28 == 0) goto L_0x022f
            r28 = 1
            r0 = r28
            android.graphics.Typeface r25 = android.graphics.Typeface.create(r12, r0)
            goto L_0x022f
        L_0x024b:
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint     // Catch:{ Exception -> 0x0258 }
            r28 = r0
            r0 = r28
            r0.setColor(r7)     // Catch:{ Exception -> 0x0258 }
            goto L_0x00c1
        L_0x0258:
            r9 = move-exception
            r0 = r32
            android.text.TextPaint r0 = r0.mPaint
            r28 = r0
            r29 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r28.setColor(r29)
            goto L_0x00c1
        L_0x0266:
            android.content.res.Resources r19 = r5.getResources()
            goto L_0x00d8
        L_0x026c:
            r8 = move-exception
            java.lang.String r28 = "hl"
            java.lang.String r29 = "解码字符内容error"
            android.util.Log.e(r28, r29)
            goto L_0x0170
        L_0x0276:
            r16 = -1082130432(0xffffffffbf800000, float:-1.0)
            r0 = r32
            java.util.Vector<java.lang.Float> r0 = r0.m_LineWidth
            r28 = r0
            java.lang.Float r29 = java.lang.Float.valueOf(r16)
            r28.add(r29)
            r16 = 0
            goto L_0x0202
        L_0x0289:
            r28 = 0
            r28 = r27[r28]
            r0 = r28
            double r0 = (double) r0
            r28 = r0
            double r28 = java.lang.Math.ceil(r28)
            r0 = r28
            int r0 = (int) r0
            r28 = r0
            int r26 = r26 + r28
            r0 = r26
            r1 = r17
            if (r0 <= r1) goto L_0x02d6
            r0 = r32
            int r0 = r0.m_iRealLine
            r28 = r0
            int r28 = r28 + 1
            r0 = r28
            r1 = r32
            r1.m_iRealLine = r0
            r0 = r32
            java.util.Vector<java.lang.String> r0 = r0.m_String
            r28 = r0
            r0 = r21
            java.lang.String r29 = r0.substring(r15, r14)
            r28.addElement(r29)
            r15 = r14
            int r14 = r14 + -1
            r26 = 0
            r0 = r32
            java.util.Vector<java.lang.Float> r0 = r0.m_LineWidth
            r28 = r0
            java.lang.Float r29 = java.lang.Float.valueOf(r16)
            r28.add(r29)
            r16 = 0
            goto L_0x0202
        L_0x02d6:
            r28 = 0
            r28 = r27[r28]
            float r16 = r16 + r28
            int r28 = r21.length()
            int r28 = r28 + -1
            r0 = r28
            if (r14 != r0) goto L_0x0202
            r0 = r32
            int r0 = r0.m_iRealLine
            r28 = r0
            int r28 = r28 + 1
            r0 = r28
            r1 = r32
            r1.m_iRealLine = r0
            r0 = r32
            java.util.Vector<java.lang.String> r0 = r0.m_String
            r28 = r0
            int r29 = r21.length()
            r0 = r21
            r1 = r29
            java.lang.String r29 = r0.substring(r15, r1)
            r28.addElement(r29)
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r22 = r28.getTextAlign()
            boolean r28 = com.p000hl.android.core.utils.StringUtils.isEmpty(r22)
            if (r28 != 0) goto L_0x0344
            r0 = r32
            com.hl.android.book.entity.TextComponentEntity r0 = r0.mEntity
            r28 = r0
            java.lang.String r28 = r28.getTextAlign()
            java.lang.String r22 = java.net.URLDecoder.decode(r28)
            java.lang.String r28 = "center"
            r0 = r28
            r1 = r22
            boolean r28 = r0.equals(r1)
            if (r28 == 0) goto L_0x0344
            r0 = r32
            java.util.Vector<java.lang.Float> r0 = r0.m_LineWidth
            r28 = r0
            java.lang.Float r29 = java.lang.Float.valueOf(r16)
            r28.add(r29)
            r16 = 0
            goto L_0x0202
        L_0x0344:
            r16 = -1082130432(0xffffffffbf800000, float:-1.0)
            r0 = r32
            java.util.Vector<java.lang.Float> r0 = r0.m_LineWidth
            r28 = r0
            java.lang.Float r29 = java.lang.Float.valueOf(r16)
            r28.add(r29)
            r16 = 0
            goto L_0x0202
        L_0x0357:
            r0 = r32
            float r0 = r0.m_iFontHeight
            r28 = r0
            r0 = r32
            int r0 = r0.m_iRealLine
            r29 = r0
            r0 = r29
            float r0 = (float) r0
            r29 = r0
            float r28 = r28 * r29
            r0 = r28
            int r0 = (int) r0
            r28 = r0
            r0 = r32
            r1 = r28
            r0.setTextHeight(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.textview.TextViewComponent.drawView(float):void");
    }

    public ComponentEntity getEntity() {
        return this.mEntity;
    }

    public void setEntity(ComponentEntity entity) {
        this.mEntity = (TextComponentEntity) entity;
    }

    public void load() {
    }

    private void setBGAlphaAndColor(int alpha, String color) {
        String[] a = URLDecoder.decode(color).split(";");
        setBackgroundColor(Color.argb(alpha, Integer.valueOf(a[0]).intValue(), Integer.valueOf(a[1]).intValue(), Integer.valueOf(a[2]).intValue()));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        String[] a = this.mEntity.getBorderColor().split(";");
        this.paint.setColor(Color.argb(255, Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])));
        canvas.drawLine(0.0f, 0.0f, (float) (getWidth() - 1), 0.0f, this.paint);
        canvas.drawLine(0.0f, 0.0f, 0.0f, (float) (getHeight() - 1), this.paint);
        canvas.drawLine((float) (getWidth() - 1), 0.0f, (float) (getWidth() - 1), (float) (getHeight() - 1), this.paint);
        canvas.drawLine(0.0f, (float) (getHeight() - 1), (float) (getWidth() - 1), (float) (getHeight() - 1), this.paint);
        drawText(canvas);
    }

    public void setFontSize(String fontSize) {
        this.mPaint.reset();
        this.m_String.clear();
        this.m_LineWidth.clear();
        drawView(Float.valueOf(URLDecoder.decode(fontSize)).floatValue());
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    /* access modifiers changed from: protected */
    public void drawText(Canvas canvas) {
        int i = 0;
        int j = 0;
        while (i < this.m_iRealLine) {
            if (!StringUtils.isEmpty(this.mEntity.getTextAlign())) {
                if ("center".equals(URLDecoder.decode(this.mEntity.getTextAlign()))) {
                    canvas.drawText((String) this.m_String.elementAt(i), (((float) getLayoutParams().width) - ((Float) this.m_LineWidth.elementAt(i)).floatValue()) / 2.0f, this.f54y + (this.m_iFontHeight * ((float) j)), this.mPaint);
                    i++;
                    j++;
                }
            }
            if (((Float) this.m_LineWidth.elementAt(i)).floatValue() < 0.0f) {
                canvas.drawText((String) this.m_String.elementAt(i), this.f53x, this.f54y + (this.m_iFontHeight * ((float) j)), this.mPaint);
            } else {
                drawLine((String) this.m_String.elementAt(i), this.f53x, (this.m_iFontHeight * ((float) j)) + this.f54y, canvas, i);
            }
            i++;
            j++;
        }
    }

    private void drawLine(String line, float x, float y, Canvas canvas, int rowNum) {
        float a = (((float) getLayoutParams().width) - ((Float) this.m_LineWidth.elementAt(rowNum)).floatValue()) / ((float) line.length());
        String str = "";
        float lineWidth = 0.0f;
        for (int i = 0; i < line.length(); i++) {
            String s = line.substring(i, i + 1);
            float[] widths = new float[1];
            this.mPaint.getTextWidths(s, widths);
            if (i == 0) {
                canvas.drawText(s, x, y, this.mPaint);
            } else {
                canvas.drawText(s, x + lineWidth + a, y, this.mPaint);
            }
            lineWidth = widths[0] + lineWidth + a;
        }
    }

    private void drawLineEN(String line, float x, float y, Canvas canvas, int rowNum) {
        float a = ((float) getLayoutParams().width) - measureLineENWidth(line);
        float[] widths = new float[1];
        this.mPaint.getTextWidths(" ", widths);
        String ssss = getSpace((int) (a / widths[0]));
        String[] aS = line.split(" ");
        String lineNew = "";
        int lineSpaceCount = ssss.length();
        if (aS.length > 1) {
            int aaaa = lineSpaceCount / (aS.length - 1);
            for (int i = 0; i < aS.length; i++) {
                lineNew = lineNew + aS[i];
                if (i != aS.length - 1) {
                    lineNew = lineNew + getSpace(aaaa);
                }
            }
            canvas.drawText(lineNew, x, y, this.mPaint);
            return;
        }
        canvas.drawText(line, x, y, this.mPaint);
    }

    private String getSpace(int aaa) {
        String aa = "";
        for (int i = 0; i < aaa; i++) {
            aa = aa + " ";
        }
        return aa;
    }

    private float measureLineENWidth(String line) {
        String str = "";
        float lineWidth = 0.0f;
        String line2 = line.replaceAll(" ", "");
        for (int i = 0; i < line2.length(); i++) {
            float[] widths = new float[1];
            this.mPaint.getTextWidths(line2.substring(i, i + 1), widths);
            lineWidth += widths[0];
        }
        return lineWidth;
    }

    private boolean isAllCNAndEN(String line) {
        if (line.length() < line.getBytes().length) {
            return true;
        }
        return false;
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }

    public int getTextHeight() {
        return this.textHeight;
    }

    public void setTextHeight(int textHeight2) {
        this.textHeight = textHeight2;
    }
}
