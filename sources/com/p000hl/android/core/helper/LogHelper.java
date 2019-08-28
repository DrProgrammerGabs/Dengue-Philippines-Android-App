package com.p000hl.android.core.helper;

import android.util.Log;

/* renamed from: com.hl.android.core.helper.LogHelper */
public final class LogHelper {
    private static final String CLASS_METHOD_LINE_FORMAT = "方法：%s.%s()  所在行:%d  所在文件：%s";
    private static boolean mIsDebugMode = true;
    private static String mLogTag = "SunYongle";

    public static void setLogTag(String tag) {
        mLogTag = tag;
    }

    public static void trace(String tittle, String showContent) {
        if (mIsDebugMode) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(mLogTag, String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()}) + "\nTITTLE：" + tittle + "   VALUE：" + showContent);
        }
    }

    public static void trace(String tittle, String showContent, boolean showInError) {
        if (!showInError) {
            trace(tittle, showContent);
        } else if (mIsDebugMode) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(mLogTag, String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()}) + "\nTITTLE：" + tittle + "   VALUE：" + showContent);
        }
    }
}
