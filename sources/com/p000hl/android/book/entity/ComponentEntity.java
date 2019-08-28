package com.p000hl.android.book.entity;

import android.graphics.PointF;
import android.view.animation.Animation;
import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.ComponentEntity */
public class ComponentEntity {
    public boolean IsEnableGyroHor = false;
    public float alpha = 1.0f;
    public int animationRepeat = 1;
    public ArrayList<AnimationEntity> anims;
    public boolean autoLoop;
    public ArrayList<BehaviorEntity> behaviors;
    public String className;
    public String componentId;
    public String curText = "";
    public int currentPlayTime = 0;
    public Animation currentPlayingAnim;
    public int currentPlayingIndex;
    public int currentRepeat = 0;
    public double delay;
    public String downSourceID;
    public String htmlUrl;
    public double imageScale;
    public String imageType;
    public boolean isAllowUserZoom = false;
    public boolean isHideAtBegining;
    public Boolean isMoveScale = Boolean.valueOf(false);
    private boolean isOnlineSource = false;
    public boolean isPageInnerSlide = false;
    public boolean isPageTweenSlide = false;
    public boolean isPause;
    public boolean isPlayAnimationAtBegining;
    public boolean isPlayVideoOrAudioAtBegining;
    public boolean isPushBack;
    public boolean isStroyTelling;
    public boolean isSynchronized;
    private LinkageObj linkageObj;
    public String localSourceId;
    public String multiMediaUrl;
    public float oldHeight;
    public float oldWidth;
    public ArrayList<PointF> ptList = new ArrayList<>();
    public float rotation;
    public boolean showProgress = false;
    public float slideBindingAlha = 1.0f;
    public int slideBindingHeight = 0;
    public int slideBindingWidth = 0;
    public int slideBindingX = 0;
    public int slideBindingY = 0;
    public float sliderHorRate = 0.0f;
    public float sliderVerRate = 1.0f;

    /* renamed from: x */
    public int f7x;
    public int xOffset = 0;

    /* renamed from: y */
    public int f8y;
    public int yOffset = 0;
    public String zoomType = "zoom_out";

    public LinkageObj getLinkPageObj() {
        if (this.linkageObj == null) {
            this.linkageObj = new LinkageObj();
        }
        return this.linkageObj;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String imageType2) {
        this.imageType = imageType2;
    }

    public double getImageScale() {
        return this.imageScale;
    }

    public void setImageScale(double imageScale2) {
        this.imageScale = imageScale2;
    }

    public String getComponentId() {
        return this.componentId;
    }

    public void setComponentId(String componentId2) {
        this.componentId = componentId2;
    }

    public void setOnlineSource(boolean isOnlineSource2) {
        this.isOnlineSource = isOnlineSource2;
    }

    public boolean isOnlineSource() {
        return this.isOnlineSource;
    }

    public ArrayList<AnimationEntity> getAnims() {
        return this.anims;
    }

    public void setAnims(ArrayList<AnimationEntity> anims2) {
        this.anims = anims2;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation2) {
        this.rotation = rotation2;
    }

    public double getDelay() {
        return this.delay;
    }

    public void setDelay(double delay2) {
        this.delay = delay2;
    }

    public String getLocalSourceId() {
        return this.localSourceId;
    }

    public void setLocalSourceId(String localSourceId2) {
        this.localSourceId = localSourceId2;
    }

    public boolean isSynchronized() {
        return this.isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized2) {
        this.isSynchronized = isSynchronized2;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className2) {
        this.className = className2;
    }

    public boolean isAutoLoop() {
        return this.autoLoop;
    }

    public void setAutoLoop(boolean autoLoop2) {
        this.autoLoop = autoLoop2;
    }

    public String getMultiMediaUrl() {
        return this.multiMediaUrl;
    }

    public void setMultiMediaUrl(String multiMediaUrl2) {
        this.multiMediaUrl = multiMediaUrl2;
    }

    public boolean isAllowUserZoom() {
        return this.isAllowUserZoom;
    }

    public void setAllowUserZoom(boolean isAllowUserZoom2) {
        this.isAllowUserZoom = isAllowUserZoom2;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl2) {
        this.htmlUrl = htmlUrl2;
    }
}
