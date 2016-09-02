package com.magnify.utils.ui.ui_view;

import android.content.Context;

import com.magnify.utils.R;
import com.yan.fastview_library.poupwindows.BaseLinearPoupWindows;
import com.yan.fastview_library.view.ThrowAnimationView;

/**
 * Created by heinigger on 16/9/2.
 */
public class ThrowAnimationPoup extends BaseLinearPoupWindows {

    private ThrowAnimationView throwView;

    public ThrowAnimationPoup(Context context) {
        super(context, R.layout.view_throw_shop_car);
        throwView = (ThrowAnimationView) views.findViewById(R.id.throwView);
    }

}
