package com.magnify.yutils;

import android.util.Log;

import com.google.gson.Gson;

public class LogUtils {

    public static int DISABLE = 0x0;
    /**
     * 所有等级日志
     */
    public static int ALL = 0x3f;
    /**
     * 详细日志
     */
    public static int VERBOSE = 0x1;
    /**
     * 调试日志
     */
    public static int DEBUG = 0x2;
    /**
     * 信息日志
     */
    public static int INFO = 0x4;
    /**
     * 警告日志
     */
    public static int WARN = 0x8;
    /**
     * 错误日志
     */
    public static int ERROR = 0x10;
    /**
     * 断言日志
     */
    public static int ASSERT = 0x20;

    /**
     * 当前启用的日志等级(默认打开所有)
     */
    private static int LEVEL = ALL;
    private static Gson mGson;

    static {
        mGson = new Gson();
    }

    /**
     * 设置日志等级 例： LogUtil.setLevel(LogUtil.ALL);
     * LogUitl.setLevel(LogUtil.VERBOSE);
     *
     * @param level
     */
    public static void setLevel(int level) {
        LEVEL = level;
    }

    public static void v(String tag, Object... args) {
        if ((LEVEL & VERBOSE) > 0)
            log(Log.VERBOSE, tag, args);
    }

    public static void d(String tag, Object... args) {
        if ((LEVEL & DEBUG) > 0)
            log(Log.DEBUG, tag, args);
    }

    public static void i(String tag, Object... args) {
        if ((LEVEL & INFO) > 0)
            log(Log.INFO, tag, args);
    }

    public static void w(String tag, Object... args) {
        if ((LEVEL & WARN) > 0)
            log(Log.WARN, tag, args);
    }

    public static void e(String tag, Object... args) {
        if ((LEVEL & ERROR) > 0)
            log(Log.ERROR, tag, args);
    }

    public static void a(String tag, Object... args) {
        if ((LEVEL & ASSERT) > 0)
            log(Log.ASSERT, tag, args);
    }

    private static void log(int priority, String tag, Object... args) {
        Log.println(priority, tag, mGson.toJson(args));
    }
}
