package org.apache.http.p002io;

@Deprecated
/* renamed from: org.apache.http.io.HttpTransportMetrics */
public interface HttpTransportMetrics {
    long getBytesTransferred();

    void reset();
}
