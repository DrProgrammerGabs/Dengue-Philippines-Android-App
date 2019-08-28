package com.p000hl.android.view.component.moudle.masksliderimag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextPaint;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.moudle.MaskBean;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ScreenUtils;
import com.p000hl.android.core.utils.StringUtils;

/* renamed from: com.hl.android.view.component.moudle.masksliderimag.MaskViewBean */
public class MaskViewBean {
    private boolean isBig = false;
    public Bitmap mBitmap;
    private Context mContext;
    public MaskBean mMaskBean;
    private Bitmap soundBitmap;

    public MaskViewBean(Context context, MaskBean maskBean) {
        this.mMaskBean = maskBean;
        this.mContext = context;
        this.mBitmap = BitmapUtils.getBitMap(this.mMaskBean.imgSource, this.mContext);
        if (!StringUtils.isEmpty(maskBean.audioSourceID)) {
            this.soundBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), C0048R.drawable.sound);
        }
    }

    public void drawMaskView(Canvas canvas, RectF dst, Boolean isPortlet, boolean drawtext) {
        if (isPortlet.booleanValue()) {
            canvas.drawBitmap(this.mBitmap, null, dst, null);
        } else {
            drawBigView(canvas, dst, drawtext);
        }
    }

    private void drawBigView(Canvas canvas, RectF dst, boolean drawtext) {
        TextPaint paint = new TextPaint();
        paint.setTextSize((float) ScreenUtils.dip2px(this.mContext, 20.0f));
        paint.setColor(-1);
        int x = (int) ((dst.width() - paint.measureText(this.mMaskBean.title)) / 2.0f);
        if (drawtext) {
            canvas.drawText(this.mMaskBean.title, (float) x, (float) (ScreenUtils.dip2px(this.mContext, 50.0f) / 2), paint);
        }
        float top = (float) ScreenUtils.dip2px(this.mContext, 60.0f);
        RectF bitmapDst = new RectF(dst.left, top, dst.right, ((float) BookSetting.BOOK_HEIGHT) - top);
        canvas.drawBitmap(this.mBitmap, null, bitmapDst, null);
        int x2 = (int) ((dst.width() - paint.measureText(this.mMaskBean.dec)) / 2.0f);
        if (drawtext) {
            canvas.drawText(this.mMaskBean.dec, (float) x2, dst.bottom - ((float) (ScreenUtils.dip2px(this.mContext, 50.0f) / 2)), paint);
        }
        RectF soundDst = new RectF(bitmapDst.left + 20.0f, bitmapDst.bottom - 80.0f, dst.left + 80.0f, bitmapDst.bottom - 20.0f);
        if (this.soundBitmap != null) {
            canvas.drawBitmap(this.soundBitmap, null, soundDst, null);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0088 A[SYNTHETIC, Splitter:B:37:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008e A[Catch:{ Exception -> 0x0093 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void playMedia(android.media.MediaPlayer r15) {
        /*
            r14 = this;
            r15.reset()
            r6 = 0
            r10 = 0
            com.hl.android.book.entity.moudle.MaskBean r0 = r14.mMaskBean
            java.lang.String r12 = r0.audioSourceID
            boolean r0 = com.p000hl.android.core.utils.StringUtils.isEmpty(r12)
            if (r0 == 0) goto L_0x0010
        L_0x000f:
            return
        L_0x0010:
            boolean r0 = com.p000hl.android.common.HLSetting.IsResourceSD     // Catch:{ Exception -> 0x0055 }
            if (r0 == 0) goto L_0x006a
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r9 = r0.getFilePath(r12)     // Catch:{ Exception -> 0x0055 }
            android.content.Context r0 = r14.mContext     // Catch:{ Exception -> 0x0055 }
            java.io.File r0 = r0.getFilesDir()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r13 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x0055 }
            boolean r0 = r9.contains(r13)     // Catch:{ Exception -> 0x0055 }
            if (r0 == 0) goto L_0x0051
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0055 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0055 }
            r0.<init>(r9)     // Catch:{ Exception -> 0x0055 }
            r11.<init>(r0)     // Catch:{ Exception -> 0x0055 }
            java.io.FileDescriptor r8 = r11.getFD()     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r15.setDataSource(r8)     // Catch:{ Exception -> 0x009f, all -> 0x009c }
            r10 = r11
        L_0x003e:
            r15.prepare()     // Catch:{ Exception -> 0x0055 }
            r15.start()     // Catch:{ Exception -> 0x0055 }
            if (r6 == 0) goto L_0x004a
            r6.close()     // Catch:{ Exception -> 0x0098 }
            r6 = 0
        L_0x004a:
            if (r10 == 0) goto L_0x000f
            r10.close()     // Catch:{ Exception -> 0x009a }
        L_0x004f:
            r10 = 0
            goto L_0x000f
        L_0x0051:
            r15.setDataSource(r9)     // Catch:{ Exception -> 0x0055 }
            goto L_0x003e
        L_0x0055:
            r7 = move-exception
        L_0x0056:
            r7.printStackTrace()     // Catch:{ all -> 0x0085 }
            if (r6 == 0) goto L_0x005f
            r6.close()     // Catch:{ Exception -> 0x0065 }
            r6 = 0
        L_0x005f:
            if (r10 == 0) goto L_0x000f
            r10.close()     // Catch:{ Exception -> 0x0065 }
            goto L_0x004f
        L_0x0065:
            r7 = move-exception
        L_0x0066:
            r7.printStackTrace()
            goto L_0x000f
        L_0x006a:
            com.hl.android.core.utils.FileUtils r0 = com.p000hl.android.core.utils.FileUtils.getInstance()     // Catch:{ Exception -> 0x0055 }
            android.content.Context r1 = r14.mContext     // Catch:{ Exception -> 0x0055 }
            android.content.res.AssetFileDescriptor r6 = r0.getFileFD(r1, r12)     // Catch:{ Exception -> 0x0055 }
            java.io.FileDescriptor r1 = r6.getFileDescriptor()     // Catch:{ Exception -> 0x0055 }
            long r2 = r6.getStartOffset()     // Catch:{ Exception -> 0x0055 }
            long r4 = r6.getLength()     // Catch:{ Exception -> 0x0055 }
            r0 = r15
            r0.setDataSource(r1, r2, r4)     // Catch:{ Exception -> 0x0055 }
            goto L_0x003e
        L_0x0085:
            r0 = move-exception
        L_0x0086:
            if (r6 == 0) goto L_0x008c
            r6.close()     // Catch:{ Exception -> 0x0093 }
            r6 = 0
        L_0x008c:
            if (r10 == 0) goto L_0x0092
            r10.close()     // Catch:{ Exception -> 0x0093 }
            r10 = 0
        L_0x0092:
            throw r0
        L_0x0093:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0092
        L_0x0098:
            r7 = move-exception
            goto L_0x0066
        L_0x009a:
            r7 = move-exception
            goto L_0x0066
        L_0x009c:
            r0 = move-exception
            r10 = r11
            goto L_0x0086
        L_0x009f:
            r7 = move-exception
            r10 = r11
            goto L_0x0056
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.view.component.moudle.masksliderimag.MaskViewBean.playMedia(android.media.MediaPlayer):void");
    }

    public void recyle() {
        BitmapUtils.recycleBitmap(this.mBitmap);
        BitmapUtils.recycleBitmap(this.soundBitmap);
    }
}
