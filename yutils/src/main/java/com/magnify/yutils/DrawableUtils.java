package com.magnify.yutils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;

/**
 * Created by 洒笑天涯 on 2015/11/10. Function:
 */
public class DrawableUtils {

    /**
     * @param radius 圆角矩形的半径
     * @return 得到带有圆角矩形, 带有点击效果的按钮,传进来两个颜色, 得到一个stateDrawable
     */
    public static StateListDrawable getStateCorRectDrawable(@ColorInt int color, int radius) {
        return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(color), radius));
    }

    /**
     * @return 得到带有圆角矩形, 带有点击效果的按钮,传进来两个颜色, 得到一个stateDrawable
     */
    public static StateListDrawable getStateCorRectDrawable(@ColorInt int color, @ColorInt int pressColor, int radius) {
        return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(pressColor), radius));
    }

    /**
     * @param color 普通状态的颜色
     * @return 得到带有圆形, 带有点击效果的按钮,传进来两个颜色, 得到一个stateDrawable
     */
    public static StateListDrawable getStateOvalDrawable(@ColorInt int color) {
        return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(color)));
    }

    /**
     * @param color      普通状态的颜色
     * @param pressColor 焦点改变的颜色
     * @return 得到带有圆形, 带有点击效果的按钮,传进来两个颜色, 得到一个stateDrawable
     */
    public static StateListDrawable getStateOvalDrawable(@ColorInt int color, @ColorInt int pressColor) {
        return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(pressColor)));
    }

    /**
     * @param color      normal color
     * @param pressColor pressColor
     * @return 得到一个矩形的按钮的按钮
     */
    public static StateListDrawable getStateRectDrawable(@ColorInt int color, @ColorInt int pressColor) {
        return addToStateDrawable(getRectDrawable(color), getRectDrawable(pressColor));
    }

    /**
     * @param color 自动生成另外一个颜色
     * @return 得到一个矩形的按钮的按钮
     */
    public static StateListDrawable getStateRectDrawable(@ColorInt int color) {
        return addToStateDrawable(getRectDrawable(color), getRectDrawable(getPressColor(color)));
    }

    /**
     * @return 为每个点击状态添加Drawable
     */
    private static StateListDrawable addToStateDrawable(GradientDrawable normalDrawable, GradientDrawable pressDrawable) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_focused}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        sd.addState(new int[]{}, normalDrawable);
        return sd;
    }

    /**
     * @return 解析传进来的一个颜色, 得到浅一点的颜色
     */
    public static int getPressColor(@ColorInt int color) {
        int alpha = (int) (Color.alpha(color) * 0.8);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red > 200 && green > 200 && blue > 200) {
            red = (int) (red * 0.9);
            green = (int) (green * 0.9);
            blue = (int) (blue * 0.9);
        }
        color = Color.argb(alpha, red, green, blue);
        return color;
    }

    /***
     * @return 得到一个矩形的背景
     */
    public static GradientDrawable getRectDrawable(@ColorInt int color) {
        return getGradientDrawable(ShapeType.RECT, color, 0);
    }

    /***
     * @return 得到一个圆形的背景
     */
    public static GradientDrawable getOvalDrawable(@ColorInt int color) {
        return getGradientDrawable(ShapeType.OVAL, color, 0);
    }

    /***
     * @return 得到一个圆角矩形的按钮
     */
    public static GradientDrawable getCorRectDrawable(@ColorInt int color, int radius) {
        return getGradientDrawable(ShapeType.CORNERRECT, color, radius);
    }

    /**
     * @return 根据颜色还有半径, 得到一个gradientDrawable
     */
    public static GradientDrawable getGradientDrawable(int shapeType, @ColorInt int color, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (shapeType == ShapeType.RECT) {
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        } else if (shapeType == ShapeType.OVAL) {
            gradientDrawable.setShape(GradientDrawable.OVAL);
        } else if (shapeType == ShapeType.CORNERRECT) {
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadii(new float[]{radius, radius, radius, radius});
        }
        gradientDrawable.setDither(true);
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    /**
     * RECT 圆形; OVAL 圆形; CORNERRECT 圆角矩形;
     */
    public static class ShapeType {

        public static final int RECT = 1;

        public static final int OVAL = 2;

        public static final int CORNERRECT = 3;
    }
}
