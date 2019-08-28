package com.p000hl.android.core.utils;

import android.os.AsyncTask;
import com.p000hl.android.core.utils.WebUtils.CallBack;

/* renamed from: com.hl.android.core.utils.DownAsyncTask */
/* compiled from: WebUtils */
class DownAsyncTask extends AsyncTask<String, String, String> {

    /* renamed from: mC */
    private CallBack f20mC;
    private String mLocalUrl;
    private String mUrl;

    public DownAsyncTask(String url, String localUrl, CallBack c) {
        this.mUrl = url;
        this.mLocalUrl = localUrl;
        this.f20mC = c;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... params) {
        return WebUtils.downLoadResource(this.mUrl, this.mLocalUrl);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        this.f20mC.successAction(result);
    }
}
