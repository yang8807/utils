package com.yan.fastview_library.viewgroup.base_swpie;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.magnify.basea_dapter_library.ViewHolder;

import java.util.List;


/**
 * Created by heinigger on 16/9/5.
 */
public abstract class CommonBaseSwipeAdapter<T> extends BaseSwipeAdapter {
    private List<T> datas;
    //滑动的布局
    private int mSwipeLayout;
    //滑动的SwipeLayout的id
    private int mSwipeID;
    private Context mContext;
    private int width;

    public CommonBaseSwipeAdapter(@LayoutRes int mSwipeLayout, @IdRes int mSwipeID, List<T> datas, Context mContext) {
        this.datas = datas;
        this.mSwipeLayout = mSwipeLayout;
        this.mSwipeID = mSwipeID;
        this.mContext = mContext;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        SwipeLayout parentView = (SwipeLayout) inflater.inflate(mSwipeLayout, parent, false);
        ViewHolder mViewHolder = new ViewHolder(mContext, parentView, parent, position);
        onPreCreate(mViewHolder);
        return mViewHolder.getConvertView();
    }

    protected void onPreCreate(ViewHolder mViewHolder) {
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return mSwipeID;
    }


    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder mViewHolders = (ViewHolder) convertView.getTag();
        convert(position, mViewHolders, convertView, datas.get(position));
    }


    protected abstract void convert(int position, ViewHolder mViewHolders, View convertView, T item);

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void closeAndNotOpenAgain(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        width = lp.width;
        lp.width = 0;
        view.requestLayout();
    }

    public void openAgain(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.requestLayout();
    }
}
