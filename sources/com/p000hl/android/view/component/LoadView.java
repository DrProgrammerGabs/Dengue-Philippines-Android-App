package com.p000hl.android.view.component;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/* renamed from: com.hl.android.view.component.LoadView */
public class LoadView extends LinearLayout {
    public ProgressBar loadBar;

    public LoadView(Context context, LayoutParams param) {
        this(context);
        setLayoutParams(param);
    }

    public LoadView(Context context) {
        super(context);
        setBackgroundColor(-16777216);
        getBackground().setAlpha(20);
        this.loadBar = new ProgressBar(context);
        addView(this.loadBar, new LinearLayout.LayoutParams(37, 37));
        setGravity(17);
    }
}
