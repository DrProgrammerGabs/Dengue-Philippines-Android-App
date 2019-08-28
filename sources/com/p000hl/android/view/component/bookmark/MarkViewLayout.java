package com.p000hl.android.view.component.bookmark;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.p000hl.android.C0048R;
import com.p000hl.android.HLLayoutActivity;
import com.p000hl.android.common.BookSetting;

@SuppressLint({"ViewConstructor"})
/* renamed from: com.hl.android.view.component.bookmark.MarkViewLayout */
public class MarkViewLayout extends RelativeLayout {
    private static final int MARK_VIEW_ID_BOTTOM = 5000001;
    private static final int MARK_VIEW_ID_TOP = 5000000;
    /* access modifiers changed from: private */
    public HLLayoutActivity mActivity;
    /* access modifiers changed from: private */
    public MarkItemsAdapter markItemsAdapter;

    public MarkViewLayout(HLLayoutActivity activity) {
        super(activity);
        this.mActivity = activity;
        MarkItemsAdapter markItemsAdapter2 = new MarkItemsAdapter(activity);
        this.markItemsAdapter = markItemsAdapter2;
        RelativeLayout markTop = new RelativeLayout(this.mActivity);
        markTop.setPadding(0, 0, 0, -5);
        ImageButton imageButton = new ImageButton(this.mActivity);
        imageButton.setBackgroundResource(C0048R.drawable.mark_title);
        LayoutParams titleLp = new LayoutParams(-2, 52);
        ImageButton closeBtn = new ImageButton(this.mActivity);
        closeBtn.setBackgroundResource(C0048R.drawable.mark_close_btn);
        LayoutParams closeLp = new LayoutParams(70, 52);
        closeLp.addRule(11);
        C01311 r0 = new OnClickListener() {
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) BookSetting.SCREEN_HEIGHT);
                animation.setDuration(300);
                animation.setRepeatCount(0);
                MarkViewLayout.this.setAnimation(animation);
                animation.startNow();
                MarkViewLayout.this.setVisibility(8);
            }
        };
        closeBtn.setOnClickListener(r0);
        LayoutParams markTopParams = new LayoutParams(-2, 47);
        markTopParams.addRule(10);
        markTop.setId(MARK_VIEW_ID_TOP);
        markTop.addView(imageButton, titleLp);
        markTop.addView(closeBtn, closeLp);
        LinearLayout markBottom = new LinearLayout(this.mActivity);
        markBottom.setPadding(0, -6, 0, 0);
        markBottom.setOrientation(0);
        markBottom.setId(MARK_VIEW_ID_BOTTOM);
        ImageButton addBtn = new ImageButton(this.mActivity);
        addBtn.setBackgroundResource(C0048R.drawable.mark_add_btn);
        addBtn.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams addLp = new LinearLayout.LayoutParams(130, 37);
        C01322 r02 = new OnClickListener() {
            public void onClick(View v) {
                BookMarkManager.addCurPage(MarkViewLayout.this.mActivity);
            }
        };
        addBtn.setOnClickListener(r02);
        markBottom.addView(addBtn, addLp);
        ImageButton editBtn = new ImageButton(this.mActivity);
        editBtn.setBackgroundResource(C0048R.drawable.mark_edit_btn);
        editBtn.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams editLp = new LinearLayout.LayoutParams(130, 37);
        C01333 r03 = new OnClickListener() {
            public void onClick(View v) {
                BookMarkView.showDelete = !BookMarkView.showDelete;
                MarkViewLayout.this.markItemsAdapter.notifyDataSetChanged();
            }
        };
        editBtn.setOnClickListener(r03);
        markBottom.addView(editBtn, editLp);
        LayoutParams markBottomParams = new LayoutParams(-2, -2);
        markBottomParams.addRule(12);
        ListView markItems = new ListView(this.mActivity);
        markItems.setBackgroundColor(-1118482);
        markItems.setCacheColorHint(0);
        markItems.setSelector(17170445);
        LayoutParams markItemsParams = new LayoutParams(259, -1);
        markItemsParams.addRule(3, markTop.getId());
        markItemsParams.addRule(2, markBottom.getId());
        markItemsParams.addRule(11);
        addView(markTop, markTopParams);
        addView(markBottom, markBottomParams);
        addView(markItems, markItemsParams);
        markItems.setAdapter(this.markItemsAdapter);
    }

    public void refresh() {
        this.markItemsAdapter.notifyDataSetChanged();
    }
}
