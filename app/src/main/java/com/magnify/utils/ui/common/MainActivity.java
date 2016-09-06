package com.magnify.utils.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.utils.ui.ActivityDialog;
import com.magnify.utils.ui.component.PictureSelectActivity;
import com.magnify.utils.ui.ui_adapter.HeaderChildFooterActivity;
import com.magnify.utils.ui.ui_view.ActivityShowView;
import com.magnify.utils.ui.ui_view.FileActivity;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/2.
 */
public class MainActivity extends CurrentBaseActivity {
    private RecyclerView recyler;
    private ArrayList<ActivityBean> arrayLists = new ArrayList<>();
    //直接到最新创建的页面
    private boolean directGONewGlass = true;
    //是否跳转到指定页面
    private boolean goAssignClass = true;
    //跳转的指定页面
    private Class aClass = HeaderChildFooterActivity.class;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置当前页面不可以滑动关闭
        SwipeBackHelper.getCurrentPage(self).setSwipeBackEnable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setTitle(R.string.frist_title);
        createData();
        setAdapter();
        //直达最新创建的activity
        if (directGONewGlass)
            startNewActivity(arrayLists.get(arrayLists.size() - 1));
            //到达指定跳转的Activity
        else if (goAssignClass && aClass != null) {
            for (int i = 0; i < arrayLists.size(); i++) {
                if (arrayLists.get(i).getaClass().equals(aClass)) {
                    startNewActivity(arrayLists.get(i));
                    break;
                }
            }
        }
    }

    private void createData() {
        add(R.string.title_github_collection, R.string.title_github_collection_description, CollectingActivity.class);
        add(R.string.titile_adapter, R.string.titile_adapter_decription, CategoryActivity.class, CategoryActivity.TYPE_ADAPTER);
        add(R.string.title_data, R.string.title_data_description, CategoryActivity.class, CategoryActivity.TYPE_DATA);
        add(R.string.title_view, R.string.title_view_description, CategoryActivity.class, CategoryActivity.TYPE_VIEW);
        add(R.string.title_animation, R.string.title_animation_description, CategoryActivity.class, CategoryActivity.TYPE_ANIMATION);
        add(R.string.title_dialog, R.string.title_dialog_description, ActivityDialog.class);
        add(R.string.title_viewpager_animation, R.string.title_viewpager_animation_description, ActivityShowView.class, ActivityShowView.TYPE_VIEW_PAGER_ANIMATION);
        add(R.string.titile_infinite_adapter, R.string.titile_infinite_adapter_decription, ActivityShowView.class, ActivityShowView.TYPE_INFINITE_VIEWPAGER_ADAPTER);
        add(R.string.title_file_expore, R.string.title_file_expore_decription, FileActivity.class);
        add(R.string.title_picture_component, R.string.title_picture_component_decription, PictureSelectActivity.class);

        add(R.string.title_single_swipe, R.string.title_single_swipe_description, ActivityShowView.class, ActivityShowView.TYPE_COMMOM_SINGLE_SWIPE);
        add(R.string.title_multi_swipe, R.string.title_multi_swipe_description, ActivityShowView.class, ActivityShowView.TYPE_COMMOM_MULTI_SWIPE_LAYOUT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyler != null && arrayLists != null)
            recyler.scrollToPosition(arrayLists.size() - 1);
    }

    private void setAdapter() {
        recyler = (RecyclerView) findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(this));
        recyler.setAdapter(new CommonAdapter<ActivityBean>(self, R.layout.item_text_view, arrayLists) {
            @Override
            public void convert(ViewHolder holder, int position, final ActivityBean item) {
                holder.setText(R.id.text_name, item.getName())
                        .setText(R.id.text_description, item.getDescription())
                        .getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNewActivity(item);
                    }
                });
            }
        });
    }


    public void add(String text, String description, Class<?> clazz, Object... objects) {
        arrayLists.add(new ActivityBean(text, description, clazz, objects));
    }

    public void add(@StringRes int text, @StringRes int description, Class<?> clazz, Object... objects) {
        arrayLists.add(new ActivityBean(getString(text), getString(description), clazz, objects));
    }

}
