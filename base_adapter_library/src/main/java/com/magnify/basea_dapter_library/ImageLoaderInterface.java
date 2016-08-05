package com.magnify.basea_dapter_library;

import android.widget.ImageView;

/**
 * Created by heinigger on 16/8/5.
 */
public interface ImageLoaderInterface {
    public void displayImage(String url, ImageView imageView);

    public void displayImage(String url, ImageView imageView, int defaultDrawable);

    public void displayRoundImage(String url, ImageView imageView);

    public void displayRoundImage(String url, ImageView imageView, int defaultDrawable);
}
