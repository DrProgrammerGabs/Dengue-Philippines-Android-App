package org.vudroid.core.events;

public interface ZoomListener {

    public static class CommitZoomEvent extends SafeEvent<ZoomListener> {
        public void dispatchSafely(ZoomListener listener) {
            listener.commitZoom();
        }
    }

    void commitZoom();

    void zoomChanged(float f, float f2);
}
