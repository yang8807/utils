package com.yan.picture_select;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.magnify.yutils.data.BitmapBlurHelper;
import com.magnify.yutils.data.ImageUtils;
import com.magnify.yutils.data.SPUtil;
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
public class NormalBrowseImageActivity extends BaseActivity {
    private ViewPager viewPager;
    public static String IMAGE_FOLDERS = "image_foldes";
    public static String IMAGE_POSITION = "image_position";
    public long time;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_view_pager);
        String localPaths = SPUtil.getString(self, Constants.BROWSE_ACTIVITY_BACKGROUND);

        if (!TextUtils.isEmpty(localPaths)) {
            imageView = (ImageView) findViewById(R.id.image_blur);
            Bitmap mBitmap = BitmapBlurHelper.doBlurJniArray(ImageUtils.scalePicture(localPaths, 200, 200), 3, false);
            if (mBitmap != null) {
                imageView.setImageBitmap(mBitmap);
            } else {
                SingleInstanceManager.getImageLoader().displayImage("file://" + localPaths, imageView);
            }
        }


        viewPager = (ViewPager) findViewById(R.id.viewpagers);
        List<String> imageFloders = (List<String>) getIntent().getSerializableExtra(IMAGE_FOLDERS);

        viewPager.setAdapter(new CommonViewPagerAdapter<String>(imageFloders, self, R.layout.item_view_pager_image) {

            @Override
            protected void convert(ViewHolder viewHolder, int position, String child) {
                viewHolder.displayImage("file://" + child, R.id.photoView)
                        .setTag(R.id.image_save, child).setOnClickListener(R.id.image_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String localPath = (String) view.getTag();
                        SPUtil.save(self, Constants.BROWSE_ACTIVITY_BACKGROUND, localPath);
                        ViewHolder.getImageLoaderInterface().displayImage(localPath, imageView);
                    }
                });
            }
        });
        viewPager.setCurrentItem(getIntent().getIntExtra(IMAGE_POSITION, 0));
    }


    public static Intent getIntent(Context mContext, int position, List<String> imageFloders) {
        Intent intent = new Intent(mContext, NormalBrowseImageActivity.class);
        intent.putExtra(IMAGE_FOLDERS, (Serializable) imageFloders);
        intent.putExtra(IMAGE_POSITION, position);
        return intent;
    }

    public static Intent getIntent(Context mContext, String imageUrl) {
        ArrayList<String> imageFloders = new ArrayList<>();
        imageFloders.add(imageUrl);
        Intent intent = new Intent(mContext, NormalBrowseImageActivity.class);
        intent.putExtra(IMAGE_FOLDERS, (Serializable) imageFloders);
        return intent;
    }

}
