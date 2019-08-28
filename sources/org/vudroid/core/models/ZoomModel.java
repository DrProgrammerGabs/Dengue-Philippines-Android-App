package org.vudroid.core.models;

import org.vudroid.core.events.BringUpZoomControlsEvent;
import org.vudroid.core.events.EventDispatcher;
import org.vudroid.core.events.ZoomChangedEvent;
import org.vudroid.core.events.ZoomListener.CommitZoomEvent;

public class ZoomModel extends EventDispatcher {
    private static final float INCREMENT_DELTA = 0.05f;
    private boolean horizontalScrollEnabled;
    private boolean isCommited;
    private float zoom = 1.0f;

    public void setZoom(float zoom2) {
        float zoom3 = Math.max(zoom2, 1.0f);
        if (zoom3 > 4.0f) {
            zoom3 = 4.0f;
        }
        if (zoom3 < 1.05f) {
            zoom3 = 1.0f;
        }
        if (this.zoom != zoom3) {
            float oldZoom = this.zoom;
            this.zoom = zoom3;
            this.isCommited = false;
            dispatch(new ZoomChangedEvent(zoom3, oldZoom));
        }
    }

    public void setZoomNoDispatch(float zoom2) {
        float zoom3 = Math.max(zoom2, 1.0f);
        if (zoom3 < 1.05f) {
            zoom3 = 1.0f;
        }
        if (this.zoom != zoom3) {
            this.zoom = zoom3;
            this.isCommited = false;
        }
    }

    public float getZoom() {
        return this.zoom;
    }

    public void increaseZoom() {
        setZoom(getZoom() + INCREMENT_DELTA);
    }

    public void decreaseZoom() {
        setZoom(getZoom() - INCREMENT_DELTA);
    }

    public void toggleZoomControls() {
        dispatch(new BringUpZoomControlsEvent());
    }

    public void setHorizontalScrollEnabled(boolean horizontalScrollEnabled2) {
        this.horizontalScrollEnabled = horizontalScrollEnabled2;
    }

    public boolean isHorizontalScrollEnabled() {
        return this.horizontalScrollEnabled;
    }

    public boolean canDecrement() {
        return this.zoom > 1.0f;
    }

    public void commit() {
        if (!this.isCommited) {
            this.isCommited = true;
            dispatch(new CommitZoomEvent());
        }
    }
}
