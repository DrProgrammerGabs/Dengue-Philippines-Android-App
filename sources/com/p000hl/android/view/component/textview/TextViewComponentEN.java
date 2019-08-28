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
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Vector;

/* renamed from: com.hl.android.view.component.textview.TextViewComponentEN */
public class TextViewComponentEN extends TextView implements Component {
    static final float COMPLEXITY = 0.0f;
    static final String SYSTEM_NEWLINE = "\n";
    ArrayList<Integer> alLast = new ArrayList<>();
    public AnimationSet animationset = null;
    public ComponentEntity entity = null;
    private TextPaint mPaint = null;
    Vector<Float> m_LineWidth = new Vector<>();
    Vector<String> m_String = new Vector<>();
    float m_iFontHeight;
    int m_iRealLine = 0;
    Paint paint = new Paint();
    private int textHeight;

    /* renamed from: x */
    float f55x = COMPLEXITY;

    /* renamed from: y */
    float f56y = COMPLEXITY;

    public TextViewComponentEN(Context context) {
        super(context);
    }

    public TextViewComponentEN(Context context, ComponentEntity entity2) {
        super(context);
        setEntity(entity2);
        TextComponentEntity en = (TextComponentEntity) this.entity;
        setBGAlphaAndColor((int) (Float.valueOf(en.getBgalpha()).floatValue() * 255.0f), en.getBgcolor());
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x010d  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0215  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadEnglishText() {
        /*
            r24 = this;
            r0 = r24
            com.hl.android.book.entity.ComponentEntity r7 = r0.entity
            com.hl.android.book.entity.TextComponentEntity r7 = (com.p000hl.android.book.entity.TextComponentEntity) r7
            java.lang.String r18 = r7.getTotalParaTextContent()
            java.lang.String r16 = java.net.URLDecoder.decode(r18)
            java.lang.String r20 = "hl"
            java.lang.StringBuilder r21 = new java.lang.StringBuilder
            r21.<init>()
            java.lang.String r22 = "en text is  |||||       "
            java.lang.StringBuilder r21 = r21.append(r22)
            r0 = r21
            r1 = r16
            java.lang.StringBuilder r21 = r0.append(r1)
            java.lang.String r21 = r21.toString()
            android.util.Log.d(r20, r21)
            java.lang.String r20 = r7.getFontFamily()
            java.lang.String r11 = java.net.URLDecoder.decode(r20)
            java.lang.String r20 = r7.getFontWeight()
            java.lang.String r13 = java.net.URLDecoder.decode(r20)
            r19 = 0
            java.lang.String r20 = "Times New Roman"
            r0 = r20
            boolean r20 = r11.equals(r0)
            if (r20 == 0) goto L_0x01cc
            android.content.Context r20 = r24.getContext()
            android.content.res.AssetManager r14 = r20.getAssets()
            java.lang.String r20 = "fonts/times.ttf"
            r0 = r20
            android.graphics.Typeface r19 = android.graphics.Typeface.createFromAsset(r14, r0)
            java.lang.String r20 = "normal"
            r0 = r20
            boolean r20 = r13.equals(r0)
            if (r20 == 0) goto L_0x01b5
            r20 = 0
            r0 = r24
            r1 = r19
            r2 = r20
            r0.setTypeface(r1, r2)
        L_0x006b:
            android.text.TextPaint r20 = new android.text.TextPaint
            r20.<init>()
            r0 = r20
            r1 = r24
            r1.mPaint = r0
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            r21 = 1
            r20.setAntiAlias(r21)
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            r21 = 1
            r20.setDither(r21)
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            java.lang.String r21 = "H"
            r20.measureText(r21)
            java.lang.String r17 = r7.getTextColor()     // Catch:{ Exception -> 0x0207 }
            java.lang.String r17 = java.net.URLDecoder.decode(r17)     // Catch:{ Exception -> 0x0207 }
            java.lang.String r20 = ";"
            r0 = r17
            r1 = r20
            java.lang.String[] r3 = r0.split(r1)     // Catch:{ Exception -> 0x0207 }
            r20 = 0
            r20 = r3[r20]     // Catch:{ Exception -> 0x0207 }
            java.lang.Integer r20 = java.lang.Integer.valueOf(r20)     // Catch:{ Exception -> 0x0207 }
            int r20 = r20.intValue()     // Catch:{ Exception -> 0x0207 }
            r21 = 1
            r21 = r3[r21]     // Catch:{ Exception -> 0x0207 }
            java.lang.Integer r21 = java.lang.Integer.valueOf(r21)     // Catch:{ Exception -> 0x0207 }
            int r21 = r21.intValue()     // Catch:{ Exception -> 0x0207 }
            r22 = 2
            r22 = r3[r22]     // Catch:{ Exception -> 0x0207 }
            java.lang.Integer r22 = java.lang.Integer.valueOf(r22)     // Catch:{ Exception -> 0x0207 }
            int r22 = r22.intValue()     // Catch:{ Exception -> 0x0207 }
            int r6 = android.graphics.Color.rgb(r20, r21, r22)     // Catch:{ Exception -> 0x0207 }
            if (r17 == 0) goto L_0x00df
            java.lang.String r20 = ""
            r0 = r17
            r1 = r20
            boolean r20 = r0.equals(r1)     // Catch:{ Exception -> 0x0207 }
            if (r20 == 0) goto L_0x01fa
        L_0x00df:
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint     // Catch:{ Exception -> 0x0207 }
            r20 = r0
            r21 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r20.setColor(r21)     // Catch:{ Exception -> 0x0207 }
        L_0x00ea:
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            r0 = r20
            r1 = r19
            r0.setTypeface(r1)
            java.lang.String r20 = r7.getFontSize()
            java.lang.String r20 = java.net.URLDecoder.decode(r20)
            java.lang.Float r20 = java.lang.Float.valueOf(r20)
            float r12 = r20.floatValue()
            android.content.Context r5 = r24.getContext()
            if (r5 != 0) goto L_0x0215
            android.content.res.Resources r15 = android.content.res.Resources.getSystem()
        L_0x0111:
            float r12 = com.p000hl.android.core.utils.ScreenUtils.getHorScreenValue(r12)
            r20 = 2
            android.util.DisplayMetrics r21 = r15.getDisplayMetrics()
            r0 = r20
            r1 = r21
            float r9 = android.util.TypedValue.applyDimension(r0, r12, r1)
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            r0 = r20
            r0.setTextSize(r9)
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            android.graphics.Paint$FontMetrics r10 = r20.getFontMetrics()
            float r4 = r10.leading
            float r0 = r10.bottom
            r20 = r0
            float r0 = r10.top
            r21 = r0
            float r20 = r20 - r21
            r0 = r20
            double r0 = (double) r0
            r20 = r0
            double r20 = java.lang.Math.ceil(r20)
            java.lang.String r22 = r7.getLineHeight()
            java.lang.Float r22 = java.lang.Float.valueOf(r22)
            float r22 = r22.floatValue()
            float r22 = r22 * r4
            r0 = r22
            double r0 = (double) r0
            r22 = r0
            double r20 = r20 + r22
            r0 = r20
            float r0 = (float) r0
            r20 = r0
            r0 = r20
            r1 = r24
            r1.m_iFontHeight = r0
            float r0 = r10.descent
            r20 = r0
            float r0 = r10.ascent
            r21 = r0
            float r20 = r20 - r21
            r0 = r20
            r1 = r24
            r1.f56y = r0
            android.view.ViewGroup$LayoutParams r20 = r24.getLayoutParams()
            r0 = r20
            int r0 = r0.width
            r20 = r0
            r0 = r20
            float r0 = (float) r0
            r20 = r0
            r0 = r24
            r1 = r16
            r2 = r20
            r0.justifyText(r1, r2)
            r0 = r24
            float r0 = r0.m_iFontHeight
            r20 = r0
            r0 = r24
            int r0 = r0.m_iRealLine
            r21 = r0
            r0 = r21
            float r0 = (float) r0
            r21 = r0
            float r20 = r20 * r21
            r0 = r20
            int r0 = (int) r0
            r20 = r0
            r0 = r24
            r1 = r20
            r0.setTextHeight(r1)
            return
        L_0x01b5:
            java.lang.String r20 = "bold"
            r0 = r20
            boolean r20 = r13.equals(r0)
            if (r20 == 0) goto L_0x006b
            r20 = 1
            r0 = r24
            r1 = r19
            r2 = r20
            r0.setTypeface(r1, r2)
            goto L_0x006b
        L_0x01cc:
            java.lang.String r20 = "normal"
            r0 = r20
            boolean r20 = r13.equals(r0)
            if (r20 == 0) goto L_0x01e7
            r20 = 0
            r0 = r20
            android.graphics.Typeface r19 = android.graphics.Typeface.create(r11, r0)
        L_0x01de:
            r0 = r24
            r1 = r19
            r0.setTypeface(r1)
            goto L_0x006b
        L_0x01e7:
            java.lang.String r20 = "bold"
            r0 = r20
            boolean r20 = r13.equals(r0)
            if (r20 == 0) goto L_0x01de
            r20 = 1
            r0 = r20
            android.graphics.Typeface r19 = android.graphics.Typeface.create(r11, r0)
            goto L_0x01de
        L_0x01fa:
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint     // Catch:{ Exception -> 0x0207 }
            r20 = r0
            r0 = r20
            r0.setColor(r6)     // Catch:{ Exception -> 0x0207 }
            goto L_0x00ea
        L_0x0207:
            r8 = move-exception
            r0 = r24
            android.text.TextPaint r0 = r0.mPaint
            r20 = r0
            r21 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r20.setColor(r21)
            goto L_0x00ea
        L_0x0215:
            android.content.res.Resources r15 = r5.getResources()
            goto L_0x0111
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.textview.TextViewComponentEN.loadEnglishText():void");
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
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
        String[] a = ((TextComponentEntity) this.entity).getBorderColor().split(";");
        this.paint.setColor(Color.argb(255, Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])));
        canvas.drawLine(COMPLEXITY, COMPLEXITY, (float) (getWidth() - 1), COMPLEXITY, this.paint);
        canvas.drawLine(COMPLEXITY, COMPLEXITY, COMPLEXITY, (float) (getHeight() - 1), this.paint);
        canvas.drawLine((float) (getWidth() - 1), COMPLEXITY, (float) (getWidth() - 1), (float) (getHeight() - 1), this.paint);
        canvas.drawLine(COMPLEXITY, (float) (getHeight() - 1), (float) (getWidth() - 1), (float) (getHeight() - 1), this.paint);
        drawText(canvas);
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
            drawLineEN((String) this.m_String.elementAt(i), this.f55x, (this.m_iFontHeight * ((float) j)) + this.f56y, canvas, i);
            i++;
            j++;
        }
    }

    private void drawLineEN(String line, float x, float y, Canvas canvas, int rowNum) {
        canvas.drawText(line, x, y, this.mPaint);
    }

    private boolean isAllCNAndEN(String line) {
        if (line.length() < line.getBytes().length) {
            return true;
        }
        return false;
    }

    private static String wrap(String s, float width, Paint p) {
        String[] str = s.split("\\s");
        StringBuilder smb = new StringBuilder();
        smb.append(SYSTEM_NEWLINE);
        for (int x = 0; x < str.length; x++) {
            float length = p.measureText(str[x]);
            String[] pieces = smb.toString().split(SYSTEM_NEWLINE);
            try {
                if (p.measureText(pieces[pieces.length - 1]) + length > width) {
                    smb.append(SYSTEM_NEWLINE);
                }
            } catch (Exception e) {
            }
            smb.append(str[x] + " ");
        }
        return smb.toString().replaceFirst(SYSTEM_NEWLINE, "");
    }

    private static String removeLast(String s, String g) {
        if (!s.contains(g)) {
            return s;
        }
        int index = s.lastIndexOf(g);
        int indexEnd = index + g.length();
        if (index == 0) {
            return s.substring(1);
        }
        if (index == s.length() - 1) {
            return s.substring(0, index);
        }
        return s.substring(0, index) + s.substring(indexEnd);
    }

    public void justifyText(String text, float origWidth) {
        String[] splits = text.split(SYSTEM_NEWLINE);
        float width = origWidth - 5.0f;
        for (int x = 0; x < splits.length; x++) {
            if (this.mPaint.measureText(splits[x]) > width) {
                splits[x] = wrap(splits[x], width, this.mPaint);
                String[] microSplits = splits[x].split(SYSTEM_NEWLINE);
                for (int y = 0; y < microSplits.length - 1; y++) {
                    microSplits[y] = justify(removeLast(microSplits[y], " "), width, this.mPaint);
                    this.m_String.add(microSplits[y]);
                    this.m_iRealLine++;
                }
                if (microSplits.length > 2) {
                    this.m_String.add(microSplits[microSplits.length - 1]);
                    this.m_iRealLine++;
                }
            } else {
                this.m_String.add(splits[x]);
                this.m_iRealLine++;
            }
        }
        StringBuilder smb = new StringBuilder();
        String[] arr$ = splits;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            smb.append(arr$[i$] + SYSTEM_NEWLINE);
        }
        setGravity(3);
    }

    private String justifyOperation(String s, float width, Paint p) {
        float holder;
        double random = Math.random();
        while (true) {
            holder = (float) (random * 0.0d);
            if (!s.contains(Float.toString(holder))) {
                break;
            }
            random = Math.random();
        }
        String holder_string = Float.toString(holder);
        float lessThan = width;
        int current = 0;
        while (p.measureText(s) < lessThan && current < 100) {
            s = s.replaceFirst(" ([^" + holder_string + "])", " " + holder_string + "$1");
            lessThan = (p.measureText(holder_string) + lessThan) - p.measureText(" ");
            current++;
        }
        return s.replaceAll(holder_string, " ");
    }

    private String justify(String s, float width, Paint p) {
        while (p.measureText(s) < width) {
            if (!s.contains(" ")) {
                s = " " + s;
            }
            s = justifyOperation(s, width, p);
        }
        return s;
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
