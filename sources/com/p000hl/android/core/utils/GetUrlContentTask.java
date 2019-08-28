package com.p000hl.android.core.utils;

import android.os.AsyncTask;
import com.p000hl.android.core.utils.WebUtils.CallBack;

/* renamed from: com.hl.android.core.utils.GetUrlContentTask */
/* compiled from: WebUtils */
class GetUrlContentTask extends AsyncTask<String, String, String> {

    /* renamed from: mC */
    private CallBack f21mC;
    private String mEncoding;
    private String mUrl;

    public GetUrlContentTask(String url, String encoding, CallBack c) {
        this.mUrl = url;
        this.f21mC = c;
        this.mEncoding = encoding;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... params) {
        return WebUtils.getUrlContent(this.mUrl, this.mEncoding);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        if (this.f21mC != null) {
            if (StringUtils.isEmpty(result)) {
                this.f21mC.failAction(result);
            } else {
                this.f21mC.successAction(result);
            }
        }
    }
}
