package com.yan.fastview_library.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.yan.fastview_library.R;


/**
 * Created by 洒笑天涯 on 2016/2/26.;
 * https://github.com/heinigger/LoadingView.git
 */
public class LoadingView extends View {

    //小圆的半径，大圆的半径
    private int mRadius = dip2px(25), nRadius;
    private int mParentWidth;
    //所需要绘制圆的个数
    private int mCount = 8;
    //每个圆之间间隔的角度
    private double mAngle;
    //绘制的画笔
    private Paint mPaint;
    private int[] mColorList = new int[]{getColor(R.color.loading_1), getColor(R.color.loading_2),
            getColor(R.color.loading_3), getColor(R.color.loading_4), getColor(R.color.loading_5),
            getColor(R.color.loading_6), Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.GRAY,
            Color.DKGRAY, Color.LTGRAY};
    //剩余的宽度,为了让小圆都往中间靠.就是收拢的距离
    private int surplusWidth;
    //持续时间
    private int rotatioDuration = 1200, radiusDuration = 1200;
    //收拢比例
    private float surplusRatio = 0.5f;
    //大圆半径动画
    private ValueAnimator radiusAnimator;
    //view的旋转动画
    private ObjectAnimator rotateAnimator;
    private int color;

    private AnimatorSet mAnimatorSet;

    private boolean isStop = false;

    private boolean isFirstMeasure = true;

    public LoadingView(Context context) {
        super(context);
    }

    private int dip2px(int value) {
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getProperties(context, attrs);
        iniPaint();
        iniRotationAnimator();
    }

    public void setColorList(int[] mColorList) {
        this.mColorList = mColorList;
        mCount = mColorList.length;
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getWidth() != 0 && mParentWidth == 0)
            mParentWidth = getWidth();
        nRadius = mParentWidth / 2;
        mAngle = 2 * Math.PI / mCount;
        if (isFirstMeasure) {
            initValueAnimator();
            isFirstMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mCount; i++) {
            mPaint.setColor(mColorList[i % mColorList.length]);
            canvas.drawCircle((float) (nRadius + (nRadius - mRadius - surplusWidth) * Math.cos(mAngle * i)), (float)
                    (nRadius + (nRadius - mRadius - surplusWidth) * Math.sin(mAngle * i)), mRadius, mPaint);
        }
    }

    /***
     * @return 对旋转动画进行初始化
     */
    private void iniRotationAnimator() {
        rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setDuration(rotatioDuration);
        rotateAnimator.setRepeatCount(-1);
    }

    /***
     * @return 初始化插值动画
     */
    private void initValueAnimator() {
        radiusAnimator = ValueAnimator.ofInt(mParentWidth / 2, (int) (mParentWidth / 2 * surplusRatio), mParentWidth
                / 2);
        radiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        radiusAnimator.setDuration(radiusDuration);
        radiusAnimator.setRepeatCount(-1);
        radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isStop == true) {
                    return;
                }
                surplusWidth = mParentWidth / 2 - (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        startAnimation();
    }

    public void getProperties(Context context, AttributeSet attrs) {
        TypedArray tpArray = null;
        try {
            tpArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            int count = tpArray.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = tpArray.getIndex(i);
                if (attr == R.styleable.LoadingView_lv_colorlist) {
                    int colorIDs = tpArray.getResourceId(attr, 0);

                } else if (attr == R.styleable.LoadingView_lv_count) {
                    mCount = tpArray.getInteger(attr, 6);

                } else if (attr == R.styleable.LoadingView_surplusRatio) {
                    surplusRatio = tpArray.getFloat(attr, 0.5f);

                } else if (attr == R.styleable.LoadingView_lv_radius) {
                    mRadius = (int) tpArray.getDimension(attr, dip2px(10));

                } else if (attr == R.styleable.LoadingView_lv_rotation_duration) {
                    rotatioDuration = tpArray.getInt(attr, 1200);

                } else if (attr == R.styleable.LoadingView_lv_radius_duration) {
                    radiusDuration = tpArray.getInt(attr, 1200);

                } else if (attr == R.styleable.LoadingView_lv_diameter) {
                    mParentWidth = (int) tpArray.getDimension(attr, dip2px(35));
                }
            }
        } finally {
            tpArray.recycle();
            tpArray = null;
        }
    }

    public void stopAnimation() {
        mAnimatorSet.cancel();
        isStop = true;
    }

    public void startAnimation() {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(radiusAnimator).with(rotateAnimator);
        mAnimatorSet.start();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public int getColor(int color) {
        return getContext().getResources().getColor(color);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
        if (rotateAnimator != null && rotateAnimator.isRunning()) {
            rotateAnimator.cancel();
            rotateAnimator = null;
        }
        if (radiusAnimator != null && radiusAnimator.isRunning()) {
            radiusAnimator.cancel();
            radiusAnimator = null;
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

}
