package com.magnify.yutils.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by heinigger on 16/8/2.
 */
public class BaseActivity extends AppCompatActivity {
    private Toast mToast;
    protected BaseActivity self;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        self = this;
    }

    /*-------------------start:,与整体架构无关------------------------------------------------*/
    public void setOnClickListener(View.OnClickListener clickListener, View... views) {
        for (int i = 0; i < views.length; i++)
            views[i].setOnClickListener(clickListener);
    }

    public void setOnClickListener(View.OnClickListener clickListener, int... ids) {
        for (int i = 0; i < ids.length; i++)
            findViewById(ids[i]).setOnClickListener(clickListener);
    }

    /*
   * 重置视图大小
   * @param width
   * @param height
   * @param view
   * */
    public void resetViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.requestLayout();
    }

    public void setVisibility(boolean isVisibility, View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    public void setVisibility(boolean isVisibility, int... views) {
        for (int i = 0; i < views.length; i++) {
            findViewById(views[i]).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示提示
     *
     * @param content
     */
    protected void showToast(CharSequence content) {
        mToast.setText(content);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * @param resId
     * @see #showToast(CharSequence)
     */
    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 显示提示
     *
     * @param content
     * @param duration 显示时长
     */
    protected void showToast(CharSequence content, int duration) {
        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.show();
    }

    /**
     * @param resId
     * @param duration
     * @see #showToast(int, int)
     */
    protected void showToast(int resId, int duration) {
        showToast(getString(resId), duration);
    }

    /**
     * 吐司 信息
     *
     * @param s
     */
    public void toastMessage(CharSequence s) {
        mToast.setText(s);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }


    public void toastMessage(int resId) {
        toastMessage(getString(resId));
    }

    /**
     * textview加载文字
     */
    protected TextView setText(int viewId, CharSequence text) {
        return setText(getWindow().getDecorView(), viewId, text);
    }

    /**
     * textview加载文字
     */
    protected TextView setText(View parent, int viewId, CharSequence text) {
        TextView view = (TextView) parent.findViewById(viewId);
        if (view != null) {
            view.setText(text);
        }
        return view;
    }

    /**
     * imageview加载图片
     */
    protected ImageView setImage(int viewId, String src) {
        return setImage(getWindow().getDecorView(), viewId, src);
    }

    /**
     * imageview加载图片
     */
    protected ImageView setImage(View parent, int viewId, String src) {
        ImageView view = (ImageView) parent.findViewById(viewId);
        if (view != null && src != null && src.length() > 5) {
//            ImageLoaderUtil.DisplayImage(src, view);
        }
        return view;
    }

    /**
     * imageview加载图片
     */
    protected ImageView setImage(int viewId, int resId) {
        return setImage(getWindow().getDecorView(), viewId, resId);
    }

    /**
     * imageview加载图片
     */
    protected ImageView setImage(View parent, int viewId, int resId) {
        ImageView view = (ImageView) parent.findViewById(viewId);
        if (view != null && resId > 0) {
            view.setImageResource(resId);
        }
        return view;
    }

    /*-------------------end:Activity中常用的操作,与整体架构无关------------------------------------------------*/
}
