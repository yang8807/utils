package com.magnify.utils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.dialog.BoundsAnimationDialog;
import com.magnify.utils.ui.dialog.HomeBannerDialog;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityDialog extends CurrentBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //定向运动的弹窗
        findViewById(R.id.ptv_show_translation_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new HomeBannerDialog(self).setTarget(v).show();
            }
        });
        //弹性弹窗
        findViewById(R.id.ptv_show_bound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoundsAnimationDialog dialod = new BoundsAnimationDialog(self);
                dialod.show();
            }
        });
    }
}