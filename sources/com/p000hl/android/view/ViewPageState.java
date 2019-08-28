package com.p000hl.android.view;

/* renamed from: com.hl.android.view.ViewPageState */
public class ViewPageState {
    public boolean idied;
    public boolean papared;
    public boolean playing;
    public boolean stopped;

    public void setIdiled() {
        this.papared = false;
        this.stopped = false;
        this.playing = false;
    }

    public void setStoped() {
        this.papared = false;
        this.stopped = true;
        this.playing = false;
    }

    public boolean isPapared() {
        return this.papared;
    }

    public void setPapared(boolean papared2) {
        this.papared = papared2;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void setPlaying(boolean playing2) {
        this.playing = playing2;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void setStopped(boolean stopped2) {
        this.stopped = stopped2;
    }

    public boolean isIdied() {
        return this.idied;
    }

    public void setIdied(boolean idied2) {
        this.idied = idied2;
    }
}
