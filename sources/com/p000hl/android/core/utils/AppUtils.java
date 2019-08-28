package com.p000hl.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* renamed from: com.hl.android.core.utils.AppUtils */
public class AppUtils {
    private static final String Mc_String_DataPath = "HLData";
    private static String appPath = "";
    private static String appSdPath;

    public static String getAppPath(Activity activity) {
        if (StringUtils.isEmpty(appPath)) {
            appPath = activity.getApplication().getDir(Mc_String_DataPath, 2).getAbsolutePath();
        }
        return appPath;
    }

    public static String getAppSdPath() {
        if (appSdPath != null) {
            return appSdPath;
        }
        String strPath = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            strPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            appSdPath = strPath.endsWith(File.separator) ? strPath : strPath + File.separator;
        }
        if (strPath == null) {
            strPath = "/mnt/sdcard/";
        }
        if (!new File(strPath).exists()) {
            return null;
        }
        appSdPath = strPath;
        return appSdPath;
    }

    public static String getAppDataPath(Activity activity) {
        String str = "";
        String sdPath = getAppSdPath();
        if (sdPath == null) {
            return getAppPath(activity);
        }
        return StringUtils.contactForPath(sdPath, activity.getPackageName(), Mc_String_DataPath);
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static boolean detectPackage(Context activity, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }

    public static void installAssetsApk(Activity activity, String apkFile, int requestCode) throws IOException {
        InputStream fis = activity.getAssets().open(apkFile);
        String apkSdFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hl/apk/" + apkFile;
        if (FileUtils.copyFile(fis, apkSdFile)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(apkSdFile)), "application/vnd.android.package-archive");
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static int parseColor(String value) {
        if (value == null || value.length() < 8) {
            return -16777216;
        }
        return Color.rgb(Integer.parseInt(value.substring(2, 4), 16), Integer.parseInt(value.substring(4, 6), 16), Integer.parseInt(value.substring(6, 8), 16));
    }
}
