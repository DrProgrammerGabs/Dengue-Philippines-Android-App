package com.p000hl.android.view.pageflip;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import com.p000hl.android.controller.BookController;

/* renamed from: com.hl.android.view.pageflip.AbstractPageFlipView */
public abstract class AbstractPageFlipView extends View {
    boolean _preload = false;
    ViewGroup bookLayout;
    protected Bitmap mCurPageBitmap;
    protected Bitmap mNextPageBitmap = null;

    public abstract void hide();

    public abstract void play(int i, int i2, ActionOnEnd actionOnEnd);

    public void setPreLoad(boolean preload) {
        this._preload = preload;
    }

    public AbstractPageFlipView(Context context) {
        super(context);
    }

    public void show() {
        setVisibility(0);
        bringToFront();
        BookController.getInstance().hlActivity.commonLayout.bringToFront();
    }

    public void setBitmap(Bitmap curBitmap) {
        this.mCurPageBitmap = curBitmap;
    }

    public void setNewBitmap(Bitmap newBitmap) {
        this.mNextPageBitmap = newBitmap;
    }

    public void setViewPage(ViewGroup viewPage) {
        this.bookLayout = viewPage;
    }

    public void recycleBitmap() {
        try {
            hide();
            if (this.mCurPageBitmap != null && !this.mCurPageBitmap.isRecycled()) {
                this.mCurPageBitmap.recycle();
                this.mCurPageBitmap = null;
            }
            if (this.mNextPageBitmap != null && !this.mNextPageBitmap.isRecycled()) {
                this.mNextPageBitmap.recycle();
                this.mNextPageBitmap = null;
            }
        } catch (Exception e) {
        }
    }
}
