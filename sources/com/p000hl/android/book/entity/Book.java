package com.p000hl.android.book.entity;

import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.hl.android.book.entity.Book */
public class Book {
    public static String push_server = "http://smartappscreator.com/sac/index.php?m=Wapps&a=app_msg_load&account=qwe&password=asd123";
    public boolean ActivePush = false;
    public String PushID = "";
    private BookInfoEntity bookInfo = new BookInfoEntity();
    private ArrayList<ButtonEntity> buttons = new ArrayList<>();
    public String device_id = "";
    private ArrayList<String> pages = new ArrayList<>();
    private ArrayList<SectionEntity> sections = new ArrayList<>();
    private ArrayList<SnapshotEntity> snapshots = new ArrayList<>();
    private String startPageID;

    public BookInfoEntity getBookInfo() {
        return this.bookInfo;
    }

    public void setBookInfo(BookInfoEntity bookInfo2) {
        this.bookInfo = bookInfo2;
    }

    public ArrayList<String> getPages() {
        return this.pages;
    }

    public void setPages(ArrayList<String> pages2) {
        this.pages = pages2;
    }

    public String getStartPageID() {
        return this.startPageID;
    }

    public void setStartPageID(String startPageID2) {
        this.startPageID = startPageID2;
    }

    public ArrayList<SectionEntity> getSections() {
        return this.sections;
    }

    public void setSections(ArrayList<SectionEntity> sections2) {
        this.sections = sections2;
    }

    public ArrayList<SnapshotEntity> getSnapshots() {
        return this.snapshots;
    }

    public void setSnapshots(ArrayList<SnapshotEntity> snapshots2) {
        this.snapshots = snapshots2;
    }

    public ArrayList<ButtonEntity> getButtons() {
        return this.buttons;
    }

    public void setButtons(ArrayList<ButtonEntity> buttons2) {
        this.buttons = buttons2;
    }

    public String getSnapshotIdByPageId(String pageID) {
        Iterator i$ = this.snapshots.iterator();
        while (i$.hasNext()) {
            SnapshotEntity entity = (SnapshotEntity) i$.next();
            if (entity.pageID.equals(pageID)) {
                return entity.f19id;
            }
        }
        return "";
    }
}
