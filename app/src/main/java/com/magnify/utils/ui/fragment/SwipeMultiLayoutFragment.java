package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.datautils.RandomUtil;
import com.example.datautils.User;
import com.magnify.utils.R;
import com.magnify.utils.bean.Contact;
import com.magnify.utils.ui.fragment.adapter.SwipeMultiAdapter;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by heinigger on 16/9/6.
 */
public class SwipeMultiLayoutFragment extends BaseFragment {
    private ListView mListView;
    private SideBar mSideBar;
    private HashMap<String, ArrayList<User>> sortUsers = new HashMap<>();
    private SwipeMultiAdapter mSwipeMultiAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.activity_listview, container, false);
        mListView = (ListView) parentView.findViewById(R.id.listview);
        mSideBar = (SideBar) parentView.findViewById(R.id.sideBar);

        mSideBar.setOnTouchCharactersListerner(new SideBar.onTouchCharactersListerner() {
            @Override
            public boolean onTouchCharacter(int position, String touchText) {
                if (mSwipeMultiAdapter == null) return false;
                int scrollePosition = mSwipeMultiAdapter.getHeaderPositionAtListView(touchText);
                if (scrollePosition >= 0) {
                    mListView.smoothScrollToPositionFromTop(scrollePosition, 0);
                    return true;
                }
                return false;
            }
        });
        ArrayList<Contact> contacts = new ArrayList<>();
        //创建数据,并将相同key的数据,放进同一个集合中
        for (int i = 0; i < 1000; i++) {
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
        mSideBar.setAvaliableCharacters(sortUsers.keySet());

        mSwipeMultiAdapter = new SwipeMultiAdapter(getActivity(), contacts);
        mListView.setAdapter(mSwipeMultiAdapter);
        return parentView;
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
