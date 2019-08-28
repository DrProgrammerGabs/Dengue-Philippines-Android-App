package com.p000hl.android.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/* renamed from: com.hl.android.core.utils.StringUtils */
public class StringUtils {
    private static final String C_String_Date_Format = "yyyy-MM-dd";
    private static final String C_String_Date_Time_Format = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormater = new SimpleDateFormat(C_String_Date_Format);
    private static final SimpleDateFormat dateTimeFormater = new SimpleDateFormat(C_String_Date_Time_Format);

    public static String formatNowDate(String... format) {
        String result;
        synchronized (dateFormater) {
            if (format != null) {
                if (format.length > 0) {
                    dateFormater.applyPattern(format[0]);
                    result = dateFormater.format(new Date());
                    dateFormater.applyPattern(C_String_Date_Format);
                }
            }
            result = dateFormater.format(new Date());
        }
        return result;
    }

    public static String formatNowDateTime(String... format) {
        String result;
        synchronized (dateTimeFormater) {
            if (format != null) {
                if (format.length > 0) {
                    dateTimeFormater.applyPattern(format[0]);
                    result = dateTimeFormater.format(new Date());
                    dateTimeFormater.applyPattern(C_String_Date_Time_Format);
                }
            }
            result = dateTimeFormater.format(new Date());
        }
        return result;
    }

    public static String formatDate(Date date, String... format) {
        String result;
        if (date == null) {
            return "";
        }
        synchronized (dateFormater) {
            if (format != null) {
                if (format.length > 0) {
                    dateFormater.applyPattern(format[0]);
                    result = dateFormater.format(date);
                    dateFormater.applyPattern(C_String_Date_Format);
                }
            }
            result = dateFormater.format(date);
        }
        return result;
    }

    public static String formatDateTime(Date date, String... format) {
        String result;
        if (date == null) {
            return "";
        }
        synchronized (dateTimeFormater) {
            if (format != null) {
                if (format.length > 0) {
                    dateTimeFormater.applyPattern(format[0]);
                    result = dateTimeFormater.format(date);
                    dateTimeFormater.applyPattern(C_String_Date_Time_Format);
                }
            }
            result = dateTimeFormater.format(date);
        }
        return result;
    }

    public static String contactForPath(String... paths) {
        if (paths == null || paths.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String append : paths) {
            sb.append(append).append("/");
        }
        String result = sb.toString();
        while (true) {
            int index = result.indexOf("//");
            if (index < 0) {
                return result;
            }
            result = result.substring(0, index) + result.substring(index + 1);
        }
    }

    public static String contactForFile(String... paths) {
        String result = contactForPath(paths);
        if (result.endsWith("/")) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static boolean isEmpty(String content) {
        return content == null || content.trim().length() == 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002b A[Catch:{ Exception -> 0x0030, all -> 0x005b }, LOOP:0: B:8:0x0024->B:10:0x002b, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0049 A[SYNTHETIC, Splitter:B:22:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0047 A[EDGE_INSN: B:36:0x0047->B:21:0x0047 ?: BREAK  
    EDGE_INSN: B:36:0x0047->B:21:0x0047 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String file2String(java.io.File r8, java.lang.String r9) {
        /*
            r3 = 0
            java.io.StringWriter r5 = new java.io.StringWriter
            r5.<init>()
            if (r9 == 0) goto L_0x0014
            java.lang.String r6 = ""
            java.lang.String r7 = r9.trim()     // Catch:{ Exception -> 0x0030 }
            boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x0030 }
            if (r6 == 0) goto L_0x003b
        L_0x0014:
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0030 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0030 }
            r6.<init>(r8)     // Catch:{ Exception -> 0x0030 }
            r4.<init>(r6, r9)     // Catch:{ Exception -> 0x0030 }
            r3 = r4
        L_0x001f:
            r6 = 1024(0x400, float:1.435E-42)
            char[] r0 = new char[r6]     // Catch:{ Exception -> 0x0030 }
            r2 = 0
        L_0x0024:
            r6 = -1
            int r2 = r3.read(r0)     // Catch:{ Exception -> 0x0030 }
            if (r6 == r2) goto L_0x0047
            r6 = 0
            r5.write(r0, r6, r2)     // Catch:{ Exception -> 0x0030 }
            goto L_0x0024
        L_0x0030:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x005b }
            r6 = 0
            if (r3 == 0) goto L_0x003a
            r3.close()     // Catch:{ IOException -> 0x0056 }
        L_0x003a:
            return r6
        L_0x003b:
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0030 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0030 }
            r6.<init>(r8)     // Catch:{ Exception -> 0x0030 }
            r4.<init>(r6)     // Catch:{ Exception -> 0x0030 }
            r3 = r4
            goto L_0x001f
        L_0x0047:
            if (r3 == 0) goto L_0x004c
            r3.close()     // Catch:{ IOException -> 0x0051 }
        L_0x004c:
            java.lang.String r6 = r5.toString()
            goto L_0x003a
        L_0x0051:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x004c
        L_0x0056:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003a
        L_0x005b:
            r6 = move-exception
            if (r3 == 0) goto L_0x0061
            r3.close()     // Catch:{ IOException -> 0x0062 }
        L_0x0061:
            throw r6
        L_0x0062:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.core.utils.StringUtils.file2String(java.io.File, java.lang.String):java.lang.String");
    }

    public static boolean isUri(String content) {
        return !isEmpty(content);
    }
}
