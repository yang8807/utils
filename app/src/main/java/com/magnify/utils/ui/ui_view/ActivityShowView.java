package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.fragment.ExactlyPagerAnimationFragmenmt;
import com.magnify.utils.ui.fragment.InfiniteAdapterFragment;
import com.magnify.utils.ui.fragment.SideBarFragment;
import com.magnify.utils.ui.fragment.SwipeLayoutFragment;
import com.magnify.utils.ui.fragment.SwipeMultiLayoutFragment;
import com.magnify.utils.ui.fragment.ViewPagerAnimationFragmenmt;
import com.yan.picture_select.ImageStickFragment;

/**
 * Created by heinigger on 16/8/7.
 */
public class ActivityShowView extends CurrentBaseActivity {
    //加载动画
    public static final int TYPE_LOADINGVIEW = 1;
    //textView扩展属性
    public static final int TYPE_TEXTVIEW_EXTENDS = 2;
    //装饰View
    public static final int TYPE_DECORATEEDVIEW = 3;
    //字母导航栏
    public static final int TYPE_SIDEBAR = 5;
    //雷达扩散效果
    public static final int TYPE_RIPPLE = 6;
    //抛向购物车的动效
    public static final int TYPE_SHOPCAR = 7;
    //ViewPager Animation
    public static final int TYPE_VIEW_PAGER_ANIMATION = 8;
    public static final int TYPE_VIEW_PAGER_ANIMATION_NO_LOOPER = 13;
    public static final int TYPE_INFINITE_VIEWPAGER_ADAPTER = 9;
    //file path
    public static final int TYPE_STICK_GRID_VIEW = 10;

    public static final int TYPE_COMMOM_SINGLE_SWIPE = 11;
    public static final int TYPE_COMMOM_MULTI_SWIPE_LAYOUT = 12;
    //点赞动画
    public static final int TYPE_FAVPRIATE = 14;
    public static final int TYPE_TRIGANLE_VIEW = 15;

    private int layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = (int) getObjects()[0];
        //设置默认的属性
        layout = R.layout.framlayout;
        setContentId(R.id.fragment_container);

        if (TYPE_LOADINGVIEW == type)
            layout = R.layout.activity_loadingview;
        else if (TYPE_TEXTVIEW_EXTENDS == type)
            layout = R.layout.activity_textview_extends;
        else if (TYPE_DECORATEEDVIEW == type)
            layout = R.layout.activity_decorated_view;
        else if (TYPE_SIDEBAR == type)
            switchFragment(SideBarFragment.class);
        else if (TYPE_RIPPLE == type) {
            layout = R.layout.activity_ripple_view;
        } else if (TYPE_SHOPCAR == type) {
            layout = R.layout.fragment_throw_animtion_view;
        } else if (TYPE_VIEW_PAGER_ANIMATION == type) {
            switchFragment(ExactlyPagerAnimationFragmenmt.class);
        } else if (TYPE_INFINITE_VIEWPAGER_ADAPTER == type) {
            switchFragment(InfiniteAdapterFragment.class);
        } else if (TYPE_STICK_GRID_VIEW == type) {
            switchFragment(ImageStickFragment.class);
        } else if (TYPE_COMMOM_SINGLE_SWIPE == type) {
            switchFragment(SwipeLayoutFragment.class);
        } else if (TYPE_COMMOM_MULTI_SWIPE_LAYOUT == type) {
            switchFragment(SwipeMultiLayoutFragment.class);
        } else if (TYPE_VIEW_PAGER_ANIMATION_NO_LOOPER == type) {
            switchFragment(ViewPagerAnimationFragmenmt.class);
        } else if (TYPE_FAVPRIATE == type) {
            layout = R.layout.view_favoriate_view;
        }else if (TYPE_TRIGANLE_VIEW==type){
            layout=R.layout.view_triagle_view;
        }
        setContentView(layout);
    }
}
