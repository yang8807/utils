package com.yan.fastview_library.viewgroup.viewpagers;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.InfiniteViewPagerAdapter;
import com.yan.fastview_library.R;

import java.util.ArrayList;
import java.util.List;

import utils.ViewPagerScroller;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 * 配合无限循环的适配器使用
 */
public class InfiniteLooperView<T> extends ViewPager {

    private InfiniteViewPagerAdapter<T> bannerAdapter;
    private Handler mHandler = new Handler();
    private int mDwellTime = 1500;
    private int mSwitchTime = 1000;
    //定时切换页面的任务
    private SwitchPagerRunnable mSwitchTask;
    private int index = 0;
    //是否开启自动播放
    private boolean willAutoScroll = true;
    //这样所有设置的OnPagerListener都能正常使用了
    private ArrayList<OnPageChangeListener> externalOnPagerListeners = new ArrayList<>();
    private List<T> imageUrls = new ArrayList<>();
    //需要设置的item
    private int switchItem;

    public InfiniteLooperView(Context context) {
        this(context, null);
    }

    public InfiniteLooperView(Context context, AttributeSet attrs) {
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
        this.externalOnPagerListeners.add(listener);
    }

    public void setImageUrls(int layoutid, @NonNull final LoopAdapterListener<T> listener, T... ts) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < ts.length; i++) {
            list.add(ts[i]);
        }
        setImageUrls(layoutid, listener, list);
    }

    public void setImageUrls(int layoutid, @NonNull final LoopAdapterListener<T> listener, List<T> imageUrls) {
        this.imageUrls = imageUrls;
        bannerAdapter = new InfiniteViewPagerAdapter<T>(imageUrls, getContext(), layoutid) {
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
        if (imageUrls != null && imageUrls.size() > 1) {
            //设置从角标0 开始
            index = Integer.MAX_VALUE / 2;
            index = index - index % imageUrls.size();
            super.setCurrentItem(index);
        }

        if (willAutoScroll) {//需要的时候才需要开启定时切换的任务
            if (mSwitchTask == null) {
                mSwitchTask = new SwitchPagerRunnable();
            } else {
                mHandler.removeCallbacks(mSwitchTask);
            }
            mHandler.postDelayed(mSwitchTask, mDwellTime);
        }
    }

    /**
     * pager切换事件
     */
    private OnPageChangeListener onPagerListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (!externalOnPagerListeners.isEmpty()) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageScrolled(position % imageUrls.size(), positionOffset, positionOffsetPixels);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            if (!externalOnPagerListeners.isEmpty()) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageSelected(index % imageUrls.size());
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //拖动状态也进行页面设置,就避免了到尾部
            if (!externalOnPagerListeners.isEmpty()) {
                for (int i = 0; i < externalOnPagerListeners.size(); i++) {
                    externalOnPagerListeners.get(i).onPageScrollStateChanged(state);
                }
            }
        }
    };

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
            mHandler.removeCallbacks(mSwitchTask);
            if (switchItem != 0) {
                if (switchItem > 0) {
                    --switchItem;
                    ++index;
                } else if (switchItem < 0) {
                    ++switchItem;
                    --index;
                }
                InfiniteLooperView.super.setCurrentItem(index);
                mHandler.postDelayed(this, mSwitchTime / 5);
            } else {
                ++index;
                InfiniteLooperView.super.setCurrentItem(index);
                mHandler.postDelayed(this, mDwellTime);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwitchTask);
        mHandler = null;
        mSwitchTask = null;
    }

    @Override
    public void setCurrentItem(int item) {
        if ( switchItem != 0)
            return;
        this.switchItem = item - index % imageUrls.size();
        mHandler.post(mSwitchTask);
    }

    /**
     * 将数据回调的接口
     */
    public interface LoopAdapterListener<T> {
        public void convert(ViewHolder viewHolder, int position, T t);

        public void onPreCreate(ViewHolder viewHolder);
    }
}
