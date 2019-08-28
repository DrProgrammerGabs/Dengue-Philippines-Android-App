package com.p000hl.android.view.component.bookmark;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLLayoutActivity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.Button4Play;

@SuppressLint({"ViewConstructor"})
/* renamed from: com.hl.android.view.component.bookmark.BookMarkView */
public class BookMarkView extends RelativeLayout {
    private static final int ID_MARK_DELETEIMAGE = 100904;
    private static final int ID_MARK_IMAGE = 100903;
    private static final int ID_MARK_TEXT = 100905;
    public static boolean showDelete = false;
    private ImageView deleteView;
    private ImageView img;
    /* access modifiers changed from: private */
    public HLLayoutActivity mActivity;
    /* access modifiers changed from: private */
    public int mPosition;
    private TextView text;

    public BookMarkView(HLLayoutActivity activity, int position) {
        super(activity);
        this.mActivity = activity;
        this.mPosition = position;
        initViews();
        setImageBitmap(position);
    }

    private Bitmap getImageBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.img.getDrawable();
        if (bitmapDrawable != null) {
            return bitmapDrawable.getBitmap();
        }
        return null;
    }

    private void setShowDelete() {
        if (showDelete) {
            this.deleteView.setVisibility(0);
        } else {
            this.deleteView.setVisibility(4);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setImageBitmap(int position) {
        PageEntity page = BookController.getInstance().getPageEntityByID((String) BookMarkManager.getMarkList(this.mActivity).get(position));
        Bitmap oldBitmap = getImageBitmap();
        this.img.setImageBitmap(BookController.getInstance().getSmallSnapShotCashImage(page));
        this.text.setText(page.getTitle());
        this.mPosition = position;
        setShowDelete();
        if (oldBitmap != null && !oldBitmap.isRecycled()) {
            oldBitmap.recycle();
        }
    }

    private void initViews() {
        this.img = new ImageView(this.mActivity);
        this.img.setId(ID_MARK_IMAGE);
        LayoutParams imgLp = new LayoutParams(100, 80);
        this.img.setPadding(10, 0, 0, 0);
        imgLp.addRule(9);
        addView(this.img, imgLp);
        this.deleteView = new ImageView(this.mActivity);
        this.deleteView.setTag(Boolean.valueOf(true));
        this.deleteView.setImageResource(C0048R.drawable.mark_delete_btn);
        this.deleteView.setId(ID_MARK_DELETEIMAGE);
        LayoutParams checkLp = new LayoutParams(45, 45);
        checkLp.addRule(11);
        checkLp.addRule(15);
        this.deleteView.setPadding(0, 0, 15, 0);
        addView(this.deleteView, checkLp);
        this.text = new TextView(this.mActivity);
        this.text.setClickable(true);
        this.text.setTextColor(Button4Play.BTN_TOUCH_COLOR);
        this.text.setLines(3);
        this.text.setGravity(16);
        this.text.setId(ID_MARK_TEXT);
        LayoutParams titleLp = new LayoutParams(-1, -1);
        this.text.setPadding(15, 0, 15, 0);
        titleLp.addRule(1, this.img.getId());
        titleLp.addRule(0, this.deleteView.getId());
        titleLp.addRule(15);
        addView(this.text, titleLp);
        this.deleteView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BookMarkManager.deleteMark(BookMarkView.this.mActivity, BookMarkView.this.mPosition);
                BookMarkView.this.mActivity.refreshMark();
            }
        });
        setShowDelete();
        this.img.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BookMarkView.showDelete) {
                    BookController.getInstance().playPageById((String) BookMarkManager.getMarkList(BookMarkView.this.mActivity).get(BookMarkView.this.mPosition));
                }
            }
        });
    }
}
