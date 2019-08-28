package com.p000hl.android.view.component.moudle.bookshelf;

import android.content.Context;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.view.component.moudle.bookshelf.ShelvesBook */
public class ShelvesBook {
    public static int curIndex = 233232423;
    Context context = BookController.getInstance().hlActivity;
    public String mBookID;
    public String mBookUrl;
    public String mCoverPath;
    public String mCoverUrl;
    public String mLocalPath;
    public int mState = 0;
    public String version;
    public int viewID;

    public ShelvesBook() {
        int i = curIndex;
        curIndex = i + 1;
        this.viewID = i;
    }
}
