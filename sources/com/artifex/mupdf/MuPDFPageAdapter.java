package com.artifex.mupdf;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.concurrent.RejectedExecutionException;

public class MuPDFPageAdapter extends BaseAdapter {
    private final Context mContext;
    /* access modifiers changed from: private */
    public final MuPDFCore mCore;
    /* access modifiers changed from: private */
    public final SparseArray<PointF> mPageSizes = new SparseArray<>();

    public MuPDFPageAdapter(Context c, MuPDFCore core) {
        this.mContext = c;
        this.mCore = core;
    }

    public int getCount() {
        return this.mCore.countPages();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final MuPDFPageView pageView;
        if (convertView == null) {
            pageView = new MuPDFPageView(this.mContext, this.mCore, new Point(parent.getWidth(), parent.getHeight()));
        } else {
            pageView = (MuPDFPageView) convertView;
        }
        PointF pageSize = (PointF) this.mPageSizes.get(position);
        if (pageSize != null) {
            pageView.setPage(position, pageSize);
        } else {
            pageView.blank(position);
            try {
                new AsyncTask<Void, Void, PointF>() {
                    /* access modifiers changed from: protected */
                    public PointF doInBackground(Void... arg0) {
                        return MuPDFPageAdapter.this.mCore.getPageSize(position);
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(PointF result) {
                        super.onPostExecute(result);
                        MuPDFPageAdapter.this.mPageSizes.put(position, result);
                        if (pageView.getPage() == position) {
                            pageView.setPage(position, result);
                        }
                    }
                }.execute(new Void[]{null});
            } catch (RejectedExecutionException e) {
                PointF result = this.mCore.getPageSize(position);
                this.mPageSizes.put(position, result);
                if (pageView.getPage() == position) {
                    pageView.setPage(position, result);
                }
            }
        }
        return pageView;
    }
}
