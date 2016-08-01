package com.magnify.yutils.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by heinigger on 16/7/30.
 */
public class SaveTool {
    private String FILE_NAME = "preferences_data";
    private Gson mGson;
    private Context context;

    public SaveTool(Context context) {
        mGson = new Gson();
        this.context = context;
    }

    public SaveTool(Context context, String fileName) {
        mGson = new Gson();
        FILE_NAME = fileName;
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Universal way to save the data
     * Can be used to save a little data, you better don't save larger data such as collection
     */
    public SaveTool save(String keyName, Object value) {
        if (value == null || TextUtils.isEmpty(keyName)) return this;
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(keyName, mGson.toJson(value));
        editor.commit();
        return this;
    }

    /**
     * get the save data
     */
    public <E> E getValue(String keyName, TypeToken<E> typeToken) {
        if (TextUtils.isEmpty(keyName)) return null;
        SharedPreferences sp = getPreferences();
        String value = sp.getString(keyName, "");
        if (TextUtils.isEmpty(value)) return null;
        else {
            return mGson.fromJson(value, typeToken.getType());
        }
    }

    /*----------start:Part this several types out directly, ps: if you don't like this, will throw Class transfomt Exception--*/
    public boolean getBoolean(String keyName) {
        try {
            return getValue(keyName, new TypeToken<Boolean>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getInt(String keyName) {
        try {
            return getValue(keyName, new TypeToken<Integer>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getLong(String keyName) {
        try {
            return getValue(keyName, new TypeToken<Long>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float getFloat(String keyName) {
        try {
            return getValue(keyName, new TypeToken<Float>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public double getDouble(String keyName) {
        try {
            return getValue(keyName, new TypeToken<Double>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    /*----------end:his several types take out a special, will cast exception---------------*/
}
