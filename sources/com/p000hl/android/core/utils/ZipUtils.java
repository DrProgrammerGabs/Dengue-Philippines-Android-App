package com.p000hl.android.core.utils;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* renamed from: com.hl.android.core.utils.ZipUtils */
public class ZipUtils {
    public static void unzip(InputStream zipFileName, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(zipFileName);
            long maxDelay = 0;
            for (ZipEntry entry = in.getNextEntry(); entry != null; entry = in.getNextEntry()) {
                new File(outputDirectory).mkdirs();
                if (entry.isDirectory()) {
                    String name = entry.getName();
                    new File(outputDirectory + File.separator + name.substring(0, name.length() - 1)).mkdir();
                } else {
                    File file = new File(outputDirectory + entry.getName());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[4096];
                    long start = System.currentTimeMillis();
                    while (true) {
                        int len = in.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                    long delay = System.currentTimeMillis() - start;
                    if (delay > maxDelay) {
                        maxDelay = delay;
                    }
                }
            }
            Log.d("hl", "max delay is " + maxDelay);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
        Log.v("XZip", "GetFileList(String)");
        List<File> fileList = new ArrayList<>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        String str = "";
        while (true) {
            ZipEntry zipEntry = inZip.getNextEntry();
            if (zipEntry != null) {
                String szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File folder = new File(szName.substring(0, szName.length() - 1));
                    if (bContainFolder) {
                        fileList.add(folder);
                    }
                } else {
                    File file = new File(szName);
                    if (bContainFile) {
                        fileList.add(file);
                    }
                }
            } else {
                inZip.close();
                return fileList;
            }
        }
    }

    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        Log.v("XZip", "UpZip(String, String)");
        ZipFile zipFile = new ZipFile(zipFileString);
        return zipFile.getInputStream(zipFile.getEntry(fileString));
    }

    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        Log.v("XZip", "UnZipFolder(String, String)");
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        String str = "";
        File file1 = new File(outPathString);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        while (true) {
            ZipEntry zipEntry = inZip.getNextEntry();
            if (zipEntry != null) {
                String szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    new File(outPathString + File.separator + szName.substring(0, szName.length() - 1)).mkdirs();
                } else {
                    File file = new File(outPathString + File.separator + szName);
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = inZip.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            } else {
                inZip.close();
                return;
            }
        }
    }

    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        File file = new File(srcFileString);
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        outZip.finish();
        outZip.close();
    }

    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
        if (zipOutputSteam != null) {
            File file = new File(folderString + fileString);
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(fileString);
                FileInputStream inputStream = new FileInputStream(file);
                zipOutputSteam.putNextEntry(zipEntry);
                byte[] buffer = new byte[4096];
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len != -1) {
                        zipOutputSteam.write(buffer, 0, len);
                    } else {
                        zipOutputSteam.closeEntry();
                        inputStream.close();
                        return;
                    }
                }
            } else {
                String[] fileList = file.list();
                if (fileList.length <= 0) {
                    zipOutputSteam.putNextEntry(new ZipEntry(fileString + File.separator));
                    zipOutputSteam.closeEntry();
                }
                for (int i = 0; i < fileList.length; i++) {
                    ZipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
                }
            }
        }
    }

    public void finalize() throws Throwable {
    }
}
