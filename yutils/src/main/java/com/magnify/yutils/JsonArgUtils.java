package com.magnify.yutils;

import java.util.ArrayList;

/**
 * Created by 洒笑天涯 on 2016/1/1.
 */
public class JsonArgUtils<T> {

    private StringBuilder builder = new StringBuilder();
    private String _split = "\"";

    /***
     * @param parameter 参数的名称
     * @param args      需要添加的参数
     * @return 添加参数
     */
    public JsonArgUtils add(String parameter, T args) {
        builder.append(_split + parameter + _split + ":" + _split + String.valueOf(args) + _split + ",");
        return this;
    }
    /***
     * @return 添加json数据
     */
    public JsonArgUtils addJson(String parameter,String json) {
        builder.append(_split + parameter + _split + ":" +json+ ",");
        return this;
    }
    /***
     * @param parameter 参数的名称
     * @param arrayList  需要添加的参数
     * @return 添加集合
     */
    public JsonArgUtils add(String parameter, ArrayList<T> arrayList) {
        builder.append(_split+parameter+_split+":[");
        for (int i = 0; i < arrayList.size(); i++) {
            if (i != arrayList.size() - 1) {
                builder.append(_split + String.valueOf(arrayList.get(i)) + _split + ",");
            } else {
                builder.append(_split + String.valueOf(arrayList.get(i)) + _split);
            }
        }
        builder.append("],");
        return this;
    }

    public String toString() {
        String result = builder.toString();
        if (result.length() < 1) return "";
        return "{" + result.substring(0, result.length() - 1) + "}";
    }
}