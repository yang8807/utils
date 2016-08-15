package com.magnify.utils.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.People;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/3.
 */
public class ImageBrowseActivity extends CurrentBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final ArrayList<People> peoples = ((ArrayList<People>) getIntent().getSerializableExtra("people"));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPage);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new CommonViewPagerAdapter<People>(peoples, self, R.layout.activity_view_pager_details) {
            @Override
            protected void convert(ViewHolder viewHolder, int position, People people) {
                viewHolder.setText(R.id.tv_description, people.getUserName() + ":" + people.getAge() + "岁 " + people.getSex() + " " + people.getPhone());
                viewHolder.displayImage(people.getAvators(), R.id.image, R.mipmap.ic_launcher);
            }
        });

        //直接再convert中会出现问题,因为convert是默认显示三遍的,就是会直接显示第三次的内容
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                People people = peoples.get(position);
                getSupportActionBar().setTitle(people.getUserName());
                getSupportActionBar().setSubtitle(people.getAge() + "岁 " + people.getSex() + " " + people.getPhone());
            }
        });
        viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
    }

    public static Intent getIntent(Context context, ArrayList<People> peoples, int position) {
        Intent intent = new Intent(context, ImageBrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("people", peoples);
        intent.putExtra("position", position);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getIntent(Context context, ArrayList<People> peoples) {
        return getIntent(context, peoples, 0);
    }
}
