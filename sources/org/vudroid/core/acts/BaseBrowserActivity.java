package org.vudroid.core.acts;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import com.p000hl.android.C0048R;
import java.io.File;
import java.io.FileFilter;

public abstract class BaseBrowserActivity extends Activity {
    private static final String CURRENT_DIRECTORY = "currentDirectory";
    private BrowserAdapter adapter;
    protected final FileFilter filter = createFileFilter();
    private final OnItemClickListener onItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            File file = ((BrowserAdapter) adapterView.getAdapter()).getItem(i);
            if (file.isDirectory()) {
                BaseBrowserActivity.this.setCurrentDir(file);
            } else {
                BaseBrowserActivity.this.showDocument(file);
            }
        }
    };
    private UriBrowserAdapter recentAdapter;
    private ViewerPreferences viewerPreferences;

    /* access modifiers changed from: protected */
    public abstract FileFilter createFileFilter();

    /* access modifiers changed from: protected */
    public abstract void showDocument(Uri uri);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0048R.C0050layout.browser);
        this.viewerPreferences = new ViewerPreferences(this);
        final ListView browseList = initBrowserListView();
        final ListView recentListView = initRecentListView();
        TabHost tabHost = (TabHost) findViewById(C0048R.C0049id.browserTabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Browse").setIndicator("Browse").setContent(new TabContentFactory() {
            public View createTabContent(String s) {
                return browseList;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec("Recent").setIndicator("Recent").setContent(new TabContentFactory() {
            public View createTabContent(String s) {
                return recentListView;
            }
        }));
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        File sdcardPath = new File("/sdcard");
        if (sdcardPath.exists()) {
            setCurrentDir(sdcardPath);
        } else {
            setCurrentDir(new File("/"));
        }
        if (savedInstanceState != null) {
            String absolutePath = savedInstanceState.getString(CURRENT_DIRECTORY);
            if (absolutePath != null) {
                setCurrentDir(new File(absolutePath));
            }
        }
    }

    private ListView initBrowserListView() {
        ListView listView = new ListView(this);
        this.adapter = new BrowserAdapter(this, this.filter);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(this.onItemClickListener);
        listView.setLayoutParams(new LayoutParams(-1, -1));
        return listView;
    }

    private ListView initRecentListView() {
        ListView listView = new ListView(this);
        this.recentAdapter = new UriBrowserAdapter();
        listView.setAdapter(this.recentAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaseBrowserActivity.this.showDocument(((UriBrowserAdapter) adapterView.getAdapter()).getItem(i));
            }
        });
        listView.setLayoutParams(new LayoutParams(-1, -1));
        return listView;
    }

    /* access modifiers changed from: private */
    public void showDocument(File file) {
        showDocument(Uri.fromFile(file));
    }

    /* access modifiers changed from: private */
    public void setCurrentDir(File newDir) {
        this.adapter.setCurrentDirectory(newDir);
        getWindow().setTitle(newDir.getAbsolutePath());
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_DIRECTORY, this.adapter.getCurrentDirectory().getAbsolutePath());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.recentAdapter.setUris(this.viewerPreferences.getRecent());
    }
}
