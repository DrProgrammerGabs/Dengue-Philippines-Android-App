package com.p000hl.android.view.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.FileUtils;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/* renamed from: com.hl.android.view.widget.image.ImageLoaderTask */
public class ImageLoaderTask extends AsyncTask<String, Bitmap, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Bitmap mBitmap;
    private Context mContext;
    public String mFileID;
    private int mResizeHeight;
    private int mResizeWidth;

    public ImageLoaderTask(ImageView imageView, Context context, String fileID, int resizeWidth, int resizeHeight) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.mContext = context;
        this.mFileID = fileID;
        this.mContext = context;
        this.mResizeWidth = resizeWidth;
        this.mResizeHeight = resizeHeight;
    }

    /* access modifiers changed from: protected */
    public Bitmap doInBackground(String... params) {
        load(this.mFileID);
        return this.mBitmap;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (this.imageViewReference != null && bitmap != null) {
            ImageView imageView = (ImageView) this.imageViewReference.get();
            if (imageView != null) {
                ((CommonImageView) imageView).setBitmap(bitmap);
            }
        }
    }

    public void load(String fileID) {
        try {
            if (HLSetting.IsResourceSD) {
                load(FileUtils.getInstance().getFileInputStream(fileID));
            } else {
                load(FileUtils.getInstance().getFileInputStream(this.mContext, fileID));
            }
        } catch (OutOfMemoryError e) {
            Log.e("hl", "load error", e);
        }
    }

    public void load(InputStream is) {
        Options _option = new Options();
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[32768];
        try {
            this.mBitmap = BitmapFactory.decodeStream(is, null, _option);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            _option.inSampleSize = 2;
            this.mBitmap = BitmapFactory.decodeStream(is, null, _option);
        }
        ((ImageView) this.imageViewReference.get()).setImageBitmap(Bitmap.createScaledBitmap(this.mBitmap, this.mResizeWidth, this.mResizeHeight, true));
    }
}
