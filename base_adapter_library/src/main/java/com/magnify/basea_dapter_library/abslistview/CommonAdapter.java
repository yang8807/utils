package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, null);
            holder = new ViewHolder(mContext, convertView, parent, position);
            onPreCreate(holder, position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.updatePosition(position);
        }
        convert(holder, position, getItem(position));
        return holder.getConvertView();
    }

    protected void onPreCreate(ViewHolder holder, int position) {

    }

    public abstract void convert(ViewHolder holder, int position, T t);

}
