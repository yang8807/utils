package com.magnify.utils.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.datautils.RandomUserUtil;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseHeaderChildFooterAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 * Created by heinigger on 16/8/6.
 */
public class HeaderChildFooterActivity extends CurrentBaseActivity {
    private ListView listView;
    private ArrayList<User> userLists;
    private HashMap<String, ArrayList<User>> sortUsers = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.listview);
        ArrayList<Contact> contacts = new ArrayList<>();
        //创建数据,并将相同key的数据,放进同一个集合中
        for (int i = 0; i < 500; i++) {
            User user = RandomUserUtil.createRandomUser();
            String sortKey = user.getSortKey();
            if (!sortUsers.containsKey(sortKey)) {
                sortUsers.put(sortKey, new ArrayList<User>());
            }
            sortUsers.get(sortKey).add(user);
        }
        //创建数据,然后排列
        for (String key : sortUsers.keySet()) {
            contacts.add(new Contact(sortUsers.get(key)));
        }

        Collections.sort(contacts, new PinyinComparator());

        listView.setAdapter(new BaseHeaderChildFooterAdapter<Contact, User>(self, contacts, R.layout.item_header_layout, R.layout.item_child_layout, R.layout.item_footer_layout) {
            @Override
            protected void convertChild(ViewHolder childHolder, int childPosition, User child, boolean isLastChild) {
                childHolder.setText(R.id.tv_userName, child.getUserName())
                        .setText(R.id.tv_age, child.getSex() + "  " + child.getAddress())
                        .setText(R.id.tv_phone, child.getPhone())
                        .displayRoundImage(child.getImageAvator(), R.id.img_avators);
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
        });
    }

    /**
     * 对客户名称进行排序;
     * 按A-Z进行排序
     *
     * @author 石钟辉
     */
    class PinyinComparator implements Comparator<Contact> {

        public int compare(Contact o1, Contact o2) {
            if (o1.getSortKey().equals("@") || o2.getSortKey().equals("#")) {
                return -1;
            } else if (o1.getSortKey().equals("#") || o2.getSortKey().equals("@")) {
                return 1;
            } else {//这句话就能额解决从A-Z的数据排列了
                return o1.getSortKey().compareTo(o2.getSortKey());
            }
        }
    }
}
