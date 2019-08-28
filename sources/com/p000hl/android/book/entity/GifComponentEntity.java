package com.p000hl.android.book.entity;

import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.GifComponentEntity */
public class GifComponentEntity extends ComponentEntity {
    private boolean IsPlayOnetime;
    private ArrayList<String> frameList = new ArrayList<>();
    private double gifDuration;

    public GifComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }

    public boolean isIsPlayOnetime() {
        return this.IsPlayOnetime;
    }

    public void setIsPlayOnetime(boolean isPlayOnetime) {
        this.IsPlayOnetime = isPlayOnetime;
    }

    public double getGifDuration() {
        return this.gifDuration;
    }

    public void setGifDuration(double gifDuration2) {
        this.gifDuration = gifDuration2;
    }

    public ArrayList<String> getFrameList() {
        return this.frameList;
    }

    public void setFrameList(ArrayList<String> frameList2) {
        this.frameList = frameList2;
    }
}
