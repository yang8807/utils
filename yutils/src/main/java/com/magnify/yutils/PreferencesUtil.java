package com.magnify.yutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PreferencesUtil {

    private static final String FILE_NAME = "preferences";
    private static Gson mGson;

    static {
        mGson = new Gson();
    }

    /**
     * 获取SharedPreferences实例
     *
     * @param context
     * @param needUserId 保存的信息是否与用户相关
     * @return
     */
    private static SharedPreferences getPreferences(Context context, boolean needUserId) {
        String uid = "0";
        /*if (needUserId) {
            try {
                uid = AccountManager.getInstance().getUserId();
            } catch (Exception e) {
            }
        }*/
        return context.getSharedPreferences(FILE_NAME + uid, Context.MODE_PRIVATE);
    }

    /**
     * 通用保存数据方法;
     * 只能用来保存一些小的数据,集合之类的,还是不要保存了;
     */
    public static void save(Context context, String keyName, Object value) {
        if (value == null || TextUtils.isEmpty(keyName)) return;
        SharedPreferences.Editor editor = getPreferences(context, false).edit();
        editor.putString(keyName, mGson.toJson(value));
        editor.commit();
    }

    /**
     * 获取数据
     */
    public static <E> E getValue(Context context, String keyName, TypeToken<E> typeToken) {
        if (TextUtils.isEmpty(keyName)) return null;
        SharedPreferences sp = getPreferences(context, false);
        String value = sp.getString(keyName, "");
        if (TextUtils.isEmpty(value)) return null;
        else {
            return mGson.fromJson(value, typeToken.getType());
        }
    }

    /*----------start:这几种类型直接取出,ps:部分如果不这样写的话,会报转换异常---------------*/
    public static boolean getBoolean(Context context, String keyName) {
        return getValue(context, keyName, new TypeToken<Boolean>() {
        });
    }

    public static int getInt(Context context, String keyName) {
        return getValue(context, keyName, new TypeToken<Integer>() {
        });
    }

    public static float getFloat(Context context, String keyName) {
        return getValue(context, keyName, new TypeToken<Float>() {
        });
    }

    public static double getDouble(Context context, String keyName) {
        return getValue(context, keyName, new TypeToken<Double>() {
        });
    }
    /*----------end:这几种类型取出比较特殊,会报转换异常---------------*/
}
