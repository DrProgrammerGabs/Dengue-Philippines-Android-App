package org.apache.http.p002io;

import java.io.IOException;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
/* renamed from: org.apache.http.io.SessionOutputBuffer */
public interface SessionOutputBuffer {
    void flush() throws IOException;

    HttpTransportMetrics getMetrics();

    void write(int i) throws IOException;

    void write(byte[] bArr) throws IOException;

    void write(byte[] bArr, int i, int i2) throws IOException;

    void writeLine(String str) throws IOException;

    void writeLine(CharArrayBuffer charArrayBuffer) throws IOException;
}
