package com.yan.fastview_library.viewgroup;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.yan.fastview_library.R;

import java.util.List;

/**
 * Created by heinigger on 16/8/15.
 * 广告栏轮播效果
 */
public class BannerLoopView extends ViewPager {

    private BannerAdapter bannerAdapter;

    public BannerLoopView(Context context) {
        this(context, null);
    }

    public BannerLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrls(List<String> imageUrls) {
        bannerAdapter = new BannerAdapter(imageUrls, getContext());
        this.setAdapter(bannerAdapter);
    }

    private class BannerAdapter extends CommonViewPagerAdapter<String> {

        public BannerAdapter(List<String> datas, Context context) {
            super(datas, context, R.layout.item_banner_looper);
        }

        @Override
        protected void convert(ViewHolder viewHolder, int position, String s) {
            viewHolder.displayImage(s, R.id.imageView);
        }
    }
}
