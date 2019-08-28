package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.View;

public interface DecodeService {

    public interface DecodeCallback {
        void decodeComplete(Bitmap bitmap, float f);
    }

    void decodePage(Object obj, int i, DecodeCallback decodeCallback);

    void decodePage(Object obj, int i, DecodeCallback decodeCallback, float f, RectF rectF);

    Bitmap getBitmap(int i, int i2, int i3);

    int getEffectivePagesHeight();

    int getEffectivePagesHeight(int i);

    int getEffectivePagesWidth();

    int getEffectivePagesWidth(int i);

    int getPageCount();

    int getPageHeight(int i);

    int getPageWidth(int i);

    void open(String str);

    void recycle();

    void setContainerView(View view);

    void stopDecoding(Object obj);
}
