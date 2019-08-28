package com.artifex.mupdf;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PointF;
import android.graphics.RectF;

public class MuPDFCore {
    private int numPages = -1;
    public float pageHeight;
    private int pageNum = -1;
    public float pageWidth;

    public static native boolean authenticatePasswordInternal(String str);

    private static native int countPagesInternal();

    public static native void destroying();

    public static native void drawPage(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6);

    public static native OutlineItem[] getOutlineInternal();

    private static native float getPageHeight();

    public static native int getPageLink(int i, float f, float f2);

    public static native LinkInfo[] getPageLinksInternal(int i);

    private static native float getPageWidth();

    private static native void gotoPageInternal(int i);

    public static native boolean hasOutlineInternal();

    public static native boolean needsPasswordInternal();

    private static native int openFile(String str);

    public static native RectF[] searchPage(String str);

    static {
        System.loadLibrary("mupdf");
    }

    public MuPDFCore(String filename) throws Exception {
        if (openFile(filename) <= 0) {
            throw new Exception("Failed to open " + filename);
        }
    }

    public int countPages() {
        if (this.numPages < 0) {
            this.numPages = countPagesSynchronized();
        }
        return this.numPages;
    }

    private synchronized int countPagesSynchronized() {
        return countPagesInternal();
    }

    public void gotoPage(int page) {
        if (page > this.numPages - 1) {
            page = this.numPages - 1;
        } else if (page < 0) {
            page = 0;
        }
        if (this.pageNum != page) {
            gotoPageInternal(page);
            this.pageNum = page;
            this.pageWidth = getPageWidth();
            this.pageHeight = getPageHeight();
        }
    }

    public synchronized PointF getPageSize(int page) {
        gotoPage(page);
        return new PointF(this.pageWidth, this.pageHeight);
    }

    public synchronized void onDestroy() {
        destroying();
    }

    public synchronized void drawPage(int page, Bitmap bitmap, int pageW, int pageH, int patchX, int patchY, int patchW, int patchH) {
        gotoPage(page);
        drawPage(bitmap, pageW, pageH, patchX, patchY, patchW, patchH);
    }

    public synchronized Bitmap drawPage(int page, int pageW, int pageH, int patchX, int patchY, int patchW, int patchH) {
        Bitmap bm;
        gotoPage(page);
        bm = Bitmap.createBitmap(pageW, pageH, Config.ARGB_8888);
        drawPage(bm, pageW, pageH, patchX, patchY, pageW, pageH);
        return bm;
    }

    public synchronized int hitLinkPage(int page, float x, float y) {
        return getPageLink(page, x, y);
    }

    public synchronized LinkInfo[] getPageLinks(int page) {
        return getPageLinksInternal(page);
    }

    public synchronized RectF[] searchPage(int page, String text) {
        gotoPage(page);
        return searchPage(text);
    }

    public synchronized boolean hasOutline() {
        return hasOutlineInternal();
    }

    public synchronized OutlineItem[] getOutline() {
        return getOutlineInternal();
    }

    public synchronized boolean needsPassword() {
        return needsPasswordInternal();
    }

    public synchronized boolean authenticatePassword(String password) {
        return authenticatePasswordInternal(password);
    }
}
