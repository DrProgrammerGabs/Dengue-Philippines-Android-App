package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.inter.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.component.moudle.CatalogModel */
public class CatalogModel extends HorizontalScrollView implements Component {
    public static boolean CHANGEBUTTON = false;
    public static boolean ISHORIZONTAL = true;
    Context _con;
    Options _option;
    LinearLayout galleryrl;
    private ArrayList<String> imagelist;

    /* renamed from: com.hl.android.view.component.moudle.CatalogModel$runview */
    class runview implements Runnable {
        int _index;
        String _name;

        public runview(String name, int index) {
            this._name = name;
            this._index = index;
        }

        public void run() {
            try {
                if (HLSetting.IsResourceSD) {
                    CatalogModel.this.load(FileUtils.getInstance().getFileInputStream(this._name));
                } else {
                    CatalogModel.this.load(FileUtils.getInstance().getFileInputStream(CatalogModel.this.getContext(), this._name));
                }
            } catch (OutOfMemoryError e) {
                Log.e("hl", "load error", e);
            }
        }
    }

    public CatalogModel(Context context, ComponentEntity entity) {
        super(context);
        this._con = context;
        this.galleryrl = new LinearLayout(context);
        this.imagelist = new ArrayList<>();
    }

    public CatalogModel(Context context) {
        super(context);
        this._con = context;
        this.galleryrl = new LinearLayout(context);
    }

    public ComponentEntity getEntity() {
        return null;
    }

    public void setEntity(ComponentEntity entity) {
    }

    public void load() {
        addView(this.galleryrl, new LayoutParams(-1, -1));
        if (ISHORIZONTAL) {
            this.galleryrl.setOrientation(0);
        } else {
            this.galleryrl.setOrientation(1);
        }
        for (int i = 0; i < this.imagelist.size(); i++) {
            post(new runview((String) this.imagelist.get(i), i));
        }
    }

    public void load(InputStream is) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, this._option);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            this._option.inSampleSize = 2;
            bitmap = BitmapFactory.decodeStream(is, null, this._option);
        }
        Drawable dbg = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, getLayoutParams().width, getLayoutParams().height, true));
        ImageButton ib = new ImageButton(this._con);
        ib.setBackgroundDrawable(dbg);
        this.galleryrl.addView(ib);
        ib.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CatalogModel.CHANGEBUTTON) {
                }
            }
        });
        try {
            is.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }

    public void recyle() {
        if (this.galleryrl != null) {
            this.galleryrl.removeAllViews();
            this.galleryrl = null;
        }
        removeAllViews();
    }
}
