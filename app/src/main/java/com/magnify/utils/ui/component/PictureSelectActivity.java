package com.magnify.utils.ui.component;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.IntentUtil;
import com.magnify.yutils.LogUtil;
import com.magnify.yutils.LogUtils;
import com.magnify.yutils.StorageUtil;
import com.yan.fastview_library.base.SingleInstanceManager;

import java.io.File;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity {

    //    file:///mnt/sdcard/external_sd/test.txt
    private Uri uri;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        //单选
        findViewById(R.id.btn_single_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, ActivityPictureActivity.class);
                intent.putExtra(ActivityPictureActivity.FRAGMENT_TYPE, 0);
                startActivity(intent);
            }
        });
        //多选
        findViewById(R.id.btn_multi_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, ActivityPictureActivity.class);
                intent.putExtra(ActivityPictureActivity.FRAGMENT_TYPE, 1);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                file = new File(StorageUtil.getDirDCIM(), "yanwu/" + "yanwu" + System.currentTimeMillis() + ".jpg");
                startActivityForResult(IntentUtil.camera(), 11);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentUtil.addToAlbum(file);
        LogUtils.v("mine", file.exists() ? "文件存在" : "文件不存在");
        uri = Uri.fromFile(file);
        SingleInstanceManager.getImageLoader().displayImage(uri.toString(), (ImageView) getView(R.id.cameraFile));
        if (requestCode == 11) {
            LogUtil.v("mine", "it's ok" + uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
