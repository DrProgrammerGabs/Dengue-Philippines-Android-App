package com.p000hl.android.book.entity;

import android.content.Context;
import android.graphics.Bitmap;
import com.p000hl.android.core.utils.BitmapManager;
import com.p000hl.android.core.utils.BitmapUtils;

/* renamed from: com.hl.android.book.entity.SubImageItem */
public class SubImageItem {
    public String aniProperty;
    public String aniType;
    private Bitmap bitmap = null;
    public long delay;
    public long duration;
    public int mIndex = -1;
    public String sourceID;

    public Bitmap getBitmap(Context context) {
        if (this.bitmap == null) {
            changeSourceID2Bitmap(context);
        }
        return this.bitmap;
    }

    public void changeSourceID2Bitmap(Context context) {
        this.bitmap = BitmapManager.getBitmapFromCache(this.sourceID);
        if (this.bitmap == null) {
            this.bitmap = BitmapUtils.getBitMap(this.sourceID, context);
            BitmapManager.putBitmapCache(this.sourceID, this.bitmap);
        }
    }
}
