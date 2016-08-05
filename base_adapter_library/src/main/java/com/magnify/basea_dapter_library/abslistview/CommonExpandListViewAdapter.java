package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/5.
 * P:parent
 * C:child
 */
public abstract class CommonExpandListViewAdapter<P, C> extends BaseExpandableListAdapter {
    private ArrayList<P> parents;
    private Context mContext;
    private ExpandableListView expandableListView;
    //父布局
    private int parentLayoutId;
    //子布局
    private int childLayout;
    //是否展开全部
    private boolean isExpandAll = false;

    public CommonExpandListViewAdapter(ExpandableListView expandableListView, ArrayList<P> parents, int groupLayout, int childLayout) {
        this.parents = parents;
        this.mContext = expandableListView.getContext();
        this.expandableListView = expandableListView;
        expandableListView.setGroupIndicator(null);
        this.parentLayoutId = groupLayout;
        this.childLayout = childLayout;
    }

    @Override
    public int getGroupCount() {
        return parents == null ? 0 : parents.size();
    }


    @Override
    public P getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        P p = parents.get(groupPosition);
        return getChild(p, childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(parentLayoutId, null);
            viewHolder = new ViewHolder(mContext, convertView, parent, groupPosition);
            convertView.setTag(parentLayoutId, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(parentLayoutId);
        }
        convertGroup(viewHolder, groupPosition, isExpanded, getGroup(groupPosition));
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(childLayout, null);
            viewHolder = new ViewHolder(mContext, convertView, parent, groupPosition);
            convertView.setTag(childLayout, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(childLayout);
        }
        convertChild(viewHolder, childPosition, isLastChild, getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    protected abstract C getChild(P p, int childPosition);

    public abstract int getChildrenCount(int groupPosition);

    protected abstract void convertGroup(ViewHolder viewHolder, int groupPosition, boolean isExpanded, P groupItem);

    protected abstract void convertChild(ViewHolder viewHolder, int childPosition, boolean isLastChild, C child);

    public void addData(ArrayList<P> datas) {
        parents.addAll(datas);
        //设置了展开全部的属性
        expandAll();
        notifyDataSetChanged();

    }

    /**
     * 展开所有的分组
     */
    private void expandAll() {
        if (isExpandAll) {
            for (int i = 0; i < parents.size(); i++) {
                if (!expandableListView.isGroupExpanded(i))
                    expandableListView.expandGroup(i);
            }
        }
    }

    public void setExpandAll(boolean expandAll) {
        isExpandAll = expandAll;
        expandAll();
    }
}
