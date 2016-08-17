package com.yan.fastview_library.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.data.ColorUtils;
import com.yan.fastview_library.R;

/**
 * Created by heinigger on 16/8/16.
 */
public class BannerLooperIndicator extends View {
    private int mChildCount;
    private int index;
    private ViewPager externalViewPager;
    //view之间的间距
    private int mIntervalPadding = DeviceUtil.dipToPx(getContext(), 10);
    //horizontal =0,vertical=1
    private int mOriention;
    private int mPheight, mPwidth;
    private int mScrolleWidth;
    private Drawable mNormalDrawable, mSelectDrawable;
    private int mDefaultDrawableSize = DeviceUtil.dipToPx(getContext(), 15);
    private int mDrawableSize;
    private int itemSize = 0;

    public BannerLooperIndicator(Context context) {
        this(context, null);
    }

    public BannerLooperIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperties(context.obtainStyledAttributes(attrs, R.styleable.BannerLooperIndicator));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPwidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mPheight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        itemSize = mDrawableSize + mIntervalPadding * 2;
    }

    private void initProperties(TypedArray typedArray) {
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BannerLooperIndicator_android_orientation) {

            } else if (attr == R.styleable.BannerLooperIndicator_bli_interval_padding) {
                mIntervalPadding = (int) typedArray.getDimension(attr, mIntervalPadding);
            } else if (attr == R.styleable.BannerLooperIndicator_bli_normal) {
                int color = typedArray.getColor(attr, 0);
                mNormalDrawable = ColorUtils.isColor(color) ? createGriandDrawable(color) : typedArray.getDrawable(attr);
            } else if (attr == R.styleable.BannerLooperIndicator_bli_select) {
                int color = typedArray.getColor(attr, 0);
                mSelectDrawable = ColorUtils.isColor(color) ? createGriandDrawable(color) : typedArray.getDrawable(attr);
            } else {
                mDrawableSize = (int) typedArray.getDimension(attr, mDefaultDrawableSize);
            }
        }
        if (mDrawableSize != 0) {
            if (mNormalDrawable != null)
                mNormalDrawable.setBounds(0, 0, mDrawableSize, mDrawableSize * mNormalDrawable.getIntrinsicHeight() / mNormalDrawable.getIntrinsicWidth());
            if (mSelectDrawable != null)
                mSelectDrawable.setBounds(0, 0, mDrawableSize, mDrawableSize * mSelectDrawable.getIntrinsicHeight() / mSelectDrawable.getIntrinsicWidth());
        } else {
            if (mNormalDrawable != null)
                mNormalDrawable.setBounds(0, 0, mDefaultDrawableSize, mDefaultDrawableSize);
            if (mSelectDrawable != null)
                mSelectDrawable.setBounds(0, 0, mDefaultDrawableSize, mDefaultDrawableSize);
            mDrawableSize = mDefaultDrawableSize;
        }
        typedArray.recycle();
    }

    private Drawable createGriandDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setDither(true);
        gradientDrawable.setColor(color);
        gradientDrawable.setBounds(0, 0, mDefaultDrawableSize, mDefaultDrawableSize);
        return gradientDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int startY = (mPheight - mDrawableSize) / 2;
        int startX = (mPwidth - itemSize * mChildCount) / 2;
        canvas.save();//保存画布的状态
        canvas.translate(startX += mIntervalPadding, startY);
        for (int i = 0; i < mChildCount; i++) {
            mNormalDrawable.draw(canvas);
            canvas.translate(mIntervalPadding * 2 + mDrawableSize, 0);
        }
        canvas.restore();//恢复画布的状态,原点在位置0
        canvas.translate(startX += mScrolleWidth, startY);
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
                mScrolleWidth = (int) ((position) * itemSize + positionOffset * itemSize);
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
