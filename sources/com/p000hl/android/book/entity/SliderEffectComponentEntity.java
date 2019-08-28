package com.p000hl.android.book.entity;

import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.SliderEffectComponentEntity */
public class SliderEffectComponentEntity extends ComponentEntity {
    public boolean isEndToStart;
    public boolean isLoop;
    public boolean isPageInnerSlide;
    public boolean isPageTweenSlide;
    public boolean isUseSlide;
    public int repeat = -1;
    public float slideBindingAlpha;
    public float slideBindingHeight;
    public float slideBindingWidth;
    public float slideBindingX;
    public float slideBindingY;
    public ArrayList<SubImageItem> subItems = new ArrayList<>();
    public String switchType;

    public SliderEffectComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }
}
