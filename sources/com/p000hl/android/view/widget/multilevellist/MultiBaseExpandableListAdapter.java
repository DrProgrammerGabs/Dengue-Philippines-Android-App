package com.p000hl.android.view.widget.multilevellist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/* renamed from: com.hl.android.view.widget.multilevellist.MultiBaseExpandableListAdapter */
public class MultiBaseExpandableListAdapter extends BaseExpandableListAdapter {
    private String[][] children = {new String[]{"Arnold", "Barry", "Chuck", "David"}, new String[]{"Ace", "Bandit", "Cha-Cha", "Deuce"}, new String[]{"Fluffy", "Snuggles"}, new String[]{"Goldy", "Bubbles"}};
    private String[] groups = {"People Names", "Dog Names", "Cat Names", "Fish Names"};
    private Context mContext;

    public MultiBaseExpandableListAdapter(Context context) {
        this.mContext = context;
    }

    public int getGroupCount() {
        return this.groups.length;
    }

    public int getChildrenCount(int groupPosition) {
        return this.children[groupPosition].length;
    }

    public Object getGroup(int groupPosition) {
        return this.groups[groupPosition];
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.children[groupPosition][childPosition];
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public TextView getGenericView() {
        LayoutParams lp = new LayoutParams(-1, 64);
        TextView textView = new TextView(this.mContext);
        textView.setLayoutParams(lp);
        textView.setGravity(19);
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

    public Context getmContext() {
        return this.mContext;
    }

    public void setmContext(Context mContext2) {
        this.mContext = mContext2;
    }
}
