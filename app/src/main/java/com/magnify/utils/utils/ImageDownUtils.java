package com.magnify.utils.utils;

import android.text.TextUtils;

import com.example.datautils.RandomUtil;
import com.magnify.yutils.StorageUtil;
import com.magnify.yutils.app.ThreadManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by heinigger on 16/9/8.
 */
public class ImageDownUtils {

    private OkHttpClient okHttpClient = new OkHttpClient();
    private String url = RandomUtil.getRandomImage();
    private String localPath;

    public void downloadImage(final String url) {
        this.localPath = StorageUtil.getDirDOWNLOADS().getAbsolutePath() + "\\" + System.currentTimeMillis() + ".jpg";
        if (TextUtils.isEmpty(url)) return;
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    InputStream is = response.body().byteStream();
                    saveImageToDisk(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //把从服务器获得图片的输入流InputStream写到本地磁盘
    public  void saveImageToDisk(InputStream inputStream) {
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localPath);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
