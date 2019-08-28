package com.p000hl.android.view.widget.treeview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.hl.android.view.widget.treeview.TreeViewAdapter */
public class TreeViewAdapter extends BaseExpandableListAdapter {
    public static final int ItemHeight = 48;
    public static final int PaddingLeft = 36;
    private int myPaddingLeft = 0;
    Context parentContext;
    List<TreeNode> treeNodes = new ArrayList();

    /* renamed from: com.hl.android.view.widget.treeview.TreeViewAdapter$TreeNode */
    public static class TreeNode {
        List<Object> childs = new ArrayList();
        Object parent;
    }

    public TreeViewAdapter(Context view, int myPaddingLeft2) {
        this.parentContext = view;
        this.myPaddingLeft = myPaddingLeft2;
    }

    public List<TreeNode> GetTreeNode() {
        return this.treeNodes;
    }

    public void UpdateTreeNode(List<TreeNode> nodes) {
        this.treeNodes = nodes;
    }

    public void RemoveAll() {
        this.treeNodes.clear();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ((TreeNode) this.treeNodes.get(groupPosition)).childs.get(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        return ((TreeNode) this.treeNodes.get(groupPosition)).childs.size();
    }

    public static TextView getTextView(Context context) {
        LayoutParams lp = new LayoutParams(-1, 48);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setGravity(19);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getTextView(this.parentContext);
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setPadding(this.myPaddingLeft + 36, 0, 0, 0);
        return textView;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = getTextView(this.parentContext);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(this.myPaddingLeft + 18, 0, 0, 0);
        return textView;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public Object getGroup(int groupPosition) {
        return ((TreeNode) this.treeNodes.get(groupPosition)).parent;
    }

    public int getGroupCount() {
        return this.treeNodes.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}
