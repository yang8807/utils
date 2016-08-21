package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
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

    public BaseShowChildAdapter(List<P> folders, Context mContext, int layoutId) {
        this.folders = folders;
        this.mContext = mContext;
        this.layoutID = layoutId;
        positionInfos = new ArrayList<>();

        for (int i = 0; i < folders.size(); i++) {
            List<C> mImages = getChild(folders.get(i));
            if (mImages != null && !mImages.isEmpty()) {
                positionInfos.add(new PositionInfo(mImages.size(), counter));
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
        ViewHolder viewHolder = ViewHolder.get(mContext, view, viewGroup, layoutID, i);
        convert(viewHolder, view, i, getParent(i), getItem(i));
        return viewHolder.getConvertView();
    }

    protected abstract void convert(ViewHolder viewHolder, View convertView, int position, P parent, C child);

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

    private class PositionInfo {
        private int count;
        private int startPosition;

        public PositionInfo(int count, int startPosition) {
            this.count = count;
            this.startPosition = startPosition;
        }

        public boolean isRange(int i1) {
            return i1 >= startPosition && i1 < startPosition + count;
        }
    }
}
