package com.p000hl.android.view.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.p000hl.android.book.entity.ComponentEntity;
import com.p000hl.android.book.entity.MapComponentEntity;
import com.p000hl.android.view.component.bean.ViewRecord;
import com.p000hl.android.view.component.inter.Component;
import java.io.InputStream;
import org.apache.http.protocol.HTTP;

@SuppressLint({"NewApi"})
/* renamed from: com.hl.android.view.component.MapComponent */
public class MapComponent extends LinearLayout implements Component {
    private MapComponentEntity _entity;
    private Activity activity;

    /* renamed from: id */
    private int f26id = 11112;
    public ViewRecord initRecord;
    StringBuffer sbFrame = new StringBuffer();
    private WebView webView;

    public MapComponent(Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    public MapComponent(Context context, ComponentEntity entity) {
        super(context);
        setEntity(entity);
        this._entity = (MapComponentEntity) entity;
        this.activity = (Activity) context;
        this.webView = new WebView(context);
        this.webView.setWebChromeClient(new WebChromeClient());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDefaultTextEncodingName(HTTP.UTF_8);
        addView(this.webView, new LayoutParams(-1, -1));
        setId(this.f26id);
    }

    public ComponentEntity getEntity() {
        return this._entity;
    }

    public void setEntity(ComponentEntity entity) {
        this._entity = (MapComponentEntity) entity;
    }

    public void load() {
        this.sbFrame = new StringBuffer();
        this.sbFrame.append("<!DOCTYPE html>");
        this.sbFrame.append("<html lang=\"en\">");
        this.sbFrame.append("<head>");
        this.sbFrame.append(" <meta charset=\"UTF-8\">");
        this.sbFrame.append(" <title></title>");
        this.sbFrame.append("</head>");
        this.sbFrame.append("<script type=\"text/javascript\">");
        this.sbFrame.append("window.onload=function () {");
        this.sbFrame.append("resetMap();");
        this.sbFrame.append("};");
        this.sbFrame.append("window.addEventListener(\"resize\", function (e) {");
        this.sbFrame.append("resetMap();");
        this.sbFrame.append("});");
        this.sbFrame.append("function resetMap() {");
        this.sbFrame.append("var deviceWidth = window.innerWidth|| document.documentElement.clientWidth|| document.body.clientWidth;");
        this.sbFrame.append("var deviceHeight = window.innerHeight|| document.documentElement.clientHeight|| document.body.clientHeight;");
        this.sbFrame.append("deviceWidth = deviceWidth;");
        this.sbFrame.append("deviceHeight = deviceHeight;");
        this.sbFrame.append("var mapFrame = document.getElementById(\"mapFrame\");");
        this.sbFrame.append("var es = mapFrame.style;");
        this.sbFrame.append(" es.width = deviceWidth+'px';");
        this.sbFrame.append(" es.height = deviceHeight+'px'");
        this.sbFrame.append("  }");
        this.sbFrame.append("</script><body>");
        this.sbFrame.append("</body>");
        this.sbFrame.append("<iframe id=\"mapFrame\" frameborder=\"0\" style=\"border:0\" src=\"https://www.google.com/maps/embed/v1/place?key=AIzaSyAdmW-LYHPAh9lFzqa4GA5UDjPNmZQMj4A&q=place_id:");
        this.sbFrame.append(this._entity.getPlaceID());
        this.sbFrame.append("\" allowfullscreen></iframe>");
        this.sbFrame.append("</html>");
        Log.e("zhaoq", this.sbFrame.toString());
        this.webView.loadData(this.sbFrame.toString(), "text/html", HTTP.UTF_8);
    }

    public void load(InputStream is) {
    }

    public void play() {
    }

    public void stop() {
        this.webView.destroy();
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
