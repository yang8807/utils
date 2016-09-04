package com.yan.fastview_library.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.R;

/**
 * Created by heinigger on 16/9/2.
 * 设置clipChildrn,设置的动画就可以超出父控件进行运动
 */
public class ThrowAnimationLayout extends RelativeLayout {

    private Path mPath = new Path();
    private ObjectAnimator mObjectAnimator;
    private Paint mPaint;
    private int type = 0;
    private int height;

    public ThrowAnimationLayout(Context context) {
        this(context, null);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThrowAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        height = DeviceUtil.dipToPx(getContext(), 400);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        iniProperties(context, attrs);
    }

    private void iniProperties(Context context, AttributeSet attrs) {
        TypedArray tps = context.obtainStyledAttributes(attrs, R.styleable.ThrowAnimationLayout);
        int count = tps.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tps.getIndex(i);
            if (attr == R.styleable.ThrowAnimationLayout_beizier) {
                type = tps.getInt(attr, 0);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPath.reset();
        //两次贝塞尔曲线
        if (type == 0) {
            mPath.moveTo(0, getMeasuredHeight() * 2 / 3);
            mPath.quadTo(getMeasuredWidth() / 2, 0, getMeasuredWidth(), getMeasuredHeight() * 2 / 3);
        } else {
            //塞次贝塞尔曲线
            mPath.moveTo(0, getMeasuredHeight() * 2 / 3);
            mPath.cubicTo(getMeasuredWidth() / 10, -height, getMeasuredWidth() / 10 * 9, -height, getMeasuredWidth(), getMeasuredHeight() * 2 / 3);
        }
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
