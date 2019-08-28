package com.artifex.mupdf;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

/* compiled from: PageView */
class PatchInfo {

    /* renamed from: bm */
    public Bitmap f1bm;
    public Rect patchArea;
    public Point patchViewSize;

    public PatchInfo(Bitmap aBm, Point aPatchViewSize, Rect aPatchArea) {
        this.f1bm = aBm;
        this.patchViewSize = aPatchViewSize;
        this.patchArea = aPatchArea;
    }
}
