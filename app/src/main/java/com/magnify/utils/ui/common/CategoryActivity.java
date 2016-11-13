package com.magnify.utils.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import com.magnify.utils.ui.ui_adapter.ActivityRecylerMultiTypeAdapter;
import com.magnify.utils.ui.ui_adapter.AutoCompeleActivity;
import com.magnify.utils.ui.ui_adapter.CreateDataUtilsActivity;
import com.magnify.utils.ui.ui_adapter.HeaderChildFooterActivity;
import com.magnify.utils.ui.ui_adapter.RandCharActivity;
import com.magnify.utils.ui.ui_animation.ViewAnimationActivity;
import com.magnify.utils.ui.ui_utils.ActivityEncryptUtil;
import com.magnify.utils.ui.ui_utils.SaveToolAndPreferenceActivity;
import com.magnify.utils.ui.ui_view.ActivityBannerView;
import com.magnify.utils.ui.ui_view.ActivityEditTextAtListView;
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
    //动画
    public final static int TYPE_ANIMATION = 4;
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
        add(R.string.title_github_collection, R.string.title_github_collection_description, CollectingActivity.class);
        if (type == TYPE_VIEW) {
            addViewData();
        } else if (type == TYPE_ADAPTER) {
            addAdapterData();
        } else if (type == TYPE_DATA) {
            addDataData();
        } else if (type == TYPE_ANIMATION) {
            addAnimationData();
        }
    }

    private void addAnimationData() {
        add(R.string.title_view_animation, R.string.title_view_animation_description, ViewAnimationActivity.class);
    }

    private void addDataData() {
        add(R.string.title_encryptutil, R.string.title_encryptutil_description, ActivityEncryptUtil.class);
        add(R.string.title_savetool_preferencesutil, R.string.title_savetool_preferencesutil_description, SaveToolAndPreferenceActivity.class);
        add(R.string.titile_randomcharutils, R.string.titile_randomcharutils_description, RandCharActivity.class);
        add(R.string.titile_randomcharutils, R.string.titile_randomcharutils_create_people_description, CreateDataUtilsActivity.class);
    }

    private void addAdapterData() {
        add(R.string.titile_baseheaderchildfooteradapter, R.string.titile_baseheaderchildfooteradapter_decription, HeaderChildFooterActivity.class);
        add(R.string.titile_baseautocompleteadapter, R.string.titile_baseautocompleteadapter_decription, AutoCompeleActivity.class);
        add(R.string.titile_basemultitypeadapter, R.string.titile_basemultitypeadapter_decription, ActivityMultiTypeAdapter.class);
        add(R.string.titile_activityrecylermultitypeadapter, R.string.titile_activityrecylermultitypeadapter_decription, ActivityRecylerMultiTypeAdapter.class);
    }

    private void addViewData() {
        add(R.string.title_dialog, R.string.title_dialog_description, ActivityDialog.class);
        add(R.string.title_loadingview, R.string.title_loadingview_description, ActivityShowView.class, ActivityShowView.TYPE_LOADINGVIEW);
        add(R.string.title_decorated_background, R.string.title_title_decorated_background_description, ActivityShowView.class, ActivityShowView.TYPE_DECORATEEDVIEW);
        add(R.string.title_decorated_background, R.string.title_title_decorated_background_description, ActivityShowView.class, 4);
        add(R.string.title_textview_extends, R.string.title_textview_extends_description, ActivityShowView.class, ActivityShowView.TYPE_TEXTVIEW_EXTENDS);
        add(R.string.title_sidebar, R.string.title_sidebar_description, ActivityShowView.class, ActivityShowView.TYPE_SIDEBAR);
        add(R.string.title_edittext_at_listview, R.string.title_edittext_at_listview_description, ActivityEditTextAtListView.class);
        add(R.string.title_ripple_view, R.string.title_ripple_view_description, ActivityShowView.class, ActivityShowView.TYPE_RIPPLE);
        add(R.string.title_throw_animation_layout, R.string.title_throw_animation_layout_description, ActivityShowView.class, ActivityShowView.TYPE_SHOPCAR);
        add(R.string.title_banner_looper_view, R.string.title_banner_looper_view_description, ActivityBannerView.class);
        add(R.string.sticky_grid_view, R.string.sticky_grid_view_description, ActivityShowView.class, ActivityShowView.TYPE_STICK_GRID_VIEW);
        add(R.string.title_single_swipe, R.string.title_single_swipe_description, ActivityShowView.class, ActivityShowView.TYPE_COMMOM_SINGLE_SWIPE);
        add(R.string.title_multi_swipe, R.string.title_multi_swipe_description, ActivityShowView.class, ActivityShowView.TYPE_COMMOM_MULTI_SWIPE_LAYOUT);
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
