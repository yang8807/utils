package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/8.
 */
public abstract class BaseMultiTypeAdapter<T> extends BaseAdapter {
    private ArrayList<T> datas;
    private Context mContext;
    private int[] layoutids;

    private LayoutInflater inflater;

    /*typecount不知道的情况下,直接进行数据的遍历*/
    public BaseMultiTypeAdapter(Context context, ArrayList<T> datas, int... layoutids) {
        this.datas = datas;
        this.mContext = context;
        this.layoutids = layoutids;
        inflater = LayoutInflater.from(context);

/*
        for (int i = 0; i < layoutids.length; i++) {
            int layoutId = layoutids[i];
            for (int i1 = i + 1; i1 < layoutids.length; i1++) {
                if (layoutId == layoutids[i1]) {
                    throw new IllegalArgumentException("you are supported use diffrent layout");
                }
            }
        }
*/
    }

    @Override
    public int getViewTypeCount() {
        return layoutids == null ? 0 : layoutids.length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(datas.get(position));
    }

    protected abstract int getItemViewType(T data);

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder[] viewHolder = new ViewHolder[layoutids.length];
        int itemType = getItemViewType(position);
        if (convertView == null) {
            convertView = inflater.inflate(layoutids[itemType], null);
            viewHolder[itemType] = new ViewHolder(mContext, convertView, parent, position);
            convertView.setTag(layoutids[itemType], viewHolder[itemType]);
        } else {
            viewHolder[itemType] = (ViewHolder) convertView.getTag(layoutids[itemType]);
        }
        converData(viewHolder[itemType], itemType, datas.get(position), position);

        return convertView;
    }

    protected abstract void converData(ViewHolder viewHolder, int itemType, T t, int position);
}
