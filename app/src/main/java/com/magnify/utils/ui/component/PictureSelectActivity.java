package com.magnify.utils.ui.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        //单选
        findViewById(R.id.btn_single_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(self, ActivityPictureActivity.class));
            }
        });
        //多选
        findViewById(R.id.btn_multi_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(self, ActivityPictureActivity.class));
            }
        });

    }

}
