package com.magnify.yutils.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PreferencesUtil {

    private static final String FILE_NAME = "preferences_";
    private static Gson mGson;
    //截取首字母的个数,截取的个数越多,生成的文件个数越多
    private static final int SEPARATESIZE = 1;

    static {
        mGson = new Gson();
    }

    private static SharedPreferences getPreferences(Context context, String fristCharacter) {
        //prefereneces,取keyname的头一个字,组成SharePreference的文件名,防止一个Preference保存的数据太多,读取速率下降或者文件太大,导致内存溢出
        return context.getSharedPreferences(FILE_NAME + fristCharacter.toLowerCase(), Context.MODE_PRIVATE);
    }

    /**
     * Universal way to save the data
     * Can be used to save a little data, you better don't save larger data such as collection
     */
    public static void save(Context context, String keyName, Object value) {
        if (value == null || TextUtils.isEmpty(keyName)) return;
        SharedPreferences.Editor editor = getPreferences(context, keyName.substring(0, SEPARATESIZE)).edit();
        editor.putString(keyName, mGson.toJson(value));
        editor.commit();
    }

    /**
     * 获取数据
     */
    public static <E> E getValue(Context context, String keyName, TypeToken<E> typeToken) {
        if (TextUtils.isEmpty(keyName)) return null;
        SharedPreferences sp = getPreferences(context, keyName.substring(0, SEPARATESIZE));
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

    public static String getString(Context context, String keyName) {
        try {
            return getValue(context, keyName, new TypeToken<String>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /*----------end:his several types take out a special, will cast exception---------------*/
}
