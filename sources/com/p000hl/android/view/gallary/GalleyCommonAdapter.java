package com.p000hl.android.view.gallary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.core.utils.ImageUtils;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.gallary.GalleyCommonAdapter */
public class GalleyCommonAdapter extends BaseAdapter {
    public ArrayList<Bitmap> bitmapList = new ArrayList<>();
    int height = 280;

    /* renamed from: lp */
    LayoutParams f59lp;
    private Context mContext;
    ArrayList<String> snaps;
    int width = 320;

    public GalleyCommonAdapter(Context c, ArrayList<String> snaps2, int _width, int _height) {
        Log.i("hl", "GalleyCommonAdapter created");
        this.mContext = c;
        this.height = BookSetting.SNAPSHOTS_HEIGHT - 10;
        this.width = BookSetting.SNAPSHOTS_WIDTH;
        this.snaps = snaps2;
        this.f59lp = new LayoutParams(this.width, this.height);
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
        Bitmap newBitmp = BitmapUtils.getBitMap((String) this.snaps.get(position), this.mContext, this.width, this.height);
        Log.e("hl", "GalleyCommonAdapter  recyled");
        ImageView i = (ImageView) convertView;
        Bitmap bitmap = ((BitmapDrawable) i.getBackground()).getBitmap();
        this.bitmapList.remove(bitmap);
        i.setImageBitmap(newBitmp);
        this.bitmapList.add(newBitmp);
        bitmap.recycle();
        return convertView;
    }

    public void recycle() {
        ImageUtils.recyleBitmapList(this.bitmapList);
    }
}
