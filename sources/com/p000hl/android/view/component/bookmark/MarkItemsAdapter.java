package com.p000hl.android.view.component.bookmark;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.p000hl.android.HLLayoutActivity;

/* renamed from: com.hl.android.view.component.bookmark.MarkItemsAdapter */
public class MarkItemsAdapter extends BaseAdapter {
    HLLayoutActivity mActivity;
    boolean showDelete;

    public MarkItemsAdapter(HLLayoutActivity activity) {
        this.mActivity = activity;
    }

    public int getCount() {
        return BookMarkManager.getMarkList(this.mActivity).size();
    }

    public Object getItem(int position) {
        return BookMarkManager.getMarkList(this.mActivity).get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            return new BookMarkView(this.mActivity, position);
        }
        ((BookMarkView) convertView).setImageBitmap(position);
        return convertView;
    }
}
