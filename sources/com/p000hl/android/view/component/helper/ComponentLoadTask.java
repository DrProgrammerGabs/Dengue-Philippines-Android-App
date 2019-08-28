package com.p000hl.android.view.component.helper;

import android.os.AsyncTask;
import android.view.View;
import com.p000hl.android.view.component.inter.Component;

/* renamed from: com.hl.android.view.component.helper.ComponentLoadTask */
public class ComponentLoadTask extends AsyncTask<Component, Integer, Component> {
    /* access modifiers changed from: protected */
    public Component doInBackground(Component... params) {
        params[0].load();
        return params[0];
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Component component) {
        ((View) component).postInvalidate();
        super.onPostExecute(component);
    }
}
