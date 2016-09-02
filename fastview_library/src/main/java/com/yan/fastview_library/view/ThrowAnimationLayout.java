package com.yan.fastview_library.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.RelativeLayout;

import com.magnify.yutils.DeviceUtil;

/**
 * Created by heinigger on 16/9/2.
 */
public class ThrowAnimationLayout extends RelativeLayout {

    private Path mPath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();

    private ObjectAnimator mObjectAnimator;
    private Paint mPaint;

    public ThrowAnimationLayout(Context context) {
        this(context, null);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        setTopPoint(100, 100);
        start();
    }

    public void setTopPoint(float x, float y) {
        mPath.reset();
//        mPath.moveTo(0, getMeasuredHeight());
//        mPath.quadTo(x, y, getMeasuredWidth(), getMeasuredHeight());
        mPath.moveTo(0, DeviceUtil.dipToPx(getContext(), 50));
        mPath.quadTo(x, y, DeviceUtil.dipToPx(getContext(), 50), DeviceUtil.dipToPx(getContext(), 50));
        mPathMeasure.setPath(mPath, true);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath,mPaint);
    }

    public void start() {
        if (getChildCount() > 0) {
            if (mObjectAnimator == null) {
                mObjectAnimator = ObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
                mObjectAnimator.setRepeatCount(-1);
                mObjectAnimator.setInterpolator(new PathInterpolator(mPath));
                mObjectAnimator.setDuration(5000);
            } else {
                mObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
                mObjectAnimator.setInterpolator(new PathInterpolator(mPath));
            }
            mObjectAnimator.start();
        }
    }
}
