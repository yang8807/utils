package com.yan.fastview_library.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.yan.fastview_library.R;

import java.util.ArrayList;
import java.util.List;

import utils.ViewPagerScroller;

/**
 * Created by heinigger on 16/8/15.
 * 广告栏轮播效果:2-0-1-2-0:实现无线循环的一种方法:
 * 实现原理 ：
 * 有三张图片，实现无限循环。在viewpager中设置5个view，第一个为三张图片的最后一张，第五张为三张图片的第一张。图片顺序如下数字：
 * 2-0-1-2-0
 * 0-1-2为正常的三个图片。2，0  为添加的两个图片view
 * 滑动的顺序：进入页面显示0图片，向右滑动到0时,将0页设置为0，则可以继续向右滑动。同理当向左滑动到  2 时，将2页设置为2。
 */
public class BannerLoopView<T> extends ViewPager {

    private CommonViewPagerAdapter<T> bannerAdapter;
    private Handler mHandler = new Handler();
    //在当前页面停留的时间 TODO:当时间为1000秒的时候,就会出现那种普通的效果,看起来不正常的一样,切换速度也是一样会影响
    private int mDwellTime = 1500;
    private int mSwitchTime = 1500;
    //定时切换页面的任务
    private SwitchPagerRunnable mSwitchTask;
    private List<T> mImageUrls;
    //临时无用的数目
    public static final int TMEPCOUNT = 2;
    private int index = 0;

    public BannerLoopView(Context context) {
        this(context, null);
    }

    public BannerLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.BannerLoopView);
        mDwellTime = tp.getInt(R.styleable.BannerLoopView_blv_dwell_time, mDwellTime);
        mSwitchTime = tp.getInt(R.styleable.BannerLoopView_blv_switch_time, mSwitchTime);
        tp.recycle();
        //设置页面之间切换的速度
        ViewPagerScroller pagerScroller = new ViewPagerScroller(getContext());
        pagerScroller.setScrollDuration(mSwitchTime);
        pagerScroller.initViewPagerScroll(this);
        addOnSelfPagerListener();
    }

    /**
     * 为自己添加切换事件
     */
    private void addOnSelfPagerListener() {
        this.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mImageUrls.size() - 1) {//当是最后一个的时候,设置为第二个,之前将事件放在这里执行,两个任务同时执行,导致没有动画
                    position = 1;
                } else if (position == 0) {
                    position = mImageUrls.size() - BannerLoopView.TMEPCOUNT;
                }
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //拖动状态也进行页面设置,就避免了到尾部
                if ((state == SCROLL_STATE_IDLE || state == SCROLL_STATE_DRAGGING) && (index == 1 || index == mImageUrls.size() - BannerLoopView.TMEPCOUNT)) {//将任务放在这里执行,就能正常显示动画了
                    setCurrentItem(index, false);
                }
            }
        });
    }

    public void setImageUrls(int layoutid, @NonNull final LoopAdapterListener<T> listener, T... ts) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < ts.length; i++) {
            list.add(ts[i]);
        }
        setImageUrls(layoutid, listener, list);
    }

    public void setImageUrls(int layoutid, @NonNull final LoopAdapterListener<T> listener, List<T> imageUrls) {
        this.mImageUrls = imageUrls;
        //添加到最后一个
        mImageUrls.add(mImageUrls.get(0));
        //添加最后一个有效的,加入第一个集合
        mImageUrls.add(0, mImageUrls.get(mImageUrls.size() - TMEPCOUNT));
        bannerAdapter = new CommonViewPagerAdapter<T>(imageUrls, getContext(), layoutid) {
            @Override
            protected void convert(ViewHolder viewHolder, int position, T o) {
                listener.convert(viewHolder, position, o);
            }
        };
        setAdapter(bannerAdapter);
        if (mSwitchTask == null) {
            mSwitchTask = new SwitchPagerRunnable();
        } else {
            mHandler.removeCallbacks(mSwitchTask);
        }
        setCurrentItem(1);
        mHandler.postDelayed(mSwitchTask, mDwellTime);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.postDelayed(mSwitchTask, mDwellTime);
                break;
            default:
                mHandler.removeCallbacks(mSwitchTask);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    private class SwitchPagerRunnable implements Runnable {

        @Override
        public void run() {
            ++index;
            setCurrentItem(index);
            mHandler.postDelayed(this, mDwellTime);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwitchTask);
        mHandler = null;
        mSwitchTask = null;
    }

    /**
     * 将数据回调的接口
     */
    public interface LoopAdapterListener<T> {
        public void convert(ViewHolder viewHolder, int position, T t);
    }
}
