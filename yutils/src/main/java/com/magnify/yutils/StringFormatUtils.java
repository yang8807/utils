package com.magnify.yutils;

import android.text.Spanned;

import com.magnify.yutils.data.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/10/14.
 */

public class StringFormatUtils {
    private ArrayList<FormatSpanned> formatDatas = new ArrayList<>();

    /**
     * 数据是空,就不反回了,格式化,直接没有该单位
     */
    public StringFormatUtils formatEmpty(String text, Object object) {
        if (object == null) return this;
        formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(object)), 0));
        return this;
    }

    /**
     * 格式化的时候带默认值,只能是%s
     */
    public StringFormatUtils format(String text, Object object, Object defaultValue) {
        if (object == null) {
            formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(defaultValue)), 0));
        } else
            formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(object)), 0));
        return this;
    }

    /**
     * 带颜色的格式化,并且数据object是空的话,格式化的字符串,直接不带有本单位
     */
    public StringFormatUtils formatEmptyColor(String text, Object object, int color) {
        if (object == null) return this;
        formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(object)), color));
        return this;
    }

    public StringFormatUtils formatColor(String text, Object object, Object defaultValue, int color) {
        if (object == null) {
            formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(defaultValue)), color));
        } else
            formatDatas.add(new FormatSpanned(String.format(text, String.valueOf(object)), color));
        return this;
    }

    /**
     * 创建带有颜色的数组
     */
    public Spanned createSpanned() {
        StringBuilder stringBuilder = new StringBuilder();
        int[] colors = new int[getAvaliableColorCount()];
        for (int i = 0; i < formatDatas.size(); i++) {
            FormatSpanned formatSpanned = formatDatas.get(i);
            stringBuilder.append(formatSpanned.formatText);
        }
        return StringUtil.formatColor(stringBuilder.toString(), colors);
    }

    /**
     * 得到不需要颜色字符串,最后一步调用
     */
    public String createString() {
        if (formatDatas != null && !formatDatas.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < formatDatas.size(); i++) {
            stringBuilder.append(formatDatas.get(i).formatText);
        }
        return stringBuilder.toString();
    }

    public int getAvaliableColorCount() {
        List<Integer> interger = new ArrayList<>();

        for (int i = 0; i < formatDatas.size(); i++) {
            int color = formatDatas.get(i).color;
            if (color != 0) interger.add(color);
        }
        return interger.size();
    }

    private class FormatSpanned {
        int color;
        String formatText;

        public FormatSpanned(String formatText, int color) {
            this.color = color;
            this.formatText = formatText;
        }
    }

}
