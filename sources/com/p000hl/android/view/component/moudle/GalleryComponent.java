package com.p000hl.android.view.component.moudle;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.moudle.MoudleComponentEntity;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.gallary.GalleyCommonAdapter;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.moudle.GalleryComponent */
public class GalleryComponent extends Gallery implements Component {
    ComponentEntity entity;
    int height;
    int width;

    public GalleryComponent(Context context) {
        super(context);
    }

    public GalleryComponent(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        setBackgroundColor(-16776961);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
        LayoutParams lp = getLayoutParams();
        this.width = lp.width;
        this.height = lp.height;
        setAdapter(new GalleyCommonAdapter(getContext(), ((MoudleComponentEntity) this.entity).getSourceIDList(), this.width, this.height));
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
        this.width = getLayoutParams().width;
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    public void resume() {
    }

    public void pause() {
    }
}
