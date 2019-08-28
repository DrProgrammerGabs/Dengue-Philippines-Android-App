package com.p000hl.android.book.entity;

import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.ContainerEntity */
public class ContainerEntity {

    /* renamed from: ID */
    public String f9ID;
    public boolean IsStroyTelling;
    public ArrayList<AnimationEntity> animations = new ArrayList<>();
    public boolean autoLoop;
    public ArrayList<BehaviorEntity> behaviors = new ArrayList<>();
    public ComponentEntity component = new ComponentEntity();
    public float height;
    public boolean isHideAtBegining;
    public Boolean isMoveScale;
    public boolean isPlayAnimationAtBegining;
    public boolean isPlayVideoOrAudioAtBegining;
    public boolean isPushBack = false;
    public int maxValue;
    public int minValue;
    public String name;
    public float rotation;
    public float width;

    /* renamed from: x */
    public float f10x;

    /* renamed from: y */
    public float f11y;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getID() {
        return this.f9ID;
    }

    public void setID(String iD) {
        this.f9ID = iD;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation2) {
        this.rotation = rotation2;
    }

    public float getX() {
        return this.f10x;
    }

    public void setX(float x) {
        this.f10x = x;
    }

    public float getY() {
        return this.f11y;
    }

    public void setY(float y) {
        this.f11y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width2) {
        this.width = width2;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height2) {
        this.height = height2;
    }

    public boolean isPlayAnimationAtBegining() {
        return this.isPlayAnimationAtBegining;
    }

    public void setPlayAnimationAtBegining(boolean isPlayAnimationAtBegining2) {
        this.isPlayAnimationAtBegining = isPlayAnimationAtBegining2;
    }

    public boolean isPlayVideoOrAudioAtBegining() {
        return this.isPlayVideoOrAudioAtBegining;
    }

    public void setPlayVideoOrAudioAtBegining(boolean isPlayVideoOrAudioAtBegining2) {
        this.isPlayVideoOrAudioAtBegining = isPlayVideoOrAudioAtBegining2;
    }

    public boolean isHideAtBegining() {
        return this.isHideAtBegining;
    }

    public void setHideAtBegining(boolean isHideAtBegining2) {
        this.isHideAtBegining = isHideAtBegining2;
    }

    public ComponentEntity getComponent() {
        return this.component;
    }

    public void setComponent(ComponentEntity component2) {
        this.component = component2;
    }

    public ArrayList<AnimationEntity> getAnimations() {
        return this.animations;
    }

    public void setAnimations(ArrayList<AnimationEntity> animations2) {
        this.animations = animations2;
    }

    public ArrayList<BehaviorEntity> getBehaviors() {
        return this.behaviors;
    }

    public void setBehaviors(ArrayList<BehaviorEntity> behaviors2) {
        this.behaviors = behaviors2;
    }
}
