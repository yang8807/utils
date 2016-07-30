package com.magnify.yutils.data;

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
     * Universal way to save the data
     * Can be used to save a little data, you better don't save larger data such as collection
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

    /*----------start:Part this several types out directly, ps: if you don't like this, will throw Class transfomt Exception--*/
    public static boolean getBoolean(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<Boolean>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getInt(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<Integer>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getLong(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<Long>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloat(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<Float>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public static double getDouble(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<Double>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    /*----------end:his several types take out a special, will cast exception---------------*/
}
