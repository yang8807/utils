package com.magnify.utils.ui.component;

import android.os.Bundle;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.picture_select.ImageFilterFragment;
import com.yan.picture_select.ImagePickerConfiguration;

/**
 * Created by heinigger on 16/8/20.
 */
public class ActivityPictureActivity extends CurrentBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framlayout);
        setTopTitle("图片选择");
        setContentId(R.id.fragment_container);
        ImagePickerConfiguration.getInstance().setStyleColor(getColor(R.color.colorPrimary));
        switchFragment(ImageFilterFragment.class);
    }
}
