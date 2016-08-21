package com.magnify.utils.ui.component;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.LogUtil;
import com.yan.bean.ImageFloder;
import com.magnify.basea_dapter_library.abslistview.BaseShowChildAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by heinigger on 16/8/20.
 */
public class ActivityPicture2Activity extends CurrentBaseActivity {

    private GridView gridView;
    //存放图片文件夹
    private HashSet<String> mDirPaths = new HashSet<>();
    //所有有图片的文件夹
    protected List<ImageFloder> mImageFloders = new ArrayList<>();
    private String[] defaultFilters = new String[]{"jpg", "png", "jpeg"};

    //所有图片的数量
    private int totalCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gridView.setAdapter(new BaseShowChildAdapter<ImageFloder, String>(mImageFloders, self, R.layout.item_images_view) {
                @Override
                protected List<String> getChild(ImageFloder imageFloder) {
                    return imageFloder.getAllImages();
                }

                @Override
                protected void convert(ViewHolder viewHolder, View convertView, int position, ImageFloder parent, String child) {
                    String url = "file://" + parent.getDir() + "/" + child;
                    LogUtil.v("mine", url);
                    viewHolder.displayImage(url, R.id.image, R.drawable.ic_android_red_400_36dp);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scansImages();
        gridView = new GridView(self);
        setContentView(gridView);

    }

    /**
     * 遍历获取本地的数据
     */
    public void scansImages() {
        LogUtil.v("mine", "停止在这里");
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.v("mine", "start_scans");
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = self.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?", getFilterImageTypes()/*new String[]{"image/jpeg", "image/png"}*/,//这里表示要查询的类型,需要查什么,就传入什么进来
                        MediaStore.Images.Media.DATE_MODIFIED);//按什么时候来进行排序,这里是按照修改的是假来进行排序
                LogUtil.v("mine", "at_thread");
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    LogUtil.v("mine", path);
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
        }).start();
    }

    /**
     * 得到每个文件夹下的图片
     */
    private void caculatePictureCount(ImageFloder imageFloder) {
        LogUtil.v("mine", "caculatePictureCount");
        List<String> mimages = scanFiles(imageFloder);
        int count = mimages == null ? 0 : mimages.size();
        imageFloder.setCount(count);
        totalCount += count;
        imageFloder.setAllImages(mimages);
        if (count > 0) mImageFloders.add(imageFloder);
        LogUtil.v(mImageFloders, "mine");
    }

    /**
     * 遍历得到所有的图片文件
     */
    public List<String> scanFiles(ImageFloder floder) {
        File mImgDir = new File(floder.getDir());
        List<String> mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return isImage(filename);
            }
        }));
        return mImgs;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 拼接成去ContentResolver检索的条件
     */
    private String[] getFilterImageTypes() {
        String[] imagesTypes = getImagesTypes();

        if (imagesTypes == null || imagesTypes.length == 0) {
            imagesTypes = defaultFilters;
        }
        String[] types = new String[2];//只能有两个参数,进行查询的时候
        for (int i = 0; i < imagesTypes.length; i++) {
            if (i >= 2) break;
            types[i] = "image/" + imagesTypes[i];
        }

        return types;
    }

    /**
     * 简单判断是不是图片
     */
    private boolean isImage(String fileName) {
        String[] imageTypes = getImagesTypes();
        for (int i = 0; i < imageTypes.length; i++) {
            if (fileName.endsWith("." + imageTypes[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 可以重新定义过滤图片的类型
     */
    protected String[] getImagesTypes() {
        return defaultFilters;
    }
}
