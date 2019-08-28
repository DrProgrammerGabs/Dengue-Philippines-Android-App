package org.vudroid.core.acts;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;

public class PdfViewerActivity extends BaseViewerActivity {
    /* access modifiers changed from: protected */
    public DecodeService createDecodeService() {
        return new DecodeServiceBase(new PdfContext());
    }
}
