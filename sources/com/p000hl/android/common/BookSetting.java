package com.p000hl.android.common;

import android.os.Environment;
import com.p000hl.android.book.entity.ButtonEntity;
import java.util.ArrayList;

/* renamed from: com.hl.android.common.BookSetting */
public class BookSetting {
    public static int BOOK_HEIGHT = 800;
    public static float BOOK_HEIGHT4CALCULATE = ((float) BOOK_HEIGHT);
    public static String BOOK_ID = "";
    public static String BOOK_PATH = "";
    public static String BOOK_RESOURCESD_ROOT = (Environment.getExternalStorageDirectory() + "/hl/");
    public static String BOOK_RESOURCE_DIR = "book/";
    public static String BOOK_RESOURCE_ZIP_NAME = "book.zip";
    public static int BOOK_WIDTH = 480;
    public static float BOOK_WIDTH4CALCULATE = ((float) BOOK_WIDTH);
    public static String CURRENTBOOKID = "";
    public static boolean FITSCREEN_TENSILE = true;
    public static boolean FIX_SIZE = false;
    public static int FLIPCODE = 1;
    public static boolean FLIP_CHANGE_PAGE = true;
    public static int GALLEYCODE = 1;
    public static String INIT_BOOK_PATH = "";
    public static int INIT_SCREEN_HEIGHT = 800;
    public static int INIT_SCREEN_WIDTH = 480;
    public static boolean ISSUBPAGE_ENABLE = true;
    public static boolean IS_AUTOPAGE = false;
    public static boolean IS_CLOSED = false;
    public static boolean IS_HOR = true;
    public static boolean IS_HOR_VER = false;
    public static boolean IS_NO_NAVIGATION = false;
    public static boolean IS_READER = false;
    public static boolean IS_REMEMBER_READPAGE = false;
    public static boolean IS_SHELVES = false;
    public static boolean IS_SHELVES_COMPONENT = false;
    public static boolean IS_SHOW_LOADINGBAR = false;
    public static boolean IS_TRY = false;
    public static String LOCATION = null;
    public static final float MAX_SCALE = 5.0f;
    public static final float MIN_SCALE = 1.0f;
    public static float PAGE_RATIO = 1.0f;
    public static float PAGE_RATIOX = 1.0f;
    public static float PAGE_RATIOY = 1.0f;
    public static float RESIZE_COUNT = 1.0f;
    public static float RESIZE_HEIGHT = 1.0f;
    public static float RESIZE_WIDTH = 1.0f;
    public static int SCREEN_HEIGHT = 800;
    public static int SCREEN_WIDTH = 480;
    public static int SNAPSHOTS_HEIGHT = 280;
    public static int SNAPSHOTS_WIDTH = 320;
    public static ArrayList<ButtonEntity> buttons;
    public static String fileName = "book.dat";
    public static boolean noBackGround = false;
}
