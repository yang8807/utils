package com.magnify.utils.ui.ui_adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;
import android.widget.ListView;

import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseMultiTypeAdapter;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.MultiLayoutBean;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityMultiTypeAdapter extends CurrentBaseActivity {
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new BaseMultiTypeAdapter<MultiLayoutBean>(self, MultiLayoutBean.createData(300),
                R.layout.view_image_view, R.layout.activity_mygridview,
                R.layout.activity_loadingview, R.layout.item_child_layout,
                R.layout.text_view, R.layout.activity_mygridview) {
            @Override
            protected int getItemViewType(MultiLayoutBean data) {
                return data.getType();
            }

            @Override
            protected void converData(ViewHolder viewHolder, int itemType, MultiLayoutBean multiLayoutBean, int position) {
                switch (itemType) {
                    case 0:
                        final String url = multiLayoutBean.getObject();
                        viewHolder.displayImage(url, R.id.imageView);
                        break;
                    case 1:
                        ArrayList<String> urls = multiLayoutBean.getObject();
                        GridView gridView = viewHolder.getView(R.id.gridView);
                        gridView.setAdapter(new CommonAdapter<String>(self, R.layout.item_gridview, urls) {
                            public void convert(ViewHolder holder, int position, String s) {
                                holder.displayRoundImage(s, R.id.image_grid);
                            }
                        });
                        break;
                    case 2:

                        break;
                    case 3:
                        User child = multiLayoutBean.getObject();
                        viewHolder.setText(R.id.tv_userName, child.getUserName())
                                .setText(R.id.tv_age, child.getSex() + "  " + child.getAddress())
                                .setText(R.id.tv_phone, child.getPhone())
                                .displayRoundImage(child.getImageAvator(), R.id.img_avators);
                        break;
                    case 4:
                        String text = multiLayoutBean.getObject();
                        viewHolder.setText(R.id.text, text);
                        break;
                    case 5:
                        ArrayList<User> users = multiLayoutBean.getObject();
                        GridView gridView1 = viewHolder.getView(R.id.gridView);
                        gridView1.setAdapter(new CommonAdapter<User>(self, R.layout.item_image_text, users) {
                            @Override
                            public void convert(ViewHolder holder, int position, User user) {
                                holder.setText(R.id.tv_user, user.getUserName()).displayImage(user.getImageAvator(), R.id.image_user);
                            }
                        });
                        break;
                }
            }
        });
    }
}
