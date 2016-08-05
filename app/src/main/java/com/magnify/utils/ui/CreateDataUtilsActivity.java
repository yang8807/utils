package com.magnify.utils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.datautils.RandomUserUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.basea_dapter_library.recyclerview.DividerItemDecoration;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.People;
import com.magnify.utils.ui.common.ImageBrowseActivity;

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(self));
        recyclerView.addItemDecoration(new DividerItemDecoration(self, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(new CommonAdapter<People>(self, R.layout.item_header_contact, peoples) {
            @Override
            public void convert(final ViewHolder holder, final int position, final People people) {
                holder.setText(R.id.tv_userName, people.getUserName()).setText(R.id.tv_age, people.getAge() + "岁  " + people.getSex()).setText(R.id.tv_phone, people.getPhone());
                holder.displayRoundImage(people.getAvators(), R.id.img_avators, R.mipmap.ic_launcher);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        self.startActivity(ImageBrowseActivity.getIntent(self, peoples, position));
                    }
                });
            }
        });
    }
}
