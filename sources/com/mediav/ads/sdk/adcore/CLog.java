package com.mediav.ads.sdk.adcore;

import android.util.Log;

public class CLog {
    private static final String TAG = "MEDIAV";

    private CLog() {
    }

    /* renamed from: i */
    public static void m6i(String msg) {
        if (msg != null) {
            Log.i(TAG, msg);
        }
    }

    /* renamed from: d */
    public static void m2d(String msg) {
        if (msg != null) {
            Log.d(TAG, msg);
        }
    }

    /* renamed from: w */
    public static void m8w(Exception ex) {
        if (ex != null) {
            ex.printStackTrace();
        }
    }

    /* renamed from: e */
    public static void m4e(String msg) {
        if (msg != null) {
            Log.e(TAG, msg);
        }
    }

    /* renamed from: i */
    public static void m7i(String tag, String msg) {
        if (msg != null) {
            Log.i(tag, msg);
        }
    }

    /* renamed from: d */
    public static void m3d(String tag, String msg) {
        if (msg != null) {
            Log.d(tag, msg);
        }
    }

    /* renamed from: w */
    public static void m9w(String tag, String msg) {
        if (msg != null) {
            Log.w(tag, msg);
        }
    }

    /* renamed from: e */
    public static void m5e(String tag, String msg) {
        if (msg != null) {
            Log.e(tag, msg);
        }
    }
}
