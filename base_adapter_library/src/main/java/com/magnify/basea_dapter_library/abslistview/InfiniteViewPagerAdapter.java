package com.magnify.basea_dapter_library.abslistview;

/**
 * Created by ${洒笑天涯} on 2016/9/3.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * 无限循环适配器,因为在同一页面要显示三个的话,真适配器会出现问题:具体看所谓真适配器的实现原理
 * 这里的适配器,View是复用的,所以不会出现一只new的情况,是较之真适配比较稳定的.
 * (如果view不复用,还是使用真是配#link{BannerLooperView,就是使用针适配器的方式})
 * 只解决了但类型复用的问题,多类型复用得重新指定一个好的方案
 * <p>
 * 那么ViewPager需要将item设置在最中间,才能实现无限滑动:假性无限,因为数字特别大,大到可以忽略
 */
public abstract class InfiniteViewPagerAdapter<T> extends PagerAdapter {

    //显示的数据
    private List<T> datas = null;

    private LinkedList<View> mViewCache = null;

    private Context mContext;

    private LayoutInflater mLayoutInflater = null;
    private int layoutId;


    public InfiniteViewPagerAdapter(List<T> datas, Context context, int layoutID) {
        super();
        this.datas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mViewCache = new LinkedList<>();
        this.layoutId = layoutID;
    }

    @Override
    public int getCount() {
        return datas == null || datas.isEmpty() ? 0 : datas.size() == 1 ? 1 : Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % datas.size();

        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViewCache.size() == 0) {
            convertView = this.mLayoutInflater.inflate(layoutId, null, false);
            viewHolder = new ViewHolder(mContext, convertView, container, position);
            onPreCreate(viewHolder);
        } else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.updatePosition(position);
        }
        convert(viewHolder, position, datas.get(position));
        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
    }

    protected void onPreCreate(ViewHolder viewHolder) {
    }

    protected abstract void convert(ViewHolder viewHolder, int position, T t);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}