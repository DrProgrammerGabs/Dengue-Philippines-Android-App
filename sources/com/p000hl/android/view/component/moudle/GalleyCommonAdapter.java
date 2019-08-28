package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.GalleyCommonAdapter */
public class GalleyCommonAdapter extends BaseAdapter {
    int height = 280;
    private Context mContext;
    Options options;
    ArrayList<String> snaps;
    int width = 320;

    public GalleyCommonAdapter(Context c, ArrayList<String> snaps2, int _width, int _height) {
        this.mContext = c;
        this.width = _width;
        this.height = _height;
        this.snaps = snaps2;
        this.options = new Options();
        this.options.inTempStorage = new byte[16384];
    }

    public int getCount() {
        return this.snaps.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(this.mContext);
        i.setScaleType(ScaleType.FIT_XY);
        i.setLayoutParams(new LayoutParams(this.width, this.height));
        return i;
    }
}
