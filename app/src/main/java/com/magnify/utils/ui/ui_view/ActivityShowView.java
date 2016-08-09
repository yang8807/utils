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
    public static final int TYPE_TEXTVIEW_EXTENDS = 2;
    private int layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = (int) getObjects()[0];
        if (TYPE_LOADINGVIEW == type)
            layout = R.layout.activity_loadingview;
        else if (TYPE_TEXTVIEW_EXTENDS == type)
            layout = R.layout.activity_textview_extends;
        setContentView(layout);
    }
}