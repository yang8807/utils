package com.yan.fastview_library.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by heinigger on 16/8/2.
 */
public class BaseActivity extends AppCompatActivity {
    private SparseArray<View> mViews = new SparseArray<>();
    private Toast mToast;
    protected BaseActivity self;
    private int mContentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        self = this;
    }

    public <E extends View> E getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (E) view;
    }

    public void setContentId(int mContentId) {
        this.mContentId = mContentId;
    }

    /*-------------------start:------------------------------------------------*/
    public void setOnClickListener(View.OnClickListener clickListener, View... views) {
        for (int i = 0; i < views.length; i++)
            views[i].setOnClickListener(clickListener);
    }

    public void setOnClickListener(View.OnClickListener clickListener, int... ids) {
        for (int i = 0; i < ids.length; i++)
            getView(ids[i]).setOnClickListener(clickListener);
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
            getView(views[i]).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
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


    public void toastMessage(@StringRes int resId) {
        toastMessage(getString(resId));
    }

    protected TextView setText(@IdRes int viewId, CharSequence text) {
        return setText(getWindow().getDecorView(), viewId, text);
    }

    protected TextView setText(View parent, @IdRes int viewId, CharSequence text) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(text);
        }
        return view;
    }

    protected ImageView displayImage(@IdRes int viewId, String url) {
        ImageView view = getView(viewId);
        if (view != null && !TextUtils.isEmpty(url)) {
            SingleInstanceManager.getImageLoader().displayImage(url, view);
        }
        return view;
    }

    protected ImageView displayRoundImage(@IdRes int viewId, String src) {
        ImageView view = getView(viewId);
        if (view != null && !TextUtils.isEmpty(src)) {
            SingleInstanceManager.getImageLoader().displayRoundImage(src, view);
        }
        return view;
    }

    protected ImageView setImage(@IdRes int viewId, @DrawableRes int resId) {
        ImageView view = getView(viewId);
        if (view != null && resId > 0) {
            view.setImageResource(resId);
        }
        return view;
    }

    /**
     * 用来方便fagment的使用,fagment是一种简便的,activity代替品
     */
    public void switchFragment(Class<?> cls) {
        String mClassName = cls.getName();
        FragmentManager fm = getSupportFragmentManager();
        //先将其他的fagment进行隐藏

        FragmentTransaction ft = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && !fragments.isEmpty())
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragmentNow = fragments.get(i);
                if (fragmentNow != null && !mClassName.equals(fragmentNow.getTag()) && !fragmentNow.isHidden()) {
                    ft.hide(fragmentNow);
                }
            }

        Fragment fragmentShow = fm.findFragmentByTag(mClassName);
        if (fragmentShow == null) {
            fragmentShow = Fragment.instantiate(self, mClassName);
            try {
                ft.add(mContentId, fragmentShow, mClassName);
            } catch (Exception e) {
                throw new IllegalArgumentException("please set ContentID for Fragment");
            }
        } else {
            ft.show(fragmentShow);
        }
        ft.commitAllowingStateLoss();
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentByTag(fragment.getClass().getName());
        if (currentFragment != null) {
            ft.show(fragment);
        } else {
            ft.add(mContentId, fragment, fragment.getClass().getName());
        }
        ft.commitAllowingStateLoss();
    }

    public void showDialogFragment(Class<?> clazz) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String clsName = clazz.getName();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (fragment != null && fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                if (dialogFragment.isVisible()) dialogFragment.dismiss();
            }
        }
        Fragment fragmentShow = fragmentManager.findFragmentByTag(clsName);
        DialogFragment dialogFragment = null;
        if (fragmentShow != null && fragmentShow instanceof DialogFragment) {
            dialogFragment = (DialogFragment) fragmentShow;
        } else {
            dialogFragment = (DialogFragment) DialogFragment.instantiate(self, clsName);
        }
        dialogFragment.show(getSupportFragmentManager(), clsName);

    }

    public int getColors(int colorPrimary) {
        return getResources().getColor(colorPrimary);
    }

    /*-------------------end------------------------------------------------*/
}
