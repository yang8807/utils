package com.magnify.yutils.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by heinigger on 16/10/8.
 * //写一个工具类,避免频繁的创建gson对象
 */

public class GsonConvertUtils<T> {

    private static Gson mGson;
    private static JsonParser mJsonPar;

    static {
        mGson = new Gson();
    }

    public static String toJson(Object obj) {
        if (obj == null) return "";
        return mGson.toJson(obj);
    }

    public static JsonElement toJsonTree(List obj) {
        if (obj == null) return null;
        return mGson.toJsonTree(obj);
    }

    public T fromJson(String text, TypeToken<T> mTypeToken) {
        return mGson.fromJson(text, mTypeToken.getType());
    }

    public static Gson getGson() {
        return mGson;
    }

    public static JsonParser getJsonParser() {
        if (mJsonPar == null) mJsonPar = new JsonParser();
        return mJsonPar;
    }
}
