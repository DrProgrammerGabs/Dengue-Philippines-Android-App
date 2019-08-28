package com.p000hl.android.view.gallary.base;

import android.graphics.Bitmap;
import java.io.Serializable;

/* renamed from: com.hl.android.view.gallary.base.ImageMessage */
public class ImageMessage implements Serializable {
    private static final long serialVersionUID = 1;
    public Bitmap image;
    public String isNull;
    public String name;
    public String path;

    public String getIsNull() {
        return this.isNull;
    }

    public void setIsNull(String isNull2) {
        this.isNull = isNull2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image2) {
        this.image = image2;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }
}
