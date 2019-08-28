package com.p000hl.android.view.plugin.sound;

import android.media.AudioRecord;
import android.util.Log;

/* renamed from: com.hl.android.view.plugin.sound.RecordThread */
public class RecordThread extends Thread {
    private static int SAMPLE_RATE_IN_HZ = 8000;

    /* renamed from: ar */
    private AudioRecord f64ar = new AudioRecord(1, SAMPLE_RATE_IN_HZ, 2, 2, this.f65bs);

    /* renamed from: bs */
    private int f65bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, 2, 2);
    private boolean isRun = false;

    public void run() {
        super.run();
        this.f64ar.startRecording();
        byte[] buffer = new byte[this.f65bs];
        this.isRun = true;
        while (this.isRun) {
            int r = this.f64ar.read(buffer, 0, this.f65bs);
            int v = 0;
            for (int i = 0; i < buffer.length; i++) {
                v += buffer[i] * buffer[i];
            }
            Log.d("spl", String.valueOf(((float) v) / ((float) r)));
            this.f64ar.stop();
        }
    }

    public void pause() {
        this.isRun = false;
    }

    public void start() {
        if (!this.isRun) {
            super.start();
        }
    }
}
