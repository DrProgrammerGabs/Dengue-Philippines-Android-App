package com.p000hl.android.view.widget.treeview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import com.p000hl.android.view.widget.treeview.TreeViewAdapter.TreeNode;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.hl.android.view.widget.treeview.SuperTreeViewAdapter */
public class SuperTreeViewAdapter extends BaseExpandableListAdapter {
    private Context parentContext;
    private OnChildClickListener stvClickEvent;
    private List<SuperTreeNode> superTreeNodes = new ArrayList();

    /* renamed from: com.hl.android.view.widget.treeview.SuperTreeViewAdapter$SuperTreeNode */
    public static class SuperTreeNode {
        List<TreeNode> childs = new ArrayList();
        Object parent;
    }

    public SuperTreeViewAdapter(Context view, OnChildClickListener stvClickEvent2) {
        this.parentContext = view;
        this.stvClickEvent = stvClickEvent2;
    }

    public List<SuperTreeNode> GetTreeNode() {
        return this.superTreeNodes;
    }

    public void UpdateTreeNode(List<SuperTreeNode> node) {
        this.superTreeNodes = node;
    }

    public void RemoveAll() {
        this.superTreeNodes.clear();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ((SuperTreeNode) this.superTreeNodes.get(groupPosition)).childs.get(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        return ((SuperTreeNode) this.superTreeNodes.get(groupPosition)).childs.size();
    }

    public ExpandableListView getExpandableListView() {
        LayoutParams lp = new LayoutParams(-1, 48);
        ExpandableListView superTreeView = new ExpandableListView(this.parentContext);
        superTreeView.setLayoutParams(lp);
        return superTreeView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListView treeView = getExpandableListView();
        TreeViewAdapter treeViewAdapter = new TreeViewAdapter(this.parentContext, 0);
        List<TreeNode> tmp = treeViewAdapter.GetTreeNode();
        final TreeNode treeNode = (TreeNode) getChild(groupPosition, childPosition);
        tmp.add(treeNode);
        treeViewAdapter.UpdateTreeNode(tmp);
        treeView.setAdapter(treeViewAdapter);
        treeView.setOnChildClickListener(this.stvClickEvent);
        treeView.setOnGroupExpandListener(new OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                treeView.setLayoutParams(new LayoutParams(-1, ((treeNode.childs.size() + 1) * 48) + 10));
            }
        });
        treeView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                treeView.setLayoutParams(new LayoutParams(-1, 48));
            }
        });
        treeView.setPadding(36, 0, 0, 0);
        return treeView;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public Object getGroup(int groupPosition) {
        return null;
    }

    public int getGroupCount() {
        return 0;
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
