package com.yan.picture_select.helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.magnify.yutils.IntentUtil;
import com.magnify.yutils.StorageUtil;
import com.yan.picture_select.ImagePickerConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by heinigger on 16/9/5.
 */
public class ImagePickerHelper {
    //相机
    public static final int REQUEST_CAMEARA = 0x34;
    //单选
    public static final int REQUEST_SINGLE_SELECT = 0x35;
    public static final int REQUEST_GALLEY_SELECT = 0x36;
    //多选
    public static final int REQUEST_MULTI_SELECT = 0x36;
    //文件名
    private String fileName;
    private File pictureFile;

    private Activity mActivity;

    private OnFinishSelectListener onFinishSelectListener;

    public void setOnFinishSelectListener(OnFinishSelectListener onFinishSelectListener) {
        this.onFinishSelectListener = onFinishSelectListener;
    }

    public ImagePickerHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public ImagePickerHelper(Activity mActivity, String fileName) {
        this.mActivity = mActivity;
        this.fileName = fileName;
    }

    public void goCamera() {
        if (TextUtils.isEmpty(fileName))
            fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date()) + ".jpg";// 照片命名
        pictureFile = new File(StorageUtil.getDirDCIM(), fileName);
        mActivity.startActivityForResult(IntentUtil.camera(pictureFile), REQUEST_CAMEARA);
    }

    public void goGalley() {
        mActivity.startActivityForResult(IntentUtil.imagePick(), REQUEST_SINGLE_SELECT);
    }

    public void goMultiSelect(Class<?> targetActivity) {
        ImagePickerConfiguration.getInstance().setType(ImagePickerConfiguration.ImageType.multi);
        Intent intent = new Intent(mActivity, targetActivity);
        mActivity.startActivityForResult(intent, REQUEST_MULTI_SELECT);
    }

    public void goSingleSelect(Class<?> targetActivity) {
        ImagePickerConfiguration.getInstance().setType(ImagePickerConfiguration.ImageType.single);
        Intent intent = new Intent(mActivity, targetActivity);
        mActivity.startActivityForResult(intent, REQUEST_SINGLE_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //相机拍照的时候,使用路径方式,会把data会返回空
            if (requestCode == REQUEST_CAMEARA) {
                dealCameraData();
            } else if (requestCode == REQUEST_MULTI_SELECT || requestCode == REQUEST_SINGLE_SELECT) {
                if (data != null) {
                    ArrayList<String> datas = (ArrayList<String>) data.getSerializableExtra("data");
                    if (datas != null && onFinishSelectListener != null) {
                        onFinishSelectListener.onFinishSelect(datas, requestCode);
                    }
                }
            } else if (requestCode == REQUEST_GALLEY_SELECT && data != null) {
                dealCurrentGalley(data.getData());
            }
        }
    }

    /**
     * 从图库中取回来的图片
     */
    protected void dealCurrentGalley(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (TextUtils.isEmpty(picturePath)) {
                Toast.makeText(mActivity, "找不到图片", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> datas = new ArrayList<>();
            datas.add(picturePath);
            if (onFinishSelectListener != null)
                onFinishSelectListener.onFinishSelect(datas, REQUEST_SINGLE_SELECT);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast.makeText(mActivity, "找不到图片", Toast.LENGTH_SHORT).show();
                return;

            }
            ArrayList<String> datas = new ArrayList<>();
            datas.add(file.getAbsolutePath());
            if (onFinishSelectListener != null)
                onFinishSelectListener.onFinishSelect(datas, REQUEST_SINGLE_SELECT);
        }
    }

    /**
     * 处理相机拍摄返回的数据
     */
    private void dealCameraData() {
        if (pictureFile == null) {
            pictureFile = new File(StorageUtil.getDirDCIM(), fileName);
        }
        //这里还要加一句通知文件更新图片的通知
        mActivity.sendBroadcast(IntentUtil.addToAlbum(pictureFile));
        ArrayList<String> datas = new ArrayList<>();
        datas.add(pictureFile.getAbsolutePath());
        if (onFinishSelectListener != null)
            onFinishSelectListener.onFinishSelect(datas, ImagePickerHelper.REQUEST_CAMEARA);
    }

    public static interface OnFinishSelectListener {
        public void onFinishSelect(ArrayList<String> datas, int state);
    }


}
