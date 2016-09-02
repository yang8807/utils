package com.yan.fastview_library.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtil;
import com.yan.fastview_library.R;

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
    private int mRippleColor = Color.RED;
    private int mRippleStorkWidth = 10;
    private ValueAnimator[] valueAnimators;
    //所需要绘制的圆环数量
    private int mRippleCount = 5;
    private int[] mdrawabRadius;
    private AnimatorSet animatorSet;
    //绘制的最小半径
    private int minRippleRadius;
    //执行一圈动画需要的时间
    private int mRippleDuration = 1000;


    public RippleCircleView(Context context) {
        this(context, null);
    }

    public RippleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniPaint();
        iniPropeties(context, attrs);
        valueAnimators = new ValueAnimator[mRippleCount];
        mPaints = new Paint[mRippleCount];
        mdrawabRadius = new int[mRippleCount];

        for (int i = 0; i < valueAnimators.length; i++) {
            mPaints[i] = iniPaint();
            ValueAnimator valueAnimator = ValueAnimator.ofInt(minRippleRadius, mRadius).setDuration(mRippleCount * mRippleDuration);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setStartDelay(i * 1000);
            final int finalI = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (mRadius == 0) return;
                    mdrawabRadius[finalI] = (int) valueAnimator.getAnimatedValue();
                    int alpha = (mRadius - mdrawabRadius[finalI]) * 255 / mRadius;
                    mPaints[finalI].setAlpha(alpha);
                    invalidate();
                    LogUtil.v("mine", "animation is still ");
                }
            });
            valueAnimators[i] = valueAnimator;
        }
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimators);
    }

    /**
     * 初始化属性
     */
    private void iniPropeties(Context context, AttributeSet attrs) {
        TypedArray tps = context.obtainStyledAttributes(attrs, R.styleable.RippleCircleView);
        int count = tps.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tps.getIndex(i);
            if (attr == R.styleable.RippleCircleView_minRippleRadius) {
                minRippleRadius = (int) tps.getDimension(attr, DeviceUtil.dipToPx(getContext(), 5));
            } else if (attr == R.styleable.RippleCircleView_mRippleColor) {
                mRippleColor = tps.getColor(attr, Color.RED);
            } else if (attr == R.styleable.RippleCircleView_mRippleStorkWidth) {
                mRippleStorkWidth = (int) tps.getDimension(attr, DeviceUtil.dipToPx(getContext(), 5));
            } else if (attr == R.styleable.RippleCircleView_mRippleCount) {
                mRippleCount = tps.getInt(attr, 4);
            } else if (attr == R.styleable.RippleCircleView_mRippleDuration) {
                mRippleDuration = tps.getInt(attr, 1000);
            }
        }

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
            valueAnimators[i].setIntValues(minRippleRadius, mRadius);
        }
        if (!animatorSet.isRunning() && mRadius != 0) animatorSet.start();
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        for (int i = 0; i < valueAnimators.length; i++) {
            canvas.drawCircle(mCenterX, mCenterY, mdrawabRadius[i], mPaints[i]);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        willStartAnimation(visibility == View.VISIBLE);
    }

    /**
     * 页面显示状态改变,停止动画
     */
    private void willStartAnimation(boolean isVisible) {
        if (valueAnimators != null && valueAnimators.length != 0) {
            for (int i = 0; i < valueAnimators.length; i++) {
                ValueAnimator valueAnimator = valueAnimators[i];
                if (valueAnimator != null) {
                    if (isVisible)
                        valueAnimator.start();
                    else
                        valueAnimator.cancel();
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {

        if (valueAnimators != null) {
            for (int i = 0; i < valueAnimators.length; i++) {
                if (valueAnimators[i] != null && valueAnimators[i].isRunning()) {
                    valueAnimators[i].addUpdateListener(null);
                    valueAnimators[i].cancel();
                    valueAnimators[i] = null;
                }
            }
            valueAnimators = null;
        }
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
            animatorSet = null;
        }
        super.onDetachedFromWindow();
    }
}
