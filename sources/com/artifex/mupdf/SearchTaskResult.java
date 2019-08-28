package com.artifex.mupdf;

import android.graphics.RectF;

/* compiled from: MuPDFActivity */
class SearchTaskResult {
    private static SearchTaskResult singleton;
    public final int pageNumber;
    public final RectF[] searchBoxes;
    public final String txt;

    SearchTaskResult(String _txt, int _pageNumber, RectF[] _searchBoxes) {
        this.txt = _txt;
        this.pageNumber = _pageNumber;
        this.searchBoxes = _searchBoxes;
    }

    public static SearchTaskResult get() {
        return singleton;
    }

    public static void set(SearchTaskResult r) {
        singleton = r;
    }
}
