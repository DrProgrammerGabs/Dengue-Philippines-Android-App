package com.artifex.mupdf;

public class OutlineActivityData {
    private static OutlineActivityData singleton;
    public OutlineItem[] items;
    public int position;

    public static void set(OutlineActivityData d) {
        singleton = d;
    }

    public static OutlineActivityData get() {
        if (singleton == null) {
            singleton = new OutlineActivityData();
        }
        return singleton;
    }
}
