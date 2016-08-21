package com.yan.picture_select;

import android.graphics.Color;

/**
 * Created by heinigger on 16/8/21.
 */
public class ImagePickerConfiguration {
    //每行显示的个数
    public static int NUMCOLUMNS = 4;
    //每个item之间的间距
    public static int SPACING = 4;
    //整体的风格
    public static int STYLE_COLOR = Color.GREEN;
    //弹出框的占屏幕的比例
    public static double DIALOG_RATIO = 0.7;

    private static ImagePickerConfiguration instance;

    public static ImagePickerConfiguration getInstance() {
        if (instance == null) {
            instance = new ImagePickerConfiguration();
        }
        return instance;
    }

    public ImagePickerConfiguration setDialogRatio(double dialogRatio) {
        DIALOG_RATIO = dialogRatio;
        return instance;
    }

    public ImagePickerConfiguration setNumColumns(int NUMCOLUMNS) {
        ImagePickerConfiguration.NUMCOLUMNS = NUMCOLUMNS;
        return instance;
    }

    public ImagePickerConfiguration setSpacing(int SPACING) {
        ImagePickerConfiguration.SPACING = SPACING;
        return instance;
    }

    public ImagePickerConfiguration setStyleColor(int styleColor) {
        ImagePickerConfiguration.STYLE_COLOR = styleColor;
        return instance;
    }
}
