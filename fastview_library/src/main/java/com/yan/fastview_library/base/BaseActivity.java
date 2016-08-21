package com.yan.fastview_library.base;

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

    /*-------------------start:------------------------------------------------*/
    public void setOnClickListener(View.OnClickListener clickListener, View... views) {
        for (int i = 0; i < views.length; i++)
            views[i].setOnClickListener(clickListener);
    }

    public void setOnClickListener(View.OnClickListener clickListener, int... ids) {
        for (int i = 0; i < ids.length; i++)
            findViewById(ids[i]).setOnClickListener(clickListener);
    }

    /*
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
     * @param content
     */
    protected void showToast(CharSequence content) {
        mToast.setText(content);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }


    protected void showToast(int resId) {
        showToast(getString(resId));
    }

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

    protected TextView setText(int viewId, CharSequence text) {
        return setText(getWindow().getDecorView(), viewId, text);
    }

    protected TextView setText(View parent, int viewId, CharSequence text) {
        TextView view = (TextView) parent.findViewById(viewId);
        if (view != null) {
            view.setText(text);
        }
        return view;
    }

    protected ImageView setImage(int viewId, String src) {
        return setImage(getWindow().getDecorView(), viewId, src);
    }

    protected ImageView setImage(View parent, int viewId, String src) {
        ImageView view = (ImageView) parent.findViewById(viewId);
        if (view != null && src != null && src.length() > 5) {
            SingleInstanceManager.getImageLoader().displayImage(src, view);
        }
        return view;
    }

    protected ImageView setCircleImage(View parent, int viewId, String src) {
        ImageView view = (ImageView) parent.findViewById(viewId);
        if (view != null && src != null && src.length() > 5) {
            SingleInstanceManager.getImageLoader().displayRoundImage(src, view);
        }
        return view;
    }

    protected ImageView setImage(int viewId, int resId) {
        return setImage(getWindow().getDecorView(), viewId, resId);
    }

    protected ImageView setImage(View parent, int viewId, int resId) {
        ImageView view = (ImageView) parent.findViewById(viewId);
        if (view != null && resId > 0) {
            view.setImageResource(resId);
        }
        return view;
    }

    /*-------------------end------------------------------------------------*/
}
