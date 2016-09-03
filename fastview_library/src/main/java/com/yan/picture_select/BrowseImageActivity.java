package com.yan.picture_select;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonShowChildViewPagerAdapter;
import com.magnify.yutils.app.ThreadManager;
import com.magnify.yutils.bean.ImageFloder;
import com.magnify.yutils.data.BitmapBlurHelper;
import com.magnify.yutils.data.FileUtils;
import com.magnify.yutils.data.ImageUtils;
import com.magnify.yutils.data.PreferencesUtil;
import com.yan.constants.Constants;
import com.yan.fastview_library.R;
import com.yan.fastview_library.base.BaseActivity;
import com.yan.fastview_library.base.SingleInstanceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 * 用于图片浏览的页面
 */
public class BrowseImageActivity extends BaseActivity {
    private ViewPager viewPager;
    public static String IMAGE_FOLDERS = "image_foldes";
    public static String IMAGE_POSITION = "image_position";
    public static String IMAGEFOLDER_PATH = "image_folder_path";
    public static String IMAGE_CURRENT_IMAGE_PATH = "image_current_image_path";
    public long time;
    private ImageView imageView;
    private List<ImageFloder> imageFloders;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_view_pager);

        viewPager = (ViewPager) findViewById(R.id.viewpagers);

        showBackground();
        setImageDatas();
    }

    /**
     * 扫描获取背景图片
     */
    private void showBackground() {
        String localPaths = PreferencesUtil.getString(self, Constants.BROWSE_ACTIVITY_BACKGROUND);

        if (!TextUtils.isEmpty(localPaths)) {
            imageView = (ImageView) findViewById(R.id.image_blur);
            Bitmap mBitmap = BitmapBlurHelper.doBlurJniArray(ImageUtils.scalePicture(localPaths, 200, 200), 3, false);
            if (mBitmap != null) {
                imageView.setImageBitmap(mBitmap);
            } else {
                SingleInstanceManager.getImageLoader().displayImage("file://" + localPaths, imageView);
            }
        }
    }

    /***
     * 通过路径,遍历得到图片集合
     */
    private void setImageDatas() {
        final String path = getIntent().getStringExtra(IMAGEFOLDER_PATH);
        if (!TextUtils.isEmpty(path)) {//通过路径进行扫描,获取图片列表
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    setImageFolderAdapter();
                }
            };
            if (imageFloders == null) imageFloders = new ArrayList<>();
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    ImageFloder mImageFolder = new ImageFloder(path);
                    mImageFolder.setAllImages(new ArrayList<String>(FileUtils.scanImages(path)));
                    imageFloders.add(mImageFolder);
                    mHandler.sendEmptyMessage(0);
                }
            });

        } else {//通过传递过来,获取数据
            imageFloders = (List<ImageFloder>) getIntent().getSerializableExtra(IMAGE_FOLDERS);
            if (imageFloders != null)
                setImageFolderAdapter();
        }
    }

    /**
     * 设置适配器
     */
    private void setImageFolderAdapter() {
        String currentPath = getIntent().getStringExtra(IMAGE_CURRENT_IMAGE_PATH);

        viewPager.setAdapter(new CommonShowChildViewPagerAdapter<ImageFloder, String>(imageFloders, self, R.layout.item_view_pager_image) {
            @Override
            protected List<String> getChild(ImageFloder imageFloder) {
                return imageFloder.getAllImages();
            }

            @Override
            protected void convert(ViewHolder viewHolder, int position, final ImageFloder parent, final String child) {
                viewHolder.displayImage("file://" + parent.getDir() + "/" + child, R.id.photoView)
                        .setTag(R.id.image_save, parent.getDir() + "/" + child).setOnClickListener(R.id.image_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String localPath = (String) view.getTag();
                        PreferencesUtil.save(self, Constants.BROWSE_ACTIVITY_BACKGROUND, localPath);
                        ViewHolder.getImageLoaderInterface().displayImage(localPath, imageView);
                    }
                });
            }
        });
        //设置默认选中的位置
        if (!TextUtils.isEmpty(currentPath)) {
            int count = -1;
            for (int i = 0; i < imageFloders.size(); i++) {
                ImageFloder mImageFolder = imageFloders.get(i);
                ArrayList<String> mAllImages = mImageFolder.getAllImages();
                for (int i1 = 0; i1 < mAllImages.size(); i1++) {
                    count++;
                    if (currentPath.endsWith(mAllImages.get(i1))) {
                        viewPager.setCurrentItem(count);
                        break;
                    }
                }
            }
        } else {
            viewPager.setCurrentItem(getIntent().getIntExtra(IMAGE_POSITION, 0));
        }
    }


    public static Intent getIntent(Context mContext, int position, List<ImageFloder> imageFloders) {
        Intent intent = new Intent(mContext, BrowseImageActivity.class);
        intent.putExtra(IMAGE_FOLDERS, (Serializable) imageFloders);
        intent.putExtra(IMAGE_POSITION, position);
        return intent;
    }

    public static Intent getIntent(Context mContext, String FolderPath) {
        Intent intent = new Intent(mContext, BrowseImageActivity.class);
        intent.putExtra(IMAGEFOLDER_PATH, FolderPath);
        return intent;
    }

    public static Intent getIntent(Context mContext, String FolderPath, String picPath) {
        Intent intent = new Intent(mContext, BrowseImageActivity.class);
        intent.putExtra(IMAGEFOLDER_PATH, FolderPath);
        intent.putExtra(IMAGE_CURRENT_IMAGE_PATH, picPath);
        return intent;
    }

}
