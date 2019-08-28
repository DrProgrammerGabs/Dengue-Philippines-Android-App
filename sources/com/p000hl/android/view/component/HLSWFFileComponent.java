package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.SWFFileEntity;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import com.p000hl.android.view.component.inter.Component;
import com.p000hl.android.view.component.listener.OnComponentCallbackListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.protocol.HTTP;

@SuppressLint({"NewApi", "ViewConstructor"})
/* renamed from: com.hl.android.view.component.HLSWFFileComponent */
public class HLSWFFileComponent extends LinearLayout implements Component, ComponentListener {
    /* access modifiers changed from: private */
    public static int HAS_PLAYEND = 65554;
    /* access modifiers changed from: private */
    public static int PAUSE = 65553;
    /* access modifiers changed from: private */
    public static int PLAY = 65552;
    /* access modifiers changed from: private */
    public SWFFileEntity entity;
    private String filePath = "";
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public boolean hasStopPlay = false;
    private Context mContext;
    /* access modifiers changed from: private */
    public WebView mWeb;
    /* access modifiers changed from: private */
    public OnComponentCallbackListener onComponentCallbackListener;
    private String strFile;

    /* renamed from: com.hl.android.view.component.HLSWFFileComponent$CallJava */
    public final class CallJava {
        public CallJava() {
        }

        public void consoleFlashProgress(float progressSize) {
            if (progressSize >= 100.0f) {
                if (HLSWFFileComponent.this.entity.isLoop) {
                    HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.PLAY);
                    HLSWFFileComponent.this.hasStopPlay = false;
                } else {
                    HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.PAUSE);
                    HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.HAS_PLAYEND);
                }
                BookController.getInstance().runBehavior(HLSWFFileComponent.this.entity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
            }
        }

        public void FlashLoaded() {
            if (HLSWFFileComponent.this.hasStopPlay) {
                HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.PAUSE);
                BookController.getInstance().runBehavior(HLSWFFileComponent.this.entity, Behavior.BEHAVIOR_ON_AUDIO_VIDEO_END);
            } else if (HLSWFFileComponent.this.entity.isPlayVideoOrAudioAtBegining) {
                HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.PLAY);
                HLSWFFileComponent.this.hasStopPlay = false;
            } else {
                HLSWFFileComponent.this.handler.sendEmptyMessage(HLSWFFileComponent.PAUSE);
                HLSWFFileComponent.this.hasStopPlay = true;
            }
        }
    }

    public HLSWFFileComponent(Context context, ComponentEntity entity2) {
        super(context);
        this.mContext = context;
        this.entity = (SWFFileEntity) entity2;
    }

    public ComponentEntity getEntity() {
        return this.entity;
    }

    public void setEntity(ComponentEntity entity2) {
        this.entity = (SWFFileEntity) entity2;
    }

    @SuppressLint({"HandlerLeak", "SetJavaScriptEnabled"})
    public void load() {
        try {
            InputStream is = this.mContext.getAssets().open("index.html");
            this.strFile = readTextFile(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (HLSetting.IsResourceSD) {
            this.filePath = "file://" + BookSetting.BOOK_PATH + this.entity.localSourceId;
        } else {
            this.filePath = "file:///android_asset/book/" + this.entity.localSourceId;
        }
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 65552:
                        HLSWFFileComponent.this.mWeb.loadUrl("javascript:Play()");
                        return;
                    case 65553:
                        HLSWFFileComponent.this.mWeb.loadUrl("javascript:Pause()");
                        return;
                    case 65554:
                        HLSWFFileComponent.this.onComponentCallbackListener.setPlayComplete();
                        return;
                    default:
                        return;
                }
            }
        };
        if (this.mWeb == null) {
            this.mWeb = new WebView(this.mContext);
            this.mWeb.getSettings().setJavaScriptEnabled(true);
            this.mWeb.getSettings().setPluginState(PluginState.ON);
            this.mWeb.getSettings().setAllowFileAccess(true);
            this.mWeb.addJavascriptInterface(new CallJava(), "CallJava");
            this.mWeb.setWebChromeClient(new WebChromeClient());
            LayoutParams videoLayoutLP = new LayoutParams(-1, -1);
            videoLayoutLP.gravity = 17;
            addView(this.mWeb, videoLayoutLP);
            this.mWeb.setBackgroundColor(0);
        } else {
            this.mWeb.resumeTimers();
            this.mWeb.refreshDrawableState();
        }
        this.mWeb.loadDataWithBaseURL(null, this.strFile.replace("flash.swf", this.filePath), "text/html", HTTP.UTF_8, null);
        if (this.entity.isHideAtBegining) {
            setVisibility(4);
        }
    }

    private String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (true) {
            try {
                int len = inputStream.read(buf);
                if (len == -1) {
                    break;
                }
                outputStream.write(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toString();
    }

    public void load(InputStream is) {
    }

    public void play() {
        this.handler.sendEmptyMessage(PLAY);
        this.hasStopPlay = false;
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIRO_ON_AUDIO_VIDEO_PLAY);
    }

    public void stop() {
        this.mWeb.loadDataWithBaseURL(null, this.strFile.replace("flash.swf", this.filePath), "text/html", HTTP.UTF_8, null);
        this.hasStopPlay = true;
    }

    public void hide() {
        setVisibility(4);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_HIDE);
    }

    public void show() {
        setVisibility(0);
        BookController.getInstance().runBehavior(this.entity, Behavior.BEHAVIOR_ON_SHOW);
    }

    public void resume() {
        this.handler.sendEmptyMessage(PLAY);
    }

    public void pause() {
        this.handler.sendEmptyMessage(PAUSE);
    }

    public void registerCallbackListener(OnComponentCallbackListener callbackListner) {
        this.onComponentCallbackListener = callbackListner;
    }

    public void callBackListener() {
        this.onComponentCallbackListener.setPlayComplete();
    }
}
