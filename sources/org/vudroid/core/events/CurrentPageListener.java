package org.vudroid.core.events;

public interface CurrentPageListener {

    public static class CurrentPageChangedEvent extends SafeEvent<CurrentPageListener> {
        private final int pageIndex;

        public CurrentPageChangedEvent(int pageIndex2) {
            this.pageIndex = pageIndex2;
        }

        public void dispatchSafely(CurrentPageListener listener) {
            listener.currentPageChanged(this.pageIndex);
        }
    }

    void currentPageChanged(int i);
}
