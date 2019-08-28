package org.vudroid.core.multitouch;

import android.view.MotionEvent;

public interface MultiTouchZoom {
    boolean isResetLastPointAfterZoom();

    boolean onTouchEvent(MotionEvent motionEvent);

    void setResetLastPointAfterZoom(boolean z);
}
