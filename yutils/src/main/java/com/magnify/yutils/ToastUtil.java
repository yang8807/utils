package com.magnify.yutils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast;

	@SuppressWarnings("static-access")
	private static Toast getToast(Context context) {
		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
		}
		mToast.setDuration(Toast.LENGTH_SHORT);
		return mToast;
	}

	/**
	 * 弹出Toast消息
	 *
	 * @param context 上下文
	 * @param msg 要显示的消息
	 */
	public static void show(Context context, String msg) {
		Toast toast = getToast(context);
		toast.setText(msg);
		toast.show();
	}

	/**
	 * @see #show(Context, String)
	 */
	public static void show(Context context, int msg) {
		Toast toast = getToast(context);
		toast.setText(msg);
		toast.show();
	}

	/**
	 * 弹出Toast消息
	 *
	 * @param context 上下文
	 * @param msg 要显示的消息
	 * @param time 消失显示时间
	 */
	public static void show(Context context, String msg, int time) {
		Toast toast = getToast(context);
		toast.setText(msg);
		toast.setDuration(time);
		toast.show();
	}
}
