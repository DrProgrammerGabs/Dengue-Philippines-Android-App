package com.p000hl.android.view.component.moudle.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

/* renamed from: com.hl.android.view.component.moudle.common.CommonImageView */
public class CommonImageView extends ImageView {

    /* renamed from: com.hl.android.view.component.moudle.common.CommonImageView$RecycleBin */
    class RecycleBin {
        private final SparseArray<View> mScrapHeap = new SparseArray<>();

        RecycleBin() {
        }

        public void put(int position, View v) {
            this.mScrapHeap.put(position, v);
        }

        /* access modifiers changed from: 0000 */
        public View get(int position) {
            View result = (View) this.mScrapHeap.get(position);
            if (result != null) {
                this.mScrapHeap.delete(position);
            }
            return result;
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            SparseArray<View> scrapHeap = this.mScrapHeap;
            int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                if (((View) scrapHeap.valueAt(i)) != null) {
                }
            }
            scrapHeap.clear();
        }
    }

    public CommonImageView(Context context) {
        super(context);
    }
}
