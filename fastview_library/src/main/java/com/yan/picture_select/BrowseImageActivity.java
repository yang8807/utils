package com.yan.picture_select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonShowChildViewPagerAdapter;
import com.magnify.yutils.bean.ImageFloder;
import com.magnify.yutils.data.PreferencesUtil;
import com.yan.constants.Constants;
import com.yan.fastview_library.R;
import com.yan.fastview_library.base.BaseActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 * 用于图片浏览的页面
 */
public class BrowseImageActivity extends BaseActivity {
    private ViewPager viewPager;
    public static String IMAGE_FOLDERS = "image_foldes";
    public static String IMAGE_POSITION = "image_position";
    public long time;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_view_pager);
        String localPaths = PreferencesUtil.getString(self, Constants.BROWSE_ACTIVITY_BACKGROUND);

        if (!TextUtils.isEmpty(localPaths)) {
            imageView = (ImageView) findViewById(R.id.image_blur);
            ViewHolder.getImageLoaderInterface().displayImage(localPaths, imageView);
//            Bitmap mBitmap = BitmapBlurHelper.doBlurJniArray(BitmapFactory.decodeFile(localPaths), 50, false);
//            if (mBitmap != null) imageView.setImageBitmap(mBitmap);
        }


        viewPager = (ViewPager) findViewById(R.id.viewpagers);
        List<ImageFloder> imageFloders = (List<ImageFloder>) getIntent().getSerializableExtra(IMAGE_FOLDERS);

        viewPager.setAdapter(new CommonShowChildViewPagerAdapter<ImageFloder, String>(imageFloders, self, R.layout.item_view_pager_image) {
            @Override
            protected List<String> getChild(ImageFloder imageFloder) {
                return imageFloder.getAllImages();
            }

            @Override
            protected void convert(ViewHolder viewHolder, int position, final ImageFloder parent, final String child) {
                viewHolder.displayImage("file://" + parent.getDir() + "/" + child, R.id.photoView)
                        .setTag(R.id.image_save, "file://" + parent.getDir() + "/" + child).setOnClickListener(R.id.image_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String localPath = (String) view.getTag();
                        PreferencesUtil.save(self, Constants.BROWSE_ACTIVITY_BACKGROUND, localPath);
                        ViewHolder.getImageLoaderInterface().displayImage(localPath, imageView);
                    }
                });
            }
        });

        viewPager.setCurrentItem(getIntent().getIntExtra(IMAGE_POSITION, 0));
    }


    public static Intent getIntent(Context mContext, int position, List<ImageFloder> imageFloders) {
        Intent intent = new Intent(mContext, BrowseImageActivity.class);
        intent.putExtra(IMAGE_FOLDERS, (Serializable) imageFloders);
        intent.putExtra(IMAGE_POSITION, position);
        return intent;
    }

}
