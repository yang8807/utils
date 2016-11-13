package com.magnify.yutils.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * @author heinigger
 */
public class SPUtil {
    private static final String FILE_NAME = "preferences_";
    private static Gson mGson;
    //截取首字母的个数,截取的个数越多,生成的文件个数越多:经过md5加密,可能生成文件的个数36*36
    private static final int SEPARATESIZE = 2;

    static {
        mGson = GsonConvertUtils.getGson();
    }

    private static SharedPreferences getPreferences(Context context, String fristCharacter) {
        //prefereneces,取keyname的头一个字,组成SharePreference的文件名,防止一个Preference保存的数据太多,读取速率下降或者文件太大,导致内存溢出
        return context.getSharedPreferences(FILE_NAME + fristCharacter.toLowerCase(), Context.MODE_PRIVATE);
    }

    /***
     * 添加
     */
    public static SharedPreferences getPreferences(Context context, String fileNameExtends, String fristCharacter) {
        //prefereneces,取keyname的头一个字,组成SharePreference的文件名,防止一个Preference保存的数据太多,读取速率下降或者文件太大,导致内存溢出
        return context.getSharedPreferences(FILE_NAME + fileNameExtends + fristCharacter.toLowerCase(), Context.MODE_PRIVATE);
    }

    /**
     * Universal way to save the data
     * Can be used to save a little data, you better don't save larger data such as collection
     */
    public static void save(Context context, String keyName, Object value) {
        SPUtil.save(context, "", keyName, value);
    }

    /***
     * 带扩展名的
     */
    public static void save(Context context, String fileNameExtends, String keyName, Object value) {
        if (value == null || TextUtils.isEmpty(keyName)) return;
        keyName = MD5Util.getMessageDigest(keyName.getBytes());
        SharedPreferences.Editor editor = getPreferences(context, fileNameExtends, separateRule(keyName)).edit();
        editor.putString(keyName, mGson.toJson(value));
        editor.commit();
    }

    /**
     * 切割规则,这里是从开始进行切割;之后要将这里改成从中间进行切割,这样,存储的才比较均匀
     */
    @NonNull
    private static String separateRule(String keyName) {
        return keyName.substring(0, SEPARATESIZE);
    }

    /**
     * 获取数据
     */
    public static <E> E getValue(Context context, String keyName, TypeToken<E> typeToken) {
        if (TextUtils.isEmpty(keyName)) return null;
        keyName = MD5Util.getMessageDigest(keyName.getBytes());
        SharedPreferences sp = getPreferences(context, separateRule(keyName));
        String value = sp.getString(keyName, "");
        if (TextUtils.isEmpty(value)) return null;
        else {
            return mGson.fromJson(value, typeToken.getType());
        }
    }


    /*----------start:Part this several types out directly, ps: if you don't like this, will throw Class transfomt Exception--*/
    public static boolean getBoolean(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getBoolean(context, keyName + fileNameExtends);
    }

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

    public static int getInt(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getInt(context, keyName + fileNameExtends);
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

    public static long getLong(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getLong(context, keyName + fileNameExtends);
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

    public static float getFloat(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getFloat(context, keyName + fileNameExtends);
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

    public static double getDouble(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getDouble(context, keyName + fileNameExtends);
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

    public static String getString(String defaultValue, Context context, String keyName) {
        try {
            String value = getValue(context, keyName, new TypeToken<String>() {
            });
            return TextUtils.isEmpty(value) ? defaultValue : value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getString(Context context, String fileNameExtends, String keyName) {
        return SPUtil.getString(context, keyName + fileNameExtends);
    }
    /*----------end:his several types take out a special, will cast exception---------------*/
}
