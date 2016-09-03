package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.viewgroup.BannerLoopView;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */
public class BasePagerAmimationFragment extends BaseFragment {
    protected BannerLoopView<String> bannerLoopView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_banner_looper_view, container, false);
        bannerLoopView = (BannerLoopView<String>) view.findViewById(R.id.banner_looper_view);
        bannerLoopView.setImageUrls(R.layout.item_image_view, new BannerLoopView.LoopAdapterListener<String>() {
            @Override
            public void convert(ViewHolder viewHolder, int position, String o) {
                viewHolder.displayImage(o, R.id.image);
            }
        }, RandomUtil.getRandomImage(5));
        return view;
    }
}
