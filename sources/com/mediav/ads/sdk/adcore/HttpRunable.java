package com.mediav.ads.sdk.adcore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.mediav.ads.sdk.adcore.HttpRequester.Listener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.apache.http.client.methods.HttpGet;

/* compiled from: HttpRequester */
class HttpRunable implements Runnable {
    private final int NET_TIMEOUT = 1000;
    private Context context = null;
    private Handler handler = null;
    private Boolean isUsecache = Boolean.valueOf(true);
    private Listener listener = null;
    private String urlString = null;

    public HttpRunable(String url, Handler handler2, Listener listener2, Context context2, Boolean isUsecache2) {
        this.urlString = url;
        this.handler = handler2;
        this.listener = listener2;
        this.context = context2;
        this.isUsecache = isUsecache2;
    }

    public void run() {
        Message msg = new Message();
        HashMap<String, Object> data = new HashMap<>();
        data.put("callback", this.listener);
        HttpCacher httpCacher = HttpCacher.get(this.context);
        byte[] cache = httpCacher.getAsBinary(this.urlString);
        if (cache == null || !this.isUsecache.booleanValue()) {
            if (this.isUsecache.booleanValue()) {
                if (HttpRequester.isOpenLog.booleanValue()) {
                    CLog.m2d("异步:缓存未命中");
                }
            } else if (HttpRequester.isOpenLog.booleanValue()) {
                CLog.m2d("异步:不使用缓存");
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(this.urlString).openConnection();
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
                    msg.what = 0;
                    data.put("data", b);
                    msg.obj = data;
                    if (this.isUsecache.booleanValue()) {
                        httpCacher.put(this.urlString, b, (int) HttpCacher.TIME_DAY);
                    }
                } else {
                    msg.what = 1;
                    data.put("error", String.valueOf(conn.getResponseCode()));
                    msg.obj = data;
                    conn.disconnect();
                }
            } catch (Exception e) {
                msg.what = 2;
                data.put("error", e.getMessage());
                msg.obj = data;
            }
        } else {
            if (HttpRequester.isOpenLog.booleanValue()) {
                CLog.m2d("异步:缓存命中");
            }
            msg.what = 0;
            data.put("data", cache);
            msg.obj = data;
        }
        this.handler.dispatchMessage(msg);
    }

    private byte[] getBytes(InputStream is) throws IOException {
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
