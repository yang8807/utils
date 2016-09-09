package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;

import yan.animations.ScaleInTransformer;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */
public class ViewPagerAnimationFragmenmt extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_viewpager_looper_view, container, false);
        //DepthPageTransformer
//        findView(R.id.banner_looper_view1, view, "DepthPageTransformer", new DepthPageTransformer());
        findView(R.id.banner_looper_view2, view, "ScaleInTransformer", new ScaleInTransformer());
//        findView(R.id.banner_looper_view3, view, "RotateDownPageTransformer", new RotateDownPageTransformer());
//        findView(R.id.banner_looper_view4, view, "RotateUpPageTransformer", new RotateUpPageTransformer());
//        findView(R.id.banner_looper_view5, view, "RotateYTransformer", new RotateYTransformer());
//        findView(R.id.banner_looper_view6, view, "AlphaPageTransformer", new AlphaPageTransformer());
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.banner_looper_view1);
        mViewPager.setAdapter(new CommonViewPagerAdapter<String>(RandomUtil.getRandomImage(5), getActivity(), R.layout.item_image) {
            @Override
            protected void convert(ViewHolder viewHolder, int position, String s) {
                viewHolder.displayRoundImage(s, R.id.imageView);
            }
        });

        return view;
    }

    private void findView(int viewId, View view, String tiltle, ViewPager.PageTransformer mPagerFomer) {
        ViewPager bannerLoopView = (ViewPager) view.findViewById(viewId);
        bannerLoopView.setPageTransformer(true, mPagerFomer);
        bannerLoopView.setOffscreenPageLimit(3);
        bannerLoopView.setTag(tiltle);
        bannerLoopView.setCurrentItem(Integer.MAX_VALUE / 2);
        setAdapter(bannerLoopView);
    }

    //设置控件动画
    private void setAdapter(final ViewPager bannerLoopView) {

        bannerLoopView.setAdapter(new CommonViewPagerAdapter<String>(RandomUtil.getRandomImage(5), getActivity(), R.layout.item_image_view) {
            @Override
            protected void convert(ViewHolder viewHolder, int position, String s) {
                viewHolder.displayImage(s, R.id.image)
                        .setText(R.id.tv_description, (String) bannerLoopView.getTag());
            }
        });
    }
}
