package com.magnify.utils.ui.fragment.adapter;

import android.content.Context;

import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.magnify.utils.bean.Contact;
import com.yan.fastview_library.viewgroup.base_swpie.BaseSwipeHeaderChildFooterAdapter;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/9/6.
 */
public class SwipeMultiAdapter extends BaseSwipeHeaderChildFooterAdapter<Contact, User> {

    public SwipeMultiAdapter(Context mContext, ArrayList<Contact> groupDatas) {
        super(mContext, groupDatas, R.layout.item_header_layout, R.layout.item_swipe_child_layout, R.layout.item_footer_layout);
    }

    @Override
    protected void convertChild(ViewHolder childHolder, int childPosition, User child, boolean isLastChild) {
        childHolder.setText(R.id.tv_userName, child.getUserName())
                .setText(R.id.tv_age, child.getSex() + "  " + child.getAddress())
                .setText(R.id.tv_phone, child.getPhone())
                .displayRoundImage(child.getImageAvator(), R.id.img_avators);
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
    public int getChildCount(Contact contact, int groupPosition) {
        ArrayList<User> peopels = contact.getPeoples();
        return peopels == null ? 0 : peopels.size();
    }
}
