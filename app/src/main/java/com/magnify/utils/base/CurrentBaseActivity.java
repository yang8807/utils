package com.magnify.utils.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.yutils.LogUtil;
import com.magnify.yutils.app.BaseActivity;

/**
 * Created by heinigger on 16/8/2.
 */
public class CurrentBaseActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    private static final String OBJETS = "objects";
    private Object[] object;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE));
        getSupportActionBar().setSubtitle(getIntent().getStringExtra(SUBTITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //拖动左边,结束当前activity的操作帮助助手
        SwipeBackHelper.onCreate(this);
        object = (Object[]) getIntent().getSerializableExtra(OBJETS);
        LogUtil.v("AActivity", "---" + self.getLocalClassName() + "---");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    public void startNewActivity(ActivityBean item) {
        Intent intent = new Intent(self, item.getaClass());
        intent.putExtra(CurrentBaseActivity.TITLE, item.getName());
        intent.putExtra(CurrentBaseActivity.SUBTITLE, item.getDescription());
        intent.putExtra(CurrentBaseActivity.OBJETS, item.getObject());
        startActivity(intent);
    }

    public Object[] getObjects() {
        return object;
    }
}
