package com.magnify.utils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.dialog.BoundsAnimationDialog;
import com.magnify.utils.ui.dialog.HomeBannerDialog;
import com.yan.fastview_library.poupwindows.BaseLinearPoupWindows;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityDialog extends CurrentBaseActivity {
    private BaseLinearPoupWindows mAnyWherePoupLeft, mAnyWherePoupRight, mAnyWherePoupTop, mAnyWherePoupBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //定向运动的弹窗
        findViewById(R.id.ptv_show_translation_dialog).setOnClickListener(v -> new HomeBannerDialog(self).setTarget(v).show());
        //弹性弹窗
        findViewById(R.id.ptv_show_bound).setOnClickListener(v -> new BoundsAnimationDialog(self).show());

        mAnyWherePoupLeft = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupRight = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupRight.setDrawableNRadius(R.color.colorAccent, 10);
        mAnyWherePoupTop = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupBottom = new BaseLinearPoupWindows(self, R.layout.item_poup_image);

        //任意方向的弹窗
        findViewById(R.id.btn_show_anywhere).setOnClickListener(view -> {
            mAnyWherePoupRight.showAtCenter(view, Gravity.RIGHT);
            mAnyWherePoupLeft.showAtCenter(view, Gravity.LEFT);
            mAnyWherePoupTop.showAtCenter(view, Gravity.TOP);
            mAnyWherePoupBottom.showAtCenter(view, Gravity.BOTTOM);
        });
    }
}
