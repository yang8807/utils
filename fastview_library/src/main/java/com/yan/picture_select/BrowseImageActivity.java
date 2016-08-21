package com.yan.picture_select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonShowChildViewPagerAdapter;
import com.magnify.yutils.app.BaseActivity;
import com.yan.bean.ImageFloder;
import com.yan.fastview_library.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public class BrowseImageActivity extends BaseActivity {
    private ViewPager viewPager;
    public static String IMAGE_FOLDERS = "image_foldes";
    public static String IMAGE_POSITION = "image_position";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_view_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpagers);
        List<ImageFloder> imageFloders = (List<ImageFloder>) getIntent().getSerializableExtra(IMAGE_FOLDERS);

        viewPager.setAdapter(new CommonShowChildViewPagerAdapter<ImageFloder, String>(imageFloders, self, R.layout.item_view_pager_image) {
            @Override
            protected List<String> getChild(ImageFloder imageFloder) {
                return imageFloder.getAllImages();
            }

            @Override
            protected void convert(ViewHolder viewHolder, int position, ImageFloder parent, String child) {
                viewHolder.displayImage("file://" + parent.getDir() + "/" + child, R.id.photoView);
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
