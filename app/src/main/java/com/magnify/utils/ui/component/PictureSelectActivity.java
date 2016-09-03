package com.magnify.utils.ui.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.IntentUtil;
import com.magnify.yutils.StorageUtil;
import com.magnify.yutils.data.ImageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by heinigger on 16/8/20.
 */
public class PictureSelectActivity extends CurrentBaseActivity {

    //    file:///mnt/sdcard/external_sd/test.txt
    private Uri uri;

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
                uri = Uri.parse("file://" + StorageUtil.getDirDCIM() + "/yanwu/" + "yanwu" + System.currentTimeMillis() + ".jpg");
                startActivityForResult(IntentUtil.camera(), 11);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            try {
                ImageUtils.saveImageToSD(self, uri.getPath(), (Bitmap) data.getParcelableExtra("data"), 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IntentUtil.addToAlbum(new File(uri.getPath()));
            this.<ImageView>getView(R.id.cameraFile).setImageBitmap((Bitmap) data.getParcelableExtra("data"));
//            SingleInstanceManager.getImageLoader().displayImage(uri.toString(), this.<ImageView>getView(R.id.cameraFile));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
