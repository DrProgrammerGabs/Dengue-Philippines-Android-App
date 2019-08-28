package com.p000hl.android.view.gallary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.C0048R;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.FileUtils;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.gallary.Galley3DAdapter */
public class Galley3DAdapter extends BaseAdapter {
    int height = 280;
    private Context mContext;
    int mGalleryItemBackground;
    Options options;
    ArrayList<String> snaps;
    int width = 320;

    public Galley3DAdapter(Context c, Integer[] ImageIds) {
        this.mContext = c;
    }

    public Galley3DAdapter(Context c, ArrayList<String> snaps2, int _width, int _height) {
        this.mContext = c;
        this.width = _width;
        this.height = _height;
        if (BookSetting.IS_HOR) {
            this.width = BookSetting.SCREEN_WIDTH / 5;
        } else {
            this.width = BookSetting.SCREEN_WIDTH / 5;
        }
        this.snaps = snaps2;
        TypedArray a = c.obtainStyledAttributes(C0048R.styleable.Gallery1);
        this.mGalleryItemBackground = a.getResourceId(0, 0);
        a.recycle();
        this.options = new Options();
        this.options.inTempStorage = new byte[16384];
    }

    public boolean createReflectedImages() {
        return true;
    }

    private Resources getResources() {
        return null;
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
        return makeImage((String) this.snaps.get(position));
    }

    public View makeImage(String imageId) {
        Bitmap originalImage = null;
        try {
            if (HLSetting.IsResourceSD) {
                originalImage = BitmapFactory.decodeStream(FileUtils.getInstance().getFileInputStream(imageId), null, this.options);
            } else {
                originalImage = BitmapFactory.decodeStream(FileUtils.getInstance().getFileInputStream(this.mContext, imageId), null, this.options);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int imageHeight = (int) (((double) this.height) * 0.6d);
        Bitmap resizeBmp = Bitmap.createScaledBitmap(originalImage, this.width, imageHeight, true);
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(resizeBmp, 0, (int) (((double) imageHeight) * 0.6d), this.width, (int) (((double) imageHeight) * 0.3d), matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(resizeBmp, 0.0f, 0.0f, null);
        new Paint().setAntiAlias(false);
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (imageHeight + 4), null);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setShader(new LinearGradient(0.0f, (float) resizeBmp.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 4), 1895825407, 16777215, TileMode.MIRROR));
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, (float) imageHeight, (float) this.width, (float) (bitmapWithReflection.getHeight() + 4), paint);
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageBitmap(bitmapWithReflection);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setLayoutParams(new LayoutParams(this.width, this.height));
        return imageView;
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0.0f, 1.0f / ((float) Math.pow(2.0d, (double) Math.abs(offset))));
    }
}
