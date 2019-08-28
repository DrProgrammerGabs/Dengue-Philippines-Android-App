package com.p000hl.android.view.gallary.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLActivity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.BookState;
import com.p000hl.android.core.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* renamed from: com.hl.android.view.gallary.base.AbstractGalley */
public abstract class AbstractGalley extends Gallery {
    public static String ISFALSE = "ISFALSE";
    public static String ISTRUE = "ISTRUE";
    private static final int TIME_OUT_DISPLAY = 500;
    ImageAdapter adpter;
    private int currentSectionIndex = -1;
    int height = 280;
    protected List<ImageMessage> imageList = new ArrayList();
    protected HLActivity mContext;
    private ImageButton mHideImgButton;
    /* access modifiers changed from: private */
    public TextView mPageTextView;
    private ArrayList<String> mSnapshots;
    private int showSize = 3;
    /* access modifiers changed from: private */
    public int showingIndex = -1;
    /* access modifiers changed from: private */
    public int toShowIndex = 0;
    int width = 320;

    /* renamed from: com.hl.android.view.gallary.base.AbstractGalley$ImageAdapter */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            this.mContext = c;
        }

        public int getCount() {
            if (AbstractGalley.this.imageList != null) {
                return AbstractGalley.this.imageList.size();
            }
            return 0;
        }

        public Object getItem(int position) {
            if (AbstractGalley.this.imageList != null) {
                return AbstractGalley.this.imageList.get(position);
            }
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(this.mContext);
            imageView.setScaleType(ScaleType.FIT_XY);
            int imageHeight = AbstractGalley.this.height;
            if (BookSetting.GALLEYCODE == 1) {
                imageHeight = (int) (1.2f * ((float) AbstractGalley.this.height));
            }
            imageView.setLayoutParams(new LayoutParams(AbstractGalley.this.width, imageHeight));
            if (((ImageMessage) AbstractGalley.this.imageList.get(position)).getIsNull().equals(AbstractGalley.ISTRUE)) {
                AbstractGalley.this.setWaitLoad(imageView);
            } else {
                imageView.setImageBitmap(((ImageMessage) AbstractGalley.this.imageList.get(position)).getImage());
            }
            return imageView;
        }

        public float getScale(boolean focused, int offset) {
            return Math.max(0.0f, 1.0f / ((float) Math.pow(2.0d, (double) Math.abs(offset))));
        }
    }

    /* renamed from: com.hl.android.view.gallary.base.AbstractGalley$MyThread */
    class MyThread extends Thread {
        int index;

        public MyThread(int index2) {
            this.index = index2;
        }

        public void run() {
            if (((ImageMessage) AbstractGalley.this.imageList.get(this.index)).getIsNull().equals(AbstractGalley.ISTRUE)) {
                Log.d("hl", "hl show index paths is " + ((ImageMessage) AbstractGalley.this.imageList.get(this.index)).getPath());
                Bitmap b = AbstractGalley.this.getBitmap(((ImageMessage) AbstractGalley.this.imageList.get(this.index)).getPath(), AbstractGalley.this.width, AbstractGalley.this.height);
                if (b != null) {
                    ((ImageMessage) AbstractGalley.this.imageList.get(this.index)).setImage(b);
                    ((ImageMessage) AbstractGalley.this.imageList.get(this.index)).setIsNull(AbstractGalley.ISFALSE);
                }
            }
            AbstractGalley.this.refreshAdapter();
        }
    }

    /* access modifiers changed from: protected */
    public abstract Bitmap getBitmap(String str, int i, int i2);

    /* access modifiers changed from: protected */
    public abstract RelativeLayout.LayoutParams getGalleryLp();

    /* access modifiers changed from: protected */
    public abstract float getSizeRatio();

    /* access modifiers changed from: protected */
    public abstract void setWaitLoad(ImageView imageView);

    public AbstractGalley(Context context) {
        super(context);
        setSpacing(5);
        this.mContext = (HLActivity) context;
        this.height = (int) (((float) BookSetting.SNAPSHOTS_HEIGHT) * getSizeRatio());
        this.width = (int) (((float) BookSetting.SNAPSHOTS_WIDTH) * getSizeRatio());
        this.showSize = ScreenUtils.getScreenWidth((Activity) context) / this.width;
        if (this.showSize < 3) {
            this.showSize = 3;
        }
        this.adpter = new ImageAdapter(context);
        setAdapter(this.adpter);
        setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                AbstractGalley.this.mPageTextView.setText((AbstractGalley.this.getSelectedItemPosition() + 1) + "/" + AbstractGalley.this.imageList.size());
                AbstractGalley.this.toShowIndex = position;
                AbstractGalley.this.showItem();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mPageTextView = new TextView(context);
        this.mPageTextView.setText("1/5");
        this.mPageTextView.setGravity(1);
        this.mPageTextView.setTypeface(Typeface.defaultFromStyle(1));
        this.mPageTextView.getPaint().setFakeBoldText(true);
        this.mPageTextView.setTextColor(-1);
        this.mPageTextView.setBackgroundColor(0);
        this.mPageTextView.setVisibility(8);
        RelativeLayout.LayoutParams textViewLp = new RelativeLayout.LayoutParams(-2, -2);
        textViewLp.addRule(14);
        textViewLp.addRule(12);
        this.mPageTextView.setLayoutParams(textViewLp);
        this.mHideImgButton = new ImageButton(context);
        this.mHideImgButton.setBackgroundResource(C0048R.drawable.downgallerybtn);
        RelativeLayout.LayoutParams hideBtnLp = new RelativeLayout.LayoutParams(120, 48);
        hideBtnLp.addRule(12);
        hideBtnLp.addRule(14);
        this.mHideImgButton.setLayoutParams(hideBtnLp);
        this.mHideImgButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AbstractGalley.this.hideGalleryInfor();
                BookController.getInstance().getViewPage().playVideo();
                BookState.getInstance().setPlayViewPage();
            }
        });
        setBackgroundResource(C0048R.drawable.tbp);
        setLayoutParams(getGalleryLp());
    }

    public TextView getPageTextView() {
        return this.mPageTextView;
    }

    public ImageButton getHideImgButton() {
        return this.mHideImgButton;
    }

    /* access modifiers changed from: private */
    public void showItem() {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (AbstractGalley.this.showingIndex != AbstractGalley.this.toShowIndex) {
                    AbstractGalley.this.showingIndex = AbstractGalley.this.toShowIndex;
                    if (AbstractGalley.this.toShowIndex < AbstractGalley.this.imageList.size()) {
                        AbstractGalley.this.addImage(AbstractGalley.this.toShowIndex);
                    }
                }
            }
        };
        new Thread() {
            public void run() {
                int myIndex = AbstractGalley.this.toShowIndex;
                try {
                    sleep(500);
                    if (myIndex == AbstractGalley.this.toShowIndex) {
                        handler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void showGalleryInfor() {
        if (this.mPageTextView != null) {
            this.mPageTextView.setVisibility(0);
            this.mPageTextView.bringToFront();
        }
        if (this.mHideImgButton != null) {
            this.mHideImgButton.setVisibility(0);
            this.mHideImgButton.bringToFront();
        }
        setVisibility(0);
        if (this.mContext.adViewLayout != null) {
            this.mContext.adViewLayout.setVisibility(8);
        }
    }

    public void hideGalleryInfor() {
        if (this.mPageTextView != null) {
            this.mPageTextView.setVisibility(4);
        }
        if (this.mHideImgButton != null) {
            this.mHideImgButton.setVisibility(4);
        }
        setVisibility(4);
        if (this.mContext.adViewLayout != null) {
            this.mContext.adViewLayout.setVisibility(0);
        }
    }

    public int getCurrentSectionIndex() {
        return this.currentSectionIndex;
    }

    public void setCurrentSectionIndex(int currentSectionIndex2) {
        this.currentSectionIndex = currentSectionIndex2;
    }

    /* access modifiers changed from: protected */
    public ArrayList<String> getSnashots() {
        return this.mSnapshots;
    }

    public void setSnapshots(ArrayList<String> snapshots) {
        this.mSnapshots = snapshots;
        recycle();
        Iterator i$ = snapshots.iterator();
        while (i$.hasNext()) {
            String snapshot = (String) i$.next();
            ImageMessage im = new ImageMessage();
            im.setPath(snapshot);
            im.setIsNull(ISTRUE);
            this.imageList.add(im);
        }
        Log.d("hl", "show index is " + this.showingIndex);
        showItem();
        this.adpter.notifyDataSetChanged();
    }

    public void playAnimation() {
    }

    public void recycle() {
        ImageManager.clearImage(this.imageList, 0, this.imageList.size());
        this.imageList.clear();
    }

    public void refreshAdapter() {
        BookController.getInstance().hlActivity.runOnUiThread(new Runnable() {
            public void run() {
                AbstractGalley.this.adpter.notifyDataSetChanged();
            }
        });
    }

    public void addImage(int args) {
        int start = this.toShowIndex - ((this.showSize / 2) + (this.showSize % 2));
        int end = this.toShowIndex + (this.showSize / 2) + (this.showSize % 2);
        if (start < 0) {
            start = 0;
        }
        if (end >= this.imageList.size()) {
            end = this.imageList.size() - 1;
        }
        this.imageList = ImageManager.clearImage(this.imageList, start, end);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = start; i <= end; i++) {
            Log.d("hl", "hl requeir index paths is " + ((ImageMessage) this.imageList.get(i)).getPath());
            pool.execute(new MyThread(i));
        }
        pool.shutdown();
    }

    public void setHideBtn(ImageButton hideBtn) {
        this.mHideImgButton = hideBtn;
        this.mHideImgButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                AbstractGalley.this.setVisibility(8);
                AbstractGalley.this.hideGalleryInfor();
            }
        });
    }
}
