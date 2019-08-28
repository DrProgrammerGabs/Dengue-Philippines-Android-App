package com.mediav.ads.sdk.adcore;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.mediav.ads.sdk.adcore.HttpRequester.Listener;
import com.mediav.ads.sdk.interfaces.IBridge;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UpdateBridge {
    private static final String SP_SDK_VER = "ad_sdk_ver";
    /* access modifiers changed from: private */
    public static Activity activity = null;
    /* access modifiers changed from: private */
    public static String md5 = null;

    @TargetApi(14)
    public static IBridge getBridge(Activity _activity) {
        activity = _activity;
        try {
            File dir = new File(new StringBuilder(String.valueOf(activity.getFilesDir().getAbsolutePath())).append(Config.UPDATE_DIR).toString());
            File opt_dir = new File(new StringBuilder(String.valueOf(activity.getFilesDir().getAbsolutePath())).append(Config.OPT_DIR).toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!opt_dir.exists()) {
                opt_dir.mkdirs();
            }
            File n_jar_file = new File(dir.getAbsolutePath() + "/" + Config.NEW_JAR);
            File d_jar_file = new File(dir.getAbsolutePath() + "/" + Config.DEFAULT_JAR);
            if (n_jar_file.exists()) {
                if (d_jar_file.exists()) {
                    d_jar_file.delete();
                }
                n_jar_file.renameTo(d_jar_file);
                CLog.m2d("new jar replaced old jar");
            } else if (!d_jar_file.exists()) {
                d_jar_file.createNewFile();
                InputStream in = activity.getAssets().open(Config.DEFAULT_JAR);
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                in.close();
                FileOutputStream fos = new FileOutputStream(d_jar_file, false);
                fos.write(buffer);
                fos.close();
            }
            IBridge mvad = (IBridge) new DexClassLoader(d_jar_file.getAbsolutePath(), opt_dir.getAbsolutePath(), null, activity.getClassLoader()).loadClass(Config.PACKAGE_URI).newInstance();
            getNewJar();
            return mvad;
        } catch (ClassNotFoundException e) {
            CLog.m4e("重建更新环境失败: ClassNotFoundException + " + e.getMessage());
            File dir2 = new File(new StringBuilder(String.valueOf(activity.getFilesDir().getAbsolutePath())).append(Config.UPDATE_DIR).toString());
            File n_jar_file2 = new File(dir2.getAbsolutePath() + "/" + Config.NEW_JAR);
            File d_jar_file2 = new File(dir2.getAbsolutePath() + "/" + Config.DEFAULT_JAR);
            if (n_jar_file2.exists()) {
                n_jar_file2.delete();
            }
            if (d_jar_file2.exists()) {
                d_jar_file2.delete();
            }
            return null;
        } catch (Exception e2) {
            CLog.m4e("重建更新环境失败: " + e2.getMessage());
            return null;
        }
    }

    private static void getNewJar() {
        HttpRequester.getAsynData(activity, "http://show.m.mediav.com/update?sdkv=160", Boolean.valueOf(false), new Listener() {
            public void onGetDataSucceed(byte[] data) {
                UpdateBridge.parserJson(data);
            }

            public void onGetDataFailed(String error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public static void parserJson(byte[] data) {
        try {
            JSONObject adjson = (JSONObject) new JSONTokener(new String(data)).nextValue();
            final int newVer = adjson.getInt("ver");
            int currentVer = Integer.parseInt(getVer(activity));
            md5 = adjson.getString("md5");
            String url = adjson.getString("sdk_url");
            if (newVer > currentVer) {
                CLog.m2d("new version sdk found");
                HttpRequester.getAsynData(activity, url, Boolean.valueOf(false), new Listener() {
                    public void onGetDataSucceed(byte[] buffer) {
                        CLog.m4e("download jar");
                        File n_jar_file = new File(new StringBuilder(String.valueOf(new File(new StringBuilder(String.valueOf(UpdateBridge.activity.getFilesDir().getAbsolutePath())).append(Config.UPDATE_DIR).toString()).getAbsolutePath())).append("/").append(Config.NEW_JAR).toString());
                        if (n_jar_file.exists()) {
                            n_jar_file.delete();
                        }
                        try {
                            n_jar_file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(n_jar_file, false);
                            fos.write(buffer);
                            fos.close();
                            if (UpdateBridge.md5 == null) {
                                return;
                            }
                            if (!UpdateBridge.getMd5ByFile(n_jar_file).equals(UpdateBridge.md5)) {
                                CLog.m4e("md5 校验失败");
                                n_jar_file.delete();
                                return;
                            }
                            CLog.m2d("new jar saved");
                            UpdateBridge.setVer(UpdateBridge.activity, new StringBuilder(String.valueOf(newVer)).toString());
                        } catch (Exception e) {
                            CLog.m4e("写入新包错误:" + e.getMessage());
                        }
                    }

                    public void onGetDataFailed(String error) {
                    }
                });
                return;
            }
            CLog.m2d("sdk update to latest");
        } catch (Exception e) {
            CLog.m4e("解析更新数据失败:" + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public static void setVer(Activity activity2, String version) {
        Editor editor = activity2.getSharedPreferences("ver_info", 0).edit();
        editor.putString(SP_SDK_VER, version);
        editor.commit();
    }

    private static String getVer(Activity activity2) {
        String str = "";
        SharedPreferences verInfo = activity2.getSharedPreferences("ver_info", 0);
        String ver = verInfo.getString(SP_SDK_VER, null);
        if (ver != null) {
            return ver;
        }
        verInfo.edit().putString(SP_SDK_VER, Config.DEFAULT_SDK_VER).commit();
        return verInfo.getString(SP_SDK_VER, null);
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(MapMode.READ_ONLY, 0, file.length());
            MessageDigest md52 = MessageDigest.getInstance("MD5");
            md52.update(byteBuffer);
            value = new BigInteger(1, md52.digest()).toString(16);
            in.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }
}
