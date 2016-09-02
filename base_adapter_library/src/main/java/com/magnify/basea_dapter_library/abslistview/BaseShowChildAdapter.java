package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public abstract class BaseShowChildAdapter<P, C> extends BaseAdapter {
    private List<P> folders;
    private Context mContext;
    private List<C> datas = new ArrayList<>();
    private ArrayList<PositionInfo> positionInfos = new ArrayList<>();

    private int counter;
    private int layoutID;
    private int travseCount = 0;
    //
    private boolean hasGoTheDefineMethod = false;

    public BaseShowChildAdapter(List<P> folders, Context mContext, int layoutId) {
        this.folders = folders;
        this.mContext = mContext;
        this.layoutID = layoutId;
        positionInfos = new ArrayList<>();

        traverseDatas();
    }

    public List<P> getParentData() {
        return folders;
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

        for (int i = travseCount; i < folders.size(); i++) {
            List<C> mImages = getChild(folders.get(i));
            if (mImages != null && !mImages.isEmpty()) {
                positionInfos.add(new PositionInfo(mImages.size(), counter, i));
                counter += mImages.size();
                datas.addAll(mImages);
            }
        }
    }

    protected abstract List<C> getChild(P p);

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public C getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(layoutID, null);
            viewHolder = new ViewHolder(mContext, view, viewGroup, i);
            view = viewHolder.getConvertView();
            onPreCreate(viewHolder, view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.updatePosition(i);
        }
        convert(viewHolder, view, i, getParent(i), getItem(i));
        return viewHolder.getConvertView();
    }

    //当View创建的时候,部分需要动态改变他的大小
    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
    }

    protected abstract void convert(ViewHolder viewHolder, View convertView, int position, P parent, C child);

    private P getParent(int i) {
        P p = null;

//        count = positionInfos.size() % 2 == 0 ? positionInfos.size() / 2 : positionInfos.size() + 1;
        for (int i1 = 0; i1 < positionInfos.size(); i1++) {
            PositionInfo positionInfo = positionInfos.get(i1);
            if (positionInfo.isRange(i)) {
                p = folders.get(i1);
                break;
            }
        }
        return p;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setDatas(List<P> mFolders) {
        this.folders = mFolders;
        travseCount = 0;
        hasGoTheDefineMethod = true;
        notifyDataSetChanged();
    }

    public void setDatas(P mFolder) {
        this.folders = new ArrayList<>();
        this.folders.add(mFolder);
        travseCount = 0;
        hasGoTheDefineMethod = true;
        notifyDataSetChanged();
    }

    public void addDatas(List<P> mFolders) {
        travseCount = this.folders.size() - 1;
        if (mFolders != null && !mFolders.isEmpty()) {
            this.folders.addAll(mFolders);
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
