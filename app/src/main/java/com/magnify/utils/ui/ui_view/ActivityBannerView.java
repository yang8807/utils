package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.fastview_library.indicator.BannerLooperIndicator;
import com.yan.fastview_library.viewgroup.BannerLoopView;

/**
 * Created by heinigger on 16/8/15.
 */
public class ActivityBannerView extends CurrentBaseActivity implements View.OnClickListener {
    private BannerLoopView banner_looper_view;
    private ViewPager viewpager_normal;
    private BannerLooperIndicator baner_indicator, baner_indicator_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_view);
        setOnClickListener(this, R.id.btn_net, R.id.btn_local, R.id.btn_normal);
        banner_looper_view = (BannerLoopView) findViewById(R.id.banner_looper_view);
        baner_indicator = (BannerLooperIndicator) findViewById(R.id.baner_indicator);
        baner_indicator_normal = (BannerLooperIndicator) findViewById(R.id.baner_indicator_normal);
        viewpager_normal = (ViewPager) findViewById(R.id.viewpager_normal);
        setNetWorkAdapter();

        baner_indicator.setUpViewPager(banner_looper_view, 15);
        baner_indicator_normal.setUpViewPager(viewpager_normal, 15);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_net://用来加载网络图片
                setNetWorkAdapter();
                baner_indicator_normal.setUpViewPager(viewpager_normal, 15);
                setVisibility(true, R.id.rly_looper);
                setVisibility(false, R.id.rly_normal);
                break;
            case R.id.btn_local://用来加载本地图片
                setLocalAdapter();
                setVisibility(true, R.id.rly_looper);
                setVisibility(false, R.id.rly_normal);
                baner_indicator.setUpViewPager(banner_looper_view, 3);
                break;
            case R.id.btn_normal:
                setVisibility(true, R.id.rly_normal);
                setVisibility(false, R.id.rly_looper);
                viewpager_normal.setAdapter(new CommonViewPagerAdapter<String>(RandomUtil.getRandomImage(15), self, R.layout.item_banner_looper) {
                    @Override
                    protected void convert(ViewHolder viewHolder, int position, String s) {
                        viewHolder.displayImage(s, R.id.imageView);
                    }
                });
                break;
        }
    }

    /**
     * 用来加载本地的数据
     */
    private void setLocalAdapter() {
        banner_looper_view.setImageUrls(R.layout.item_banner_looper, new BannerLoopView.LoopAdapterListener<Integer>() {
                    @Override
                    public void convert(ViewHolder viewHolder, int position, Integer s) {
                        viewHolder.displayImage(s, R.id.imageView);
                    }
                },
                R.drawable.ic_watch_later_blue_grey_700_36dp,
                R.drawable.ic_perm_identity_green_500_36dp,
                R.drawable.ic_https_blue_600_36dp);
    }

    /**
     * 用来加载网络的图片数据
     */
    private void setNetWorkAdapter() {
        banner_looper_view.setImageUrls(R.layout.item_banner_looper, new BannerLoopView.LoopAdapterListener<String>() {
                    @Override
                    public void convert(ViewHolder viewHolder, int position, String s) {
                        viewHolder.displayImage(s, R.id.imageView);
                    }
                },
                RandomUtil.getRandomImage(15));

    }
}
