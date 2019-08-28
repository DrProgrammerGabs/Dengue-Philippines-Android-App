package com.p000hl.android.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.p000hl.android.view.widget.image.BitmapWorkerTask;
import com.p000hl.android.view.widget.image.CommonImageView;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.hl.android.view.widget.ImageCacheWidget */
public class ImageCacheWidget {
    private AsyncTask<Integer, Integer, Integer> clearAsyncTask = null;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private Context mContext;
    /* access modifiers changed from: private */
    public ArrayList<CommonImageView> mImageList;
    private ArrayList<String> mSourceIDS;
    private HashMap<String, AsyncTask<String, Bitmap, Bitmap>> mTaskMap = null;

    public ImageCacheWidget(Context context, ArrayList<String> sourceIDS, ArrayList<CommonImageView> imageList) {
        this.mContext = context;
        this.mSourceIDS = sourceIDS;
        this.mImageList = imageList;
        this.mTaskMap = new HashMap<>();
    }

    public void loadBitmap(int childIndex) {
        try {
            if (this.clearAsyncTask != null) {
                this.clearAsyncTask.cancel(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (((CommonImageView) this.mImageList.get(childIndex)).getBitmap() == null && !isLoadingWork((String) this.mSourceIDS.get(childIndex), (ImageView) this.mImageList.get(childIndex))) {
            new BitmapWorkerTask((ImageView) this.mImageList.get(childIndex), this.mContext, (String) this.mSourceIDS.get(childIndex)).execute(new String[]{(String) this.mSourceIDS.get(childIndex)});
        }
        preLoad(childIndex);
        clear(childIndex);
    }

    private void preLoad(int childIndex) {
        int pre;
        int next;
        if (childIndex == 0) {
            pre = this.mImageList.size() - 1;
            next = 1;
        } else if (childIndex == this.mImageList.size() - 1) {
            pre = this.mImageList.size() - 2;
            next = 0;
        } else {
            pre = childIndex - 1;
            next = childIndex + 1;
        }
        if (((CommonImageView) this.mImageList.get(pre)).getBitmap() == null && !isLoadingWork((String) this.mSourceIDS.get(pre), (ImageView) this.mImageList.get(pre))) {
            new BitmapWorkerTask((ImageView) this.mImageList.get(pre), this.mContext, (String) this.mSourceIDS.get(pre)).execute(new String[]{(String) this.mSourceIDS.get(pre)});
        }
        if (((CommonImageView) this.mImageList.get(next)).getBitmap() == null && !isLoadingWork((String) this.mSourceIDS.get(next), (ImageView) this.mImageList.get(next))) {
            new BitmapWorkerTask((ImageView) this.mImageList.get(next), this.mContext, (String) this.mSourceIDS.get(next)).execute(new String[]{(String) this.mSourceIDS.get(next)});
        }
    }

    private void clear(int childIndex) {
        this.clearAsyncTask = new AsyncTask<Integer, Integer, Integer>() {
            /* access modifiers changed from: protected */
            public Integer doInBackground(Integer... v) {
                int pre;
                int next;
                int imageCount = ImageCacheWidget.this.mImageList.size();
                if (v[0].intValue() == 0) {
                    pre = ImageCacheWidget.this.mImageList.size() - 1;
                    next = 1;
                } else if (v[0].intValue() == ImageCacheWidget.this.mImageList.size() - 1) {
                    pre = ImageCacheWidget.this.mImageList.size() - 2;
                    next = 0;
                } else {
                    pre = v[0].intValue() - 1;
                    next = v[0].intValue() + 1;
                }
                for (int i = 0; i < imageCount; i++) {
                    if (!(i == v[0].intValue() || i == pre || i == next)) {
                        ((CommonImageView) ImageCacheWidget.this.mImageList.get(i)).recycle();
                    }
                }
                return v[0];
            }

            /* access modifiers changed from: protected */
            public void onPreExecute() {
            }
        };
        this.clearAsyncTask.execute(new Integer[]{Integer.valueOf(childIndex)});
    }

    public boolean isLoadingWork(String data, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(data);
        if (bitmapWorkerTask == null || !bitmapWorkerTask.mFileID.equals(data)) {
            return false;
        }
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(String id) {
        return (BitmapWorkerTask) this.mTaskMap.get(id);
    }

    public void recycle() {
        int childCount = this.mImageList.size();
        for (int i = 0; i < childCount; i++) {
            ((CommonImageView) this.mImageList.get(i)).recycleImidiate();
        }
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(int imageHeight2) {
        this.imageHeight = imageHeight2;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(int imageWidth2) {
        this.imageWidth = imageWidth2;
    }
}
