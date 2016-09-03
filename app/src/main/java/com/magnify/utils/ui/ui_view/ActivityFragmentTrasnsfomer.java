package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magnify.utils.R;
import com.magnify.utils.ui.fragment.BasePagerAmimationFragment;
import com.yan.fastview_library.base.BaseActivity;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */
public class ActivityFragmentTrasnsfomer extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framlayout);
        setContentId(R.id.fragment_container);
        switchFragment(BasePagerAmimationFragment.class);
    }
}
