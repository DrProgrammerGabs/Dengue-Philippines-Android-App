package org.vudroid.core.events;

public interface DecodingProgressListener {

    public static class DecodingProgressEvent extends SafeEvent<DecodingProgressListener> {
        private final int currentlyDecoding;

        public DecodingProgressEvent(int currentlyDecoding2) {
            this.currentlyDecoding = currentlyDecoding2;
        }

        public void dispatchSafely(DecodingProgressListener listener) {
            listener.decodingProgressChanged(this.currentlyDecoding);
        }
    }

    void decodingProgressChanged(int i);
}
