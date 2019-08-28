package com.p000hl.android.book.entity;

/* renamed from: com.hl.android.book.entity.MapComponentEntity */
public class MapComponentEntity extends ComponentEntity {
    private String address;
    private String lat;
    private String lng;
    private String placeID;

    public MapComponentEntity(ComponentEntity component) {
        if (component != null) {
            this.animationRepeat = component.animationRepeat;
            this.alpha = component.alpha;
        }
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getPlaceID() {
        return this.placeID;
    }

    public void setPlaceID(String placeID2) {
        this.placeID = placeID2;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String lat2) {
        this.lat = lat2;
    }

    public String getLng() {
        return this.lng;
    }

    public void setLng(String lng2) {
        this.lng = lng2;
    }
}
