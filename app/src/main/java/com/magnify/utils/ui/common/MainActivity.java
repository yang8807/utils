package com.magnify.utils.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.utils.ui.ActivityDialog;
import com.magnify.utils.ui.ui_adapter.HeaderChildFooterActivity;

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
        add("一些非常有用的github收藏", "切勿忘记积累", CollectingActivity.class);
        add("一些公用Adapter展示效果", "通用的一些adapter基类所创建出来的", CategoryActivity.class, CategoryActivity.TYPE_ADAPTER);
        add("一些数据安全或者保存工具", "简化数据加密和数据保存的操作", CategoryActivity.class, CategoryActivity.TYPE_DATA);
        add("自定义的视图或者之前为解决一类问题而收集的View", "view,效果...", CategoryActivity.class, CategoryActivity.TYPE_VIEW);
        add("案例展示:ActivityDialog", "一些常用的dialog动画", ActivityDialog.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyler != null && arrayLists != null)
            recyler.scrollToPosition(arrayLists.size() - 1);
    }

    private void setAdapter() {
        recyler = (RecyclerView) findViewById(R.id.recyler);
        recyler.setBackgroundColor(Color.WHITE);
        recyler.setLayoutManager(new LinearLayoutManager(this));
        recyler.setAdapter(new CommonAdapter<ActivityBean>(self, R.layout.item_text_view, arrayLists) {
            @Override
            public void convert(ViewHolder holder, int position, final ActivityBean item) {
                holder.setText(R.id.text_name, item.getName())
                        .setText(R.id.text_description, item.getDescription())
                        .getConvertView().setOnClickListener((v) -> startNewActivity(item));
            }
        });
    }


    public void add(String text, String description, Class<?> clazz, Object... objects) {
        arrayLists.add(new ActivityBean(text, description, clazz, objects));
    }

}
