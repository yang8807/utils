package com.yan.fastview_library.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by heinigger on 16/9/2.
 */
public class ThrowAnimationLayout extends RelativeLayout {

    private Path mPath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();

    private ObjectAnimator mObjectAnimator;

    public ThrowAnimationLayout(Context context) {
        this(context, null);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTopPoint(100, 100);
        start();
    }

    public void setTopPoint(float x, float y) {
        mPath.reset();
        mPath.moveTo(0, getMeasuredHeight());
        mPath.quadTo(x, y, getMeasuredWidth(), getMeasuredHeight());
        mPathMeasure.setPath(mPath, true);
    }

    public void start() {
        if (getChildCount() > 0) {
            if (mObjectAnimator == null) {
                mObjectAnimator = ObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
                mObjectAnimator.setDuration(5000);
            } else {
                mObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
            }
            mObjectAnimator.start();
        }
    }
}
