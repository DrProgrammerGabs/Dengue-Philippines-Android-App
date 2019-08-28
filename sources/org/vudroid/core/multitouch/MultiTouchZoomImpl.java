package org.vudroid.core.multitouch;

import android.view.MotionEvent;
import org.vudroid.core.models.ZoomModel;

public class MultiTouchZoomImpl implements MultiTouchZoom {
    private float lastZoomDistance;
    private boolean resetLastPointAfterZoom;
    private final ZoomModel zoomModel;

    public MultiTouchZoomImpl(ZoomModel zoomModel2) {
        this.zoomModel = zoomModel2;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if ((ev.getAction() & 5) == 5) {
            this.lastZoomDistance = getZoomDistance(ev);
            return true;
        } else if ((ev.getAction() & 6) == 6) {
            this.lastZoomDistance = 0.0f;
            this.zoomModel.commit();
            this.resetLastPointAfterZoom = true;
            return true;
        } else if (ev.getAction() != 2 || this.lastZoomDistance == 0.0f) {
            return false;
        } else {
            float zoomDistance = getZoomDistance(ev);
            this.zoomModel.setZoom((this.zoomModel.getZoom() * zoomDistance) / this.lastZoomDistance);
            this.lastZoomDistance = zoomDistance;
            return true;
        }
    }

    private float getZoomDistance(MotionEvent ev) {
        return (float) Math.sqrt(Math.pow((double) (ev.getX(0) - ev.getX(1)), 2.0d) + Math.pow((double) (ev.getY(0) - ev.getY(1)), 2.0d));
    }

    public boolean isResetLastPointAfterZoom() {
        return this.resetLastPointAfterZoom;
    }

    public void setResetLastPointAfterZoom(boolean resetLastPointAfterZoom2) {
        this.resetLastPointAfterZoom = resetLastPointAfterZoom2;
    }
}
