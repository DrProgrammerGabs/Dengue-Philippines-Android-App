package com.artifex.mupdf;

import android.content.Context;
import android.widget.ImageView;

/* compiled from: PageView */
class OpaqueImageView extends ImageView {
    public OpaqueImageView(Context context) {
        super(context);
    }

    public boolean isOpaque() {
        return true;
    }
}
