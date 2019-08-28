package com.p000hl.android.core.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Environment;
import android.util.Log;
import com.p000hl.android.book.BookDecoder;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.controller.BookController;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/* renamed from: com.hl.android.core.utils.FileUtils */
public class FileUtils {
    public static FileUtils fileUtils;
    private String bookStorePath;

    public void init(Context context) {
    }

    public static FileUtils getInstance() {
        if (fileUtils == null) {
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    public AssetFileDescriptor getFileFD(Context con, String fileName) {
        AssetFileDescriptor assFD = null;
        try {
            return con.getAssets().openFd(BookSetting.BOOK_RESOURCE_DIR + fileName);
        } catch (IOException e) {
            Log.e("hl", "getFileFD  ", e);
            return assFD;
        }
    }

    public InputStream getFileInputStream(Context con, String fileName) {
        try {
            if (HLSetting.IsResourceSD) {
                return getFileInputStream(fileName);
            }
            return con.getAssets().open(BookSetting.BOOK_RESOURCE_DIR + fileName);
        } catch (IOException e) {
            Log.e("hl", " getFileInputStream ", e);
            return null;
        }
    }

    public String getFilePath(String fileName) {
        return BookSetting.BOOK_PATH + "/" + fileName;
    }

    public InputStream getFileInputStream(String fileName) {
        try {
            FileInputStream in = new FileInputStream(new File(BookSetting.BOOK_PATH + "/" + fileName));
            return in;
        } catch (Exception e) {
            return null;
        }
    }

    public InputStream getFileInputStreamFilePath(String fileName) {
        try {
            FileInputStream in = new FileInputStream(new File(fileName));
            return in;
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] readFileToByteArrayForHead(Context con, String fileName) throws IOException {
        if (HLSetting.IsResourceSD) {
            return readFileToByteArray(fileName, 0, 1048576);
        }
        try {
            InputStream in = con.getAssets().open(BookSetting.BOOK_RESOURCE_DIR + fileName);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            return buffer;
        } catch (Exception e) {
            Log.e("hl", fileName + " not exists");
            return null;
        }
    }

    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String str = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                return buffer.toString();
            }
            buffer.append(line);
        }
    }

    public static InputStream readFile(String fileName) throws IOException {
        InputStream in;
        if (HLSetting.IsResourceSD) {
            File file = new File(BookSetting.BOOK_PATH + "/" + fileName);
            if (!file.exists()) {
                return null;
            }
            try {
                in = new FileInputStream(file);
            } catch (Exception e) {
                return null;
            }
        } else {
            in = BookController.getInstance().hlActivity.getAssets().open(BookSetting.BOOK_RESOURCE_DIR + fileName);
        }
        return in;
    }

    public static InputStream readBookPage(String fileName, int startIndex, int endIndex) {
        InputStream in = null;
        InputStream is = null;
        try {
            InputStream in2 = readFile(fileName);
            if (in2 == null) {
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    is.close();
                }
                return null;
            }
            byte[] indexLen = new byte[4];
            in2.read(indexLen);
            byte[] dd = new byte[(endIndex - startIndex)];
            in2.skip((long) (BookDecoder.fromArray(indexLen) + startIndex));
            in2.read(dd);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dd);
            if (in2 != null) {
                try {
                    in2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            ByteArrayInputStream byteArrayInputStream2 = byteArrayInputStream;
            return byteArrayInputStream;
        } catch (Exception e3) {
            Log.e("hl", "readBookPage " + fileName + " error " + e3.getMessage());
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return null;
                }
            }
            if (is != null) {
                is.close();
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                    throw th;
                }
            }
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0075 A[SYNTHETIC, Splitter:B:39:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007a A[Catch:{ Exception -> 0x007e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readBookIndex(java.lang.String r12) {
        /*
            r2 = 0
            r4 = 0
            java.io.InputStream r2 = readFile(r12)     // Catch:{ Exception -> 0x0043 }
            if (r2 != 0) goto L_0x0019
            r8 = 0
            if (r2 == 0) goto L_0x000e
            r2.close()     // Catch:{ Exception -> 0x0014 }
        L_0x000e:
            if (r4 == 0) goto L_0x0013
            r4.close()     // Catch:{ Exception -> 0x0014 }
        L_0x0013:
            return r8
        L_0x0014:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0013
        L_0x0019:
            r9 = 4
            byte[] r3 = new byte[r9]     // Catch:{ Exception -> 0x0043 }
            r2.read(r3)     // Catch:{ Exception -> 0x0043 }
            int r6 = com.p000hl.android.book.BookDecoder.fromArray(r3)     // Catch:{ Exception -> 0x0043 }
            r7 = r6
            byte[] r0 = new byte[r7]     // Catch:{ Exception -> 0x0043 }
            r2.read(r0)     // Catch:{ Exception -> 0x0043 }
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0043 }
            r5.<init>(r0)     // Catch:{ Exception -> 0x0043 }
            java.lang.String r8 = inputStream2String(r5)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            if (r2 == 0) goto L_0x0037
            r2.close()     // Catch:{ Exception -> 0x003e }
        L_0x0037:
            if (r5 == 0) goto L_0x003c
            r5.close()     // Catch:{ Exception -> 0x003e }
        L_0x003c:
            r4 = r5
            goto L_0x0013
        L_0x003e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003c
        L_0x0043:
            r1 = move-exception
        L_0x0044:
            java.lang.String r9 = "hl"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0072 }
            r10.<init>()     // Catch:{ all -> 0x0072 }
            java.lang.String r11 = "get bookindex error "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0072 }
            java.lang.String r11 = r1.getMessage()     // Catch:{ all -> 0x0072 }
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0072 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0072 }
            android.util.Log.e(r9, r10)     // Catch:{ all -> 0x0072 }
            if (r2 == 0) goto L_0x0065
            r2.close()     // Catch:{ Exception -> 0x006d }
        L_0x0065:
            if (r4 == 0) goto L_0x006a
            r4.close()     // Catch:{ Exception -> 0x006d }
        L_0x006a:
            java.lang.String r8 = ""
            goto L_0x0013
        L_0x006d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x006a
        L_0x0072:
            r9 = move-exception
        L_0x0073:
            if (r2 == 0) goto L_0x0078
            r2.close()     // Catch:{ Exception -> 0x007e }
        L_0x0078:
            if (r4 == 0) goto L_0x007d
            r4.close()     // Catch:{ Exception -> 0x007e }
        L_0x007d:
            throw r9
        L_0x007e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x007d
        L_0x0083:
            r9 = move-exception
            r4 = r5
            goto L_0x0073
        L_0x0086:
            r1 = move-exception
            r4 = r5
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.core.utils.FileUtils.readBookIndex(java.lang.String):java.lang.String");
    }

    public static byte[] readFileToByteArray(String fileName, int startIndex, int endIndex) throws IOException {
        InputStream in;
        if (HLSetting.IsResourceSD) {
            File file = new File(BookSetting.BOOK_PATH + "/" + fileName);
            if (!file.exists()) {
                return null;
            }
            try {
                in = new FileInputStream(file);
            } catch (Exception e) {
                return null;
            }
        } else {
            in = BookController.getInstance().hlActivity.getAssets().open(BookSetting.BOOK_RESOURCE_DIR + fileName);
        }
        ByteArrayOutputStream bouts = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int curSize = 0;
        boolean started = false;
        do {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            curSize += len;
            Log.d("hl", "bookdata content is " + inputStream2String(new ByteArrayInputStream(buf)));
            if (curSize >= startIndex && !started) {
                if ((buf.length + startIndex) - curSize < 0) {
                }
                bouts.write(buf, 0, len);
                started = true;
            }
        } while (curSize <= endIndex);
        byte[] byteArray = bouts.toByteArray();
        in.close();
        bouts.close();
        Log.d("hl", inputStream2String(new ByteArrayInputStream(bouts.toByteArray())));
        return byteArray;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0042, code lost:
        r0.inSampleSize = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return android.graphics.BitmapFactory.decodeStream(r3, null, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f A[ExcHandler: Exception (e java.lang.Exception), Splitter:B:1:0x0017] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap load(java.lang.String r9, int r10, int r11, android.content.Context r12) {
        /*
            r8 = this;
            r4 = 0
            r7 = 1
            r1 = 0
            android.graphics.BitmapFactory$Options r0 = new android.graphics.BitmapFactory$Options
            r0.<init>()
            r5 = 0
            r0.inDither = r5
            r0.inPurgeable = r7
            r0.inInputShareable = r7
            r5 = 32768(0x8000, float:4.5918E-41)
            byte[] r5 = new byte[r5]
            r0.inTempStorage = r5
            r3 = 0
            boolean r5 = com.p000hl.android.common.HLSetting.IsResourceSD     // Catch:{ OutOfMemoryError -> 0x0036, Exception -> 0x003f }
            if (r5 == 0) goto L_0x002d
            com.hl.android.core.utils.FileUtils r5 = getInstance()     // Catch:{ OutOfMemoryError -> 0x0036, Exception -> 0x003f }
            java.io.InputStream r3 = r5.getFileInputStream(r9)     // Catch:{ OutOfMemoryError -> 0x0036, Exception -> 0x003f }
        L_0x0023:
            r5 = 0
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r3, r5, r0)     // Catch:{ Exception -> 0x003f, OutOfMemoryError -> 0x0041 }
            android.graphics.Bitmap r4 = android.graphics.Bitmap.createScaledBitmap(r1, r10, r11, r7)
        L_0x002c:
            return r4
        L_0x002d:
            com.hl.android.core.utils.FileUtils r5 = getInstance()     // Catch:{ OutOfMemoryError -> 0x0036, Exception -> 0x003f }
            java.io.InputStream r3 = r5.getFileInputStream(r12, r9)     // Catch:{ OutOfMemoryError -> 0x0036, Exception -> 0x003f }
            goto L_0x0023
        L_0x0036:
            r2 = move-exception
            java.lang.String r5 = "hl"
            java.lang.String r6 = "  load"
            android.util.Log.e(r5, r6, r2)     // Catch:{ Exception -> 0x003f, OutOfMemoryError -> 0x0041 }
            goto L_0x0023
        L_0x003f:
            r2 = move-exception
            goto L_0x002c
        L_0x0041:
            r2 = move-exception
            r5 = 2
            r0.inSampleSize = r5
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r3, r4, r0)
            r4 = r1
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p000hl.android.core.utils.FileUtils.load(java.lang.String, int, int, android.content.Context):android.graphics.Bitmap");
    }

    public String getDataFilePath(Context con, String fileName) {
        for (String name : con.getFilesDir().list()) {
            if (name.contains(fileName)) {
                return con.getFilesDir().getAbsolutePath() + "/" + fileName;
            }
        }
        return null;
    }

    public File getDataFile(Context con, String fileName) {
        for (String name : con.getFilesDir().list()) {
            if (name.contains(fileName)) {
                return new File(con.getFilesDir().getAbsolutePath() + "/" + fileName);
            }
        }
        return null;
    }

    public void copyFileToData(Context con, String fileName) {
        InputStream in;
        try {
            String[] arr$ = con.getFilesDir().list();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                if (!arr$[i$].contains(fileName)) {
                    i$++;
                } else {
                    return;
                }
            }
            if (HLSetting.IsResourceSD) {
                in = getInstance().getFileInputStream(fileName);
            } else {
                in = getInstance().getFileInputStream(con, fileName);
            }
            FileOutputStream outputStream = con.openFileOutput(fileName, 0);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    outputStream.write(buf, 0, len);
                } else {
                    in.close();
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFileToSDCard(Context con, String fileName) {
        InputStream in;
        try {
            File outFile = new File(Environment.getExternalStorageDirectory() + "/", fileName);
            if (!outFile.exists()) {
                if (HLSetting.IsResourceSD) {
                    in = getInstance().getFileInputStream(fileName);
                } else {
                    in = getInstance().getFileInputStream(con, fileName);
                }
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len > 0) {
                        out.write(buf, 0, len);
                    } else {
                        in.close();
                        out.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(InputStream inStream, String newPathFile) {
        if (inStream == null) {
            return false;
        }
        try {
            File outFile = new File(newPathFile);
            if (!outFile.exists()) {
                if (outFile.getParentFile() != null && !outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                outFile.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(outFile, false);
            byte[] buffer = new byte[1024];
            while (true) {
                int byteread = inStream.read(buffer);
                if (byteread != -1) {
                    fs.write(buffer, 0, byteread);
                } else {
                    inStream.close();
                    fs.flush();
                    fs.close();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("MarsorErroring", "复制单个文件时出错！文件：" + inStream + ",新路径：" + newPathFile, e);
            return false;
        }
    }

    public static boolean isExist(String filePath) {
        try {
            return new File(filePath).exists();
        } catch (OutOfMemoryError e) {
            Log.e("hl", "  load", e);
            return false;
        }
    }

    public static void deleteFile(String filePath) {
        try {
            del(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBookStorePath() {
        return this.bookStorePath;
    }

    public void setBookStorePath(String bookStorePath2) {
        this.bookStorePath = bookStorePath2;
    }

    public static void del(String filepath) throws IOException {
        File f = new File(filepath);
        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length == 0) {
                f.delete();
                return;
            }
            File[] delFile = f.listFiles();
            int i = f.listFiles().length;
            for (int j = 0; j < i; j++) {
                if (delFile[j].isDirectory()) {
                    del(delFile[j].getAbsolutePath());
                }
                delFile[j].delete();
            }
        }
    }
}
