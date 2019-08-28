package org.vudroid.core.acts;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class ViewerPreferences {
    private static final String FULL_SCREEN = "FullScreen";
    private SharedPreferences sharedPreferences;

    public ViewerPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences("ViewerPreferences", 0);
    }

    public void setFullScreen(boolean fullscreen) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(FULL_SCREEN, fullscreen);
        editor.commit();
    }

    public boolean isFullScreen() {
        return this.sharedPreferences.getBoolean(FULL_SCREEN, false);
    }

    public void addRecent(Uri uri) {
        Editor editor = this.sharedPreferences.edit();
        editor.putString("recent:" + uri.toString(), uri.toString() + "\n" + System.currentTimeMillis());
        editor.commit();
    }

    public List<Uri> getRecent() {
        TreeMap<Long, Uri> treeMap = new TreeMap<>();
        for (String key : this.sharedPreferences.getAll().keySet()) {
            if (key.startsWith("recent")) {
                String[] uriThenDate = this.sharedPreferences.getString(key, null).split("\n");
                treeMap.put(Long.valueOf(Long.parseLong(uriThenDate.length > 1 ? uriThenDate[1] : "0")), Uri.parse(uriThenDate[0]));
            }
        }
        ArrayList<Uri> list = new ArrayList<>(treeMap.values());
        Collections.reverse(list);
        return list;
    }
}
