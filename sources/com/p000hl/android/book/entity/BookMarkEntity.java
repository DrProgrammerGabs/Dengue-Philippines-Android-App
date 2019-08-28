package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.BookMarkEntity */
public class BookMarkEntity {
    String BookMarkLabelHorGap;
    String BookMarkLabelText;
    String BookMarkLabelVerGap;
    String BookMarkLablePositon;
    String IsShowBookMark;
    String IsShowBookMarkLabel;

    public String getIsShowBookMark() {
        return this.IsShowBookMark;
    }

    public void setIsShowBookMark(String isShowBookMark) {
        this.IsShowBookMark = isShowBookMark;
    }

    public String getIsShowBookMarkLabel() {
        return this.IsShowBookMarkLabel;
    }

    public void setIsShowBookMarkLabel(String isShowBookMarkLabel) {
        this.IsShowBookMarkLabel = isShowBookMarkLabel;
    }

    public String getBookMarkLablePositon() {
        return this.BookMarkLablePositon;
    }

    public void setBookMarkLablePositon(String bookMarkLablePositon) {
        this.BookMarkLablePositon = bookMarkLablePositon;
    }

    public String getBookMarkLabelHorGap() {
        return this.BookMarkLabelHorGap;
    }

    public void setBookMarkLabelHorGap(String bookMarkLabelHorGap) {
        this.BookMarkLabelHorGap = bookMarkLabelHorGap;
    }

    public String getBookMarkLabelVerGap() {
        return this.BookMarkLabelVerGap;
    }

    public void setBookMarkLabelVerGap(String bookMarkLabelVerGap) {
        this.BookMarkLabelVerGap = bookMarkLabelVerGap;
    }

    public String getBookMarkLabelText() {
        return this.BookMarkLabelText;
    }

    public void setBookMarkLabelText(String bookMarkLabelText) {
        this.BookMarkLabelText = bookMarkLabelText;
    }

    public BookMarkEntity(String isShowBookMark, String isShowBookMarkLabel, String bookMarkLablePositon, String bookMarkLabelHorGap, String bookMarkLabelVerGap, String bookMarkLabelText) {
        this.IsShowBookMark = isShowBookMark;
        this.IsShowBookMarkLabel = isShowBookMarkLabel;
        this.BookMarkLablePositon = bookMarkLablePositon;
        this.BookMarkLabelHorGap = bookMarkLabelHorGap;
        this.BookMarkLabelVerGap = bookMarkLabelVerGap;
        this.BookMarkLabelText = bookMarkLabelText;
    }

    public BookMarkEntity() {
    }
}
