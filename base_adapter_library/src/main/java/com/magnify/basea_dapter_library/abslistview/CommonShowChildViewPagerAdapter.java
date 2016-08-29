package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 只解决了但类型复用的问题,多类型复用得重新指定一个好的方案
 */
public abstract class CommonShowChildViewPagerAdapter<P, C> extends PagerAdapter {

    //显示的数据
    private List<P> folders = null;
    private List<C> datas = new ArrayList<>();

    private int counter;

    private ArrayList<PositionInfo> positionInfos = new ArrayList<>();

    private LinkedList<View> mViewCache = null;

    private Context mContext;


    private LayoutInflater mLayoutInflater = null;
    private int layoutId;

    private int travseCount = 0;
    //
    private boolean hasGoTheDefineMethod = false;

    public CommonShowChildViewPagerAdapter(List<P> datas, Context context, int layoutID) {
        this.folders = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mViewCache = new LinkedList<>();
        this.layoutId = layoutID;
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
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViewCache.size() == 0) {
            convertView = this.mLayoutInflater.inflate(layoutId, null, false);
            onPreCreate(viewHolder, convertView);
            viewHolder = new ViewHolder(mContext, convertView, container, position);
        } else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.updatePosition(position);
        }
        convert(viewHolder, position, getParent(position), datas.get(position));
        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
    }

    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
    }

    protected abstract void convert(ViewHolder viewHolder, int position, P parent, C child);

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

    private P getParent(int i) {
        P p = null;
        for (int i1 = 0; i1 < positionInfos.size(); i1++) {
            PositionInfo positionInfo = positionInfos.get(i1);
            if (positionInfo.isRange(i)) {
                p = folders.get(i1);
                break;
            }
        }
        return p;
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