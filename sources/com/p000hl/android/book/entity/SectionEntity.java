package com.p000hl.android.book.entity;

import com.p000hl.android.book.BookDecoder;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.SectionEntity */
public class SectionEntity {

    /* renamed from: ID */
    public String f16ID;
    public String Name;
    BookDecoder bookDecoder;
    public String bookID;
    public String bookPath = BookSetting.BOOK_PATH;
    public ArrayList<ButtonEntity> buttons;
    public boolean isResourceSD = HLSetting.IsResourceSD;
    public boolean isShelves = BookSetting.IS_SHELVES_COMPONENT;
    public String lastPageID;
    public ArrayList<String> pages = new ArrayList<>();

    public SectionEntity() {
        if (this.isShelves) {
            try {
                this.lastPageID = BookController.getInstance().getViewPage().getEntity().getID();
            } catch (Exception e) {
            }
        }
        this.buttons = BookSetting.buttons;
        this.bookDecoder = BookDecoder.bookDecoder;
    }

    public String getID() {
        return this.f16ID;
    }

    public void setID(String iD) {
        this.f16ID = iD;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<String> getPages() {
        return this.pages;
    }

    public void setPages(ArrayList<String> pages2) {
        this.pages = pages2;
    }
}
