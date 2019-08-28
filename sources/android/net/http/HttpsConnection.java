package android.net.http;

import java.io.File;

public class HttpsConnection extends Connection {
    protected SslCertificate mCertificate;
    protected AndroidHttpClientConnection mHttpClientConnection;

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    HttpsConnection() {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }

    public static void initializeEngine(File sessionDir) {
        throw new RuntimeException("Stub!");
    }
}
