package com.magnify.yutils.data;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.magnify.yutils.IOUtil;
import com.magnify.yutils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 * @author 王晓明
 */
public class ImageUtils {

    /***
     * 根据原始图片信息和目标图片尺寸计算压缩率
     *
     * @param minSideLength  最小边长度
     * @param maxNumOfPixels 最大面积
     * @return 压缩率(对应BitmapFactory.Options.inSampleSize)
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        // 初始压缩率
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 根据原始图片信息和目标图片尺寸计算初始压缩率
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 根据给定的高宽获取图片缩放比例
     *
     * @param reqWidth  目标宽度(-1表示根据高度自动计算)
     * @param reqHeight 目标高度(-1标示根据宽度自动计算)
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int sampleSize = 1;
        if ((reqHeight > 0 && height > reqHeight) || (reqWidth > 0 && width > reqWidth)) {
            if (width > height && reqHeight > 0) {
                sampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                sampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return sampleSize;
    }

    /**
     * 创建缩略图
     *
     * @param largeImagePath 原始图片路径
     * @param thumbfilePath  缩略图保存路径
     * @param reqWidth       缩略图片最大宽度
     * @param quality        缩略图清晰度(0-100)
     * @return 是否成功
     */
    public static boolean createImageThumbnail(Context context, String largeImagePath, String thumbfilePath, int reqWidth, int quality) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        // 只获取图片长宽数据,不读入内存
        bitmapOptions.inJustDecodeBounds = true;
        // 获取原始图片高宽
        BitmapFactory.decodeFile(largeImagePath, bitmapOptions);
        // 计算原始图片缩放后的宽高
        // int[] new_img_size = scaleImageSize(new int[] { bitmapOptions.outWidth, bitmapOptions.outHeight }, square_size);
        // 计算缩图比例
        int thumbSimpleSize = calculateInSampleSize(bitmapOptions, reqWidth, -1);
        LogUtil.d("thumb", "w:" + bitmapOptions.outWidth + "h:" + bitmapOptions.outHeight + " w2:" + reqWidth + "thumbSimpleSize:" + thumbSimpleSize);
        // 设置缩图参数
        bitmapOptions.inSampleSize = thumbSimpleSize;
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inInputShareable = true;
        // 获得缩小后的Bitmap
        Bitmap thumbBitmap = BitmapFactory.decodeFile(largeImagePath, bitmapOptions);

        // 保存缩小后的图片
        try {
            saveImageToSD(null, thumbfilePath, thumbBitmap, quality);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 按照指定宽高对图片进行压缩
     *
     * @param filename  图片路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大高度
     */
    public static Bitmap scalePicture(String filename, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        FileInputStream fis = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filename, opts);
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int desWidth = 0;
            int desHeight = 0;
            // 缩放比例
            double ratio = 0.0;
            if (srcWidth > srcHeight) {
                ratio = srcWidth / maxWidth;
                desWidth = maxWidth;
                desHeight = (int) (srcHeight / ratio);
            } else {
                ratio = srcHeight / maxHeight;
                desHeight = maxHeight;
                desWidth = (int) (srcWidth / ratio);
            }

            File file = new File(filename);
            fis = new FileInputStream(file);
            // 设置输出宽度、高度
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = (int) (ratio) + 1;
            newOpts.inJustDecodeBounds = false;
            newOpts.outWidth = desWidth;
            newOpts.outHeight = desHeight;
            newOpts.inPreferredConfig = Config.RGB_565;
            newOpts.inPurgeable = true;// 允许可清除
            newOpts.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
            // bitmap = BitmapFactory.decodeFile(filename, newOpts);
            bitmap = BitmapFactory.decodeStream(fis, null, newOpts);
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e) {
            Log.d("scalePicture的异常", e.toString());
            // e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 放大缩小图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 计算缩放图片的宽高
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        double ratio = square_size / (double) img_size[0];
        return new int[]{(int) (img_size[0] * ratio), (int) (img_size[1] * ratio)};
    }

    /**
     * 写图片文件到SD卡
     */
    public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(filePath));
                bitmap.compress(CompressFormat.JPEG, quality, bos);
                bos.flush();
            } finally {
                IOUtil.closeQuietly(bos);
            }
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    public static void scanPhoto(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 将drawable 转换成Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            Rect rect = drawable.getBounds();
            width = rect.width();
            height = rect.height();
        }
        if (width <= 0 || height <= 0) {
            return null;
        }
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将bitmap转换为drawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        // 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        return bd;
    }

    /**
     * 从相册选取图片获得的uri，得到图片路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getAlbumPhotoPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if (type.equalsIgnoreCase("primary")) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        } else if (uri.getScheme().equalsIgnoreCase("content")) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if (uri.getScheme().equalsIgnoreCase("file")) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 是否扩展内存的文件
     */
    private static boolean isExternalStorageDocument(Uri uri) {

        return uri.getAuthority().equals("com.android.externalstorage.documents");

    }

    /**
     * 是否下载的文件
     */
    private static boolean isDownloadDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.downloads.documents");
    }

    /**
     * 是否媒体文件
     */
    private static boolean isMediaDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.media.documents");
    }

    /**
     * 是否谷歌相册里的文件
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return uri.getAuthority().equals("com.google.android.apps.photos.content");
    }

    /**
     * 4.3版本下的默认从uri获取路径方法
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, proj, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(proj[0]);
                return cursor.getString(index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 根据URI获取图片物理路径
     */
    public static String getAbsoluteImagePath(Uri uri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        String url = "";
        try {
            cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            url = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return url;
    }

    /**
     * 根据本地图片的Exif信息，返回正确方向的图片
     *
     * @param input 输入bitmap
     * @param path  本地路径
     * @return 旋转过的图片(如无必要旋转则是返回input)
     */
    public static Bitmap rotateExifPicture(Bitmap input, String path) {
        int rotation = getExifRotation(path);
        if (rotation != 0) {
            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                return Bitmap.createBitmap(input, 0, 0, input.getWidth(), input.getHeight(), matrix, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return input;
    }

    /**
     * 返回本地Exif图片的旋转角度
     *
     * @param path 本地路径
     * @return 旋转角度
     */
    public static int getExifRotation(String path) {
        if (path.startsWith("file://"))
            path = path.substring(8);
        int rotation = 0;
        try {
            ExifInterface face = new ExifInterface(path);
            int orientation = face.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotation;
    }

    /**
     * 图片置灰
     */
    public static final Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

    public static final Bitmap createRGBImage(Drawable bitmap, int color) {
        return ImageUtils.createRGBImage(ImageUtils.drawable2Bitmap(bitmap), color);
    }

    public static final Bitmap createRGBImage(Bitmap mBitmap, int mColor) {
        Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mAlphaBitmap);
        Paint mPaint = new Paint();

        mPaint.setColor(mColor);
        //从原位图中提取只包含alpha的位图
        Bitmap alphaBitmap = mBitmap.extractAlpha();
        //在画布上（mAlphaBitmap）绘制alpha位图
        mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

        return mAlphaBitmap;
    }


}
