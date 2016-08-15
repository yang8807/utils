package com.magnify.utils.ui.ui_view;

import android.os.Bundle;

import com.example.datautils.RandomUtil;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.fastview_library.viewgroup.BannerLoopView;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/15.
 */
public class ActivityBannerView extends CurrentBaseActivity {
    private BannerLoopView banner_looper_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_view);
        banner_looper_view = (BannerLoopView) findViewById(R.id.banner_looper_view);
        ArrayList<String> imageUrls = RandomUtil.getRandomImage(3);
        banner_looper_view.setImageUrls(imageUrls);

    }
}
