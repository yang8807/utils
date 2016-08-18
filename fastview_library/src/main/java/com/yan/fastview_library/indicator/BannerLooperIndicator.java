package com.yan.fastview_library.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtil;
import com.magnify.yutils.data.ColorUtils;
import com.yan.fastview_library.R;

/**
 * Created by heinigger on 16/8/16.
 */
public class BannerLooperIndicator extends View {
    //子item的个数
    private int mChildCount;
    private ViewPager externalViewPager;
    //view之间的间距
    private int mIntervalPadding = DeviceUtil.dipToPx(getContext(), 5);
    //horizontal =0,vertical=1
    private int mOriention;
    //父高度,父宽度
    private int mPheight, mPwidth;
    //view滚动位置
    private int mScrolleWidth;
    //常规状态下,选中状态下
    private Drawable mNormalDrawable, mSelectDrawable;
    //view的大小,包括drawable和他的两个间距
    private int itemSize = 0;
    private int startX;
    //常规view的大小
    private int mNormalSize;
    //选中view的大小
    private int mSelectSize;

    public BannerLooperIndicator(Context context) {
        this(context, null);
    }

    public BannerLooperIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNormalSize = mSelectSize = DeviceUtil.dipToPx(getContext(), 10);
        initProperties(context.obtainStyledAttributes(attrs, R.styleable.BannerLooperIndicator));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的测量规则:
//        widthMeasureSpec== MeasureSpec.AT_MOST


        mPwidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mPheight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        itemSize = mNormalSize + mIntervalPadding * 2;
        startX = (mPwidth - itemSize * mChildCount) / 2;
//        setMa
    }

    private void initProperties(TypedArray typedArray) {
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BannerLooperIndicator_android_orientation) {
                mOriention = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.BannerLooperIndicator_bli_interval_padding) {
                mIntervalPadding = (int) typedArray.getDimension(attr, mIntervalPadding);
            } else if (attr == R.styleable.BannerLooperIndicator_bli_normal) {
                try {
                    int color = typedArray.getColor(attr, 0);
                    mNormalDrawable = ColorUtils.isColor(color) ? createGriandDrawable(color) : typedArray.getDrawable(attr);
                } catch (Exception e) {
                    e.printStackTrace();
                    mNormalDrawable = typedArray.getDrawable(attr);
                }
            } else if (attr == R.styleable.BannerLooperIndicator_bli_select) {
                try {
                    int color = typedArray.getColor(attr, 0);
                    mSelectDrawable = ColorUtils.isColor(color) ? createGriandDrawable(color) : typedArray.getDrawable(attr);
                } catch (Exception e) {
                    e.printStackTrace();
                    mSelectDrawable = typedArray.getDrawable(attr);
                }
            } else if (attr == R.styleable.BannerLooperIndicator_bli_indicator_normal_size) {
                mNormalSize = (int) typedArray.getDimension(attr, mNormalSize);
            } else if (attr == R.styleable.BannerLooperIndicator_bli_indicator_select_size) {
                mSelectSize = (int) typedArray.getDimension(attr, mSelectSize);
            }
        }
        if (mNormalDrawable == null) mNormalDrawable = createGriandDrawable(Color.WHITE);
        if (mSelectDrawable == null) mSelectDrawable = createGriandDrawable(Color.RED);
        mNormalDrawable.setBounds(0, 0, mNormalSize, mNormalSize * mNormalDrawable.getIntrinsicHeight() / mNormalDrawable.getIntrinsicWidth());
        mSelectDrawable.setBounds(0, 0, mSelectSize, mSelectSize * mSelectDrawable.getIntrinsicHeight() / mSelectDrawable.getIntrinsicWidth());
        typedArray.recycle();
    }

    private Drawable createGriandDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setDither(true);
        gradientDrawable.setColor(color);
        gradientDrawable.setBounds(0, 0, mNormalSize, mNormalSize);
        return gradientDrawable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int mLength = (int) event.getX() - startX;
        final int index = mLength / itemSize;
        if (index >= 0 && index <= mChildCount - 1) {
            post(new Runnable() {
                @Override
                public void run() {
                    externalViewPager.setCurrentItem(index);
                }
            });
            LogUtil.v("mine", "点击的位置:" + index);
        }

        //在这里处理一下点击事件
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int startY = (mPheight - mNormalSize) / 2;
        int startX = (mPwidth - itemSize * mChildCount) / 2;
        canvas.save();//保存画布的状态
        canvas.translate(startX += mIntervalPadding, startY);
        for (int i = 0; i < mChildCount; i++) {
            mNormalDrawable.draw(canvas);
            canvas.translate(mIntervalPadding * 2 + mNormalSize, 0);
        }
        canvas.restore();//恢复画布的状态,原点在位置0
        canvas.translate(startX += mScrolleWidth, (mPheight - mSelectSize * mSelectDrawable.getIntrinsicHeight() / mSelectDrawable.getIntrinsicWidth()) / 2);
        mSelectDrawable.draw(canvas);
    }

    public void setUpViewPager(ViewPager viewPager, int count) {
        mChildCount = count;
        externalViewPager = viewPager;
        externalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //在这里的一步,完全是为了处理无线循环动画而准备的,普通的viewPager基本不会受此影响
                if (position == mChildCount - 1 && positionOffset > 0.5) {
                    position = -1;
                }
                mScrolleWidth = (int) ((position) * itemSize + positionOffset * itemSize) - (mSelectSize - mNormalSize) / 2;
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                mScrolleWidth = position * itemSize - (mSelectSize - mNormalSize) / 2;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
