package com.magnify.utils.ui.common;

import android.content.Intent;
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
import com.magnify.utils.ui.AutoCompeleActivity;
import com.magnify.utils.ui.CreateDataUtilsActivity;
import com.magnify.utils.ui.HeaderChildFooterActivity;
import com.magnify.utils.ui.RandCharActivity;
import com.magnify.utils.ui.SaveToolAndPreferenceActivity;
import com.magnify.yutils.app.BaseActivity;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/2.
 */
public class MainActivity extends BaseActivity {
    private RecyclerView recyler;
    private ArrayList<ActivityBean> arrayLists = new ArrayList<>();
    //直接到最新创建的页面
    private boolean directGONewGlass = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void setAdapter() {
        RecyclerView recyler = (RecyclerView) findViewById(R.id.recyler);
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

    private void startNewActivity(ActivityBean item) {
        Intent intent = new Intent(self, item.getaClass());
        intent.putExtra(CurrentBaseActivity.TITLE, item.getName());
        intent.putExtra(CurrentBaseActivity.SUBTITLE, item.getDescription());
        startActivity(intent);
    }

    public void add(String text, String description, Class<?> clazz) {
        arrayLists.add(new ActivityBean(text, description, clazz));
    }

}
