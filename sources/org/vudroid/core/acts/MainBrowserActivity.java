package org.vudroid.core.acts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

public class MainBrowserActivity extends BaseBrowserActivity {
    /* access modifiers changed from: private */
    public static final HashMap<String, Class<? extends Activity>> extensionToActivity = new HashMap<>();

    static {
        extensionToActivity.put("pdf", PdfViewerActivity.class);
    }

    /* access modifiers changed from: protected */
    public FileFilter createFileFilter() {
        return new FileFilter() {
            public boolean accept(File pathname) {
                for (String s : MainBrowserActivity.extensionToActivity.keySet()) {
                    if (pathname.getName().endsWith("." + s)) {
                        return true;
                    }
                }
                if (pathname.getName().startsWith(".")) {
                    return false;
                }
                return pathname.isDirectory();
            }
        };
    }

    /* access modifiers changed from: protected */
    public void showDocument(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        String uriString = uri.toString();
        intent.setClass(this, (Class) extensionToActivity.get(uriString.substring(uriString.lastIndexOf(46) + 1)));
        startActivity(intent);
    }
}
