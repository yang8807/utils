package com.yan.fastview_library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.data.ImageUtils;
import com.yan.fastview_library.R;

/**
 * Created by heinigger on 16/9/2.
 * 抛物线动画,贝赛尔曲线
 */
public class ThrowAnimationView extends View {
    //绘制的画笔
    private Paint mPaint;
    //绘制的图片
    private Bitmap mDrawable;
    //绘制的位置
    private float[] mDrawPosition = new float[2];
    //动画效果差值器
    ValueAnimator mValueAnimator;
    private PathMeasure mPathMeasure = new PathMeasure();
    //绘制的运动路劲
    private Path mPath = new Path();
    //动画执行时间
    private int mDuration = 5000;
    //控制图片的大小
    private int mDrawableSize = DeviceUtil.dipToPx(getContext(), 10);
    //放大的比例
    private float scale;


    public ThrowAnimationView(Context context) {
        this(context, null);
    }

    public ThrowAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThrowAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniProperties(attrs, context);
        iniPaint();
        mValueAnimator = ValueAnimator.ofFloat(0, 0);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (value == 0) return;
                scale = value / mPathMeasure.getLength();
                mPathMeasure.getPosTan(value, mDrawPosition, null);
                invalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPath.reset();
        mPath.moveTo(0, getMeasuredHeight());
        mPath.quadTo(getMeasuredWidth() * 2 / 3, getMeasuredHeight() / 10, getMeasuredWidth(), getMeasuredHeight());
        mPathMeasure.setPath(mPath, true);
        int length = (int) mPathMeasure.getLength();
        mValueAnimator.setFloatValues(length, 0);
        if (length > 0)
            mValueAnimator.start();
    }

    private void iniProperties(AttributeSet attrs, Context context) {
        TypedArray tps = context.obtainStyledAttributes(attrs, R.styleable.ThrowAnimationView);
        int count = tps.getIndexCount();
//        ObjectAnimator.ofMultiFloat()
        for (int i = 0; i < count; i++) {
            int attr = tps.getIndex(i);
            if (attr == R.styleable.ThrowAnimationView_android_src) {
                Drawable drawable = tps.getDrawable(attr);
                mDrawable = ImageUtils.zoomBitmap(ImageUtils.drawable2Bitmap(drawable), mDrawableSize, mDrawableSize);
//                mDrawable = ImageUtils.drawable2Bitmap(drawable);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mDrawable, mDrawPosition[0] - mDrawable.getWidth() / 2, mDrawPosition[1] - mDrawable.getHeight() / 2, mPaint);
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }
}
