package org.vudroid.core.codec;

public interface CodecDocument {
    CodecPage getPage(int i);

    int getPageCount();

    void recycle();
}
