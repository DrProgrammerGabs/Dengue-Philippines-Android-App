package com.p000hl.android.view.component;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.p000hl.android.C0048R;
import com.p000hl.android.book.entity.MapComponentEntity;

/* renamed from: com.hl.android.view.component.MapFragement */
public class MapFragement extends Fragment {
    private String LoginID = null;
    private MapComponentEntity _entity;
    private Bundle args = null;
    private Builder builder;
    private ProgressDialog mDialog;
    private SharedPreferences preferences;

    public MapFragement(MapComponentEntity entity) {
        this._entity = entity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("SiteMapFragment", "onCreateView");
        return inflater.inflate(C0048R.C0050layout.welcome, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("SiteMapFragment", "onActivityCreated");
    }

    public void onResume() {
        super.onResume();
    }

    public void onDetach() {
        super.onDetach();
        Log.i("SiteMapFragment", "onDetach");
    }

    public void onPause() {
        super.onPause();
        Log.i("SiteMapFragment", "onPause");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("SiteMapFragment", "onDestroy");
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.i("SiteMapFragment", "onDestroyView");
    }

    public void play() {
    }
}
