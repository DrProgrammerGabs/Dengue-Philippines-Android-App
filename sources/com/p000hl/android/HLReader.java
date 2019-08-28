package com.p000hl.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import com.p000hl.android.core.utils.ZipUtils;
import com.p000hl.callback.Action;
import java.io.File;
import java.io.IOException;

/* renamed from: com.hl.android.HLReader */
public class HLReader extends HLActivity {
    public static String bookID = "";
    private static AdViewRegister mAdRegster;
    AdViewRegister adViewSupporter;

    public void onCreate(Bundle savedInstanceState) {
        try {
            if (getIntent().getExtras().getInt("readtype") == 1) {
                initForPathReader(getIntent().getExtras().getString("readpath"));
            } else {
                initReaderForAssets();
            }
        } catch (Exception e) {
        }
        if (HLSetting.IS_ASSETS_ZIP || HLSetting.IS_SD_ZIP) {
            this.preLoadAction = new Action() {
                public boolean doAction() {
                    HLReader.this.progressHandler.sendEmptyMessage(0);
                    new AsyncTask<Object, Object, Boolean>() {
                        /* access modifiers changed from: protected */
                        public Boolean doInBackground(Object... arg0) {
                            HLReader.this.doZipRelease();
                            return Boolean.valueOf(true);
                        }

                        /* access modifiers changed from: protected */
                        public void onPostExecute(Boolean result) {
                            if (result.booleanValue()) {
                                HLReader.this.progressHandler.sendEmptyMessageDelayed(2, 500);
                                HLReader.this.progressHandler.sendEmptyMessage(1);
                                return;
                            }
                            HLReader.this.progressHandler.sendEmptyMessage(0);
                            Toast.makeText(HLReader.this, "解压数据文件出错,请检查您的apk文件是否损坏", 1).show();
                            HLReader.this.finish();
                        }
                    }.execute(new Object[0]);
                    return false;
                }
            };
        }
        super.onCreate(savedInstanceState);
    }

    public static void show(Context context) {
        initReaderForAssets();
        context.startActivity(new Intent(context, HLReader.class));
    }

    public static void initReaderForAssets() {
        HLSetting.IsResourceSD = false;
        BookSetting.IS_READER = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_AUTOPAGE = false;
        BookSetting.ISSUBPAGE_ENABLE = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_REMEMBER_READPAGE = false;
        BookSetting.IS_HOR_VER = false;
        BookSetting.SCREEN_WIDTH = 480;
        BookSetting.SCREEN_HEIGHT = 800;
        BookSetting.SNAPSHOTS_WIDTH = 320;
        BookSetting.SNAPSHOTS_HEIGHT = 280;
        BookSetting.RESIZE_WIDTH = 1.0f;
        BookSetting.RESIZE_HEIGHT = 1.0f;
        BookSetting.RESIZE_COUNT = 1.0f;
        BookSetting.FLIPCODE = 1;
        BookSetting.GALLEYCODE = 1;
        HLSetting.FitScreen = true;
        HLSetting.IS_ASSETS_ZIP = false;
        HLSetting.IS_SD_ZIP = false;
    }

    public static void show(Context context, String bookpath) {
        if (!new File(bookpath).exists()) {
            Toast.makeText(context, "传入的文件不存在", 1).show();
            return;
        }
        initForPathReader(bookpath);
        Log.d("hl", "open book " + HLSetting.IsResourceSD + "   path is " + BookSetting.BOOK_PATH);
        context.startActivity(new Intent(context, HLReader.class));
    }

    public static void initForPathReader(String bookpath) {
        HLSetting.IsResourceSD = true;
        BookSetting.BOOK_PATH = bookpath;
        BookSetting.IS_READER = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_AUTOPAGE = false;
        BookSetting.ISSUBPAGE_ENABLE = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_REMEMBER_READPAGE = false;
        BookSetting.IS_HOR_VER = false;
        BookSetting.SCREEN_WIDTH = 480;
        BookSetting.SCREEN_HEIGHT = 800;
        BookSetting.SNAPSHOTS_WIDTH = 320;
        BookSetting.SNAPSHOTS_HEIGHT = 280;
        BookSetting.RESIZE_WIDTH = 1.0f;
        BookSetting.RESIZE_HEIGHT = 1.0f;
        BookSetting.RESIZE_COUNT = 1.0f;
        BookSetting.FLIPCODE = 1;
        BookSetting.GALLEYCODE = 1;
        HLSetting.IS_ASSETS_ZIP = false;
        HLSetting.IS_SD_ZIP = false;
        HLSetting.FitScreen = true;
    }

    public static void showZipFile(Context context, String bookpath) {
        if (!new File(bookpath).exists()) {
            Toast.makeText(context, "传入的文件不存在", 1).show();
            return;
        }
        HLSetting.IsResourceSD = true;
        BookSetting.BOOK_PATH = bookpath;
        BookSetting.IS_READER = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_AUTOPAGE = false;
        BookSetting.ISSUBPAGE_ENABLE = true;
        BookSetting.IS_HOR = true;
        BookSetting.IS_REMEMBER_READPAGE = false;
        BookSetting.IS_HOR_VER = false;
        BookSetting.SCREEN_WIDTH = 480;
        BookSetting.SCREEN_HEIGHT = 800;
        BookSetting.SNAPSHOTS_WIDTH = 320;
        BookSetting.SNAPSHOTS_HEIGHT = 280;
        BookSetting.RESIZE_WIDTH = 1.0f;
        BookSetting.RESIZE_HEIGHT = 1.0f;
        BookSetting.RESIZE_COUNT = 1.0f;
        BookSetting.FLIPCODE = 1;
        BookSetting.GALLEYCODE = 1;
        HLSetting.FitScreen = true;
        HLSetting.IS_ASSETS_ZIP = false;
        Intent intent = new Intent(context, HLReader.class);
        HLSetting.IS_SD_ZIP = true;
        context.startActivity(intent);
    }

    public static void setFixSize(int initScreenWidth, int initScreenHeight) {
        BookSetting.FIX_SIZE = true;
        BookSetting.INIT_SCREEN_WIDTH = initScreenWidth;
        BookSetting.INIT_SCREEN_HEIGHT = initScreenHeight;
    }

    public static void setShelves(Boolean isShelves) {
        BookSetting.IS_SHELVES = isShelves.booleanValue();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void initAdView(View view) {
    }

    /* access modifiers changed from: private */
    public boolean doZipRelease() {
        if (HLSetting.IS_ASSETS_ZIP) {
            String releasePath = BookSetting.BOOK_RESOURCESD_ROOT + getApplication().getPackageName() + "/";
            BookSetting.BOOK_PATH = releasePath + "book";
            HLSetting.IsResourceSD = true;
            AssetManager assetManager = getAssets();
            long start = System.currentTimeMillis();
            if (!new File(BookSetting.BOOK_PATH + "/book.xml").exists()) {
                try {
                    ZipUtils.unzip(assetManager.open(BookSetting.BOOK_RESOURCE_ZIP_NAME), releasePath);
                } catch (IOException e) {
                    return false;
                }
            }
            Log.i("hl", " unzip time is " + (System.currentTimeMillis() - start));
            return true;
        } else if (!HLSetting.IS_SD_ZIP) {
            return true;
        } else {
            String releasePath2 = BookSetting.BOOK_RESOURCESD_ROOT + getApplication().getPackageName();
            String zipPath = BookSetting.BOOK_PATH;
            String releasePath3 = releasePath2 + zipPath.substring(zipPath.lastIndexOf("/"), zipPath.lastIndexOf("."));
            BookSetting.BOOK_PATH = releasePath3 + "/book";
            HLSetting.IsResourceSD = true;
            if (!new File(BookSetting.BOOK_PATH + "/book.xml").exists()) {
                try {
                    ZipUtils.UnZipFolder(zipPath, releasePath3);
                } catch (Exception e2) {
                    Toast.makeText(this, "解压出错，请检查您的zip文件是否有误", 1).show();
                    return false;
                }
            }
            return true;
        }
    }

    public static void setAdRegster(AdViewRegister adRegster) {
        mAdRegster = adRegster;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (mAdRegster != null) {
            mAdRegster.recyle(this);
        }
        super.onDestroy();
    }
}
