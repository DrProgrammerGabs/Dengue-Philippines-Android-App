package com.artifex.mupdf;

import android.app.ProgressDialog;
import android.content.Context;

/* compiled from: MuPDFActivity */
class ProgressDialogX extends ProgressDialog {
    private boolean mCancelled = false;

    public ProgressDialogX(Context context) {
        super(context);
    }

    public boolean isCancelled() {
        return this.mCancelled;
    }

    public void cancel() {
        this.mCancelled = true;
        super.cancel();
    }
}
