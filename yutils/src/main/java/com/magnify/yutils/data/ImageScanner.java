package com.magnify.yutils.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.magnify.yutils.app.ThreadManager;
import com.magnify.yutils.bean.ImageFloder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by heinigger on 16/8/26.
 * <p/>
 * 搜索本地的工具
 */
public class ImageScanner {
    //存放图片文件夹
    private HashSet<String> mDirPaths = new HashSet<>();
    //所有有图片的文件夹
    protected List<ImageFloder> mImageFloders = new ArrayList<>();
    private Context mContext;
    private OnScanImageListener onScanImageListener;

    //所有图片的数量
    private int totalCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (onScanImageListener != null)
                onScanImageListener.onScanFinish(mImageFloders, totalCount);
        }
    };

    public ImageScanner(Context mContext, @NonNull OnScanImageListener onScanImageListener) {
        this.mContext = mContext;
        this.onScanImageListener = onScanImageListener;
        scansImages();
    }

    /**
     * 遍历获取本地的数据
     */
    public void scansImages() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mContext.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null/* MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?", getFilterImageTypes()*//*new String[]{"image/jpeg", "image/png"}*//*,//这里表示要查询的类型,需要查什么,就传入什么进来
                        MediaStore.Images.Media.DATE_MODIFIED*/);//按什么时候来进行排序,这里是按照修改的是假来进行排序
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) continue;
                    String dirPath = parentFile.getAbsolutePath();
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        caculatePictureCount(new ImageFloder(dirPath));
                    }

                }
                mCursor.close();
                mDirPaths = null;
                mHandler.sendEmptyMessage(0x110);
            }
        });
    }

    /**
     * 得到每个文件夹下的图片
     */
    private void caculatePictureCount(ImageFloder imageFloder) {
        List<String> mimages = scanFiles(imageFloder);
        int count = mimages == null ? 0 : mimages.size();
        imageFloder.setCount(count);
        totalCount += count;
        imageFloder.setAllImages(new ArrayList<String>(mimages));
        if (count > 0) mImageFloders.add(imageFloder);
    }

    /**
     * 遍历得到所有的图片文件
     */
    public List<String> scanFiles(ImageFloder floder) {
        File mImgDir = new File(floder.getDir());
        List<String> mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith("jpeg") || filename.endsWith(".gif");
            }
        }));
        return mImgs;
    }

    public static interface OnScanImageListener {
        public void onScanFinish(List<ImageFloder> mImageFloder, int totalCount);
    }
}
