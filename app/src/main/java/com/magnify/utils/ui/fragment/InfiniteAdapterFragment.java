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
import com.yan.fastview_library.indicator.BannerLooperIndicator;
import com.yan.fastview_library.viewgroup.viewpagers.InfiniteLooperView;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */
public class InfiniteAdapterFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.infinite_looper_view,container,false);
        InfiniteLooperView mViewPager= (InfiniteLooperView) view.findViewById(R.id.infinite_view);
        BannerLooperIndicator bannerLooperIndicator= (BannerLooperIndicator) view.findViewById(R.id.indicator);
        mViewPager.setImageUrls(R.layout.item_image_view, new InfiniteLooperView.LoopAdapterListener<String>() {
            @Override
            public void convert(ViewHolder viewHolder, int position, String o) {
            viewHolder.displayImage(o,R.id.image);
            }

            @Override
            public void onPreCreate(ViewHolder viewHolder) {

            }
        }, RandomUtil.getRandomImage(10));

        bannerLooperIndicator.setUpViewPager(mViewPager, 10);
        return view;
    }
}
