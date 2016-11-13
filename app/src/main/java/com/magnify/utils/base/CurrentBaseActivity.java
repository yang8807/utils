package com.magnify.utils.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.datautils.RandomUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.magnify.utils.R;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.utils.ui.common.BaseFragmentContainerActivity;
import com.magnify.yutils.LogUtils;
import com.magnify.yutils.data.BitmapBlurHelper;
import com.magnify.yutils.data.ImageUtils;
import com.magnify.yutils.data.SPUtil;
import com.yan.constants.Constants;
import com.yan.fastview_library.base.BaseActivity;
import com.yan.fastview_library.base.SingleInstanceManager;

public class CurrentBaseActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String OBJETS = "objects";
    private Object[] object;
    private FrameLayout fl_parent;
    private ImageView image_dog, img_back;
    private static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fl_parent = (FrameLayout) findViewById(R.id.fl_parent);
        image_dog = (ImageView) findViewById(R.id.image_dog);
        img_back = (ImageView) findViewById(R.id.img_back);

        showBackground();

        getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE));
        getSupportActionBar().setSubtitle(getIntent().getStringExtra(SUBTITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //拖动左边,结束当前activity的操作帮助助手
        SwipeBackHelper.onCreate(this);
        object = (Object[]) getIntent().getSerializableExtra(OBJETS);
        LogUtils.v("AActivity", "---" + self.getLocalClassName() + "---");
    }

    /**
     * 设置图片背景
     */
    private void showBackground() {
        image_dog.setImageResource(RandomUtil.randomInt(2) % 2 == 0 ? R.mipmap.dog : R.mipmap.duola);
        image_dog.setVisibility(RandomUtil.randomInt(2) % 2 == 0 ? View.VISIBLE : View.GONE);
        //图片模糊效果已经可以使用了
        if (bitmap == null) {
            String localPaths = SPUtil.getString(self, Constants.BROWSE_ACTIVITY_BACKGROUND);
            if (!TextUtils.isEmpty(localPaths)) {
                bitmap = BitmapBlurHelper.doBlurJniArray(ImageUtils.scalePicture(localPaths, 500, 500), 3, false);
                if (bitmap == null) {
                    SingleInstanceManager.getImageLoader().displayImage("file://" + localPaths, img_back);
                }
            }
        } else
            img_back.setImageBitmap(bitmap);
        img_back.setVisibility(RandomUtil.randomInt(2) % 2 == 0 ? View.VISIBLE : View.GONE);
    }

    public void setTopTitle(String text) {
        getSupportActionBar().setTitle(text);
    }

    public void setTopTitle(int resID) {
        getSupportActionBar().setTitle(resID);
    }

    public void setSubtitle(String text) {
        getSupportActionBar().setSubtitle(text);
    }

    public void setSubtitle(int text) {
        getSupportActionBar().setSubtitle(text);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        fl_parent.addView(LayoutInflater.from(self).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        fl_parent.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        fl_parent.addView(view, params);
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
        Class classz = item.getaClass();
        Class mSuperClassz = classz.getSuperclass();

        Intent intent;

        if (isGoBaseFragment(mSuperClassz)) {
            intent = new Intent(self, BaseFragmentContainerActivity.class);
            intent.putExtra(BaseFragmentContainerActivity.WHERE, item.getaClass());
        } else
            intent = new Intent(self, item.getaClass());

        intent.putExtra(CurrentBaseActivity.TITLE, item.getName());
        intent.putExtra(CurrentBaseActivity.SUBTITLE, item.getDescription());
        intent.putExtra(CurrentBaseActivity.OBJETS, item.getObject());
        startActivity(intent);
    }

    /**
     * 跳转到默认的fragment的依赖页面
     */
    private boolean isGoBaseFragment(Class mSuperClassz) {
        if (mSuperClassz != null) {
            if (mSuperClassz.equals(Fragment.class)) {
                return true;
            } else {
                return isGoBaseFragment(mSuperClassz.getSuperclass());
            }
        } else {
            return false;
        }
    }

    public Object[] getObjects() {
        return object;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (item.getItemId() == android.R.id.home)//左上角返回键按钮
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
