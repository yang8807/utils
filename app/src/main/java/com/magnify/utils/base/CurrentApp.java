package com.magnify.utils.base;

import com.example.datautils.RandomUtil;
import com.yan.fastview_library.base.BaseApplication;
import com.yan.fastview_library.base.SingleInstanceManager;

/**
 * Created by heinigger on 16/8/5.
 */
public class CurrentApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RandomUtil.init(CurrentApp.getAppContext());
        SingleInstanceManager.setImageLoader(new GlideImageLoader());
    }

}
