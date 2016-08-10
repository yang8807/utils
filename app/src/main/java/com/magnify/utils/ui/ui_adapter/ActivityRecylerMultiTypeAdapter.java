package com.magnify.utils.ui.ui_adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.basea_dapter_library.recyclerview.CommonMultiAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.bean.MultiLayoutBean;
import com.magnify.yutils.DeviceUtil;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityRecylerMultiTypeAdapter extends CurrentBaseActivity {
    private RecyclerView recyler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyler = (RecyclerView) findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(self));
        recyler.setAdapter(new CommonMultiAdapter<MultiLayoutBean>(self, MultiLayoutBean.createData(300),
                R.layout.view_image_view, R.layout.activity_mygridview,
                R.layout.activity_loadingview, R.layout.item_child_layout,
                R.layout.text_view, R.layout.activity_mygridview,
                R.layout.activity_mygridview) {
            @Override
            protected int getItemViewType(MultiLayoutBean data) {
                return data.getType();
            }

            @Override
            protected void converData(ViewHolder viewHolder, int itemType, MultiLayoutBean multiLayoutBean, int position) {
                switch (itemType) {
                    case 0:
                        final String url = multiLayoutBean.getObject();
                        final ImageView imageView = viewHolder.getView(R.id.imageView);
                        Glide.with(self)//activty
                                .load(url)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                        // Do something with bitmap here.
//                                                bitmap.getHeight(); //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
//                                                bitmap.getWidth();
                                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                                        lp.width = DeviceUtil.getScreenWidth(self);
                                        lp.height = lp.width * bitmap.getHeight() / bitmap.getWidth();
                                        imageView.requestLayout();
                                        imageView.setImageBitmap(bitmap);
                                    }

                                });
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
                                holder.setText(R.id.tv_user, user.getUserName()).displayRoundImage(user.getImageAvator(), R.id.image_user);
                            }
                        });
                        break;
                    case 6:
                        ArrayList<User> userss = multiLayoutBean.getObject();
                        GridView gridView2 = viewHolder.getView(R.id.gridView);
                        gridView2.setNumColumns(2);
                        gridView2.setAdapter(new CommonAdapter<User>(self, R.layout.item_doublue_in_grid_view, userss) {
                            @Override
                            protected void onPreCreate(ViewHolder holder, int position) {
                                ImageView imageView = holder.getView(R.id.image_double);
                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                                lp.width = lp.height = (DeviceUtil.getScreenWidth(self) - DeviceUtil.dipToPx(self, 5 * 4)) / 2;
                                imageView.requestLayout();
                            }

                            @Override
                            public void convert(ViewHolder holder, int position, User user) {
                                holder.displayRoundImage(user.getImageAvator(), R.id.image_double);
                            }
                        });
                        break;
                }
            }
        });
    }
}
