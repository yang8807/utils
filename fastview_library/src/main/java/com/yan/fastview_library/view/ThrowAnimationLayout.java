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
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

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
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        setTopPoint(100, 100);
    }

    public void setTopPoint(float x, float y) {

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPath.reset();
//        mPath.moveTo(0, getMeasuredHeight());
//        mPath.quadTo(x, y, getMeasuredWidth(), getMeasuredHeight());
        mPath.moveTo(0, getMeasuredHeight()*2/3);
        mPath.quadTo(getMeasuredWidth() / 2, 0, getMeasuredWidth(), getMeasuredHeight()*2/3);
        mPathMeasure.setPath(mPath, true);
        start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    public void start() {
        if (getChildCount() > 0) {
            if (mObjectAnimator == null) {
                mObjectAnimator = ObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
                mObjectAnimator.setRepeatCount(-1);
                mObjectAnimator.setInterpolator(new DecelerateInterpolator());
                mObjectAnimator.setDuration(1500);
            } else {
                mObjectAnimator.ofFloat(getChildAt(0), View.X, View.Y, mPath);
            }
            mObjectAnimator.start();
        }
    }
}
