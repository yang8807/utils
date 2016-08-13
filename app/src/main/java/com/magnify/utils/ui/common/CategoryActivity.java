package com.magnify.utils.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.ActivityBean;
import com.magnify.utils.ui.ActivityDialog;
import com.magnify.utils.ui.ui_adapter.ActivityMultiTypeAdapter;
import com.magnify.utils.ui.ui_adapter.ActivityRecylerMultiTypeAdapter;
import com.magnify.utils.ui.ui_adapter.AutoCompeleActivity;
import com.magnify.utils.ui.ui_adapter.CreateDataUtilsActivity;
import com.magnify.utils.ui.ui_adapter.HeaderChildFooterActivity;
import com.magnify.utils.ui.ui_adapter.RandCharActivity;
import com.magnify.utils.ui.ui_utils.ActivityEncryptUtil;
import com.magnify.utils.ui.ui_utils.SaveToolAndPreferenceActivity;
import com.magnify.utils.ui.ui_view.AcitvitiySideBar;
import com.magnify.utils.ui.ui_view.ActivityShowView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/9.
 */
public class CategoryActivity extends CurrentBaseActivity {
    //View视图控件集合
    public final static int TYPE_VIEW = 1;
    //数据类
    public final static int TYPE_DATA = 2;
    //分装的Adapter
    public final static int TYPE_ADAPTER = 3;
    private int type;
    private RecyclerView recyler;
    private List<ActivityBean> arrayLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        type = (int) getObjects()[0];
        createData();
        setAdapter();
    }

    private void createData() {
        add("一些非常有用的github收藏", "切勿忘记积累", CollectingActivity.class);
        if (type == TYPE_VIEW) {
            addViewData();
        } else if (type == TYPE_ADAPTER) {
            addAdapterData();
        } else if (type == TYPE_DATA) {
            addDataData();
        }
    }

    private void addDataData() {
        add("ActivityEncryptUtil", "加密解密工具类", ActivityEncryptUtil.class);
        add("SaveTool和PreferencesUtil", "万能的文本数据保存工具", SaveToolAndPreferenceActivity.class);
        add("RandomCharUtils", "随机生成汉字", RandCharActivity.class);
        add("RandomUserUtils", "随机生成一个人物", CreateDataUtilsActivity.class);
    }

    private void addAdapterData() {
        add("BaseHeaderChildFooterAdapter", "类似订单效果", HeaderChildFooterActivity.class);
        add("BaseAutoCompleteAdapter", "输入显示匹配的人", AutoCompeleActivity.class);
        add("BaseMultiTypeAdapter", "ActivityMultiTypeAdapter,多布局...", ActivityMultiTypeAdapter.class);
        add("ActivityRecylerMultiTypeAdapter", "Recyler上的多布局,多布局,ListView和RecylerView对比发现,ListView会忽略原布局的属性,而RecylerView则保留了之前的布局属性", ActivityRecylerMultiTypeAdapter.class);
    }

    private void addViewData() {
        add("ActivityDialog", "一些常用的dialog动画", ActivityDialog.class);
        add("LoadingView", "加载动画", ActivityShowView.class, ActivityShowView.TYPE_LOADINGVIEW);
        add("DecoratedView", "一个专门用于修饰的view", ActivityShowView.class, ActivityShowView.TYPE_DECORATEEDVIEW);
        add("DecoratedBackGround", "随机生成圆的,用于装饰的背景view", ActivityShowView.class, ActivityShowView.TYPE_DECORATEEDBACK);
        add("TextViewExtends", "textview一些扩展属性:一个textView搞定这些...", ActivityShowView.class, ActivityShowView.TYPE_TEXTVIEW_EXTENDS);
        add("SideBar", "字母导航栏", AcitvitiySideBar.class);
    }

    private void setAdapter() {
        recyler = (RecyclerView) findViewById(R.id.recyler);
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
