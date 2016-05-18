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
	 * ����Toast��Ϣ
	 *
	 * @param context ������
	 * @param msg Ҫ��ʾ����Ϣ
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
	 * ����Toast��Ϣ
	 *
	 * @param context ������
	 * @param msg Ҫ��ʾ����Ϣ
	 * @param time ��ʧ��ʾʱ��
	 */
	public static void show(Context context, String msg, int time) {
		Toast toast = getToast(context);
		toast.setText(msg);
		toast.setDuration(time);
		toast.show();
	}
}
