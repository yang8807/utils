package com.magnify.utils.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.utils.ui.ActivityDialog;
import com.magnify.utils.ui.ui_adapter.ActivityMultiTypeAdapter;
import com.magnify.utils.ui.ui_adapter.AutoCompeleActivity;
import com.magnify.utils.ui.ui_adapter.CreateDataUtilsActivity;
import com.magnify.utils.ui.ui_adapter.HeaderChildFooterActivity;
import com.magnify.utils.ui.ui_adapter.RandCharActivity;
import com.magnify.utils.ui.ui_utils.ActivityEncryptUtil;
import com.magnify.utils.ui.ui_utils.SaveToolAndPreferenceActivity;
import com.magnify.utils.ui.ui_view.ActivityShowView;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/2.
 */
public class MainActivity extends CurrentBaseActivity {
    private RecyclerView recyler;
    private ArrayList<ActivityBean> arrayLists = new ArrayList<>();
    //直接到最新创建的页面
    private boolean directGONewGlass = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);
        createData();
        setAdapter();
        //直达最新创建的activity
        if (directGONewGlass)
            startNewActivity(arrayLists.get(arrayLists.size() - 1));
    }

    private void createData() {
        add("一些非常有用的github收藏", "切勿忘记积累", CollectingActivity.class);
        add("SaveTool和PreferencesUtil", "万能的文本数据保存工具", SaveToolAndPreferenceActivity.class);
        add("RandomCharUtils", "随机生成汉字", RandCharActivity.class);
        add("RandomUserUtils", "随机生成一个人物", CreateDataUtilsActivity.class);
        add("BaseHeaderChildFooterAdapter", "类似订单效果", HeaderChildFooterActivity.class);
        add("BaseAutoCompleteAdapter", "输入显示匹配的人", AutoCompeleActivity.class);
        add("LoadingView", "加载动画", ActivityShowView.class, ActivityShowView.TYPE_LOADINGVIEW);
        add("TextViewExtends", "textview一些扩展属性:一个textView搞定这些...", ActivityShowView.class, ActivityShowView.TYPE_TEXTVIEW_EXTENDS);
        add("BaseMultiTypeAdapter", "ActivityMultiTypeAdapter,多布局...", ActivityMultiTypeAdapter.class);
        add("ActivityEncryptUtil", "加密解密工具类", ActivityEncryptUtil.class);
        add("ActivityDialog", "一些常用的dialog动画", ActivityDialog.class);
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
                    public void onClick(View v) {
                        startNewActivity(item);
                    }
                });
            }
        });
    }


    public void add(String text, String description, Class<?> clazz, Object... objects) {
        arrayLists.add(new ActivityBean(text, description, clazz, objects));
    }

}
