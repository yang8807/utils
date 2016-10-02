package com.magnify.yutils.view_utils;

import android.graphics.Paint;/**
 * Created by 黄艳武 on 2015/10/29.
 * Function:
 */
public class TextDrawUtils {
    /**
     * @parentHeight 在该区域内绘制文本的高度
     * @mPaint 绘制改文本的画笔
     */
    public static int getBaseLineY(Paint mPaint, int parentHeight) {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (int) (parentHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2);
    }
}

