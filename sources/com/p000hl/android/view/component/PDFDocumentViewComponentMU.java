package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.artifex.mupdf.MuPDFPageView;
import com.artifex.mupdf.PageView;
import com.artifex.mupdf.ReaderView;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.PDFComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.controller.PdfController;
import com.p000hl.android.core.utils.FileUtils;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.File;
import java.io.InputStream;

/* renamed from: com.hl.android.view.component.PDFDocumentViewComponentMU */
public class PDFDocumentViewComponentMU extends LinearLayout implements Component {
    private final int TAP_PAGE_MARGIN = 5;
    private MuPDFCore core;
    public ComponentEntity entity = null;
    public ViewRecord initRecord;
    private Context mContext;
    /* access modifiers changed from: private */
    public ReaderView mDocView;
    /* access modifiers changed from: private */
    public LinkState mLinkState = LinkState.DEFAULT;
    private int pageIndex = 0;
    String pdfFile = "";

    /* renamed from: com.hl.android.view.component.PDFDocumentViewComponentMU$LinkState */
    private enum LinkState {
        DEFAULT,
        HIGHLIGHT,
        INHIBIT
    }

    public PDFDocumentViewComponentMU(Context context) {
        super(context);
    }

    public PDFDocumentViewComponentMU(Context context, ComponentEntity entity2) {
        super(context);
        this.entity = entity2;
        this.mContext = context;
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public void load() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File sdPath = Environment.getExternalStorageDirectory();
            FileUtils.getInstance().copyFileToSDCard(getContext(), ((PDFComponentEntity) this.entity).getPdfSourceID());
            PdfController.getInstance().openFile(sdPath.getPath() + "/" + ((PDFComponentEntity) this.entity).getPdfSourceID());
            this.core = PdfController.getInstance().muPDFCore;
        }
        this.pageIndex = Integer.parseInt(((PDFComponentEntity) this.entity).getPdfPageIndex());
        this.mDocView = new ReaderView(this.mContext) {
            private boolean showButtonsDisabled;

            public boolean onSingleTapUp(MotionEvent e) {
                if (e.getX() < ((float) (super.getWidth() / 5))) {
                    super.moveToPrevious();
                } else if (e.getX() > ((float) ((super.getWidth() * 4) / 5))) {
                    super.moveToNext();
                } else if (!this.showButtonsDisabled) {
                    if (PDFDocumentViewComponentMU.this.mLinkState == LinkState.INHIBIT || ((MuPDFPageView) PDFDocumentViewComponentMU.this.mDocView.getDisplayedView()) == null) {
                    }
                    if (-1 != -1) {
                        PDFDocumentViewComponentMU.this.mDocView.setDisplayedViewIndex(-1);
                    }
                }
                return super.onSingleTapUp(e);
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            public boolean onScaleBegin(ScaleGestureDetector d) {
                this.showButtonsDisabled = true;
                return super.onScaleBegin(d);
            }

            public boolean onTouchEvent(MotionEvent event) {
                if (event.getActionMasked() == 0) {
                    this.showButtonsDisabled = false;
                }
                return super.onTouchEvent(event);
            }

            /* access modifiers changed from: protected */
            public void onChildSetup(int i, View v) {
            }

            /* access modifiers changed from: protected */
            public void onMoveToChild(int i) {
            }

            /* access modifiers changed from: protected */
            public void onSettle(View v) {
                ((PageView) v).addHq();
            }

            /* access modifiers changed from: protected */
            public void onUnsettle(View v) {
                ((PageView) v).removeHq();
            }
        };
        this.mDocView.setAdapter(new MuPDFPageAdapter(this.mContext, this.core));
        this.mDocView.setDisplayedViewIndex(this.pageIndex - 1);
        addView(this.mDocView);
    }

    public void load(InputStream is) {
    }

    public void requestLayout() {
        super.forceLayout();
        super.requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("sfsfdsfdsfdsf");
    }

    public void play() {
    }

    public void stop() {
    }

    public void hide() {
        clearAnimation();
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
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
