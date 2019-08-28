package com.p000hl.android.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.p000hl.android.book.entity.SectionEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.PageEntityController;
import com.p000hl.android.core.utils.BitmapUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.view.component.MYSearchView */
public class MYSearchView extends RelativeLayout {
    private static final int ID_LAYOUT_EDITTEXT = 65552;
    /* access modifiers changed from: private */
    public ArrayList<Description> descriptions;
    /* access modifiers changed from: private */
    public EditText editText4Search;
    private ListView listView4SearchResult;
    public BaseAdapter listViewAdapter;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public ArrayList<String> showSnapshotPageIDs;

    /* renamed from: com.hl.android.view.component.MYSearchView$Description */
    class Description {
        public String mDescription;
        public String mSectionID;
        public String mTittle;
        public String mpageID;

        public Description(String mSectionID2, String mpageID2, String mDescription2, String pageTittle) {
            this.mSectionID = mSectionID2;
            this.mpageID = mpageID2;
            this.mDescription = mDescription2;
            this.mTittle = pageTittle;
        }
    }

    public MYSearchView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        RelativeLayout layout = new RelativeLayout(this.mContext);
        layout.setId(ID_LAYOUT_EDITTEXT);
        this.editText4Search = new EditText(this.mContext);
        this.editText4Search.setSingleLine(true);
        this.editText4Search.setHint(C0048R.string.search_hint);
        this.editText4Search.setBackgroundColor(-1);
        layout.setBackgroundResource(C0048R.drawable.indesign_colle_headbgimg);
        addView(layout, new LayoutParams(-1, -2));
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(13);
        layoutParams.setMargins(5, 5, 5, 5);
        layout.addView(this.editText4Search, layoutParams);
        this.listView4SearchResult = new ListView(this.mContext);
        this.listView4SearchResult.setBackgroundResource(C0048R.drawable.indesign_colle_bgimg);
        LayoutParams params4ShowMark = new LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, -1);
        params4ShowMark.addRule(3, layout.getId());
        addView(this.listView4SearchResult, params4ShowMark);
        this.listView4SearchResult.setFadingEdgeLength(0);
        this.listView4SearchResult.setCacheColorHint(0);
        this.listView4SearchResult.setSelector(new ColorDrawable(0));
        BookController bookController = BookController.getInstance();
        int sectionCount = bookController.getBook().getSections().size();
        this.descriptions = new ArrayList<>();
        this.showSnapshotPageIDs = new ArrayList<>();
        for (int j = 0; j < sectionCount; j++) {
            for (int i = 0; i < ((SectionEntity) bookController.getBook().getSections().get(j)).getPages().size(); i++) {
                PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(this.mContext, (String) ((SectionEntity) bookController.getBook().getSections().get(j)).getPages().get(i));
                String pageDescription = pageEntity.getDescription();
                String pageTittle = pageEntity.getTitle();
                if (pageDescription != null || pageTittle != null) {
                    ArrayList<Description> arrayList = this.descriptions;
                    arrayList.add(new Description(((SectionEntity) bookController.getBook().getSections().get(j)).getID(), pageEntity.getID(), pageDescription, pageTittle));
                }
            }
        }
        this.listViewAdapter = new BaseAdapter() {
            public View getView(int position, View convertView, ViewGroup parent) {
                RelativeLayout layout = new RelativeLayout(MYSearchView.this.mContext);
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(HttpStatus.SC_MULTIPLE_CHOICES, 100);
                layout.setLayoutParams(layoutParams);
                if (MYSearchView.this.showSnapshotPageIDs.size() == 0) {
                    TextView textView = new TextView(MYSearchView.this.mContext);
                    textView.setText(C0048R.string.no_search);
                    textView.setTextSize(20.0f);
                    textView.setGravity(17);
                    textView.setTextColor(-1);
                    LayoutParams layoutParams2 = new LayoutParams(-1, -1);
                    layoutParams2.addRule(13);
                    layout.addView(textView, layoutParams2);
                } else {
                    ImageView imageView = new ImageView(MYSearchView.this.mContext);
                    imageView.setId(196624);
                    Bitmap bitmap = BitmapUtils.getBitMap(BookController.getInstance().getSnapshotIdByPageId((String) MYSearchView.this.showSnapshotPageIDs.get(position)), MYSearchView.this.mContext);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ScaleType.FIT_XY);
                    LayoutParams params4image = new LayoutParams((bitmap.getWidth() * 60) / bitmap.getHeight(), 60);
                    params4image.addRule(15);
                    params4image.leftMargin = 20;
                    layout.addView(imageView, params4image);
                    LinearLayout linearLayout = new LinearLayout(MYSearchView.this.mContext);
                    linearLayout.setOrientation(1);
                    TextView textView4tittle = new TextView(MYSearchView.this.mContext);
                    textView4tittle.setTextSize(15.0f);
                    textView4tittle.setEllipsize(TruncateAt.valueOf("END"));
                    textView4tittle.setSingleLine(true);
                    PageEntity pageEntity = PageEntityController.getInstance().getPageEntityByPageId(MYSearchView.this.mContext, (String) MYSearchView.this.showSnapshotPageIDs.get(position));
                    String aa = pageEntity.getTitle().replaceAll(MYSearchView.this.editText4Search.getText().toString().trim(), "<font color=\"#ffff00\">" + MYSearchView.this.editText4Search.getText().toString().trim() + "</font>");
                    String bb = pageEntity.getDescription().replaceAll(MYSearchView.this.editText4Search.getText().toString().trim(), "<font color=\"#ffff00\">" + MYSearchView.this.editText4Search.getText().toString().trim() + "</font>");
                    textView4tittle.setText(Html.fromHtml(aa));
                    textView4tittle.setTextColor(-1);
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams3.setMargins(20, 0, 20, 0);
                    linearLayout.addView(textView4tittle, layoutParams3);
                    TextView textView2 = new TextView(MYSearchView.this.mContext);
                    textView2.setTextSize(12.0f);
                    textView2.setEllipsize(TruncateAt.valueOf("END"));
                    textView2.setSingleLine(true);
                    textView2.setText(Html.fromHtml(bb));
                    textView2.setTextColor(-1);
                    LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams4.setMargins(20, 0, 20, 0);
                    linearLayout.addView(textView2, layoutParams4);
                    LayoutParams params4LinearLayout = new LayoutParams(-1, -2);
                    params4LinearLayout.addRule(15);
                    params4LinearLayout.addRule(1, imageView.getId());
                    layout.addView(linearLayout, params4LinearLayout);
                }
                return layout;
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public Object getItem(int position) {
                return MYSearchView.this.showSnapshotPageIDs.get(position);
            }

            public int getCount() {
                if (MYSearchView.this.showSnapshotPageIDs.size() == 0) {
                    return 1;
                }
                return MYSearchView.this.showSnapshotPageIDs.size();
            }
        };
        this.listView4SearchResult.setAdapter(this.listViewAdapter);
        this.listView4SearchResult.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (MYSearchView.this.showSnapshotPageIDs.size() != 0) {
                    BookController.getInstance().playPageById((String) MYSearchView.this.showSnapshotPageIDs.get(position));
                    ((HLLayoutActivity) MYSearchView.this.mContext).getUPNav().dismiss();
                    ((InputMethodManager) MYSearchView.this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(MYSearchView.this.getWindowToken(), 0);
                }
            }
        });
        this.editText4Search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                MYSearchView.this.showSnapshotPageIDs.clear();
                Iterator i$ = MYSearchView.this.descriptions.iterator();
                while (i$.hasNext()) {
                    Description curDescription = (Description) i$.next();
                    if (MYSearchView.this.editText4Search.getText().toString().isEmpty()) {
                        break;
                    } else if ((curDescription.mDescription + curDescription.mTittle).contains(MYSearchView.this.editText4Search.getText().toString().trim())) {
                        MYSearchView.this.showSnapshotPageIDs.add(curDescription.mpageID);
                    }
                }
                MYSearchView.this.listViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
