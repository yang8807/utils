package com.magnify.utils.ui.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/5.
 * H:Header_AND_Footer
 * C:Child
 * 类似淘宝的那种订单效果,
 * 头部显示价钱之类的,中间显示商品,数量不定,尾部显示商品的总计数量和价钱总和
 */
public abstract class BaseHeaderChildFooterAdapter<HF, C> extends BaseAdapter {

    private ArrayList<HF> headerDatas;
    private Context mContext;
    private int headerLayout;
    private int childLayout;
    private int footerLayout;
    //存放所有header或者footer的位置,0是header,1是存放的就是footer的位置
    private SparseArray<PositionInfo> positions = new SparseArray<>();
    private int positionCounter;
    /*在此中的三种类型*/
    private static final int TYPE_HEADER = 23;
    private static final int TYPE_CHILD = 24;
    private static final int TYPE_FOOTER = 25;
    private static final int TYPE_COUNT = 3;
    private LayoutInflater infalter;


    public BaseHeaderChildFooterAdapter(Context mContext, ArrayList<HF> headerDatas, int headerLayout, int childLayout, int footerLayout) {
        this.headerDatas = headerDatas;
        this.mContext = mContext;
        this.headerLayout = headerLayout;
        this.childLayout = childLayout;
        this.footerLayout = footerLayout;
        infalter = LayoutInflater.from(mContext);
        if (headerLayout==footerLayout||childLayout==footerLayout||headerLayout==childLayout){
            throw new IllegalMonitorStateException("you are supported to use diffent");
        }
        caculateTypePosition();
    }

    /*计算每个位置需要返回什么类型View*/
    private void caculateTypePosition() {
        positionCounter = 0;
        if (headerDatas != null && !headerDatas.isEmpty())
            for (int i = 0; i < headerDatas.size(); i++) {
                //key 存放在的是类型的position,用完再增加
                positions.put(positionCounter, new PositionInfo(i, TYPE_HEADER));
                //得到当前组的数量,这里都是存放child的
                for (int j = 0; j < getChildCount(headerDatas.get(i), i); j++) {
                    positions.put(++positionCounter, new PositionInfo(i, TYPE_CHILD, j));
                }//这里才是存放footer的
                positions.put(++positionCounter, new PositionInfo(i, TYPE_FOOTER));
                positionCounter++;
            }
    }

    @Override
    public void notifyDataSetChanged() {
        //当通知数据改变的时候,需要重新记录数据呀,如果是下拉刷新的化,可能第一个item的child数量变了,导致所有的位置都变了,那么就需要重新计算位置
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
        return (headerDatas == null ? 0 : headerDatas.size()) * 2 + getAllChildCount();
    }

    @Override
    public Object getItem(int position) {
        PositionInfo positionInfo = positions.get(position);
        //根据position,判断当前是哪个组,哪个child
        int type = positionInfo.getType();

        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            return headerDatas.get(positionInfo.getGroupPosition());
        } else {
            //哪个group的哪个child?
            return getChild(headerDatas.get(positionInfo.getGroupPosition()), positionInfo.getChildPosition());
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        ViewHolder headerHolder = null;
        ViewHolder childHolder = null;
        ViewHolder footerHolder = null;
        PositionInfo positionInfo = positions.get(position);
        int groupPosition = positionInfo.getGroupPosition();
        int childPosition = positionInfo.getChildPosition();
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

        HF hf = headerDatas.get(groupPosition);

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

    /*得到子类所有的数量*/
    public abstract int getAllChildCount();

    /*得到当前组的子类数量*/
    public abstract int getChildCount(HF hf, int groupPosition);

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
