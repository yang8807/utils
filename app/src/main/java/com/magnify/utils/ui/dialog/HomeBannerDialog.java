package com.magnify.utils.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.magnify.utils.R;
import com.yan.fastview_library.dialog.BaseTargetTranslationDialog;

/**
 * Created by 洒笑天涯 on 2016/6/17.
 */
public class HomeBannerDialog extends BaseTargetTranslationDialog {
    public HomeBannerDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.show_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRotationDegreen(1000);
        //why
        setAnimationView(contentView.findViewById(R.id.lly_animationView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAnimatorRunning())
                    hideDilog();
            }
        });
    }
}