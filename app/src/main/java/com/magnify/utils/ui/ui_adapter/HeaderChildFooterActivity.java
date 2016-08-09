package com.magnify.utils.ui.ui_adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;

import com.example.datautils.HanziToPinyin;
import com.example.datautils.RandomUtil;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseHeaderChildFooterAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.Contact;
import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.view.text.PowerEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 * Created by heinigger on 16/8/6.
 */
public class HeaderChildFooterActivity extends CurrentBaseActivity {
    private ListView listView;
    private PowerEditText editText;
    private HashMap<String, ArrayList<User>> sortUsers = new HashMap<>();
    private BaseHeaderChildFooterAdapter<Contact, User> mHeaderFooterAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        findViewNSetTextWatcher();
        createDataNsetAdapter();
    }

    private void findViewNSetTextWatcher() {
        listView = (ListView) findViewById(R.id.listview);
        editText = (PowerEditText) findViewById(R.id.edit_query);
        editText.setVisibility(View.VISIBLE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mHeaderFooterAdapter != null && !TextUtils.isEmpty(s)) {
                    String sortKey = HanziToPinyin.getSortKey(s.toString()).toUpperCase();
                    int scorllPosition = mHeaderFooterAdapter.getChildPositionAtListView(sortKey, s.toString());
                    if (scorllPosition >= 0)//第二个参数只滚动到该position时,离顶部的偏移量
                        listView.smoothScrollToPositionFromTop(scorllPosition, DeviceUtil.dipToPx(self, -10));//算出,并滚动到头顶
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 创建数据并设置Adapter
     */
    private void createDataNsetAdapter() {
        ArrayList<Contact> contacts = new ArrayList<>();
        //创建数据,并将相同key的数据,放进同一个集合中
        for (int i = 0; i < 500; i++) {
            User user = RandomUtil.createRandomUser();
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

        mHeaderFooterAdapter = new BaseHeaderChildFooterAdapter<Contact, User>(self, contacts, R.layout.item_header_layout, R.layout.item_child_layout, R.layout.item_footer_layout) {
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
        };
        listView.setAdapter(mHeaderFooterAdapter);
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
