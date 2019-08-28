package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.YouTubeVideoComponentEntity */
public class YouTubeVideoComponentEntity extends ComponentEntity {
    private String shareURL;
    private String videoID;

    public YouTubeVideoComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }

    public String getShareURL() {
        return this.shareURL;
    }

    public void setShareURL(String shareURL2) {
        this.shareURL = shareURL2;
    }

    public String getVideoID() {
        return this.videoID;
    }

    public void setVideoID(String videoID2) {
        this.videoID = videoID2;
    }
}
