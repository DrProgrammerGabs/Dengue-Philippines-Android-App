package com.p000hl.android.view.widget.image;

/* renamed from: com.hl.android.view.widget.image.ImageLoaderManager */
public class ImageLoaderManager {
    private static ImageLoaderManager imageLoaderManager;

    public static ImageLoaderManager getInstance() {
        if (imageLoaderManager == null) {
            imageLoaderManager = new ImageLoaderManager();
        }
        return imageLoaderManager;
    }
}
