package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * 只解决了但类型复用的问题,多类型复用得重新指定一个好的方案
 */
public abstract class CommonViewPagerAdapter<T> extends PagerAdapter {

    //显示的数据  
    private List<T> datas = null;

    private LinkedList<View> mViewCache = null;

    private Context mContext;

    private LayoutInflater mLayoutInflater = null;
    private int layoutId;


    public CommonViewPagerAdapter(List<T> datas, Context context, int layoutID) {
        super();
        this.datas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mViewCache = new LinkedList<>();
        this.layoutId = layoutID;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViewCache.size() == 0) {
            convertView = this.mLayoutInflater.inflate(layoutId, null, false);
            viewHolder = new ViewHolder(mContext, convertView, container, position);
        } else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.updatePosition(position);
        }
        convert(viewHolder, position, datas.get(position));
        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
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