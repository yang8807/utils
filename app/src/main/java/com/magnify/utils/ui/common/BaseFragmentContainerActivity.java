package com.magnify.utils.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;

import java.util.List;

/**
 * Created by heinigger on 16/9/19.
 */
public class BaseFragmentContainerActivity extends AppCompatActivity {
    private int mContentId;
    private Fragment mFragment;
    public static String WHERE = "where";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_normal_container);
        setContentId(R.id.fram_container);
        switchFragment(getFragmentClass());
    }

    public Class getFragmentClass() {
        return (Class) getIntent().getSerializableExtra(WHERE);
    }

    /**
     * 用来方便fagment的使用,fagment是一种简便的,activity代替品
     */
    public void switchFragment(Class<?> cls, Object... objects) {
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
            fragmentShow = Fragment.instantiate(BaseFragmentContainerActivity.this, mClassName);
            try {
                Bundle bundle = getIntent().getExtras();
                if (bundle == null) bundle = new Bundle();
                bundle.putSerializable(BaseFragment.OBJECTKEY, objects);
                fragmentShow.setArguments(bundle);
                ft.add(mContentId, fragmentShow, mClassName);
            } catch (Exception e) {
                throw new IllegalArgumentException("please set ContentID for Fragment");
            }
        } else {
            ft.show(fragmentShow);
        }

        if (fragmentShow instanceof BaseFragment) {
            ((BaseFragment) fragmentShow).setParams(objects);
        }
        mFragment = fragmentShow;

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
        mFragment = currentFragment;
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment != null) mFragment.onActivityResult(requestCode, resultCode, data);
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
            dialogFragment = (DialogFragment) DialogFragment.instantiate(BaseFragmentContainerActivity.this, clsName);
        }
        dialogFragment.show(getSupportFragmentManager(), clsName);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            if (mFragment instanceof BaseFragment) {
                BaseFragment mBaseFragment = (BaseFragment) mFragment;
                if (mBaseFragment.onBackPressed()) {
                    super.onBackPressed();
                }
            }
        }
    }

    public void setContentId(int mContentId) {
        this.mContentId = mContentId;
    }
}
