package com.yan.fastview_library.viewgroup.sticky_gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.PositionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/29.
 */
public abstract class BaseStickAdapter<H, C> extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    protected List<H> mPDatas;
    private Context mContext;
    //头部布局
    private int headerLayoutID;
    //子布局
    private int childLayoiutID;

    private List<C> datas = new ArrayList<>();
    private ArrayList<PositionInfo> positionInfos = new ArrayList<>();

    private int counter;
    private int travseCount = 0;
    //
    private boolean hasGoTheDefineMethod = false;

    public BaseStickAdapter(List<H> mDatas, Context mContext, int headerLayoutID, int childLayoutId) {
        this.mPDatas = mDatas;
        this.mContext = mContext;
        this.headerLayoutID = headerLayoutID;
        this.childLayoiutID = childLayoutId;
        traverseDatas();
    }

    /**
     * 遍历数据
     */
    private void traverseDatas() {

        if (travseCount == 0) {
            positionInfos.clear();
            counter = 0;
            datas.clear();
        }

        for (int i = travseCount; i < mPDatas.size(); i++) {
            List<C> mImages = getChild(mPDatas.get(i));
            if (mImages != null && !mImages.isEmpty()) {
                positionInfos.add(new PositionInfo(mImages.size(), counter, i));
                counter += mImages.size();
                datas.addAll(mImages);
            }
        }
    }

    @Override
    public long getHeaderId(int position) {
        for (int i1 = 0; i1 < positionInfos.size(); i1++) {
            PositionInfo positionInfo = positionInfos.get(i1);
            if (positionInfo.isRange(position)) {
                position = i1;
                break;
            }
        }
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHeaderViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(headerLayoutID, parent, false);
            mHeaderViewHolder = new ViewHolder(mContext, convertView, parent, position);
            onPreHeaderCreate(mHeaderViewHolder, convertView);
        } else {
            mHeaderViewHolder = (ViewHolder) convertView.getTag();
        }

        convertHeaderView(mHeaderViewHolder, getParent(position), position);
        return convertView;
    }


    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public C getItem(int position) {
        return datas.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder mChildHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(childLayoiutID, viewGroup, false);
            mChildHolder = new ViewHolder(mContext, view, viewGroup, position);
            onPreChildCreate(mChildHolder, view);
        } else {
            mChildHolder = (ViewHolder) view.getTag();
        }
        convertChildView(mChildHolder, getParent(position), datas.get(position), position);
        return view;
    }


    /**
     * 子类数据的的绑定
     */
    protected abstract void convertChildView(ViewHolder mChildHolder, H mParent, C mChild, int position);

    /**
     * 第一次创建孩子的时候,可以进行动态更改
     */
    protected void onPreChildCreate(ViewHolder mChildHolder, View view) {
    }

    protected abstract List<C> getChild(H h);

    /**
     * 第一次创建头部的时候,可以进行动态更改
     */
    protected void onPreHeaderCreate(ViewHolder mHeaderViewHolder, View convertView) {

    }

    /**
     * 头部数据的绑定
     */
    protected abstract void convertHeaderView(ViewHolder mHeaderViewHolder, H h, int position);

    private H getParent(int i) {
        H p = null;
        for (int i1 = 0; i1 < positionInfos.size(); i1++) {
            PositionInfo positionInfo = positionInfos.get(i1);
            if (positionInfo.isRange(i)) {
                p = mPDatas.get(i1);
                break;
            }
        }
        return p;
    }

    public Context getContext() {
        return mContext;
    }

    public void setDatas(List<H> mFolders) {
        this.mPDatas = mFolders;
        travseCount = 0;
        hasGoTheDefineMethod = true;
        notifyDataSetChanged();
    }

    public void setDatas(H mFolder) {
        this.mPDatas = new ArrayList<>();
        this.mPDatas.add(mFolder);
        travseCount = 0;
        hasGoTheDefineMethod = true;
        notifyDataSetChanged();
    }

    public void addDatas(List<H> mFolders) {
        travseCount = this.mPDatas.size() - 1;
        if (mFolders != null && !mFolders.isEmpty()) {
            this.mPDatas.addAll(mFolders);
        }
        hasGoTheDefineMethod = true;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        if (!hasGoTheDefineMethod) travseCount = 0;//如果用户没有调用给的方法,只能强制重新遍历一遍
        traverseDatas();
        super.notifyDataSetChanged();
    }
}
