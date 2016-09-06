package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.datautils.RandomUtil;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.viewgroup.base_swpie.CommonBaseSwipeAdapter;

/**
 * Created by heinigger on 16/9/6.
 */
public class SwipeLayoutFragment extends BaseFragment {
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.activity_listview, container, false);
        mListView = (ListView) parentView.findViewById(R.id.listview);
        mListView.setAdapter(new CommonBaseSwipeAdapter<User>(R.layout.adapter_base_swipe, R.id.swipe_layout, RandomUtil.createRandomUser(1000), getActivity()) {
            @Override
            protected void convert(int position, ViewHolder mViewHolders, View convertView, User item) {
                mViewHolders.displayImage(item.getImageAvator(), R.id.img_avators).setText(R.id.tv_userName, item.getUserName()).setText(R.id.tv_phone, item.getPhone());
            }
        });
        return parentView;
    }
}
