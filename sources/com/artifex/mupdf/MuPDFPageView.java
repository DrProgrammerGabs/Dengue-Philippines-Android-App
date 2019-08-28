package com.artifex.mupdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

public class MuPDFPageView extends PageView {
    private final MuPDFCore mCore;

    public MuPDFPageView(Context c, MuPDFCore core, Point parentSize) {
        super(c, parentSize);
        this.mCore = core;
    }

    public int hitLinkPage(float x, float y) {
        float scale = (this.mSourceScale * ((float) getWidth())) / ((float) this.mSize.x);
        return this.mCore.hitLinkPage(this.mPageNumber, (x - ((float) getLeft())) / scale, (y - ((float) getTop())) / scale);
    }

    /* access modifiers changed from: protected */
    public void drawPage(Bitmap bm, int sizeX, int sizeY, int patchX, int patchY, int patchWidth, int patchHeight) {
        this.mCore.drawPage(this.mPageNumber, bm, sizeX, sizeY, patchX, patchY, patchWidth, patchHeight);
    }

    /* access modifiers changed from: protected */
    public Bitmap drawPage(int page, int sizeX, int sizeY, int patchX, int patchY, int patchWidth, int patchHeight) {
        return this.mCore.drawPage(page, sizeX, sizeY, patchX, patchY, patchWidth, patchHeight);
    }

    /* access modifiers changed from: protected */
    public LinkInfo[] getLinkInfo() {
        return this.mCore.getPageLinks(this.mPageNumber);
    }
}
