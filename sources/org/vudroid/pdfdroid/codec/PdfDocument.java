package org.vudroid.pdfdroid.codec;

import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;

public class PdfDocument implements CodecDocument {
    private static final int FITZMEMORY = 5242880;
    private long docHandle;

    private static native void free(long j);

    private static native int getPageCount(long j);

    private static native long open(int i, String str, String str2);

    private PdfDocument(long docHandle2) {
        this.docHandle = docHandle2;
    }

    public CodecPage getPage(int pageNumber) {
        return PdfPage.createPage(this.docHandle, pageNumber + 1);
    }

    public int getPageCount() {
        return getPageCount(this.docHandle);
    }

    static PdfDocument openDocument(String fname, String pwd) {
        return new PdfDocument(open(FITZMEMORY, fname, pwd));
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    public synchronized void recycle() {
        if (this.docHandle != 0) {
            free(this.docHandle);
            this.docHandle = 0;
        }
    }
}
