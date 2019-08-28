package com.p000hl.android.core.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.hl.android.core.utils.DataUtils */
public class DataUtils {
    private static String C_Str_DataBaseName = "database";
    private static String C_Str_SharedPreferencesName = "sharedpreferences";
    private static File dataFile = null;
    private static String dataFilePath = null;
    private static SQLiteDatabase hlDb = null;
    private static SharedPreferences hlPreferences = null;

    private static SQLiteDatabase getDb(Activity activity) {
        if (hlDb == null || !hlDb.isOpen()) {
            if (dataFilePath == null) {
                dataFilePath = StringUtils.contactForFile(AppUtils.getAppPath(activity), C_Str_DataBaseName);
            }
            if (dataFile == null) {
                dataFile = new File(dataFilePath);
            }
            if (!dataFile.exists()) {
                try {
                    InputStream in = activity.getResources().getAssets().open(C_Str_DataBaseName);
                    FileOutputStream fos = new FileOutputStream(dataFilePath);
                    try {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int count = in.read(buffer);
                            if (count <= 0) {
                                break;
                            }
                            fos.write(buffer, 0, count);
                        }
                        in.close();
                        fos.close();
                    } catch (Exception e) {
                        FileOutputStream fileOutputStream = fos;
                    }
                } catch (Exception e2) {
                }
            }
            hlDb = SQLiteDatabase.openOrCreateDatabase(dataFilePath, null);
        }
        try {
            SQLiteDatabase.releaseMemory();
            if (hlDb.isDbLockedByOtherThreads()) {
                Thread.sleep(100);
            }
        } catch (Exception e3) {
        }
        return hlDb;
    }

    public static boolean checkTableExists(Activity activity, String tableName) {
        Cursor cursor = null;
        try {
            cursor = rawQuery(activity, "select * from sqlite_master where tbl_name=?", tableName);
            if (cursor.getCount() != 0) {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                return true;
            } else if (cursor == null || cursor.isClosed()) {
                return false;
            } else {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            if (cursor == null || cursor.isClosed()) {
                return false;
            }
            cursor.close();
            return false;
        } catch (Throwable th) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            throw th;
        }
    }

    public static Cursor rawQuery(Activity activity, String strSql, String... params) {
        try {
            return getDb(activity).rawQuery(strSql, params);
        } catch (Exception e) {
            Log.e("hl", "查询数据库时出现错误！", e);
            return null;
        }
    }

    public static void execSQL(Activity activity, String strSql, Object... params) {
        if (params != null) {
            try {
                if (params.length != 0) {
                    getDb(activity).execSQL(strSql, params);
                    return;
                }
            } catch (Exception e) {
                Log.e("hl", "执行SQL语句时出现错误！", e);
                return;
            }
        }
        getDb(activity).execSQL(strSql);
    }

    public static long insert(Activity activity, String strTable, String nullColumnHack, ContentValues values) {
        try {
            return getDb(activity).insert(strTable, nullColumnHack, values);
        } catch (Exception e) {
            Log.e("hl", "执行插入语句时出现错误！", e);
            return -1;
        }
    }

    public static int update(Activity activity, String strTable, ContentValues values, String whereClause, String[] params) {
        try {
            return getDb(activity).update(strTable, values, whereClause, params);
        } catch (Exception e) {
            Log.e("hl", "执行更新语句时出现错误！", e);
            return -1;
        }
    }

    public static ArrayList<HashMap<String, String>> getSqlResult(Activity activity, String sql, String... values) {
        Cursor cursor = getDb(activity).rawQuery(sql, values);
        ArrayList<HashMap<String, String>> lstResult = new ArrayList<>();
        if (cursor != null) {
            try {
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        HashMap<String, String> mapRow = new HashMap<>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            mapRow.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        lstResult.add(mapRow);
                    }
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                    return lstResult;
                }
            } catch (Exception e) {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                throw th;
            }
        }
        lstResult = null;
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return lstResult;
    }

    public static void insertTableValue(Activity activity, String table, String columnName, ContentValues values) {
        try {
            getDb(activity).insert(table, columnName, values);
        } catch (Exception e) {
            Log.e("hl", "执行插入语句时出现错误！", e);
        }
    }

    public static void closeDb() {
        if (hlDb != null) {
            hlDb.close();
            hlDb = null;
        }
    }

    private static SharedPreferences getPreferences(Context activity) {
        if (hlPreferences == null) {
            if (activity == null) {
                return null;
            }
            hlPreferences = activity.getSharedPreferences(C_Str_SharedPreferencesName, 0);
        }
        return hlPreferences;
    }

    public static void savePreference(Activity activity, String key, Boolean value) {
        try {
            getPreferences(activity).edit().putBoolean(key, value.booleanValue()).commit();
        } catch (Exception e) {
            Log.e("hl", "保存偏好设置时出错！", e);
        }
    }

    public static boolean getPreference(Activity activity, String key, boolean defValue) {
        try {
            return getPreferences(activity).getBoolean(key, defValue);
        } catch (Exception e) {
            Log.e("hl", "获取偏好设置时出错！", e);
            return defValue;
        }
    }

    public static void savePreference(Activity activity, String key, float value) {
        try {
            getPreferences(activity).edit().putFloat(key, value).commit();
        } catch (Exception e) {
            Log.e("hl", "保存偏好设置时出错！", e);
        }
    }

    public static float getPreference(Activity activity, String key, float defValue) {
        try {
            return getPreferences(activity).getFloat(key, defValue);
        } catch (Exception e) {
            Log.e("hl", "获取偏好设置时出错！", e);
            return defValue;
        }
    }

    public static void savePreference(Activity activity, String key, int value) {
        try {
            getPreferences(activity).edit().putInt(key, value).commit();
        } catch (Exception e) {
            Log.e("hl", "保存偏好设置时出错！", e);
        }
    }

    public static int getPreference(Activity activity, String key, int defValue) {
        try {
            return getPreferences(activity).getInt(key, defValue);
        } catch (Exception e) {
            Log.e("hl", "获取偏好设置时出错！", e);
            return defValue;
        }
    }

    public static void savePreference(Activity activity, String key, long value) {
        try {
            getPreferences(activity).edit().putLong(key, value).commit();
        } catch (Exception e) {
            Log.e("hl", "保存偏好设置时出错！", e);
        }
    }

    public static long getPreference(Activity activity, String key, long defValue) {
        try {
            return getPreferences(activity).getLong(key, defValue);
        } catch (Exception e) {
            Log.e("hl", "获取偏好设置时出错！", e);
            return defValue;
        }
    }

    public static void savePreference(Activity activity, String key, String value) {
        try {
            getPreferences(activity).edit().putString(key, value).commit();
        } catch (Exception e) {
            Log.e("hl", "保存偏好设置时出错！", e);
        }
    }

    public static String getPreference(Activity activity, String key, String defValue) {
        try {
            return getPreferences(activity).getString(key, defValue);
        } catch (Exception e) {
            Log.e("hl", "获取偏好设置时出错！", e);
            return defValue;
        }
    }

    public static void saveSerializable(Activity activity, String key, Serializable value) {
        try {
            String filePath = AppUtils.getAppPath(activity) + "/" + key;
            if (!new File(filePath).exists()) {
                new File(filePath).createNewFile();
            }
            ObjectOutputStream sos = new ObjectOutputStream(new FileOutputStream(filePath));
            sos.writeObject(value);
            sos.close();
        } catch (Exception e) {
            Log.e("hl", "保存序列化对象的时候出错", e);
        }
    }

    public static <T> T getSerializable(Activity activity, String key) {
        try {
            File file = new File(AppUtils.getAppPath(activity) + "/" + key);
            if (!file.exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object readObject = ois.readObject();
            ois.close();
            return readObject;
        } catch (Exception e) {
            Log.d("hl", "获得序列化对象的时候出错");
            return null;
        }
    }
}
