package com.p000hl.android.view.gallary.base;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import com.p000hl.android.core.utils.BitmapUtils;
import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: com.hl.android.view.gallary.base.ImageManager */
public class ImageManager {
    public static String ISFALSE = "ISFALSE";
    public static String ISTRUE = "ISTRUE";
    public static ConcurrentHashMap<String, Bitmap> imageMap = new ConcurrentHashMap<>();
    static int index = 0;

    public static boolean getImageFile(String fName) {
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return true;
        }
        return false;
    }

    public static Bitmap getImage(String fileName) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(fileName).getAbsolutePath());
            addImage(fileName, bitmap);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static void addImage(String uri, Bitmap bitmap) {
        imageMap.put(uri.toString(), bitmap);
    }

    private static void clearBitmap(String uri) {
        if (imageMap.get(uri) != null) {
            ((Bitmap) imageMap.get(uri)).recycle();
            imageMap.remove(uri);
        }
    }

    public static List<ImageMessage> clearImage(List<ImageMessage> imageList, int start, int end) {
        if (start >= 0 && end < imageList.size()) {
            for (int i = 0; i < start; i++) {
                if (((ImageMessage) imageList.get(i)).getIsNull().equals(ISFALSE)) {
                    ((ImageMessage) imageList.get(i)).setIsNull(ISTRUE);
                    clearBitmap(((ImageMessage) imageList.get(i)).getPath().toString());
                    clearBitmap(((ImageMessage) imageList.get(i)).getPath().toString() + "1");
                    clearBitmap(((ImageMessage) imageList.get(i)).getPath().toString() + "2");
                    ((ImageMessage) imageList.get(i)).setImage(null);
                }
            }
            for (int i2 = end + 1; i2 < imageList.size(); i2++) {
                if (((ImageMessage) imageList.get(i2)).getIsNull().equals(ISFALSE)) {
                    ((ImageMessage) imageList.get(i2)).setIsNull(ISTRUE);
                    clearBitmap(((ImageMessage) imageList.get(i2)).getPath().toString());
                    clearBitmap(((ImageMessage) imageList.get(i2)).getPath().toString() + "1");
                    clearBitmap(((ImageMessage) imageList.get(i2)).getPath().toString() + "2");
                    ((ImageMessage) imageList.get(i2)).setImage(null);
                }
            }
        }
        return imageList;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height / 2) + height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 0), new Paint());
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (height + 0), null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) bitmap.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 0), 1895825407, 16777215, TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (bitmapWithReflection.getHeight() + 0), paint);
        return bitmapWithReflection;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap, float percentHeight) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, (int) (((float) height) * percentHeight), width, (int) (((float) height) * percentHeight), matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, ((int) (((float) height) * percentHeight)) + height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 0), new Paint());
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (height + 0), null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) bitmap.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 0), 1895825407, 16777215, TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (bitmapWithReflection.getHeight() + 0), paint);
        BitmapUtils.recycleBitmap(bitmap);
        BitmapUtils.recycleBitmap(reflectionImage);
        return bitmapWithReflection;
    }
}
