package com.magnify.yutils;

import android.util.Log;

public class LogUtil {

	public static int DISABLE = 0x0;
	/**
	 * ���еȼ���־
	 */
	public static int ALL = 0x3f;
	/**
	 * ��ϸ��־
	 */
	public static int VERBOSE = 0x1;
	/**
	 * ������־
	 */
	public static int DEBUG = 0x2;
	/**
	 * ��Ϣ��־
	 */
	public static int INFO = 0x4;
	/**
	 * ������־
	 */
	public static int WARN = 0x8;
	/**
	 * ������־
	 */
	public static int ERROR = 0x10;
	/**
	 * ������־
	 */
	public static int ASSERT = 0x20;

	/**
	 * ��ǰ���õ���־�ȼ�(Ĭ�ϴ�����)
	 */
	private static int LEVEL = ALL;

	/**
	 * ������־�ȼ� ���� LogUtil.setLevel(LogUtil.ALL);
	 * LogUitl.setLevel(LogUtil.VERBOSE);
	 *
	 * @param level
	 */
	public static void setLevel(int level) {
		LEVEL = level;
	}

	public static void v(String tag, String msg, Object... args) {
		if ((LEVEL & VERBOSE) > 0)
			log(Log.VERBOSE, tag, msg, args);
	}

	public static void d(String tag, String msg, Object... args) {
		if ((LEVEL & DEBUG) > 0)
			log(Log.DEBUG, tag, msg, args);
	}

	public static void i(String tag, String msg, Object... args) {
		if ((LEVEL & INFO) > 0)
			log(Log.INFO, tag, msg, args);
	}

	public static void w(String tag, String msg, Object... args) {
		if ((LEVEL & WARN) > 0)
			log(Log.WARN, tag, msg, args);
	}

	public static void e(String tag, String msg, Object... args) {
		if ((LEVEL & ERROR) > 0)
			log(Log.ERROR, tag, msg, args);
	}

	public static void a(String tag, String msg, Object... args) {
		if ((LEVEL & ASSERT) > 0)
			log(Log.ASSERT, tag, msg, args);
	}

	private static void log(int priority, String tag, String message, Object... args) {
		if (args.length > 0) {
			message = String.format(message, args);
		}

		Log.println(priority, tag, message);
	}
}
