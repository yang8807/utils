package com.magnify.utils.ui.fragment.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.View;

import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.magnify.utils.bean.Contact;
import com.magnify.yutils.ToastUtil;
import com.yan.fastview_library.viewgroup.base_swpie.BaseSwipeHeaderChildFooterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/9/6.
 */
public class SwipeMultiAdapter extends BaseSwipeHeaderChildFooterAdapter<Contact, User> implements View.OnClickListener {

    public SwipeMultiAdapter(Context mContext, ArrayList<Contact> groupDatas) {
        super(mContext, groupDatas, R.layout.item_header_layout, R.layout.item_swipe_child_layout, R.layout.item_footer_layout);
    }

    @Override
    protected void onPreCreateChild(ViewHolder childHolder, View convertView, int position) {
        childHolder.setOnClickListener(R.id.tv_delete, this)
                .setOnClickListener(R.id.tv_userName, this);
    }

    @Override
    protected void convertChild(ViewHolder childHolder, int groupPosition, int childPosition, User child, boolean isLastChild) {
        childHolder.setText(R.id.tv_userName, child.getUserName())
                .setText(R.id.tv_age, child.getSex() + "  " + child.getAddress())
                .setText(R.id.tv_phone, child.getPhone())
                .displayRoundImage(child.getImageAvator(), R.id.img_avators)
                .setTag(R.id.tv_delete, new Point(groupPosition, childPosition))
                .setTag(R.id.tv_userName, new Point(groupPosition, childPosition));
    }

    @Override
    protected String getGroupSortKey(int groupPosition, Contact contact) {
        return contact.getSortKey();
    }

    @Override
    protected String getChildName(int position, User user) {
        return user.getUserName();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    protected void convertFooter(ViewHolder footerHolder, int groupPosition, Contact contact) {
        footerHolder.setText(R.id.tv_footer, contact.getFooterName());
    }

    @Override
    protected void convertHeader(ViewHolder headerHolder, int groupPosition, Contact contact) {
        headerHolder.setText(R.id.tv_header, contact.getGroupName());
    }

    @Override
    protected User getChild(Contact contact, int childPosition) {
        return contact.getPeoples().get(childPosition);
    }

    @Override
    public List<User> getChilds(Contact contact, int groupPosition) {
        return contact.getPeoples();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_delete) {
            Point mPoint = (Point) view.getTag();
            removeChild(mPoint.x, mPoint.y);
            closeAllItems();
        } else if (view.getId() == R.id.tv_userName) {
            Point mPoints = (Point) view.getTag();
            User mUser = getChild(mPoints.x, mPoints.y);
            ToastUtil.show(getContext(), mUser.getUserName() + mUser.getAddress());
        }
    }
}
