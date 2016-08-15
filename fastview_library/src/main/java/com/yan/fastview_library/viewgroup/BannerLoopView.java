package com.yan.fastview_library.viewgroup;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonViewPagerAdapter;
import com.magnify.yutils.LogUtil;
import com.yan.fastview_library.R;

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
public class BannerLoopView extends ViewPager {

    private BannerAdapter bannerAdapter;
    private Handler mHandler = new Handler();
    //在当前页面停留的时间
    private int mDuration = 2000;
    private SwitchPagerRunnable mSwitchTask;
    private List<String> mImageUrls;
    //临时无用的数目
    public static final int TMEPCOUNT = 2;
    private int index = 0;

    public BannerLoopView(Context context) {
        this(context, null);
    }

    public BannerLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewPagerScroller pagerScroller=new ViewPagerScroller(getContext());
        pagerScroller.setScrollDuration(1000);
        pagerScroller.initViewPagerScroll(this);
        this.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                LogUtil.v("mine", "position运行轨迹" + position);
                if (position == mImageUrls.size() - 1) {//当是最后一个的时候,设置为第二个,之前将事件放在这里执行,两个任务同时执行,导致没有动画
                    position = 1;
                    index = position;
                } else if (position == 0) {
                    position = mImageUrls.size() - BannerLoopView.TMEPCOUNT;
                    setCurrentItem(position, false);
                    index = position;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE && index == 1) {//将任务放在这里执行,就能正常显示动画了
                    setCurrentItem(index, false);
                }
            }
        });
    }

    public void setImageUrls(List<String> imageUrls) {
        this.mImageUrls = imageUrls;
        //添加到最后一个
        mImageUrls.add(mImageUrls.get(0));
        //添加最后一个有效的,加入第一个集合
        mImageUrls.add(0, mImageUrls.get(mImageUrls.size() - TMEPCOUNT));
        LogUtil.v("mine", "数量是" + mImageUrls.size());
        bannerAdapter = new BannerAdapter(imageUrls, getContext());
        this.setAdapter(bannerAdapter);
        mSwitchTask = new SwitchPagerRunnable();
        setCurrentItem(1);
        mHandler.postDelayed(mSwitchTask, mDuration);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.postDelayed(mSwitchTask, mDuration);
                break;
            default:
                mHandler.removeCallbacks(mSwitchTask);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private class BannerAdapter extends CommonViewPagerAdapter<String> {

        public BannerAdapter(List<String> datas, Context context) {
            super(datas, context, R.layout.item_banner_looper);
        }

        @Override
        protected void convert(ViewHolder viewHolder, int position, String s) {
            viewHolder.displayImage(s, R.id.imageView);
        }
    }

    private class SwitchPagerRunnable implements Runnable {

        @Override
        public void run() {
            ++index;
            setCurrentItem(index);
            mHandler.postDelayed(this, mDuration);
        }
    }
}
