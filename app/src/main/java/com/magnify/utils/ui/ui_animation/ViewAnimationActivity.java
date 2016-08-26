package com.magnify.utils.ui.ui_animation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.LogUtils;
import com.magnify.yutils.ToastUtil;

/**
 * Created by heinigger on 16/8/26.
 */
public class ViewAnimationActivity extends CurrentBaseActivity implements View.OnClickListener {
    private View viewAnimation1, viewAnimation2, viewAnimation3, viewAnimtion3, viewAnimation4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        viewAnimation1 = findViewById(R.id.view_anim1);
        viewAnimation2 = findViewById(R.id.view_anim2);
        viewAnimation3 = findViewById(R.id.view_anim3);
        viewAnimation4 = findViewById(R.id.view_anim4);
        viewAnimation1.setOnClickListener(this);
        viewAnimation2.setOnClickListener(this);
        viewAnimation3.setOnClickListener(this);
        viewAnimation4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_anim1:
                getTransLationAnimation(viewAnimation1, viewAnimation2).start();
                break;
            case R.id.view_anim2:
                getTransLationAnimation(viewAnimation2, viewAnimation3).start();
                break;
            case R.id.view_anim3:
                getTransLationAnimation(viewAnimation3, viewAnimation4).start();
                break;
            case R.id.view_anim4:
                getTransLationAnimation(viewAnimation1, viewAnimation2).start();
                break;
        }
    }

    public ObjectAnimator getTransLationAnimation(View startView, View endView) {
        if (endView.getMeasuredHeight() == 0) {
            endView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
        }
        LogUtils.v("mine", endView.getMeasuredHeight());
        endView.setVisibility(View.VISIBLE);
        //动画是以启动动画的这个View为起点,相对位置
        ToastUtil.show(self, startView.getBottom() + ":" + endView.getMeasuredHeight());
        return ObjectAnimator.ofFloat(endView, "translationY", -startView.getMeasuredHeight(), 0).setDuration(1000);
    }
}
