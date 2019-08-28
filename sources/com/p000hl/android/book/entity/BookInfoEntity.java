package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.BookInfoEntity */
public class BookInfoEntity {
    public int adType = -1;
    public String backgroundMusicId;
    public String bookFlipType;
    public int bookHeight = 0;
    public String bookIconId;
    public String bookNavType;
    public String bookType;
    public int bookWidth = 0;
    public String description;
    public String deviceType;
    public String homePageID;

    /* renamed from: id */
    public String f3id;
    public boolean isFree = true;
    public String name;
    public String position = "top";
    private double startPageTime;

    public String getId() {
        return this.f3id;
    }

    public void setId(String id) {
        this.f3id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getBackgroundMusicId() {
        return this.backgroundMusicId;
    }

    public void setBackgroundMusicId(String backgroundMusicId2) {
        this.backgroundMusicId = backgroundMusicId2;
    }

    public String getBookType() {
        return this.bookType;
    }

    public void setBookType(String bookType2) {
        this.bookType = bookType2;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType2) {
        this.deviceType = deviceType2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getBookIconId() {
        return this.bookIconId;
    }

    public void setBookIconId(String bookIconId2) {
        this.bookIconId = bookIconId2;
    }

    public String getHomePageID() {
        return this.homePageID;
    }

    public void setHomePageID(String homePageID2) {
        this.homePageID = homePageID2;
    }

    public double getStartPageTime() {
        return this.startPageTime;
    }

    public void setStartPageTime(double startPageTime2) {
        this.startPageTime = startPageTime2;
    }

    public String getBookNavType() {
        return this.bookNavType;
    }

    public void setBookNavType(String bookNavType2) {
        this.bookNavType = bookNavType2;
    }
}
