package com.magnify.utils.ui.ui_animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.LogUtils;

/**
 * Created by heinigger on 16/8/26.
 */
public class ViewAnimationActivity extends CurrentBaseActivity implements View.OnClickListener {
    private View viewAnimation1, viewAnimation2, viewAnimation3, viewAnimtion3, viewAnimation4;
    private ViewGroup mViewGoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        viewAnimation1 = findViewById(R.id.view_anim1);
        viewAnimation2 = findViewById(R.id.view_anim2);
        viewAnimation3 = findViewById(R.id.view_anim3);
        viewAnimation4 = findViewById(R.id.view_anim4);

        mViewGoup = (ViewGroup) findViewById(R.id.lly_parent);


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
                for (int i = 0; i < mViewGoup.getChildCount(); i++) {
                    if (i + 1 < mViewGoup.getChildCount()) {
                        ObjectAnimator objectAnimator = getTransLationAnimation(mViewGoup.getChildAt(i), mViewGoup.getChildAt(i + 1));
                        objectAnimator.setStartDelay(i * 100);
                        objectAnimator.start();
                    }
                }

                break;
        }
    }

    public ObjectAnimator getTransLationAnimation(View startView, View endView) {
        if (endView.getMeasuredHeight() == 0) {
            endView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
        }
        LogUtils.v("mine", startView.getBottom() + ":" + endView.getMeasuredHeight());
        endView.setVisibility(View.VISIBLE);
        //动画是以启动动画的这个View为起点,相对位置
        return ObjectAnimator.ofPropertyValuesHolder(endView, PropertyValuesHolder.ofFloat("translationY", -startView.getMeasuredHeight(), 0), PropertyValuesHolder.ofFloat("alpha", 0, 1)).setDuration(500);
    }
}
