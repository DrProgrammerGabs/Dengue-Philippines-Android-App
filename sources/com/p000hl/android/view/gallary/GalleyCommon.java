package com.p000hl.android.view.gallary;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.C0048R;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.core.utils.BitmapUtils;
import com.p000hl.android.view.gallary.base.AbstractGalley;

/* renamed from: com.hl.android.view.gallary.GalleyCommon */
public class GalleyCommon extends AbstractGalley {
    public GalleyCommon(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Bitmap getBitmap(String resourceID, int width, int height) {
        return BitmapUtils.getBitMap(resourceID, this.mContext, width, height);
    }

    /* access modifiers changed from: protected */
    public LayoutParams getGalleryLp() {
        LayoutParams galleryLp = new LayoutParams(BookSetting.SCREEN_WIDTH, BookSetting.SNAPSHOTS_HEIGHT);
        galleryLp.addRule(14);
        galleryLp.addRule(12);
        return galleryLp;
    }

    /* access modifiers changed from: protected */
    public void setWaitLoad(ImageView img) {
        img.setImageResource(C0048R.drawable.scene_ic_loading_invert);
    }

    /* access modifiers changed from: protected */
    public float getSizeRatio() {
        return 0.8f;
    }
}
