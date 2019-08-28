package org.vudroid.core.codec;

import android.graphics.Bitmap;
import android.graphics.RectF;

public interface CodecPage {
    int getHeight();

    int getWidth();

    boolean isDecoding();

    void recycle();

    Bitmap renderBitmap(int i, int i2, RectF rectF);

    void waitForDecode();
}
