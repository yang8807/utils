package com.magnify.utils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.datautils.RandomUserUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.People;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/3.
 */
public class CreateDataUtilsActivity extends CurrentBaseActivity {
    private ArrayList<People> peoples = new ArrayList<>();
    private RandomUserUtil randomUserUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomUserUtils = new RandomUserUtil(self);
        for (int i = 0; i < 150; i++) {
            peoples.add(new People(randomUserUtils.createRandomUser()));
        }

        ((ListView) findViewById(R.id.listview)).setAdapter(new CommonAdapter<People>(self, R.layout.item_header_contact, peoples) {
            @Override
            public void convert(final ViewHolder holder, final int position, final People people) {
                holder.setText(R.id.tv_userName, people.getUserName()).setText(R.id.tv_age, people.getAge() + "岁  " + people.getSex()).setText(R.id.tv_phone, people.getPhone());
                final ImageView image = holder.getView(R.id.img_avators);
                Glide.with(getApplicationContext()).load(people.getAvators()).placeholder(R.mipmap.ic_launcher).into(image);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        self.startActivity(CreateDetailActivity.getIntent(self, peoples, position));
                    }
                });
            }
        });
    }
}
