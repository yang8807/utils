package com.yan.fastview_library.viewgroup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.R;
import com.yan.fastview_library.aniamtion.BezierEvaluator;

import java.util.LinkedList;
import java.util.Random;

public class FavoriateLayout extends RelativeLayout {
    //差值器
    private Interpolator[] interpolators;
    private int mHeight;
    private int mWidth;
    private LayoutParams lp;
    //用于生成随机颜色
    private Random random = new Random();
    //图片的高
    private int dHeight;
    //图片的宽
    private int dWidth;
    private Random mRandom;
    //在屏幕中的位置
    private int[] mScreenLocations = new int[2];
    //展示的图片
    private Drawable mDrawable;
    //ImageView的回收利用
    private LinkedList<ImageView> mViewCache = new LinkedList<>();
    //图片的大小
    private int mDrawableSize;

    public FavoriateLayout(Context context) {
        this(context, null);
    }

    public FavoriateLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    public FavoriateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(false);
        setFocusable(false);

        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.FavoriateLayout);
        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.FavoriateLayout_android_src) {
                mDrawable = tp.getDrawable(attr);
            } else if (attr == R.styleable.FavoriateLayout_src_size) {
                mDrawableSize = (int) tp.getDimension(attr, 30);
            }
        }

        dHeight = mDrawable.getIntrinsicHeight();
        dWidth = mDrawable.getIntrinsicWidth();
        if (mDrawableSize > 0)
            lp = new LayoutParams(mDrawableSize, mDrawableSize);
        else
            lp = new LayoutParams(dWidth, dHeight);
        mRandom = new Random();
        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = new LinearInterpolator();// 线性
        interpolators[1] = new AccelerateInterpolator(); // 加速
        interpolators[2] = new DecelerateInterpolator();// 减速
        interpolators[3] = new AccelerateDecelerateInterpolator(); // 先加速后减速
    }

    /**
     * 从哪个地方冒出来
     */
    public void startAnimation(View target) {
        target.getLocationInWindow(mScreenLocations);
        this.mWidth = mScreenLocations[0] + target.getMeasuredWidth() / 2;
        this.mHeight = mScreenLocations[1] + target.getMeasuredHeight() / 2 - (DeviceUtil.getScreenHeight(getContext()) - getMeasuredHeight());
        ImageView imageView = null;
        if (mViewCache.size() > 0)
            imageView = mViewCache.remove();
        if (imageView == null)
            imageView = new ImageView(getContext());
        // 随机选一个
        imageView.setImageDrawable(mDrawable);
        imageView.setColorFilter(Color.argb(255, mRandom.nextInt(160) + 80, mRandom.nextInt(200) + 50, mRandom.nextInt(160) + 80));
        imageView.setLayoutParams(lp);
        imageView.setVisibility(View.GONE);
        addView(imageView);
        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Animator getAnimator(View target) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private ValueAnimator getBezierValueAnimator(View target) {
        // 初始化一个贝塞尔计算器- - 传入
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        // 这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth * 2 - dWidth) / 2, mHeight - dHeight),
                new PointF(random.nextInt(((int) mWidth * 2)), 0));
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(2500);
        return animator;
    }

    /**
     * 获取中间的两个点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(Math.abs(mWidth * 2 - 100));// 减去100 是为了控制 x轴活动范围,看效果 随意~~
        // 再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些 也可以用其他方法
        pointF.y = random.nextInt((mHeight)) / 2;
        return pointF;
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (target.getX() != 0 && target.getY() != 0 && target.getVisibility() == View.GONE) {
                target.setVisibility(View.VISIBLE);
            }
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉,并加入回收队列中
            removeView((target));
            mViewCache.add((ImageView) target);
        }
    }
}
