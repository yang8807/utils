package com.magnify.yutils.interfaces;

/**
 * Created by heinigger on 16/8/9.
 */
public class SingleInstanceManager {
    public static ImageLoaderInterface imageLoaderInterface;

    public static void setImageLoader(ImageLoaderInterface imageLoader) {
        imageLoaderInterface = imageLoader;
    }

    public static ImageLoaderInterface getImageLoader() {
        return imageLoaderInterface;
    }
}
