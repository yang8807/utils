package com.magnify.utils.ui.ui_adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.data.RandomCharUtils;

/**
 * Created by heinigger on 16/8/2.
 */
public class RandCharActivity extends CurrentBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_view);
        ((TextView) findViewById(R.id.text)).setText(RandomCharUtils.getRandomChar(1500));
    }
}
