package org.apache.http.impl.p001io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.p002io.SessionOutputBuffer;

@Deprecated
/* renamed from: org.apache.http.impl.io.ContentLengthOutputStream */
public class ContentLengthOutputStream extends OutputStream {
    public ContentLengthOutputStream(SessionOutputBuffer out, long contentLength) {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] b, int off, int len) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] b) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(int b) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
