package com.magnify.utils.base;

import com.example.datautils.RandomUtil;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.yutils.app.BaseApplication;
import com.magnify.yutils.interfaces.SingleInstanceManager;

/**
 * Created by heinigger on 16/8/5.
 */
public class CurrentApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RandomUtil.init(CurrentApp.getAppContext());
        ViewHolder.setImageLoaderInterface(new GlideImageLoader());
        SingleInstanceManager.setImageLoader(new GlideUtilsImageLoader());
    }

}
