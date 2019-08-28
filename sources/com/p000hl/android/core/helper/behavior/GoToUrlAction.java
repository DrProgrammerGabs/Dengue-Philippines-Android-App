package com.p000hl.android.core.helper.behavior;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.artifex.mupdf.MuPDFActivity;
import com.p000hl.android.book.entity.BehaviorEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.PdfController;
import com.p000hl.android.core.utils.FileUtils;

/* renamed from: com.hl.android.core.helper.behavior.GoToUrlAction */
public class GoToUrlAction extends BehaviorAction {
    public void doAction(BehaviorEntity entity) {
        super.doAction(entity);
        try {
            if (entity.Value.startsWith("pdffile://")) {
                PdfController.getInstance().destroy();
                String fileName = entity.Value.substring(10, entity.Value.length());
                FileUtils.getInstance().copyFileToData(BookController.getInstance().getViewPage().getContext(), fileName);
                Intent intent = new Intent("android.intent.action.VIEW", Uri.fromFile(FileUtils.getInstance().getDataFile(BookController.getInstance().getViewPage().getContext(), fileName)));
                intent.setClass(BookController.getInstance().getViewPage().getContext(), MuPDFActivity.class);
                BookController.getInstance().getViewPage().getContext().startActivity(intent);
            } else if (!entity.Value.startsWith("video://")) {
                BookController.getInstance().getViewPage().getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(entity.Value)));
            }
        } catch (Exception ex) {
            Log.e("hl", " ViewPage go to URL", ex);
        }
    }
}
