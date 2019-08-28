package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.SnapshotEntity */
public class SnapshotEntity {
    public String bookID;
    public String height;

    /* renamed from: id */
    public String f19id;
    public String pageID;
    public String width;

    public String getId() {
        return this.f19id;
    }

    public void setId(String id) {
        this.f19id = id;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width2) {
        this.width = width2;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height2) {
        this.height = height2;
    }

    public String getPageID() {
        return this.pageID;
    }

    public void setPageID(String pageID2) {
        this.pageID = pageID2;
    }
}
