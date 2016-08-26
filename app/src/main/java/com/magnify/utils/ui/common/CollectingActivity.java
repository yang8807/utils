package com.magnify.utils.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.utils.R;
import com.magnify.utils.base.BaseWebViewActivity;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.CollectionInfo;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/4.
 */
public class CollectingActivity extends CurrentBaseActivity {
    private ExpandableListView expandableListView;
    private ArrayList<CollectionInfo> collectionInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_activity);
        createData();
        expandableListView = (ExpandableListView) findViewById(R.id.expand_listview);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return collectionInfos == null ? 0 : collectionInfos.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return collectionInfos.get(groupPosition).getChildSize();
            }

            @Override
            public CollectionInfo getGroup(int groupPosition) {
                return collectionInfos.get(groupPosition);
            }

            @Override
            public CollectionInfo.Child getChild(int groupPosition, int childPosition) {
                return collectionInfos.get(groupPosition).getChildren().get(childPosition);
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
                    convertView = LayoutInflater.from(self).inflate(R.layout.item_collect_group, null);
                    viewHolder = new ViewHolder(self, convertView, parent, groupPosition);
                    convertView.setTag(R.layout.item_collect_group, viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag(R.layout.item_collect_group);
                }
                CollectionInfo collectionInfo = getGroup(groupPosition);
                if (collectionInfo != null) {
                    viewHolder.setText(R.id.tv_group_name, collectionInfo.getGroupName()).setText(R.id.tv_group_description, collectionInfo.getDescription());
                }
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(self).inflate(R.layout.item_collect_child, null);
                    viewHolder = new ViewHolder(self, convertView, parent, childPosition);
                    convertView.setTag(R.layout.item_collect_child, viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag(R.layout.item_collect_child);
                }
                final CollectionInfo.Child child = getChild(groupPosition, childPosition);
                if (child != null) {
                    viewHolder.setText(R.id.tv_child_description, child.getUsage()).setText(R.id.tv_child_name, child.getGit_hub_url());
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = BaseWebViewActivity.getIntent(self, child.getGit_hub_url(), child.getGit_hub_url() + child.getUsage() + "  " + child.getDescription());
                        self.startActivity(intent);
                    }
                });

                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }
        });
    }

    private void createData() {

        collectionInfos.add(new CollectionInfo("utils", "一些经常使用到的:跳转到网络设置页面...")
                .addChild("https://github.com/Blankj/AndroidUtilCode")
                .addChild("https://github.com/Jude95/SwipeBackHelper", "compile 'com.jude:swipebackhelper:3.1.2'")
                .addChild("https://github.com/wenmingvs/NotifyUtil")
                .addChild("https://github.com/laobie/StatusBarUtil", "compile 'com.jaeger.statusbaruitl:library:1.2.0'")
        );

        collectionInfos.add(new CollectionInfo("view", "HeaderViewPager")
                .addChild("https://github.com/jeasonlzy0216/HeaderViewPager")
                .addChild("https://github.com/xiaanming/DragGridView")
                .addChild("https://github.com/askerov/DynamicGrid")
                .addChild("https://github.com/theredsunrise/AndroidCoolDragAndDropGridView")
                .addChild("https://github.com/jeasonlzy0216/ImagePicker")
                .addChild("https://github.com/florent37/MaterialViewPager")
                .addChild("https://github.com/AigeStudio/DatePicker")
                .addChild("https://github.com/goushengLi/RadarView")
                .addChild("https://github.com/liuling07/PhotoPicker")
                .addChild("https://github.com/wangjiegulu/RapidFloatingActionButton")
                .addChild("https://github.com/jasonpolites/gesture-imageview")
                .addChild("https://github.com/zzz40500/android-shapeLoadingView")
                .addChild("https://github.com/sbakhtiarov/gif-movie-view")
                .addChild("https://github.com/kyleduo/SwitchButton")
                .addChild("https://github.com/linger1216/labelview")
                .addChild("https://github.com/linkaipeng/NumberCodeView")
                .addChild("https://github.com/IsseiAoki/SimpleCropView", "compile 'com.isseiaoki:simplecropview:1.1.4'")
                .addChild("https://github.com/siyamed/android-shape-imageview", "compile 'com.github.siyamed:android-shape-imageview:0.9.+@aar'")

        );

        collectionInfos.add(new CollectionInfo("net", "一些常用的网络框架")
                .addChild("https://github.com/jeasonlzy0216/OkHttpUtils")
                .addChild("https://github.com/square/retrofit")
        );

        collectionInfos.add(new CollectionInfo("animation", "一些动画")
                .addChild("https://github.com/wasabeef/recyclerview-animators", "compile 'jp.wasabeef:recyclerview-animators:2.2.3'")
        );

        collectionInfos.add(new CollectionInfo("别人总结好的东西", "https://github.com/wangchang163/awesome-android-ui"));

    }

}
