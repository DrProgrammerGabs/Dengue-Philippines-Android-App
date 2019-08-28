package com.p000hl.android.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/* renamed from: com.hl.android.core.utils.CommonUtils */
public class CommonUtils {
    public static String getCPUSerial() {
        String str = "";
        String str2 = "";
        String cpuAddress = "0000000000000000";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /proc/cpuinfo").getInputStream()));
            for (int i = 1; i < 100; i++) {
                String str3 = input.readLine();
                if (str3 == null) {
                    return cpuAddress;
                }
                if (str3.indexOf("Serial") > -1) {
                    return str3.substring(str3.indexOf(":") + 1, str3.length()).trim();
                }
            }
            return cpuAddress;
        } catch (IOException ex) {
            ex.printStackTrace();
            return cpuAddress;
        }
    }

    public static String getWiFiAddr(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        return true;
    }
}
