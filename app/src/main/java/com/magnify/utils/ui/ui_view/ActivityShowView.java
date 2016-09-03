package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;

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
            layout = R.layout.activity_sidebar;
        else if (TYPE_RIPPLE == type) {
            layout = R.layout.activity_ripple_view;
        } else if (TYPE_SHOPCAR == type) {
           layout=R.layout.fragment_throw_animtion_view;
        }

        setContentView(layout);
    }
}
