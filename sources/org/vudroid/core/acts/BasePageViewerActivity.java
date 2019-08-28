package org.vudroid.core.acts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.p000hl.android.C0048R;
import org.vudroid.core.DecodeService;
import org.vudroid.core.DocumentViewNew;
import org.vudroid.core.events.CurrentPageListener;
import org.vudroid.core.events.DecodingProgressListener;
import org.vudroid.core.models.CurrentPageModel;
import org.vudroid.core.models.DecodingProgressModel;
import org.vudroid.core.models.ZoomModel;
import org.vudroid.core.views.PageViewZoomControls;

public abstract class BasePageViewerActivity extends Activity implements DecodingProgressListener, CurrentPageListener {
    private static final int DIALOG_GOTO = 0;
    private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
    private static final int MENU_EXIT = 0;
    private static final int MENU_FULL_SCREEN = 2;
    private static final int MENU_GOTO = 1;
    private CurrentPageModel currentPageModel;
    private DecodeService decodeService;
    /* access modifiers changed from: private */
    public DocumentViewNew documentView;
    /* access modifiers changed from: private */
    public EditText editText;
    Intent intent;
    private Toast pageNumberToast;
    private ViewerPreferences viewerPreferences;

    private class MyButtonHandler implements OnClickListener {
        private MyButtonHandler() {
        }

        public void onClick(View v) {
            if (v == BasePageViewerActivity.this.findViewById(C0048R.C0049id.btn_prevnew)) {
                BasePageViewerActivity.this.documentView.changePage(-1);
            } else if (v == BasePageViewerActivity.this.findViewById(C0048R.C0049id.btn_nextnew)) {
                BasePageViewerActivity.this.documentView.changePage(1);
            } else if (v == BasePageViewerActivity.this.findViewById(C0048R.C0049id.backnew)) {
                BasePageViewerActivity.this.setResult(0, BasePageViewerActivity.this.intent);
                BasePageViewerActivity.this.finish();
            } else if (v == BasePageViewerActivity.this.findViewById(C0048R.C0049id.gotopage)) {
                try {
                    if (BasePageViewerActivity.this.editText.getText() != null && !BasePageViewerActivity.this.editText.getText().equals("")) {
                        Integer iNum = Integer.valueOf(BasePageViewerActivity.this.editText.getText().toString());
                        if (iNum.intValue() >= 0 && iNum.intValue() <= BasePageViewerActivity.this.documentView.pageCount) {
                            BasePageViewerActivity.this.documentView.goToPage(Integer.valueOf(iNum.intValue() - 1).intValue());
                            ((InputMethodManager) BasePageViewerActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(BasePageViewerActivity.this.getCurrentFocus().getWindowToken(), 2);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (v == BasePageViewerActivity.this.findViewById(C0048R.C0049id.testButton)) {
                BasePageViewerActivity.this.documentView.setTestState(true);
                BasePageViewerActivity.this.documentView.goToPage(BasePageViewerActivity.this.documentView.pageIndex);
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract DecodeService createDecodeService();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0048R.C0050layout.pdfpage);
        this.intent = getIntent();
        initDecodeService();
        this.documentView = (DocumentViewNew) findViewById(C0048R.C0049id.pdf_viewnew);
        this.documentView.setDecodeService(this.decodeService);
        DecodingProgressModel progressModel = new DecodingProgressModel();
        progressModel.addEventListener(this);
        this.currentPageModel = new CurrentPageModel();
        this.currentPageModel.addEventListener(this);
        this.documentView.setPageModel(this.currentPageModel);
        this.documentView.setProgressModel(progressModel);
        MyButtonHandler l = new MyButtonHandler();
        findViewById(C0048R.C0049id.btn_nextnew).setOnClickListener(l);
        findViewById(C0048R.C0049id.btn_prevnew).setOnClickListener(l);
        findViewById(C0048R.C0049id.backnew).setOnClickListener(l);
        findViewById(C0048R.C0049id.testButton).setOnClickListener(l);
        findViewById(C0048R.C0049id.gotopage).setOnClickListener(l);
        this.editText = (EditText) findViewById(C0048R.C0049id.pagenumber);
        this.viewerPreferences = new ViewerPreferences(this);
        this.viewerPreferences.addRecent(getIntent().getData());
        this.documentView.setBackgroundColor(-1);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            this.documentView.goToPage(0);
            this.documentView.openDoc(getIntent().getData().getPath());
        }
    }

    public void onConfigurationChanged(Configuration cf) {
        super.onConfigurationChanged(cf);
        this.documentView.refresh();
    }

    public void decodingProgressChanged(final int currentlyDecoding) {
        runOnUiThread(new Runnable() {
            public void run() {
                BasePageViewerActivity.this.getWindow().setFeatureInt(5, currentlyDecoding == 0 ? 10000 : currentlyDecoding);
            }
        });
    }

    public void currentPageChanged(int pageIndex) {
        String pageText = (pageIndex + 1) + "/" + this.decodeService.getPageCount();
        if (this.pageNumberToast != null) {
            this.pageNumberToast.setText(pageText);
        } else {
            this.pageNumberToast = Toast.makeText(this, pageText, 0);
        }
        this.pageNumberToast.setGravity(51, 0, 0);
        this.pageNumberToast.show();
        saveCurrentPage();
    }

    private void setWindowTitle() {
        getWindow().setTitle(getIntent().getData().getLastPathSegment());
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setWindowTitle();
    }

    /* access modifiers changed from: protected */
    public void setFullScreen() {
        this.viewerPreferences = new ViewerPreferences(this);
        if (this.viewerPreferences.isFullScreen()) {
            getWindow().requestFeature(1);
            getWindow().setFlags(1024, 1024);
            return;
        }
        getWindow().requestFeature(5);
    }

    /* access modifiers changed from: protected */
    public PageViewZoomControls createZoomControls(ZoomModel zoomModel) {
        PageViewZoomControls controls = new PageViewZoomControls(this, zoomModel);
        controls.setGravity(85);
        zoomModel.addEventListener(controls);
        return controls;
    }

    /* access modifiers changed from: protected */
    public FrameLayout createMainContainer() {
        return new FrameLayout(this);
    }

    private void initDecodeService() {
        if (this.decodeService == null) {
            this.decodeService = createDecodeService();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.decodeService.recycle();
        this.decodeService = null;
        this.documentView.recyle();
        super.onDestroy();
    }

    private void saveCurrentPage() {
        Editor editor = getSharedPreferences(DOCUMENT_VIEW_STATE_PREFERENCES, 0).edit();
        editor.putInt(getIntent().getData().toString(), this.documentView.getCurrentPage());
        editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Exit");
        menu.add(0, 1, 0, "Go to page");
        setFullScreenMenuItemText(menu.add(0, 2, 0, "Full screen").setCheckable(true).setChecked(this.viewerPreferences.isFullScreen()));
        return true;
    }

    private void setFullScreenMenuItemText(MenuItem menuItem) {
        menuItem.setTitle("Full screen " + (menuItem.isChecked() ? "on" : "off"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = false;
        switch (item.getItemId()) {
            case 0:
                System.exit(0);
                return true;
            case 1:
                showDialog(0);
                return true;
            case 2:
                if (!item.isChecked()) {
                    z = true;
                }
                item.setChecked(z);
                setFullScreenMenuItemText(item);
                this.viewerPreferences.setFullScreen(item.isChecked());
                finish();
                startActivity(getIntent());
                return true;
            default:
                return false;
        }
    }
}
