package com.magnify.utils.base;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.magnify.yutils.interfaces.ImageLoaderInterface;

/**
 * Created by heinigger on 16/8/9.
 */
public class GlideUtilsImageLoader implements ImageLoaderInterface {
    @Override
    public void displayImage(String url, ImageView imageView) {
        Glide.with(CurrentApp.getAppContext()).load(url).into(imageView);
    }

    @Override
    public void displayImage(String url, ImageView imageView, int defaultDrawable) {
        Glide.with(CurrentApp.getAppContext()).load(url).placeholder(defaultDrawable).into(imageView);
    }

    @Override
    public void displayRoundImage(String url, ImageView imageView) {
        Glide.with(CurrentApp.getAppContext()).load(url).transform(new GlideCircleTransform(CurrentApp.getAppContext())).into(imageView);

    }

    @Override
    public void displayRoundImage(String url, ImageView imageView, int defaultDrawable) {
        Glide.with(CurrentApp.getAppContext()).load(url).transform(new GlideCircleTransform(CurrentApp.getAppContext())).placeholder(defaultDrawable).into(imageView);
    }
}
