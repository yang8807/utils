package com.magnify.utils.ui.component;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.picture_select.helper.ImagePickerHelper;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity implements ImagePickerHelper.OnFinishSelectListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImagePickerHelper mImagePickerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);

        mImagePickerHelper = new ImagePickerHelper(self);
        mImagePickerHelper.setOnFinishSelectListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(self));

        findViewById(R.id.btn_single_select).setOnClickListener(this);
        findViewById(R.id.btn_multi_select).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mImagePickerHelper != null)
            mImagePickerHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFinishSelect(ArrayList<String> datas, int state) {
        if (datas != null)
            mRecyclerView.setAdapter(new CommonAdapter<String>(self, R.layout.item_image_view, datas) {
                @Override
                protected void onPreCreate(ViewHolder viewHolder, View convertView) {
                    viewHolder.<TextView>getView(R.id.tv_description).setTextColor(Color.BLACK);
                }

                @Override
                public void convert(ViewHolder holder, int position, String o) {
                    holder.displayImage(o, R.id.image).setText(R.id.tv_description, o);
                }
            });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_single_select) {
            mImagePickerHelper.goSingleSelect(ActivityPictureActivity.class);
        } else if (id == R.id.btn_multi_select) {
            mImagePickerHelper.goMultiSelect(ActivityPictureActivity.class);
        } else if (id == R.id.btn_camera) {
            mImagePickerHelper.goCamera();
        }
    }
}
