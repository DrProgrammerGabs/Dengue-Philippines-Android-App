package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.PDFComponentEntity;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.File;
import java.io.InputStream;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.DocumentViewNew;
import org.vudroid.core.models.CurrentPageModel;
import org.vudroid.core.models.DecodingProgressModel;
import org.vudroid.core.models.ZoomModel;
import org.vudroid.pdfdroid.codec.PdfContext;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.PDFDocumentViewComponent */
public class PDFDocumentViewComponent extends DocumentViewNew implements Component {
    public ComponentEntity entity = null;
    public ViewRecord initRecord;

    public PDFDocumentViewComponent(Context context, ComponentEntity entity2) {
        super(context);
        try {
            this.entity = entity2;
            setDecodeService(new DecodeServiceBase(new PdfContext()));
            DecodingProgressModel progressModel = new DecodingProgressModel();
            progressModel.addEventListener(this);
            this.currentPageModel = new CurrentPageModel();
            this.currentPageModel.addEventListener(this);
            setPageModel(this.currentPageModel);
            setProgressModel(progressModel);
            this.zoomModel = new ZoomModel();
            initMultiTouchZoomIfAvailable(this.zoomModel);
            this.zoomModel.addEventListener(this);
            setKeepScreenOn(true);
            this.entity = entity2;
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), 0).show();
        }
    }

    public PDFDocumentViewComponent(Context context) {
        super(context);
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
    }

    public void load(InputStream is) {
    }

    public void setRotation(float rotation) {
    }

    public void play() {
        String storageState = Environment.getExternalStorageState();
        goToPage(Integer.valueOf(((PDFComponentEntity) this.entity).getPdfPageIndex()).intValue() - 1);
        if ("mounted".equals(storageState)) {
            File sdPath = Environment.getExternalStorageDirectory();
            FileUtils.getInstance().copyFileToSDCard(getContext(), ((PDFComponentEntity) this.entity).getPdfSourceID());
            openDoc(sdPath.getPath() + "/" + ((PDFComponentEntity) this.entity).getPdfSourceID());
        }
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
