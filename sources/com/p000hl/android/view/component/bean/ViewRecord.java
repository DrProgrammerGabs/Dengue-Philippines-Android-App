package com.p000hl.android.view.component.bean;

/* renamed from: com.hl.android.view.component.bean.ViewRecord */
public class ViewRecord implements Cloneable {
    public int animationIndex = 0;
    public int mHeight = 0;
    public float mRotation = 0.0f;
    public float mScaleX = 1.0f;
    public float mScaleY = 1.0f;
    public int mWidth = 0;

    /* renamed from: mX */
    public float f28mX = 0.0f;

    /* renamed from: mY */
    public float f29mY = 0.0f;
    public long usetime = 0;

    public ViewRecord getClone() {
        try {
            return (ViewRecord) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
