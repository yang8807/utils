package com.magnify.utils.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magnify.yutils.app.BaseActivity;

/**
 * Created by heinigger on 16/8/2.
 */
public class CurrentBaseActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE));
        getSupportActionBar().setSubtitle(getIntent().getStringExtra(SUBTITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
}
