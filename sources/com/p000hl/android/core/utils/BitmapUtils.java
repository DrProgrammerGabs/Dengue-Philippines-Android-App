package com.p000hl.android.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.p000hl.android.common.BookSetting;
import com.p000hl.android.common.HLSetting;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.hl.android.core.utils.BitmapUtils */
public final class BitmapUtils {
    public static Bitmap getBitmap(Bitmap source, int x, int y, int width, int height) {
        return Bitmap.createBitmap(source, x, y, width, height);
    }

    public static Bitmap getBitMap(String localSourceID, Context context) {
        Bitmap resultBitmap = null;
        try {
            if (HLSetting.IsResourceSD) {
                resultBitmap = BitmapFactory.decodeFile(BookSetting.BOOK_PATH + "/" + localSourceID);
            } else {
                resultBitmap = load(FileUtils.getInstance().getFileInputStream(context, localSourceID));
            }
        } catch (OutOfMemoryError e) {
            Log.e("hl", " imagecomponents load", e);
        }
        if (resultBitmap == null) {
            Log.i("hl", "获取图片失败，图片名称是 " + localSourceID);
        }
        return resultBitmap;
    }

    public static Bitmap getWdyBitmapFromSD(String fileName, int width, int height) {
        System.gc();
        String actfileName = BookSetting.BOOK_PATH + "/" + fileName;
        Bitmap bitmap = null;
        Options _option = new Options();
        _option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(actfileName, _option);
        float realWidth = (float) _option.outWidth;
        float realHeight = (float) _option.outHeight;
        int scale = Math.max((int) ((1.0f * realWidth) / ((float) width)), (int) ((1.0f * realHeight) / ((float) height)));
        if (scale <= 0) {
            scale = 1;
        }
        _option.inSampleSize = scale;
        _option.inJustDecodeBounds = false;
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[16384];
        try {
            bitmap = BitmapFactory.decodeFile(actfileName, _option);
        } catch (OutOfMemoryError e) {
            while (bitmap == null) {
                scale++;
                _option.inSampleSize = scale;
                try {
                    bitmap = BitmapFactory.decodeFile(actfileName, _option);
                } catch (OutOfMemoryError e2) {
                }
            }
            Log.e("wdy", "get bitmap error", e);
            Log.e("wdy", "width*height:" + width + "*" + height);
            Log.e("wdy", "realwidth*realheight:" + realWidth + "*" + realHeight);
        }
        int bmpWidth = bitmap.getWidth();
        Log.e("wdy", "bmpWidth*bmpHeight:" + bmpWidth + "*" + bitmap.getHeight());
        return bitmap;
    }

    public static Bitmap getBitMap(String localSourceID, Context context, int width, int height) {
        Bitmap resultBitmap = null;
        try {
            if (HLSetting.IsResourceSD) {
                resultBitmap = getBitmapFromSD(localSourceID, width, height);
            } else {
                resultBitmap = load(FileUtils.getInstance().getFileInputStream(context, localSourceID), width, height);
            }
        } catch (OutOfMemoryError e) {
            Log.e("hl", "get bitmap error", e);
        }
        if (resultBitmap == null) {
            Log.i("hl", "获取图片失败，图片名称是 " + localSourceID);
        }
        return resultBitmap;
    }

    public static Bitmap getBitMap(String localSourceID, Context context, int[] size) {
        Bitmap resultBitmap = null;
        try {
            if (HLSetting.IsResourceSD) {
                resultBitmap = getWdyBitmapFromSD(localSourceID, size[0], size[1]);
            } else {
                resultBitmap = load(FileUtils.getInstance().getFileInputStream(context, localSourceID), size);
            }
        } catch (OutOfMemoryError e) {
            Log.e("hl", "get bitmap error", e);
        }
        if (resultBitmap == null) {
            Log.i("hl", "获取图片失败，图片名称是 " + localSourceID);
        }
        return resultBitmap;
    }

    public static Bitmap getImage(Context context, Bitmap source, int row, int col, int rowTotal, int colTotal, float multiple, boolean isRecycle) {
        Bitmap temp = getBitmap(source, ((col - 1) * source.getWidth()) / colTotal, ((row - 1) * source.getHeight()) / rowTotal, source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (isRecycle) {
            recycleBitmap(source);
        }
        if (((double) multiple) == 1.0d) {
            return temp;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(multiple, multiple);
        return Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
    }

    public static Drawable getDrawableImage(Context context, Bitmap source, int row, int col, int rowTotal, int colTotal, float multiple) {
        Bitmap temp = getBitmap(source, ((col - 1) * source.getWidth()) / colTotal, ((row - 1) * source.getHeight()) / rowTotal, source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (((double) multiple) != 1.0d) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
        }
        return new BitmapDrawable(context.getResources(), temp);
    }

    public static Drawable[] getDrawables(Context context, int resourseId, int row, int col, float multiple) {
        Drawable[] drawables = new Drawable[(row * col)];
        Bitmap source = decodeResource(context, resourseId);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                drawables[temp] = getDrawableImage(context, source, i, j, row, col, multiple);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
        }
        return drawables;
    }

    public static Drawable[] getDrawables(Context context, String resName, int row, int col, float multiple) {
        Drawable[] drawables = new Drawable[(row * col)];
        Bitmap source = decodeBitmapFromAssets(context, resName);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                drawables[temp] = getDrawableImage(context, source, i, j, row, col, multiple);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
        }
        return drawables;
    }

    public static Bitmap[] getBitmaps(Context context, int resourseId, int row, int col, float multiple) {
        Bitmap[] bitmaps = new Bitmap[(row * col)];
        Bitmap source = decodeResource(context, resourseId);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col, multiple, false);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
        }
        return bitmaps;
    }

    public static Bitmap[] getBitmaps(Context context, String resName, int row, int col, float multiple) {
        Bitmap[] bitmaps = new Bitmap[(row * col)];
        Bitmap source = decodeBitmapFromAssets(context, resName);
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col, multiple, false);
                temp++;
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
        }
        return bitmaps;
    }

    public static Bitmap[] getBitmapsByBitmap(Context context, Bitmap source, int row, int col, float multiple) {
        Bitmap[] bitmaps = new Bitmap[(row * col)];
        int temp = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[temp] = getImage(context, source, i, j, row, col, multiple, false);
                temp++;
            }
        }
        return bitmaps;
    }

    public static Bitmap decodeResource(Context context, int resourseId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resourseId);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeBitmapFromAssets(Context context, String resName) {
        Bitmap bitmap = null;
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream in = null;
        try {
            in = context.getAssets().open(resName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
        }
    }

    public static Bitmap getOneFrameImg(Bitmap source, int frameIndex, int totalCount) {
        int singleW = source.getWidth() / totalCount;
        return Bitmap.createBitmap(source, (frameIndex - 1) * singleW, 0, singleW, source.getHeight());
    }

    public static void recycleBitmaps(Bitmap[] bitmaps) {
        if (bitmaps != null) {
            for (Bitmap b : bitmaps) {
                recycleBitmap(b);
            }
        }
    }

    public static void recycleBitmaps(ArrayList<Bitmap> bitmapList) {
        if (bitmapList != null) {
            for (int i = 0; i < bitmapList.size() - 1; i++) {
                recycleBitmap((Bitmap) bitmapList.get(i));
            }
        }
    }

    public static Bitmap getBitmapFromSD(String fileName, int[] size) {
        String actfileName = BookSetting.BOOK_PATH + "/" + fileName;
        Options _option = new Options();
        _option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(actfileName, _option);
        float realWidth = (float) _option.outWidth;
        if (realWidth > ((float) size[0])) {
            _option.inSampleSize = (int) (realWidth / ((float) size[0]));
        }
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inJustDecodeBounds = false;
        _option.inTempStorage = new byte[16384];
        _option.outWidth = size[0];
        _option.outHeight = size[1];
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(actfileName, _option);
            if (bitmap == null) {
                _option.inSampleSize = 10;
                bitmap = BitmapFactory.decodeFile(actfileName, _option);
            }
            size[0] = _option.outWidth;
            size[1] = _option.outHeight;
            return bitmap;
        } catch (Exception e) {
            Log.e("hl", "load bitmap Exception");
            e.printStackTrace();
            if (0 == 0) {
                Log.d("hl", "文件" + fileName + "创建bitmap为空");
            }
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e("hl", "load bitmap OutOfMemoryError");
            e2.printStackTrace();
            _option.inSampleSize = 10;
            return BitmapFactory.decodeFile(actfileName, _option);
        }
    }

    public static Bitmap getBitmapFromSD(String fileName, int width, int height) {
        String actfileName = BookSetting.BOOK_PATH + "/" + fileName;
        Options _option = new Options();
        _option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(actfileName, _option);
        float realWidth = (float) _option.outWidth;
        if (realWidth > ((float) width)) {
            _option.inSampleSize = (int) (realWidth / ((float) width));
        }
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inJustDecodeBounds = false;
        _option.inTempStorage = new byte[16384];
        _option.outHeight = height;
        _option.outWidth = width;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(actfileName, _option);
            if (bitmap == null) {
                _option.inSampleSize = 10;
                bitmap = BitmapFactory.decodeFile(actfileName, _option);
            }
            return bitmap;
        } catch (Exception e) {
            Log.e("hl", "load bitmap Exception");
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e("hl", "load bitmap OutOfMemoryError");
            e2.printStackTrace();
            _option.inSampleSize = 10;
            return BitmapFactory.decodeFile(actfileName, _option);
        }
    }

    private static Bitmap load(InputStream is, int[] size) {
        if (is == null) {
            return null;
        }
        try {
            is.reset();
        } catch (IOException e) {
        }
        Options _option = new Options();
        _option.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, _option);
        int scale = (int) (((float) _option.outWidth) / ((float) size[0]));
        if (scale <= 0) {
            scale = 1;
        }
        _option.inSampleSize = scale;
        _option.inJustDecodeBounds = false;
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[16384];
        _option.outWidth = size[0];
        _option.outHeight = size[1];
        BufferedInputStream buf = new BufferedInputStream(is, 8192);
        try {
            is.reset();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(buf, null, _option);
            size[0] = _option.outWidth;
            size[1] = _option.outHeight;
            try {
                return bitmap;
            } catch (Exception e2) {
                e2.printStackTrace();
                return bitmap;
            }
        } catch (Exception e3) {
            Log.e("hl", "load bitmap Exception");
            e3.printStackTrace();
            try {
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } catch (OutOfMemoryError e5) {
            Log.e("hl", "load bitmap OutOfMemoryError");
            e5.printStackTrace();
            _option.inSampleSize = 50;
            Bitmap bitmap2 = BitmapFactory.decodeStream(is, null, _option);
            try {
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap load(InputStream is, int width, int height) {
        if (is == null) {
            return null;
        }
        try {
            is.reset();
        } catch (IOException e) {
        }
        Options _option = new Options();
        _option.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, _option);
        if (0 == 0) {
            System.out.println("bitmap为空");
        }
        int scale = (int) (((float) _option.outWidth) / ((float) width));
        if (scale <= 0) {
            scale = 1;
        }
        _option.inSampleSize = scale;
        _option.inJustDecodeBounds = false;
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[16384];
        _option.outHeight = height;
        _option.outWidth = width;
        BufferedInputStream buf = new BufferedInputStream(is, 8192);
        try {
            is.reset();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(buf, null, _option);
            try {
                return bitmap;
            } catch (Exception e2) {
                e2.printStackTrace();
                return bitmap;
            }
        } catch (Exception e3) {
            Log.e("hl", "load bitmap Exception");
            e3.printStackTrace();
            try {
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } catch (OutOfMemoryError e5) {
            Log.e("hl", "load bitmap OutOfMemoryError");
            e5.printStackTrace();
            _option.inSampleSize = 50;
            Bitmap bitmap2 = BitmapFactory.decodeStream(is, null, _option);
            try {
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getBitmapFromResource(Context context, int picId) {
        return BitmapFactory.decodeResource(context.getResources(), picId);
    }

    public static Bitmap getBitmapFromFile(Context context, String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static int[] getBitmapSizeBySourceID(Context context, String sourceID) {
        if (HLSetting.IsResourceSD) {
            return loadSize(FileUtils.getInstance().getFileInputStream(sourceID));
        }
        return loadSize(FileUtils.getInstance().getFileInputStream(context, sourceID));
    }

    public static Bitmap getBitmapBySourceID(Context context, String sourceID) {
        if (HLSetting.IsResourceSD) {
            return load(FileUtils.getInstance().getFileInputStream(sourceID));
        }
        return load(FileUtils.getInstance().getFileInputStream(context, sourceID));
    }

    public static Bitmap getWrapBitmapBySourceID(Context context, String sourceID, int width, int height) {
        int[] size = getBitmapSizeBySourceID(context, sourceID);
        int bitHeight = size[1];
        int bitWidth = size[0];
        int resultWidth = width;
        int resultHeight = (int) ((((float) resultWidth) * ((float) bitHeight)) / ((float) bitWidth));
        if (resultHeight > height) {
            resultHeight = height;
            resultWidth = (int) ((((float) resultHeight) * ((float) bitWidth)) / ((float) bitHeight));
        }
        return getBitMap(sourceID, context, resultWidth, resultHeight);
    }

    public static BitmapDrawable getBitmapDrawable(Context context, String sourceID) {
        if (StringUtils.isEmpty(sourceID)) {
            return null;
        }
        return new BitmapDrawable(getBitmapBySourceID(context, sourceID));
    }

    private static int[] loadSize(InputStream is) {
        int[] size = new int[2];
        Options _option = new Options();
        _option.inDither = false;
        _option.inPurgeable = true;
        _option.inInputShareable = true;
        _option.inTempStorage = new byte[16384];
        _option.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(is, null, _option);
            size[0] = _option.outWidth;
            size[1] = _option.outHeight;
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            Log.e("hl", "load bitmap Exception");
            e2.printStackTrace();
            try {
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        return size;
    }

    private static Bitmap load(InputStream is) {
        Bitmap bitmap = null;
        try {
            return BitmapFactory.decodeStream(is, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e2) {
            return BitmapFactory.decodeStream(is, null, null);
        }
    }

    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0.0f, 0.0f, null);
        canvas.drawBitmap(foreground, (float) ((bgWidth - fgWidth) / 2), (float) ((bgHeight - fgHeight) / 2), null);
        canvas.save(31);
        canvas.restore();
        return newmap;
    }
}
