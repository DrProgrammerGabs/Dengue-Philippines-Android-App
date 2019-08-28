package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.PDFComponentEntity */
public class PDFComponentEntity extends ComponentEntity {
    private String intailHeight;
    private String intailWidth;
    private String isAllowUserZoom;
    private String localSourceID;
    private String pdfPageIndex;
    private String pdfSourceID;

    public String getLocalSourceID() {
        return this.localSourceID;
    }

    public void setLocalSourceID(String localSourceID2) {
        this.localSourceID = localSourceID2;
    }

    public String getPdfSourceID() {
        return this.pdfSourceID;
    }

    public void setPdfSourceID(String pdfSourceID2) {
        this.pdfSourceID = pdfSourceID2;
    }

    public String getPdfPageIndex() {
        return this.pdfPageIndex;
    }

    public void setPdfPageIndex(String pdfPageIndex2) {
        this.pdfPageIndex = pdfPageIndex2;
    }

    public String getIntailWidth() {
        return this.intailWidth;
    }

    public void setIntailWidth(String intailWidth2) {
        this.intailWidth = intailWidth2;
    }

    public String getIntailHeight() {
        return this.intailHeight;
    }

    public void setIntailHeight(String intailHeight2) {
        this.intailHeight = intailHeight2;
    }

    public String getIsAllowUserZoom() {
        return this.isAllowUserZoom;
    }

    public void setIsAllowUserZoom(String isAllowUserZoom2) {
        this.isAllowUserZoom = isAllowUserZoom2;
    }
}
