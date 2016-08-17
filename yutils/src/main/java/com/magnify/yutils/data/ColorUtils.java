package com.magnify.yutils.data;

import android.graphics.Color;

public class ColorUtils {

    /**
     * 获取半透明颜色
     *
     * @param alpha 透明度， 0 ~ 1
     * @param color 需要半透明的颜色
     * @return 半透明的颜色
     */
    public static int getAlphaColor(float alpha, int color) {
        int alphaColor = (int) (alpha * 0xff);
        alphaColor = alphaColor << 24;
        color = color & 0x00ffffff;
        return alphaColor | color;
    }

    public static boolean isColor(int color) {
        return isLimit(Color.alpha(color)) && isLimit(Color.blue(color)) && isLimit(Color.green(color)) && isLimit(Color.red(color));
    }

    private static boolean isLimit(int size) {
        return size >= 0 && size <= 255;
    }
}
