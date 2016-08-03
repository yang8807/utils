package com.magnify.utils.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.People;

/**
 * Created by heinigger on 16/8/3.
 */
public class CreateDetailActivity extends CurrentBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        People people = (People) getIntent().getSerializableExtra("people");
        if (people != null) {
            Glide.with(getApplicationContext()).load(people.getAvators()).into((ImageView) findViewById(R.id.imageview));
        }
    }

    public static Intent getIntent(Context context, People people) {
        Intent intent = new Intent(context, CreateDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("people", people);
        intent.putExtra(CurrentBaseActivity.TITLE, people.getUserName());
        intent.putExtra(CurrentBaseActivity.SUBTITLE, people.getAge() + "岁 " + people.getSex() + " " + people.getPhone());
        intent.putExtras(bundle);
        return intent;
    }
}
