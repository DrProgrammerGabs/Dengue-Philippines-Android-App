package com.p000hl.android.book.entity;

import com.p000hl.android.controller.BookController;
import com.p000hl.android.core.utils.StringUtils;
import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.PageEntity */
public class PageEntity {

    /* renamed from: ID */
    private String f15ID;
    public boolean IsGroupPlay = false;
    private long PageChangeEffectDuration;
    private ContainerEntity background = new ContainerEntity();
    public String beCoveredPageID = "";
    private ArrayList<ContainerEntity> containers = new ArrayList<>();
    private String description;
    private boolean enableNavigation;
    public boolean enablePageTurnByHand = true;
    private float height;
    private boolean isCashSnapshot = false;
    private String linkPageID;
    private ArrayList<String> navePageIds;
    private String pageChangeEffectDir;
    private String pageChangeEffectType;
    private PlaySequenceEntity sequence = new PlaySequenceEntity();
    private String snapID = "";
    private String title;
    private String type;
    private float width;

    public String getSnapShotID() {
        if (StringUtils.isEmpty(this.snapID)) {
            this.snapID = BookController.getInstance().getBook().getSnapshotIdByPageId(this.f15ID);
        }
        return this.snapID;
    }

    public void setSnapShotID(String snapShotID) {
        this.snapID = snapShotID;
    }

    public boolean isCashSnapshot() {
        return this.isCashSnapshot;
    }

    public void setSnapShotType(boolean snapShotType) {
        this.isCashSnapshot = snapShotType;
    }

    public String getID() {
        return this.f15ID;
    }

    public void setID(String iD) {
        this.f15ID = iD;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
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

    public boolean isEnableNavigation() {
        return this.enableNavigation;
    }

    public void setEnableNavigation(boolean enableNavigation2) {
        this.enableNavigation = enableNavigation2;
    }

    public ArrayList<ContainerEntity> getContainers() {
        return this.containers;
    }

    public void setContainers(ArrayList<ContainerEntity> containers2) {
        this.containers = containers2;
    }

    public ContainerEntity getBackground() {
        return this.background;
    }

    public void setBackground(ContainerEntity background2) {
        this.background = background2;
    }

    public PlaySequenceEntity getSequence() {
        return this.sequence;
    }

    public void setSequence(PlaySequenceEntity sequence2) {
        this.sequence = sequence2;
    }

    public ArrayList<String> getNavePageIds() {
        if (this.navePageIds == null) {
            this.navePageIds = new ArrayList<>();
        }
        return this.navePageIds;
    }

    public void setNavePageIds(ArrayList<String> navePageIds2) {
        this.navePageIds = navePageIds2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getLinkPageID() {
        return this.linkPageID;
    }

    public void setLinkPageID(String linkPageID2) {
        this.linkPageID = linkPageID2;
    }

    public String getPageChangeEffectType() {
        return this.pageChangeEffectType;
    }

    public void setPageChangeEffectType(String pageChangeEffectType2) {
        this.pageChangeEffectType = pageChangeEffectType2;
    }

    public String getPageChangeEffectDir() {
        return this.pageChangeEffectDir;
    }

    public void setPageChangeEffectDir(String pageChangeEffectDir2) {
        this.pageChangeEffectDir = pageChangeEffectDir2;
    }

    public long getPageChangeEffectDuration() {
        return this.PageChangeEffectDuration;
    }

    public void setPageChangeEffectDuration(long pageChangeEffectDuration) {
        this.PageChangeEffectDuration = pageChangeEffectDuration;
    }
}
