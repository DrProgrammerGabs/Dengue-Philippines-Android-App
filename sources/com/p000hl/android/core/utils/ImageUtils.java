package com.p000hl.android.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import java.util.Collection;

/* renamed from: com.hl.android.core.utils.ImageUtils */
public class ImageUtils {
    public static void setViewBackground(View v, int id) {
        try {
            v.setBackgroundResource(id);
        } catch (Exception e) {
            try {
                System.gc();
            } catch (Error e2) {
            }
        }
    }

    public static StateListDrawable getButtonDrawable(String btnResource, String btnSelectResource, Context context) {
        StateListDrawable btnDrawable = new StateListDrawable();
        BitmapDrawable dbg = BitmapUtils.getBitmapDrawable(context, btnResource);
        int[] iArr = {16842919};
        btnDrawable.addState(iArr, BitmapUtils.getBitmapDrawable(context, btnSelectResource));
        btnDrawable.addState(new int[]{16842908}, dbg);
        btnDrawable.addState(new int[]{16842910}, dbg);
        btnDrawable.addState(new int[0], dbg);
        return btnDrawable;
    }

    public static void recyleBitmapList(Collection<Bitmap> bitmaps) {
        for (Bitmap b : bitmaps) {
            b.recycle();
        }
        bitmaps.clear();
    }
}
