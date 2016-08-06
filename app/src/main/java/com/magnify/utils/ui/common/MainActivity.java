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
import com.magnify.utils.bean.ActivityFunctionsBean;
import com.magnify.utils.ui.CreateDataUtilsActivity;
import com.magnify.utils.ui.RandCharActivity;
import com.magnify.utils.ui.SaveToolAndPreferenceActivity;
import com.magnify.yutils.app.BaseActivity;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/2.
 */
public class MainActivity extends BaseActivity {
    private RecyclerView recyler;
    private ArrayList<ActivityFunctionsBean> arrayLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createData();
        setAdapter();
    }

    private void createData() {
        arrayLists.add(new ActivityFunctionsBean("一些非常有用的github收藏", "切勿忘记积累", CollectingActivity.class));
        arrayLists.add(new ActivityFunctionsBean("SaveTool和PreferencesUtil", "万能的文本数据保存工具", SaveToolAndPreferenceActivity.class));
        arrayLists.add(new ActivityFunctionsBean("RandomCharUtils", "随机生成汉字", RandCharActivity.class));
        arrayLists.add(new ActivityFunctionsBean("RandomUserUtils", "随机生成一个人物", CreateDataUtilsActivity.class));
        arrayLists.add(new ActivityFunctionsBean("BaseHeaderChildFooterAdapter", "类似订单效果", HeaderChildFooterActivity.class));
    }

    private void setAdapter() {
        RecyclerView recyler = (RecyclerView) findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(this));
        recyler.setAdapter(new CommonAdapter<ActivityFunctionsBean>(self, R.layout.item_text_view, arrayLists) {
            @Override
            public void convert(ViewHolder holder, int position, final ActivityFunctionsBean item) {
                holder.setText(R.id.text_name, item.getName())
                        .setText(R.id.text_description, item.getDescription())
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(self, item.getaClass());
                                intent.putExtra(CurrentBaseActivity.TITLE, item.getName());
                                intent.putExtra(CurrentBaseActivity.SUBTITLE, item.getDescription());
                                startActivity(intent);
                            }
                        }, R.id.text_name, R.id.text_description);
            }
        });
    }



}
