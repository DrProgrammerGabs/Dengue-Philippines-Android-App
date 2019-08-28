package com.p000hl.android.controller;

import com.artifex.mupdf.MuPDFCore;

/* renamed from: com.hl.android.controller.PdfController */
public class PdfController {
    private static PdfController pdfController;
    public String documentName;
    public MuPDFCore muPDFCore;

    public static PdfController getInstance() {
        if (pdfController == null) {
            pdfController = new PdfController();
        }
        return pdfController;
    }

    public void openFile(String path) {
        if (this.muPDFCore == null || this.documentName == null || !this.documentName.equals(path)) {
            destroy();
            this.documentName = path;
            try {
                this.muPDFCore = new MuPDFCore(path);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void destroy() {
        if (this.muPDFCore != null) {
            this.muPDFCore.onDestroy();
            this.muPDFCore = null;
        }
    }
}
