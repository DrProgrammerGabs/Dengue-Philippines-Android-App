package com.p000hl.android;

import android.content.Context;
import android.widget.FrameLayout;

/* renamed from: com.hl.android.MFrameLayout */
public class MFrameLayout extends FrameLayout {
    private HLLayoutActivity mContext;

    public MFrameLayout(Context context) {
        super(context);
        this.mContext = (HLLayoutActivity) context;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mContext.updateCoverPosition();
    }
}
