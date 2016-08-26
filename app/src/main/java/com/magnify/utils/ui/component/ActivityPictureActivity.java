package com.magnify.utils.ui.component;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.picture_select.ImageFilterFragment;
import com.yan.picture_select.ImagePickerConfiguration;

/**
 * Created by heinigger on 16/8/20.
 */
public class ActivityPictureActivity extends CurrentBaseActivity {
    private FragmentTransaction transaction;
    private ImageFilterFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framlayout);

        setTopTitle("图片选择");

        ImagePickerConfiguration.getInstance().setStyleColor(getColor(R.color.colorPrimary));
        transaction = getSupportFragmentManager().beginTransaction();
        fragment = new ImageFilterFragment();
        transaction.add(R.id.fragment_container, fragment, "images");
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }
}