package com.p000hl.android.core.utils;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/* renamed from: com.hl.android.core.utils.BitmapManager */
public class BitmapManager {
    public static HashMap<String, SoftReference<Bitmap>> softMap = new HashMap<>();

    public static Bitmap getBitmapFromCache(String key) {
        Bitmap bitmap;
        synchronized (softMap) {
            SoftReference<Bitmap> softBitmap = (SoftReference) softMap.get(key);
            if (softBitmap != null) {
                bitmap = (Bitmap) softBitmap.get();
                if (bitmap == null) {
                    softMap.remove(key);
                }
            } else {
                bitmap = null;
            }
        }
        return bitmap;
    }

    public static void putBitmapCache(String key, Bitmap bitmap) {
        SoftReference<Bitmap> softBitmap = new SoftReference<>(bitmap);
        synchronized (softMap) {
            softMap.put(key, softBitmap);
        }
    }
}
