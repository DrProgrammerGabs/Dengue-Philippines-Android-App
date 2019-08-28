package org.apache.http.impl.p001io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.message.LineFormatter;
import org.apache.http.p002io.HttpMessageWriter;
import org.apache.http.p002io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
/* renamed from: org.apache.http.impl.io.AbstractMessageWriter */
public abstract class AbstractMessageWriter implements HttpMessageWriter {
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;
    protected final SessionOutputBuffer sessionBuffer;

    /* access modifiers changed from: protected */
    public abstract void writeHeadLine(HttpMessage httpMessage) throws IOException;

    public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        throw new RuntimeException("Stub!");
    }

    public void write(HttpMessage message) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
}
