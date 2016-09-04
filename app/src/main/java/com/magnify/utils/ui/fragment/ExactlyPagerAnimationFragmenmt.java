package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.viewgroup.viewpagers.InfiniteLooperView;

import yan.animations.AlphaPageTransformer;
import yan.animations.DepthPageTransformer;
import yan.animations.RotateDownPageTransformer;
import yan.animations.RotateUpPageTransformer;
import yan.animations.RotateYTransformer;
import yan.animations.ScaleInTransformer;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */
public class ExactlyPagerAnimationFragmenmt extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_banner_looper_view, container, false);
        //DepthPageTransformer
        findView(R.id.banner_looper_view1, view, "DepthPageTransformer", new DepthPageTransformer());
        findView(R.id.banner_looper_view2, view, "ScaleInTransformer", new ScaleInTransformer());
        findView(R.id.banner_looper_view3, view, "RotateDownPageTransformer", new RotateDownPageTransformer());
        findView(R.id.banner_looper_view4, view, "RotateUpPageTransformer", new RotateUpPageTransformer());
        findView(R.id.banner_looper_view5, view, "RotateYTransformer", new RotateYTransformer());
        findView(R.id.banner_looper_view6, view, "AlphaPageTransformer", new AlphaPageTransformer());
        return view;
    }

    private void findView(int viewId, View view, String tiltle, ViewPager.PageTransformer mPagerFomer) {
        InfiniteLooperView<String> bannerLoopView = (InfiniteLooperView<String>) view.findViewById(viewId);
        bannerLoopView.setPageTransformer(true, mPagerFomer);
        bannerLoopView.setOffscreenPageLimit(3);
        bannerLoopView.setTag(tiltle);
        setAdapter(bannerLoopView);
    }

    //设置控件动画
    private void setAdapter(final InfiniteLooperView<String> bannerLoopView) {

        bannerLoopView.setImageUrls(R.layout.item_image_view, new InfiniteLooperView.LoopAdapterListener<String>() {

            @Override
            public void convert(ViewHolder viewHolder, int position, String o) {
                viewHolder.displayImage(o, R.id.image).setText(R.id.tv_description, (String) bannerLoopView.getTag());
            }

            @Override
            public void onPreCreate(ViewHolder viewHolder) {
            }
        }, RandomUtil.getRandomImage(5));
    }
}
