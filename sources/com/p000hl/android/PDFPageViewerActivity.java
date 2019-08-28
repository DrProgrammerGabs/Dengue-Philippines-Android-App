package com.p000hl.android;

import android.os.Bundle;
import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.acts.BasePageViewerActivity;
import org.vudroid.pdfdroid.codec.PdfContext;

/* renamed from: com.hl.android.PDFPageViewerActivity */
public class PDFPageViewerActivity extends BasePageViewerActivity {
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public DecodeService createDecodeService() {
        return new DecodeServiceBase(new PdfContext());
    }
}
