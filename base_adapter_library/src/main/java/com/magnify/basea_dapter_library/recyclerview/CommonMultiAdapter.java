package com.magnify.basea_dapter_library.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.List;

/**
 * viewType
 */
public abstract class CommonMultiAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int[] mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;


    public CommonMultiAdapter(Context context, List<T> datas, int... layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId[viewType], -1);
        viewHolder.setType(viewType);
        onPreCreate(viewHolder, viewType);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(mDatas.get(position));
    }

    protected abstract int getItemViewType(T t);

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        converData(holder, holder.getType(), mDatas.get(position), position);
    }

    protected abstract void converData(ViewHolder holder, int viewType, T t, int position);

    /*加载之前,先该改变view的大小,一次改变就好,在这里*/
    protected void onPreCreate(ViewHolder viewHolder, int itemType) {

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


}
