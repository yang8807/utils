package com.yan.fastview_library.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.magnify.yutils.LogUtils;
import com.yan.fastview_library.base.BaseActivity;

public abstract class BaseFragment extends Fragment {


    private Toast mToast;

    public static final String WHERE = "where";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        // 进行自动注入
        LogUtils.v("AActivity", this.getClass().getSimpleName());
    }

    /*-------------------start:,与整体架构无关------------------------------------------------*/
    public void setOnClickListener(View.OnClickListener clickListener, View... views) {
        for (int i = 0; i < views.length; i++)
            views[i].setOnClickListener(clickListener);
    }

    public void resetViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.requestLayout();
    }

    public void showToast(CharSequence s) {
        showToast(s, Toast.LENGTH_LONG);
    }

    public void showToast(String s) {
        showToast(s, Toast.LENGTH_LONG);
    }

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    protected void showToast(CharSequence content, int duration) {
        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.show();
    }

    public Intent newIntent(Context mContext, Class mClass) {
        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.putExtra(WHERE, mClass);
        return intent;
    }

    protected void showToast(int resId, int duration) {
        showToast(getString(resId), duration);
    }

    public void toastMessage(CharSequence s) {
        mToast.setText(s);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /*-------------------end:Activity中常用的操作,与整体架构无关------------------------------------------------*/
}