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
 * 当页面同时要出现三个tab的时候,就会出现问题
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
    private int mSwitchTime = 1000;
    //定时切换页面的任务
    private SwitchPagerRunnable mSwitchTask;
    private List<T> mImageUrls;
    //临时无用的数目
    public static final int TMEPCOUNT = 2;
    private int index = 0;
    //这样所有设置的OnPagerListener都能正常使用了
    private ArrayList<OnPageChangeListener> externalOnPagerListeners = new ArrayList<>();
    //上次选中的位置,因为多添加了两个item  的原因,导致部分位置会重复调用,这里处理一下,让其值调用一次
    private int lastPosition;
    //是否开启自动播放
    private boolean willAutoScroll = true;

    public BannerLoopView(Context context) {
        this(context, null);
    }

    public BannerLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.BannerLoopView);
        mDwellTime = tp.getInt(R.styleable.BannerLoopView_blv_dwell_time, mDwellTime);
        mSwitchTime = tp.getInt(R.styleable.BannerLoopView_blv_switch_time, mSwitchTime);
        willAutoScroll = tp.getBoolean(R.styleable.BannerLoopView_blv_willAutoScroll, willAutoScroll);
        tp.recycle();

        if (willAutoScroll) {//如果不需要自动切换，就不需要设置自动切换的速度了
            //设置页面之间切换的速度
            ViewPagerScroller pagerScroller = new ViewPagerScroller(getContext());
            pagerScroller.setScrollDuration(mSwitchTime);
            pagerScroller.initViewPagerScroll(this);
        }
        super.addOnPageChangeListener(onPagerListener);
    }


    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        //真的是superlistner,将这个添加到OnPagerListenner集合的问题
        //super.addOnPageChangeListener(listener);
        this.externalOnPagerListeners.add(listener);
    }

    /**
     * pager切换事件
     */
    private OnPageChangeListener onPagerListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int secondPosition = getNormalPosition(position);
            secondPosition -= 1;
            if (!externalOnPagerListeners.isEmpty()) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageScrolled(secondPosition, positionOffset, positionOffsetPixels);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            int secondPosition = index = getNormalPosition(position);
            secondPosition -= 1;
            if (!externalOnPagerListeners.isEmpty() && lastPosition != secondPosition) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageSelected(secondPosition);
                }
                lastPosition = secondPosition;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //拖动状态也进行页面设置,就避免了到尾部
            int currentPosition = getCurrentItem();
            //将任务放在这里执行,就能正常显示动画了
            if (currentPosition != index && (index == 1 || index == mImageUrls.size() - BannerLoopView.TMEPCOUNT)) {
                if ((state == SCROLL_STATE_IDLE || state == SCROLL_STATE_DRAGGING)) {
                    BannerLoopView.super.setCurrentItem(index, false);
                }
            }
            if (!externalOnPagerListeners.isEmpty()) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageScrollStateChanged(state);
                }
            }
        }
    };

    /**
     * 获取正常的角标数据
     */
    private int getNormalPosition(int position) {
        int secondPosition = position;
        if (position == mImageUrls.size() - 1) {//当是最后一个的时候,设置为第二个,之前将事件放在这里执行,两个任务同时执行,导致没有动画
            secondPosition = 1;
        } else if (position == 0) {
            secondPosition = mImageUrls.size() - BannerLoopView.TMEPCOUNT;
        }
        return secondPosition;
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
            protected void onPreCreate(ViewHolder viewHolder) {
                listener.onPreCreate(viewHolder);
            }

            @Override
            protected void convert(ViewHolder viewHolder, int position, T o) {
                listener.convert(viewHolder, position, o);
            }
        };
        setAdapter(bannerAdapter);
        super.setCurrentItem(1);
        if (willAutoScroll) {//需要的时候才需要开启定时切换的任务
            if (mSwitchTask == null) {
                mSwitchTask = new SwitchPagerRunnable();
            } else {
                mHandler.removeCallbacks(mSwitchTask);
            }
            mHandler.postDelayed(mSwitchTask, mDwellTime);
        }
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
            BannerLoopView.super.setCurrentItem(index);
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
     * 还要调用这个,外界在使用的使用的时候,才能正常使用
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item + 1);
    }

    /**
     * 将数据回调的接口
     */
    public interface LoopAdapterListener<T> {
        public void convert(ViewHolder viewHolder, int position, T t);

        public void onPreCreate(ViewHolder viewHolder);
    }
}
