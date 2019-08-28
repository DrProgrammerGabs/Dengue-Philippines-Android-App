package com.p000hl.android.controller;

/* renamed from: com.hl.android.controller.BookState */
public class BookState {
    private static BookState bookState;
    public boolean isChangeTo = true;
    public boolean isFliping = false;

    public static BookState getInstance() {
        if (bookState == null) {
            bookState = new BookState();
        }
        return bookState;
    }

    public boolean setFlipState() {
        if (this.isFliping) {
            return false;
        }
        this.isFliping = true;
        return true;
    }

    public void restoreFlipState() {
        this.isFliping = false;
    }

    public boolean setPlayViewPage() {
        this.isFliping = false;
        this.isChangeTo = false;
        return true;
    }

    public void recyle() {
        bookState = null;
    }
}
