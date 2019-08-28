package com.p000hl.android.view.compositeview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.io.IOException;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.compositeview.ViewPageAdapter */
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
            return (ImageView) convertView;
        }
        ImageView imageView = new ImageView(this.mContext);
        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream(this.mContext.getAssets().open("book/" + ((String) this.mpageIDS.get(position))), null, null));
            return imageView;
        } catch (IOException e) {
            e.printStackTrace();
            return imageView;
        }
    }
}
