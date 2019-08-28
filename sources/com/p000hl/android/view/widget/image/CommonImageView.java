package com.p000hl.android.view.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.p000hl.android.core.utils.BitmapUtils;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.hl.android.view.widget.image.CommonImageView */
public class CommonImageView extends ImageView {
    private ReentrantLock lock = new ReentrantLock();
    /* access modifiers changed from: private */
    public Bitmap mBitmap = null;

    public CommonImageView(Context context) {
        super(context);
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap mBitmap2) {
        this.lock.lock();
        try {
            setScaleType(ScaleType.FIT_XY);
            setImageBitmap(mBitmap2);
            this.mBitmap = mBitmap2;
        } finally {
            this.lock.unlock();
        }
    }

    public void recycle() {
        this.mBitmap = null;
        post(new Runnable() {
            public void run() {
                try {
                    CommonImageView.this.setBitmap(null);
                    BitmapUtils.recycleBitmap(CommonImageView.this.mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void recycleImidiate() {
        setBitmap(null);
        BitmapUtils.recycleBitmap(this.mBitmap);
    }
}
