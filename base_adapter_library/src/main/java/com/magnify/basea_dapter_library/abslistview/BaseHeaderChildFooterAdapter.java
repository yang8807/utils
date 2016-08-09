package com.magnify.basea_dapter_library.abslistview;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heinigger on 16/8/5.
 * H:Header_AND_Footer
 * C:Child
 * 类似淘宝的那种订单效果,
 * 头部显示价钱之类的,中间显示商品,数量不定,尾部显示商品的总计数量和价钱总和;
 * #1.可以头部,中部,尾部使用{@link BaseHeaderChildFooterAdapter(Context ,ArrayList<HF> , int , int , int ))}
 * #2.可以头部,中部使用{@link BaseHeaderChildFooterAdapter(Context , ArrayList<HF> , int , int )}
 * #3.中部,尾部使用{@link BaseHeaderChildFooterAdapter(int , Context , ArrayList<HF> , int )}
 * 如果需要分组功能,需要复写,获取每一组每个item的position,则需要
 * {@link #getGroupSortKey(int, Object)}
 * {@link #getChildName(int, Object)}}
 */
public abstract class BaseHeaderChildFooterAdapter<HF, C> extends BaseAdapter {

    private ArrayList<HF> groupDatas;
    private Context mContext;
    private int headerLayout = -1;
    private int childLayout;
    private int footerLayout = -1;
    //存放所有header或者footer的位置,0是header,1是存放的就是footer的位置
    private SparseArray<PositionInfo> positions = new SparseArray<>();
    private int positionCounter;
    /*在此中的三种类型,只能用0,1,2这种递增方式来进行标志,不然会报错*/
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CHILD = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_COUNT = 3;
    private LayoutInflater infalter;
    /*上次groupdata的数量,用来判断是不是需要重新遍历一遍数据*/
    private int lastDataSize;
    /*遍历数组开始的位置*/
    private int traveseIndex = 0;
    //记录header的位置,进行头部导航的时候,可以使用
    private HashMap<String, Integer> groupPositions = new HashMap<>();


    public BaseHeaderChildFooterAdapter(Context mContext, ArrayList<HF> groupDatas, int headerLayout, int childLayout, int footerLayout) {
        this.groupDatas = groupDatas;
        this.mContext = mContext;
        this.headerLayout = headerLayout;
        this.childLayout = childLayout;
        this.footerLayout = footerLayout;
        infalter = LayoutInflater.from(mContext);
        this.lastDataSize = groupDatas == null ? 0 : groupDatas.size();
        caculateTypePosition();

    }

    public BaseHeaderChildFooterAdapter(Context mContext, ArrayList<HF> groupDatas, int headerLayout, int childLayout) {
        this.groupDatas = groupDatas;
        this.mContext = mContext;
        this.headerLayout = headerLayout;
        this.childLayout = childLayout;
        infalter = LayoutInflater.from(mContext);
        this.lastDataSize = groupDatas == null ? 0 : groupDatas.size();
        caculateTypePosition();
    }

    /**
     * @param footerLayout
     * @param mContext
     * @param groupDatas
     * @param childLayout
     */
    public BaseHeaderChildFooterAdapter(int footerLayout, Context mContext, ArrayList<HF> groupDatas, int childLayout) {
        this.groupDatas = groupDatas;
        this.mContext = mContext;
        this.footerLayout = footerLayout;
        this.childLayout = childLayout;
        infalter = LayoutInflater.from(mContext);
      /*  if (childLayout == footerLayout) {
            throw new IllegalMonitorStateException("you are supported to use diffent layout");
        }*/
        this.lastDataSize = groupDatas == null ? 0 : groupDatas.size();
        caculateTypePosition();
    }

    /*计算每个位置需要返回什么类型View*/
    private void caculateTypePosition() {
        if (groupDatas != null && !groupDatas.isEmpty()) {
            for (int i = traveseIndex; i < groupDatas.size(); i++) {
                if (headerLayout > 0) {//headerlayout使用的时候
                    //key 存放在的是类型的position,用完再增加
                    positions.put(positionCounter, new PositionInfo(i, TYPE_HEADER));

                    String sortKey = getGroupSortKey(i, groupDatas.get(i));
                    //sortKey不为空,再这里存储每个头部对应再adapter中的位置
                    if (!TextUtils.isEmpty(sortKey)) groupPositions.put(sortKey, positionCounter);
                    //得到当前组的数量,这里都是存放child的
                    for (int j = 0; j < getChildCount(groupDatas.get(i), i); j++) {
                        positionCounter += 1;
                        positions.put(positionCounter, new PositionInfo(i, TYPE_CHILD, j));
                    }//这里才是存放footer的
                    positionCounter += 1;
                    if (footerLayout > 0) {//没有footer部分
                        positions.put(positionCounter, new PositionInfo(i, TYPE_FOOTER));
                        positionCounter += 1;
                    }
                } else {//headerlayout不使用的时候
                    for (int j = 0; j < getChildCount(groupDatas.get(i), i); j++) {
                        positions.put(positionCounter, new PositionInfo(i, TYPE_CHILD, j));
                        positionCounter += 1;
                    }//这里才是存放footer的
                    positions.put(positionCounter, new PositionInfo(i, TYPE_FOOTER));
                    positionCounter += 1;
                }
            }
        }
    }

    /**
     * 遍历头部的时候,用这个来存储遍历过的头部,按什么
     */
    protected String getGroupSortKey(int groupPosition, HF hf) {
        return "";
    }

    protected String getChildName(int position, C c) {
        return "";
    }

    /*强制重新计算所有位置,保证每个数据返回都是正确的,仅再对数据源做了加和减操作的时候,不然效率有问题,其实也不是什么问题...,比起view嵌套之类的...*/
    public void forceNotifyDataSetChanged() {
        lastDataSize = 0;
        traveseIndex = 0;
        positionCounter = 0;
        positions.clear();
        groupPositions.clear();
        caculateTypePosition();
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        //当通知数据改变的时候,需要重新记录数据呀,如果是下拉刷新的化,可能第一个item的child数量变了,导致所有的位置都变了,那么就需要重新计算位置
        if (groupDatas == null) return;
        if (lastDataSize >= groupDatas.size())//数据比上次的还少
        {
            traveseIndex = 0;
            positionCounter = 0;
            positions.clear();
            groupPositions.clear();
        } else {//存在的问题,如果使用者先将集合数据删掉一些,再添加一些,那么开始的位置就会出现问题了...,那么可以使用强制刷新,重新将位置计算一遍
            traveseIndex = lastDataSize;
        }
        caculateTypePosition();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return positions.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {
        //头部加尾部+孩子的数量
        return positions.size();
    }

    /*获取头部在listView的位置,分组的key标志*/
    public int getHeaderPositionAtListView(String sortKey) {
        if (groupPositions.containsKey(sortKey)) return groupPositions.get(sortKey);
        return -1;
    }

    @Override
    public Object getItem(int position) {
        PositionInfo positionInfo = positions.get(position);
        //根据position,判断当前是哪个组,哪个child
        int type = positionInfo.getType();

        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            return groupDatas.get(positionInfo.getGroupPosition());
        } else {
            //哪个group的哪个child?
            return getChild(groupDatas.get(positionInfo.getGroupPosition()), positionInfo.getChildPosition());
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = 0;
        ViewHolder headerHolder = null;
        ViewHolder childHolder = null;
        ViewHolder footerHolder = null;
        PositionInfo positionInfo = positions.get(position);
        int groupPosition = positionInfo.getGroupPosition();
        int childPosition = positionInfo.getChildPosition();
        type = positionInfo.getType();
        if (convertView == null) {//当返回类型的View为空的时候
            switch (type) {
                case TYPE_HEADER:
                    convertView = infalter.inflate(headerLayout, null);
                    headerHolder = new ViewHolder(mContext, convertView, parent, groupPosition);
                    convertView.setTag(headerLayout, headerHolder);
                    break;
                case TYPE_CHILD:
                    convertView = infalter.inflate(childLayout, null);
                    childHolder = new ViewHolder(mContext, convertView, parent, childPosition);
                    convertView.setTag(childLayout, childHolder);
                    break;
                case TYPE_FOOTER:
                    convertView = infalter.inflate(footerLayout, null);
                    footerHolder = new ViewHolder(mContext, convertView, parent, groupPosition);
                    convertView.setTag(footerLayout, footerHolder);
                    break;
            }
        } else {//当不为空
            switch (type) {
                case TYPE_HEADER:
                    headerHolder = (ViewHolder) convertView.getTag(headerLayout);
                    break;
                case TYPE_CHILD:
                    childHolder = (ViewHolder) convertView.getTag(childLayout);
                    break;
                case TYPE_FOOTER:
                    footerHolder = (ViewHolder) convertView.getTag(footerLayout);
                    break;
            }
        }

        HF hf = groupDatas.get(groupPosition);

        //在这里开始设置各个布局所应该做的事情了
        switch (type) {
            case TYPE_HEADER:
                convertHeader(headerHolder, groupPosition, hf);
                break;
            case TYPE_CHILD:
                convertChild(childHolder, childPosition, getChild(hf, childPosition), getChildCount(hf, groupPosition) == childPosition + 1);
                break;
            case TYPE_FOOTER:
                convertFooter(footerHolder, groupPosition, hf);
                break;

        }
        return convertView;
    }

    /*设置再中间的数据*/
    protected abstract void convertChild(ViewHolder childHolder, int childPosition, C child, boolean isLastChild);

    /*设置底部的数据*/
    protected abstract void convertFooter(ViewHolder footerHolder, int groupPosition, HF hf);

    /*设置头部的数据*/
    protected abstract void convertHeader(ViewHolder headerHolder, int groupPosition, HF hf);

    /*得到子Item*/
    protected abstract C getChild(HF hf, int childPosition);

    /*得到当前组的子类数量*/
    public abstract int getChildCount(HF hf, int groupPosition);

    public int getChildPositionAtListView(String sortKey, String s) {
        if (TextUtils.isEmpty(sortKey) || TextUtils.isEmpty(s)) return -1;
        int position = getHeaderPositionAtListView(sortKey);
        for (int i = 0; i < groupDatas.size(); i++) {
            HF hf = groupDatas.get(i);
            if (sortKey.equals(getGroupSortKey(i, hf))) {
                for (int j = 0; j < getChildCount(hf, i); j++) {
                    C c = getChild(hf, j);
                    String childName = getChildName(j, c);
                    if (childName.contains(s)) {
                        return (position + j + 1);//找到了该位置的时候,中断循环
                    }
                }
            }
        }

        return -1;
    }

    /*记录位置信息和类型*/
    private class PositionInfo {
        private int type;
        //组的位置
        private int groupPosition;
        //孩子的位置
        private int childPosition = -1;

        public PositionInfo(int i, int headerLayout) {
            this.groupPosition = i;
            this.type = headerLayout;
        }

        public PositionInfo(int i, int headerLayout, int childPosition) {
            this.groupPosition = i;
            this.type = headerLayout;
            this.childPosition = childPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }

        public int getType() {
            return type;
        }

        public int getGroupPosition() {
            return groupPosition;
        }
    }
}
