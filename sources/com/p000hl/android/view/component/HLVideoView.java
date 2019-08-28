package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.VideoView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.VideoComponentEntity;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.HLVideoView */
public class HLVideoView extends VideoView implements Component {
    public VideoComponentEntity entity;
    public ViewRecord initRecord;

    public HLVideoView(Context context) {
        super(context);
    }

    public HLVideoView(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = (VideoComponentEntity) entity2;
    }

    public ComponentEntity getEntity() {
        return null;
    }

    public void setEntity(ComponentEntity entity2) {
    }

    public void load() {
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
    }

    public void show() {
    }

    @SuppressLint({"NewApi"})
    public ViewRecord getCurrentRecord() {
        ViewRecord curRecord = new ViewRecord();
        curRecord.mHeight = getLayoutParams().width;
        curRecord.mWidth = getLayoutParams().height;
        curRecord.f28mX = getX();
        curRecord.f29mY = getY();
        curRecord.mRotation = getRotation();
        return curRecord;
    }
}
