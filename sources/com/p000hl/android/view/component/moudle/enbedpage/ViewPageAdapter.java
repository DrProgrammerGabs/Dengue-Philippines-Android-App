package com.p000hl.android.view.component.moudle.enbedpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.ViewPage;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.enbedpage.ViewPageAdapter */
public class ViewPageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mpageIDS;

    public ViewPageAdapter(Context c, ArrayList<String> pageIDS) {
        this.mContext = c;
        this.mpageIDS = pageIDS;
    }

    public int getCount() {
        return this.mpageIDS.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            return (ViewPage) convertView;
        }
        ViewPage pageView = new ViewPage(this.mContext, null, null);
        pageView.load(BookController.getInstance().getPageEntityByID((String) this.mpageIDS.get(position)));
        return pageView;
    }
}
