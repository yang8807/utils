package com.magnify.utils.base;

import android.app.Application;
import android.content.Context;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;

/**
 * Created by heinigger on 16/8/5.
 */
public class CurrentApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = super.getApplicationContext();

        RandomUtil.init(context);
        ViewHolder.setImageLoaderInterface(new GlideImageLoader());
    }

    public static Context getAppContext() {
        return context;
    }
}
