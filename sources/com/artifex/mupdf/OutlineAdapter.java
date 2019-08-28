package com.artifex.mupdf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.p000hl.android.C0048R;

public class OutlineAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final OutlineItem[] mItems;

    public OutlineAdapter(LayoutInflater inflater, OutlineItem[] items) {
        this.mInflater = inflater;
        this.mItems = items;
    }

    public int getCount() {
        return this.mItems.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = this.mInflater.inflate(C0048R.C0050layout.outline_entry, null);
        } else {
            v = convertView;
        }
        int level = this.mItems[position].level;
        if (level > 8) {
            level = 8;
        }
        String space = "";
        for (int i = 0; i < level; i++) {
            space = space + "   ";
        }
        ((TextView) v.findViewById(C0048R.C0049id.title)).setText(space + this.mItems[position].title);
        ((TextView) v.findViewById(C0048R.C0049id.page)).setText(String.valueOf(this.mItems[position].page + 1));
        return v;
    }
}
