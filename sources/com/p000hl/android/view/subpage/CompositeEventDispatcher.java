package com.p000hl.android.view.subpage;

import android.view.GestureDetector;
import com.p000hl.android.view.component.inter.Component;
import java.util.ArrayList;

/* renamed from: com.hl.android.view.subpage.CompositeEventDispatcher */
public class CompositeEventDispatcher {
    private static CompositeEventDispatcher eventDispatcher;
    private ArrayList<Component> componentList;
    public GestureDetector mGestureDetector = null;

    public static CompositeEventDispatcher getInstance() {
        if (eventDispatcher == null) {
            eventDispatcher = new CompositeEventDispatcher();
        }
        return eventDispatcher;
    }

    public void init() {
        if (this.componentList != null) {
            this.componentList.clear();
        }
    }

    public void init(GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
    }
}
