package com.mediav.ads.sdk.adcore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.methods.HttpGet;

public class HttpRequester {
    /* access modifiers changed from: private */
    public static Handler handler = null;
    public static Boolean isOpenLog = Boolean.valueOf(true);

    public interface Listener {
        void onGetDataFailed(String str);

        void onGetDataSucceed(byte[] bArr);
    }

    public static void getAsynData(Context context, String url, Boolean isUsecache, Listener listener) {
        if (handler == null) {
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    HttpRequester.handler.post(new ResultRunable(msg));
                }
            };
        }
        new Thread(new HttpRunable(url, handler, listener, context, isUsecache)).start();
    }

    public static byte[] getSyncData(Context context, String urlString, Boolean isUsecache) {
        HttpCacher httpCacher = HttpCacher.get(context);
        byte[] cache = httpCacher.getAsBinary(urlString);
        if (cache == null || !isUsecache.booleanValue()) {
            if (isUsecache.booleanValue()) {
                if (isOpenLog.booleanValue()) {
                    CLog.m2d("同步:缓存未命中");
                }
            } else if (isOpenLog.booleanValue()) {
                CLog.m2d("同步:不使用缓存");
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
                conn.setConnectTimeout(1000);
                conn.setUseCaches(false);
                conn.setRequestMethod(HttpGet.METHOD_NAME);
                conn.setDoInput(true);
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] b = getBytes(is);
                    bis.close();
                    is.close();
                    conn.disconnect();
                    byte[] data = b;
                    if (!isUsecache.booleanValue()) {
                        return data;
                    }
                    httpCacher.put(urlString, b, (int) HttpCacher.TIME_DAY);
                    return data;
                }
                conn.disconnect();
                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            if (isOpenLog.booleanValue()) {
                CLog.m2d("同步:缓存命中");
            }
            return cache;
        }
    }

    private static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        while (true) {
            int len = is.read(b, 0, 1024);
            if (len == -1) {
                return baos.toByteArray();
            }
            baos.write(b, 0, len);
            baos.flush();
        }
    }
}
