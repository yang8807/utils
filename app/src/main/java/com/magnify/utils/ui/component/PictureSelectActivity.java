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
import com.magnify.yutils.IntentUtil;
import com.magnify.yutils.StorageUtil;
import com.yan.picture_select.ImagePickerConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity {
    private static final int REQUEST_CAMEARA = 0x34;
    private static final int REQUEST_SINGLE_SELECT = 0x35;
    private static final int REQUEST_MULTI_SELECT = 0x36;

    private RecyclerView mRecyclerView;
    private String fileName;
    private File pictureFile;

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
                fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date()) + ".jpg";// 照片命名
                pictureFile = new File(StorageUtil.getDirDCIM(), fileName);
                self.startActivityForResult(IntentUtil.camera(pictureFile), REQUEST_CAMEARA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //相机拍照的时候,使用路径方式,会把data会返回空
            if (requestCode == REQUEST_CAMEARA) {
                dealCameraData();
            } else if (requestCode == REQUEST_MULTI_SELECT || requestCode == REQUEST_SINGLE_SELECT) {
                if (data != null)
                    setPictureAdapter((ArrayList<String>) data.getSerializableExtra("data"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置浏览的适配器
     */
    private void setPictureAdapter(final ArrayList<String> datas) {
        if (datas != null || !datas.isEmpty())
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
    private void dealCameraData() {
        if (pictureFile == null) {
            pictureFile = new File(StorageUtil.getDirDCIM(), fileName);
        }
        //这里还要加一句通知文件更新图片的通知
        self.sendBroadcast(IntentUtil.addToAlbum(pictureFile));
        ArrayList<String> datas = new ArrayList<>();
        datas.add(pictureFile.getAbsolutePath());
        setPictureAdapter(datas);
    }
}
