package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.p000hl.android.book.entity.AnimationEntity;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import java.util.ArrayList;

@SuppressLint({"SetJavaScriptEnabled"})
/* renamed from: com.hl.android.view.component.WebComponent */
public class WebComponent extends WebView implements Component {
    LayoutParams alp;
    ArrayList<AnimationEntity> anims;
    private ComponentEntity entity;
    public ViewRecord initRecord;
    /* access modifiers changed from: private */
    public boolean isChangeUrl = false;
    boolean isShow = true;
    LoadView loadView;
    public boolean loadingFinished = true;
    Context mContext;
    boolean redirect = false;

    public WebComponent(Context context) {
        super(context);
        this.mContext = context;
    }

    public WebComponent(Context context, ComponentEntity entity2) {
        boolean z = true;
        super(context);
        this.mContext = context;
        this.entity = entity2;
        this.loadView = new LoadView(context);
        this.alp = new LayoutParams(-1, -1);
        addView(this.loadView, this.alp);
        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDefaultZoom(ZoomDensity.FAR);
        if (entity2.isHideAtBegining) {
            z = false;
        }
        this.isShow = z;
        setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!WebComponent.this.loadingFinished) {
                    WebComponent.this.redirect = true;
                }
                WebComponent.this.loadingFinished = false;
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                WebComponent.this.loadingFinished = false;
                if (WebComponent.this.isChangeUrl) {
                    WebComponent.this.addView(WebComponent.this.loadView, WebComponent.this.alp);
                }
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                if (WebComponent.this.isChangeUrl) {
                    WebComponent.this.removeView(WebComponent.this.loadView);
                    WebComponent.this.isChangeUrl = false;
                }
                if (!WebComponent.this.redirect) {
                    WebComponent.this.loadingFinished = true;
                }
                if (!WebComponent.this.loadingFinished || WebComponent.this.redirect) {
                    WebComponent.this.redirect = false;
                } else if (WebComponent.this.isShow) {
                    WebComponent.this.show();
                }
            }
        });
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = entity2;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 4 || !canGoBack() || event.getAction() != 1) {
            return super.dispatchKeyEvent(event);
        }
        goBack();
        return true;
    }

    public void load() {
    }

    public void load(InputStream is) {
        loadUrl(this.entity.htmlUrl);
    }

    public void play() {
        loadUrl(this.entity.htmlUrl);
    }

    public void stop() {
    }

    public void hide() {
        this.isShow = false;
        setVisibility(8);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        removeView(this.loadView);
        this.isShow = true;
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (canGoBack()) {
                    goBack();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void pause() {
    }

    public void changeUrl(String newUrl) {
        this.isChangeUrl = true;
        loadUrl(newUrl);
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
