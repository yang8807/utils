package com.yan.fastview_library.base;

import com.magnify.basea_dapter_library.ImageLoaderInterface;
import com.magnify.basea_dapter_library.ViewHolder;

/**
 * Created by heinigger on 16/8/9.
 */
public class SingleInstanceManager {
    public static ImageLoaderInterface imageLoaderInterface;

    public static void setImageLoader(ImageLoaderInterface imageLoader) {
        imageLoaderInterface = imageLoader;
        ViewHolder.setImageLoaderInterface(imageLoaderInterface);
    }

    public static ImageLoaderInterface getImageLoader() {
        return imageLoaderInterface;
    }
}
