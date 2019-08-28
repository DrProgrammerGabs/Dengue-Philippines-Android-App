package com.artifex.mupdf;

public class OutlineItem {
    public final int level;
    public final int page;
    public final String title;

    OutlineItem(int _level, String _title, int _page) {
        this.level = _level;
        this.title = _title;
        this.page = _page;
    }
}
