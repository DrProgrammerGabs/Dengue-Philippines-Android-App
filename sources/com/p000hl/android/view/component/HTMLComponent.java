package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.HTMLComponentEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import org.apache.http.HttpStatus;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.HTMLComponent */
public class HTMLComponent extends LinearLayout implements Component {
    private HTMLComponentEntity _entity;
    public ViewRecord initRecord;
    WebView webview;

    public HTMLComponent(Context context) {
        super(context);
        this.webview = new WebView(context);
        addView(this.webview, new LayoutParams(-1, -1));
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        this.webview.setFocusable(true);
        this.webview.setFocusableInTouchMode(true);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.getSettings().setBuiltInZoomControls(false);
        this.webview.getSettings().setSupportZoom(false);
        this.webview.getSettings().setDisplayZoomControls(false);
        this.webview.setInitialScale(HttpStatus.SC_OK);
        this.webview.setScrollBarStyle(0);
        this.webview.setHorizontalScrollBarEnabled(false);
        this.webview.setWebViewClient(new WebViewClient());
        this.webview.setWebChromeClient(new WebChromeClient());
    }

    public HTMLComponent(Context context, ComponentEntity entity) {
        this(context);
        setEntity(entity);
    }

    public ComponentEntity getEntity() {
        return this._entity;
    }

    public void setEntity(ComponentEntity entity) {
        this._entity = (HTMLComponentEntity) entity;
    }

    public void load() {
        String path;
        String str = "";
        if (HLSetting.IsResourceSD) {
            path = "file:///" + BookSetting.BOOK_PATH + "/" + this._entity.getHtmlFolder() + "/" + this._entity.getIndexHtml();
        } else {
            path = "file:///android_asset/" + BookSetting.BOOK_RESOURCE_DIR + this._entity.getHtmlFolder() + "/" + this._entity.getIndexHtml();
        }
        this.webview.loadUrl(path);
    }

    public void load(InputStream is) {
    }

    public void play() {
        setVisibility(0);
        bringToFront();
        invalidate();
    }

    public void stop() {
        try {
            this.webview.loadUrl("about:blank");
            clearDisappearingChildren();
            this.webview.clearCache(false);
            this.webview.pauseTimers();
        } catch (Exception e) {
        }
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
