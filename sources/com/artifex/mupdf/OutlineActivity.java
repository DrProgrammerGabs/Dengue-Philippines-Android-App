package com.artifex.mupdf;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class OutlineActivity extends ListActivity {
    OutlineItem[] mItems;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mItems = OutlineActivityData.get().items;
        setListAdapter(new OutlineAdapter(getLayoutInflater(), this.mItems));
        getListView().setSelection(OutlineActivityData.get().position);
        setResult(-1);
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        OutlineActivityData.get().position = getListView().getFirstVisiblePosition();
        setResult(this.mItems[position].page);
        finish();
    }
}
