package com.magnify.yutils.data;

import com.google.gson.JsonObject;

/**
 * Created by eelly on 2016/6/13.
 */
public class JsonUtil {
    private JsonObject jsonObject = new JsonObject();
    private String[] keys;

    public JsonUtil addKeys(String... args) {
        this.keys = args;
        return this;
    }

    public JsonUtil addValues(Object[] objects) {
        int count = keys.length > objects.length ? objects.length : keys.length;
        for (int i = 0; i < count; i++) {
            jsonObject.addProperty(keys[i], objects.toString());
        }
        return this;
    }

    public String toString() {
        return jsonObject.toString();
    }

    public JsonUtil addPty(String key, Object object) {
        jsonObject.addProperty(key, object.toString());
        return this;
    }
}
