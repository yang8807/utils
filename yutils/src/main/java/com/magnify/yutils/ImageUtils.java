package com.magnify.yutils;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 * @author ������
 */
public class ImageUtils {

    /***
     * ����ԭʼͼƬ��Ϣ��Ŀ��ͼƬ�ߴ����ѹ����
     *
     * @param minSideLength  ��С�߳���
     * @param maxNumOfPixels ������
     * @return ѹ����(��ӦBitmapFactory.Options.inSampleSize)
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        // ��ʼѹ����
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
     * ����ԭʼͼƬ��Ϣ��Ŀ��ͼƬ�ߴ�����ʼѹ����
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
     * ���ݸ����ĸ߿��ȡͼƬ���ű���
     *
     * @param reqWidth  Ŀ����(-1��ʾ���ݸ߶��Զ�����)
     * @param reqHeight Ŀ��߶�(-1��ʾ���ݿ���Զ�����)
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
     * ��������ͼ
     *
     * @param largeImagePath ԭʼͼƬ·��
     * @param thumbfilePath  ����ͼ����·��
     * @param reqWidth       ����ͼƬ�����
     * @param quality        ����ͼ������(0-100)
     * @return �Ƿ�ɹ�
     */
    public static boolean createImageThumbnail(Context context, String largeImagePath, String thumbfilePath, int reqWidth, int quality) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        // ֻ��ȡͼƬ��������,�������ڴ�
        bitmapOptions.inJustDecodeBounds = true;
        // ��ȡԭʼͼƬ�߿�
        BitmapFactory.decodeFile(largeImagePath, bitmapOptions);
        // ����ԭʼͼƬ���ź�Ŀ��
        // int[] new_img_size = scaleImageSize(new int[] { bitmapOptions.outWidth, bitmapOptions.outHeight }, square_size);
        // ������ͼ����
        int thumbSimpleSize = calculateInSampleSize(bitmapOptions, reqWidth, -1);
        LogUtil.d("thumb", "w:" + bitmapOptions.outWidth + "h:" + bitmapOptions.outHeight + " w2:" + reqWidth + "thumbSimpleSize:" + thumbSimpleSize);
        // ������ͼ����
        bitmapOptions.inSampleSize = thumbSimpleSize;
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inInputShareable = true;
        // �����С���Bitmap
        Bitmap thumbBitmap = BitmapFactory.decodeFile(largeImagePath, bitmapOptions);

        // ������С���ͼƬ
        try {
            saveImageToSD(null, thumbfilePath, thumbBitmap, quality);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * ����ָ����߶�ͼƬ����ѹ��
     *
     * @param filename  ͼƬ·��
     * @param maxWidth  ͼƬ�����
     * @param maxHeight ͼƬ���߶�
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
            // ���ű���
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
            // ���������ȡ��߶�
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = (int) (ratio) + 1;
            newOpts.inJustDecodeBounds = false;
            newOpts.outWidth = desWidth;
            newOpts.outHeight = desHeight;
            newOpts.inPreferredConfig = Config.RGB_565;
            newOpts.inPurgeable = true;// ��������
            newOpts.inInputShareable = true;// ����options���������Ա�������ʹ�òŻ���Ч��
            // bitmap = BitmapFactory.decodeFile(filename, newOpts);
            bitmap = BitmapFactory.decodeStream(fis, null, newOpts);
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e) {
            Log.d("scalePicture���쳣", e.toString());
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
     * �Ŵ���СͼƬ
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
     * ��������ͼƬ�Ŀ��
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        double ratio = square_size / (double) img_size[0];
        return new int[]{(int) (img_size[0] * ratio), (int) (img_size[1] * ratio)};
    }

    /**
     * дͼƬ�ļ���SD��
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
     * ��Gallery�������Ͽ�����ͼƬ
     */
    public static void scanPhoto(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * ��drawable ת����Bitmap
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
     * ��bitmapת��Ϊdrawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        // ��ΪBtimapDrawable��Drawable�����࣬����ֱ��ʹ��bd���󼴿ɡ�
        return bd;
    }

    /**
     * �����ѡȡͼƬ��õ�uri���õ�ͼƬ·��
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
     * �Ƿ���չ�ڴ���ļ�
     */
    private static boolean isExternalStorageDocument(Uri uri) {

        return uri.getAuthority().equals("com.android.externalstorage.documents");

    }

    /**
     * �Ƿ����ص��ļ�
     */
    private static boolean isDownloadDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.downloads.documents");
    }

    /**
     * �Ƿ�ý���ļ�
     */
    private static boolean isMediaDocument(Uri uri) {
        return uri.getAuthority().equals("com.android.providers.media.documents");
    }

    /**
     * �Ƿ�ȸ��������ļ�
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return uri.getAuthority().equals("com.google.android.apps.photos.content");
    }

    /**
     * 4.3�汾�µ�Ĭ�ϴ�uri��ȡ·������
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
     * ����URI��ȡͼƬ����·��
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
     * ���ݱ���ͼƬ��Exif��Ϣ��������ȷ�����ͼƬ
     *
     * @param input ����bitmap
     * @param path  ����·��
     * @return ��ת����ͼƬ(���ޱ�Ҫ��ת���Ƿ���input)
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
     * ���ر���ExifͼƬ����ת�Ƕ�
     *
     * @param path ����·��
     * @return ��ת�Ƕ�
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

}
