package com.p000hl.android.view.component.moudle.carouseimg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.core.utils.BitmapUtils;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.carouseimg.GalleryImageAdapter */
public class GalleryImageAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<String> mImgPaths = null;
    LayoutParams mItemLp = null;
    private int selectPos = 0;

    public GalleryImageAdapter(Context c, ArrayList<String> imgPaths, LayoutParams itemLp) {
        this.context = c;
        this.mImgPaths = imgPaths;
        this.mItemLp = itemLp;
    }

    public int getCount() {
        if (this.mImgPaths == null) {
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {
        if (this.mImgPaths == null) {
            return null;
        }
        return this.mImgPaths.get(position % this.mImgPaths.size());
    }

    public long getItemId(int position) {
        return (long) (position % this.mImgPaths.size());
    }

    public void notifyDataSetChanged(int id) {
        this.selectPos = id;
        Log.d("hl", " selectPos is " + this.selectPos);
        super.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap = null;
        if (convertView == 0) {
            bitmap = BitmapUtils.getBitMap((String) this.mImgPaths.get(position % this.mImgPaths.size()), this.context);
            ImageView img = new ImageView(this.context);
            img.setScaleType(ScaleType.FIT_CENTER);
            img.setId(position % this.mImgPaths.size());
            img.setImageBitmap(bitmap);
            convertView = img;
        } else {
            ImageView img2 = (ImageView) convertView;
            Bitmap oldBitmap = ((BitmapDrawable) img2.getDrawable()).getBitmap();
            img2.setImageBitmap(null);
            if (!oldBitmap.isRecycled()) {
                oldBitmap.recycle();
            }
        }
        Gallery.LayoutParams glp = new Gallery.LayoutParams((bitmap.getWidth() * this.mItemLp.height) / bitmap.getHeight(), this.mItemLp.height);
        convertView.setId(position % this.mImgPaths.size());
        convertView.setLayoutParams(glp);
        return convertView;
    }
}
