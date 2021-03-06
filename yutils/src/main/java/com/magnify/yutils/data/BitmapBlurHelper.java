package com.magnify.yutils.data;

import android.graphics.Bitmap;


/**
 * Created by Daemon on 2015/12/21.
 */
public class BitmapBlurHelper {

    public static Bitmap doBlurJniArray(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        if (sentBitmap == null) return null;
        long startTime = System.currentTimeMillis();

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        // Jni array calculate
        ImageBlur.blurIntArray(pix, w, h, radius);

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static Bitmap doBlurJniBitMap(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (sentBitmap == null) return null;
        try {
            long startTime = System.currentTimeMillis();

            if (canReuseInBitmap) {
                bitmap = sentBitmap;
            } else {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            }

            if (radius < 1) {
                return (null);
            }
            // Jni bitmap calculate
            ImageBlur.blurBitMap(bitmap, radius);

            return (bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
