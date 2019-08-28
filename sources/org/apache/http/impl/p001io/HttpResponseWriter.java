package org.apache.http.impl.p001io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.message.LineFormatter;
import org.apache.http.p002io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;

@Deprecated
/* renamed from: org.apache.http.impl.io.HttpResponseWriter */
public class HttpResponseWriter extends AbstractMessageWriter {
    public HttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void writeHeadLine(HttpMessage message) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
