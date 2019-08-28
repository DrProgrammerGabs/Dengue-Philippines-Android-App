package com.p000hl.android.view.component.inter;

import com.p000hl.android.book.entity.ComponentEntity;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.inter.Component */
public interface Component {
    ComponentEntity getEntity();

    void hide();

    void load();

    void load(InputStream inputStream);

    void pause();

    void play();

    void resume();

    void setEntity(ComponentEntity componentEntity);

    void show();

    void stop();
}
