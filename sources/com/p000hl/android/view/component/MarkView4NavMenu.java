package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLLayoutActivity;
import com.p000hl.android.book.entity.PageEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.bookmark.BookMarkManager;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.component.MarkView4NavMenu */
public class MarkView4NavMenu extends RelativeLayout {
    private static final int ID_LAYOUT_ADDORREMOVE_MARK = 65552;
    private ListView listView4showMark;
    /* access modifiers changed from: private */
    public ImageButton mBtn4addOrRemoveMark;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public BaseAdapter markItemsAdapter;

    public MarkView4NavMenu(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        RelativeLayout layout = new RelativeLayout(this.mContext);
        layout.setId(ID_LAYOUT_ADDORREMOVE_MARK);
        this.mBtn4addOrRemoveMark = new ImageButton(this.mContext);
        if (BookMarkManager.getMarkList((HLLayoutActivity) this.mContext).contains(BookController.getInstance().mainViewPage.getEntity().getID())) {
            this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtndown);
        } else {
            this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtnup);
        }
        layout.setBackgroundResource(C0048R.drawable.indesign_colle_headbgimg);
        addView(layout, new LayoutParams(-1, -2));
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.leftMargin = 5;
        layoutParams.addRule(15);
        layout.addView(this.mBtn4addOrRemoveMark, layoutParams);
        this.listView4showMark = new ListView(this.mContext);
        this.listView4showMark.setBackgroundResource(C0048R.drawable.indesign_colle_bgimg);
        LayoutParams params4ShowMark = new LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, -1);
        params4ShowMark.addRule(3, layout.getId());
        addView(this.listView4showMark, params4ShowMark);
        this.listView4showMark.setFadingEdgeLength(0);
        this.listView4showMark.setCacheColorHint(0);
        this.listView4showMark.setSelector(new ColorDrawable(0));
        this.markItemsAdapter = new BaseAdapter() {
            public View getView(int position, View convertView, ViewGroup parent) {
                RelativeLayout layout = new RelativeLayout(MarkView4NavMenu.this.mContext);
                ImageView imageView = new ImageView(MarkView4NavMenu.this.mContext);
                imageView.setId(262160);
                PageEntity page = BookController.getInstance().getPageEntityByID((String) BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).get((getCount() - 1) - position));
                Bitmap bitmap = BookController.getInstance().getSmallSnapShotCashImage(page);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ScaleType.FIT_XY);
                layout.setLayoutParams(new AbsListView.LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, 100));
                LayoutParams params4image = new LayoutParams((bitmap.getWidth() * 60) / bitmap.getHeight(), 60);
                params4image.addRule(15);
                params4image.leftMargin = 20;
                layout.addView(imageView, params4image);
                LinearLayout linearLayout = new LinearLayout(MarkView4NavMenu.this.mContext);
                linearLayout.setOrientation(1);
                TextView textView4tittle = new TextView(MarkView4NavMenu.this.mContext);
                textView4tittle.setTextSize(15.0f);
                textView4tittle.setEllipsize(TruncateAt.valueOf("END"));
                textView4tittle.setSingleLine(true);
                textView4tittle.setText(page.getTitle());
                textView4tittle.setTextColor(-1);
                LinearLayout.LayoutParams params4textView = new LinearLayout.LayoutParams(-2, -2);
                params4textView.setMargins(20, 0, 20, 0);
                linearLayout.addView(textView4tittle, params4textView);
                TextView textView = new TextView(MarkView4NavMenu.this.mContext);
                textView.setTextSize(12.0f);
                textView.setEllipsize(TruncateAt.valueOf("END"));
                textView.setSingleLine(true);
                textView.setText(page.getDescription());
                textView.setTextColor(-1);
                LinearLayout.LayoutParams params4text = new LinearLayout.LayoutParams(-2, -2);
                params4text.setMargins(20, 0, 20, 0);
                linearLayout.addView(textView, params4text);
                LayoutParams params4LinearLayout = new LayoutParams(-1, -2);
                params4LinearLayout.addRule(15);
                params4LinearLayout.addRule(1, imageView.getId());
                layout.addView(linearLayout, params4LinearLayout);
                return layout;
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public Object getItem(int position) {
                return BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).get(position);
            }

            public int getCount() {
                return BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).size();
            }
        };
        this.listView4showMark.setAdapter(this.markItemsAdapter);
        this.listView4showMark.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookController bookController = BookController.getInstance();
                String pageID = (String) BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).get((MarkView4NavMenu.this.markItemsAdapter.getCount() - 1) - position);
                if (!pageID.equals(BookController.getInstance().mainPageID)) {
                    bookController.playPageById(pageID);
                }
                ((HLLayoutActivity) MarkView4NavMenu.this.mContext).getUPNav().dismiss();
            }
        });
        this.mBtn4addOrRemoveMark.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String curPageId = BookController.getInstance().mainViewPage.getEntity().getID();
                if (!BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).contains(curPageId)) {
                    BookMarkManager.addCurPage((HLLayoutActivity) MarkView4NavMenu.this.mContext);
                    MarkView4NavMenu.this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtndown);
                    return;
                }
                BookMarkManager.deleteMark((HLLayoutActivity) MarkView4NavMenu.this.mContext, BookMarkManager.getMarkList((HLLayoutActivity) MarkView4NavMenu.this.mContext).indexOf(curPageId));
                MarkView4NavMenu.this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtnup);
            }
        });
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        refresh();
    }

    public void refresh() {
        if (this.markItemsAdapter != null) {
            this.markItemsAdapter.notifyDataSetChanged();
            if (BookMarkManager.getMarkList((HLLayoutActivity) this.mContext).contains(BookController.getInstance().mainViewPage.getEntity().getID())) {
                this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtndown);
            } else {
                this.mBtn4addOrRemoveMark.setBackgroundResource(C0048R.drawable.indesign_collectionbtnup);
            }
        }
    }
}
