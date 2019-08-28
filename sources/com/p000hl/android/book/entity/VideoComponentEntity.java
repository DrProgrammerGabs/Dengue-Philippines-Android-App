package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.VideoComponentEntity */
public class VideoComponentEntity extends ComponentEntity {
    private String coverSourceID;
    private boolean videoControlBarIsShow;

    public String getCoverSourceID() {
        return this.coverSourceID;
    }

    public void setCoverSourceID(String coverSourceID2) {
        this.coverSourceID = coverSourceID2;
    }

    public boolean isVideoControlBarIsShow() {
        return this.videoControlBarIsShow;
    }

    public void setVideoControlBarIsShow(boolean videoControlBarIsShow2) {
        this.videoControlBarIsShow = videoControlBarIsShow2;
    }
}
