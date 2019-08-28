package com.p000hl.android.view.component.zoom.easing;

/* renamed from: com.hl.android.view.component.zoom.easing.Cubic */
public class Cubic {
    public static float easeOut(float time, float start, float end, float duration) {
        float time2 = (time / duration) - 1.0f;
        return (((time2 * time2 * time2) + 1.0f) * end) + start;
    }
}
