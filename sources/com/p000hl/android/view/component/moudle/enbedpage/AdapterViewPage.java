package com.p000hl.android.view.component.moudle.enbedpage;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import com.p000hl.android.view.ViewPage;

/* renamed from: com.hl.android.view.component.moudle.enbedpage.AdapterViewPage */
public class AdapterViewPage extends AdapterView<ViewPageAdapter> {
    private ViewPage currentViewPage;

    public AdapterViewPage(Context context) {
        super(context);
    }

    public ViewPageAdapter getAdapter() {
        return null;
    }

    public void setAdapter(ViewPageAdapter adapter) {
    }

    public View getSelectedView() {
        return null;
    }

    public void setSelection(int position) {
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.currentViewPage != null) {
            this.currentViewPage.layout(0, 0, this.currentViewPage.getLayoutParams().width, this.currentViewPage.getLayoutParams().height);
            this.currentViewPage.startPlay();
        }
    }
}
