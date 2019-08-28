package org.vudroid.core.events;

public class ZoomChangedEvent extends SafeEvent<ZoomListener> {
    private final float newZoom;
    private final float oldZoom;

    public ZoomChangedEvent(float newZoom2, float oldZoom2) {
        this.newZoom = newZoom2;
        this.oldZoom = oldZoom2;
    }

    public void dispatchSafely(ZoomListener listener) {
        listener.zoomChanged(this.newZoom, this.oldZoom);
    }
}
