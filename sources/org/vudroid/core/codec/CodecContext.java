package org.vudroid.core.codec;

import android.content.ContentResolver;

public interface CodecContext {
    CodecDocument openDocument(String str);

    void recycle();

    void setContentResolver(ContentResolver contentResolver);
}
