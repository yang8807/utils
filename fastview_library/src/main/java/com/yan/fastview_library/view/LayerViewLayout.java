package com.yan.fastview_library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.yutils.DeviceUtil;

/**
 * Created by heinigger on 16/10/28.
 */

public class LayerViewLayout extends ViewGroup {
    //子View交叠的大小部分
    private int childOverlayMargin = DeviceUtil.dipToPx(getContext(), 10);
    private final static int DERECTION_LEFT = 1;
    private final static int DERECTION_RIGHT = 2;
    private int direction = DERECTION_RIGHT;
    private int childHeight;

    public LayerViewLayout(Context context) {
        this(context, null);
    }

    public LayerViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayerViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View mView = getChildAt(i);
//            mView.layout();

        }
    }
}
