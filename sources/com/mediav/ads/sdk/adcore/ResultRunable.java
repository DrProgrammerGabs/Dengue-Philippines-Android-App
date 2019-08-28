package com.mediav.ads.sdk.adcore;

import android.os.Message;
import com.mediav.ads.sdk.adcore.HttpRequester.Listener;
import java.util.HashMap;

/* compiled from: HttpRequester */
class ResultRunable implements Runnable {
    private static final String UNKOWN_ERROR = "unkown error";
    private Message msg = null;

    public ResultRunable(Message msg2) {
        this.msg = msg2;
    }

    public void run() {
        try {
            HashMap<String, Object> data = (HashMap) this.msg.obj;
            Listener listener = (Listener) data.get("callback");
            if (this.msg.what == 0) {
                listener.onGetDataSucceed((byte[]) data.get("data"));
            } else if (this.msg.what == 1) {
                listener.onGetDataFailed((String) data.get("error"));
            } else if (this.msg.what == 2) {
                listener.onGetDataFailed((String) data.get("error"));
            } else {
                listener.onGetDataFailed(UNKOWN_ERROR);
            }
        } catch (Exception e) {
            ((Listener) ((HashMap) this.msg.obj).get("callback")).onGetDataFailed("HttpRequester get data error:" + e.getMessage());
        }
    }
}
