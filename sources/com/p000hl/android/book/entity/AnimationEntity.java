package com.p000hl.android.book.entity;

import android.graphics.PointF;
import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.AnimationEntity */
public class AnimationEntity {
    public String AnimationEnterOrQuit;
    public String AnimationType;
    public String AnimationTypeLabel;
    public String ClassName;
    public String CurrentAnimationIndex;
    public String CustomProperties;
    public String Delay;
    public String Duration;
    public String EaseType;
    public String IsKeep = "true";
    public ArrayList<PointF> Points = new ArrayList<>();
    public String Repeat;
    public ArrayList<SeniorAnimationEntity> hEntitys;
    public boolean isKeepEndStatus = true;
}
