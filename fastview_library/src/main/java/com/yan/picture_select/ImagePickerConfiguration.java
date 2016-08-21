package com.yan.picture_select;

import android.graphics.Color;

/**
 * Created by heinigger on 16/8/21.
 */
public class ImagePickerConfiguration {
    //每行显示的个数
    private int numcloumns = 4;
    //每个item之间的间距
    private int spaciing = 4;
    //整体的风格
    private int style_color = Color.GREEN;
    //弹出框的占屏幕的比例
    private double dialogRatio = 0.7;
    //选择的类型
    private ImageType type = ImageType.multi;
    //多选选择的数量
    private int selectCount;

    enum ImageType {
        multi, single
    }

    public ImagePickerConfiguration setType(ImageType type) {
        this.type = type;
        return instance;
    }

    public ImagePickerConfiguration setSelectCount(int selectCount) {
        this.selectCount = selectCount;
        return instance;
    }

    public ImageType getType() {
        return type;
    }

    private static ImagePickerConfiguration instance;

    public static ImagePickerConfiguration getInstance() {
        if (instance == null) {
            instance = new ImagePickerConfiguration();
        }
        return instance;
    }

    public ImagePickerConfiguration setDialogRatio(double dialogRatio) {
        this.dialogRatio = dialogRatio;
        return instance;
    }

    public ImagePickerConfiguration setNumColumns(int NUMCOLUMNS) {
        this.numcloumns = NUMCOLUMNS;
        return instance;
    }

    public ImagePickerConfiguration setSpacing(int SPACING) {
        this.spaciing = SPACING;
        return instance;
    }

    public ImagePickerConfiguration setStyleColor(int styleColor) {
        this.style_color = styleColor;
        return instance;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public int getNumcloumns() {
        return numcloumns;
    }

    public int getSpaciing() {
        return spaciing;
    }

    public int getStyle_color() {
        return style_color;
    }

    public double getDialogRatio() {
        return dialogRatio;
    }


}
