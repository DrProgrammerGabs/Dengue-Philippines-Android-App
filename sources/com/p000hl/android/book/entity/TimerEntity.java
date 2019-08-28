package com.p000hl.android.book.entity;

import org.apache.http.HttpStatus;

/* renamed from: com.hl.android.book.entity.TimerEntity */
public class TimerEntity extends ComponentEntity {
    public String fontColor = "0xcc0000";
    public String fontSize = "20";
    public boolean isPlayMillisecond = true;
    public boolean isPlayOrderbyDesc = true;
    public boolean isStaticType = false;
    private int maxTimer = 1000;

    public int getMaxTimer() {
        if (this.maxTimer < 0) {
            this.maxTimer = HttpStatus.SC_OK;
        }
        return this.maxTimer;
    }

    public void setMaxTimer(int maxTimer2) {
        this.maxTimer = maxTimer2;
    }
}
