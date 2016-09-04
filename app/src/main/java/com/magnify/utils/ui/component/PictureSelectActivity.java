package com.magnify.utils.ui.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.IntentUtil;
import com.magnify.yutils.StorageUtil;
import com.magnify.yutils.data.ImageUtils;
import com.yan.picture_select.ImagePickerConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity {
    private Uri uri;
    private static final int REQUEST_CAMEARA = 0x34;
    private static final int REQUEST_SINGLE_SELECT = 0x35;
    private static final int REQUEST_MULTI_SELECT = 0x36;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(self));
        //单选
        findViewById(R.id.btn_single_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, ActivityPictureActivity.class);
                ImagePickerConfiguration.getInstance().setType(ImagePickerConfiguration.ImageType.single);
                startActivityForResult(intent, REQUEST_SINGLE_SELECT);
            }
        });
        //多选
        findViewById(R.id.btn_multi_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerConfiguration.getInstance().setType(ImagePickerConfiguration.ImageType.multi);
                Intent intent = new Intent(self, ActivityPictureActivity.class);
                startActivityForResult(intent, REQUEST_MULTI_SELECT);
            }
        });
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uri = Uri.parse("file://" + StorageUtil.getDirDCIM() + "/yanwu/" + "yanwu" + System.currentTimeMillis() + ".jpg");
                startActivityForResult(IntentUtil.camera(), REQUEST_CAMEARA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMEARA) {
                dealCameraData(data);
            } else if (requestCode == REQUEST_MULTI_SELECT || requestCode == REQUEST_SINGLE_SELECT) {
                dealSelectData(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dealSelectData(Intent data) {
        ArrayList<String> datas = (ArrayList<String>) data.getSerializableExtra("data");
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

    /**
     * 处理相机拍摄返回的数据
     */
    private void dealCameraData(Intent data) {
        try {
            ImageUtils.saveImageToSD(self, uri.getPath(), (Bitmap) data.getParcelableExtra("data"), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntentUtil.addToAlbum(new File(uri.getPath()));
//        this.<ImageView>getView(R.id.cameraFile).setImageBitmap((Bitmap) data.getParcelableExtra("data"));
    }
}
