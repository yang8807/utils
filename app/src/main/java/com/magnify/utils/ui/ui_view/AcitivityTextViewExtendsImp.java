package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;

/**
 * Created by heinigger on 16/8/7.
 */
public class AcitivityTextViewExtendsImp extends CurrentBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview_extends);
    }
}
