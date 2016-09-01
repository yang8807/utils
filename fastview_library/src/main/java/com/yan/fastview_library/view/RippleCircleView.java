package com.yan.fastview_library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.magnify.yutils.LogUtils;

/**
 * Created by heinigger on 16/9/1.
 */
public class RippleCircleView extends View {
    //最外层的动画直径
    private int mRadius;
    //开始绘制的位置
    private int mCenterX;
    private int mCenterY;
    //绘制的画笔
    private Paint[] mPaints;
    private int mRippleColor = Color.BLUE;
    private int mRippleStorkWidth = 10;
    private ValueAnimator[] valueAnimators;
    //所需要绘制的圆环数量
    private int mRippleCount = 5;
    private int[] mdrawabRadius;


    public RippleCircleView(Context context) {
        this(context, null);
    }

    public RippleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniPaint();
        valueAnimators = new ValueAnimator[mRippleCount];
        mPaints = new Paint[mRippleCount];
        mdrawabRadius = new int[mRippleCount];
    }

    private Paint iniPaint() {
        Paint mPaint = new Paint();
        mPaint.setColor(mRippleColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRippleStorkWidth);
        return mPaint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        int measureWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
        mRadius = Math.min(measureHeight, measureWidth) / 2 - mRippleStorkWidth;
        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;

        for (int i = 0; i < valueAnimators.length; i++) {
            mPaints[i] = iniPaint();
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mRadius).setDuration(5000);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setStartDelay(i * 1000);
            valueAnimator.start();
            final int finalI = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mdrawabRadius[finalI] = (int) valueAnimator.getAnimatedValue();
                    int alpha = (mRadius - mdrawabRadius[finalI]) * 255 / mRadius;
                    mPaints[finalI].setAlpha(alpha);
                    invalidate();
                    LogUtils.v("mine", mdrawabRadius + ":width  " + "alpha:" + alpha);
                }
            });
            valueAnimators[i] = valueAnimator;

        }

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        for (int i = 0; i < valueAnimators.length; i++) {
            canvas.drawCircle(mCenterX, mCenterY, mdrawabRadius[i], mPaints[i]);
        }
    }
}
