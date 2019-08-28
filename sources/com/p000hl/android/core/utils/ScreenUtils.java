package com.p000hl.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import com.p000hl.android.common.BookSetting;

/* renamed from: com.hl.android.core.utils.ScreenUtils */
public class ScreenUtils {
    public static float getHorScreenValue(float value) {
        if (BookSetting.FITSCREEN_TENSILE) {
            return (BookSetting.PAGE_RATIOX * value) + 0.001f;
        }
        return (BookSetting.PAGE_RATIO * value) + 0.001f;
    }

    public static float getVerScreenValue(float value) {
        if (BookSetting.FITSCREEN_TENSILE) {
            return (BookSetting.PAGE_RATIOY * value) + 0.001f;
        }
        return (BookSetting.PAGE_RATIO * value) + 0.001f;
    }

    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            return activity.getWindowManager().getDefaultDisplay().getWidth();
        }
        Log.d("hl", "获取屏幕宽度的传入的activity为空");
        return 0;
    }

    public static int getScreenHeight(Activity activity) {
        if (activity != null) {
            return activity.getWindowManager().getDefaultDisplay().getHeight();
        }
        Log.d("hl", "获取屏幕高度的传入的activity为空");
        return 0;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getAPILevel() {
        return Integer.valueOf(VERSION.SDK).intValue();
    }

    public static String getAppPath() {
        return Environment.getDataDirectory().getAbsolutePath().toString();
    }
}
