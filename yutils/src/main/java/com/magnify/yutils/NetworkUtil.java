package com.magnify.yutils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * ���繤����
 */
public class NetworkUtil {

	/**
	 * ��õ�ǰ��������Ϣ
	 * context
	 *
	 */
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo();
	}

	/**
	 * ��õ�ǰ����������,����ֵ(0:�ƶ�����,1:Wifi����,-1:û�п�������)
	 * context
	 *
	 */
	private static int getNetworkType(Context context) {
		NetworkInfo networkInfo = getNetworkInfo(context);
		return (networkInfo == null || !networkInfo.isConnected()) ? -1 : networkInfo.getType();
	}

	/**
	 * �Ƿ���Ի������
	 * context
	 *
	 */
	public static boolean isAvailable(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.isAvailable();
	}

	/**
	 * �����Ƿ�������
	 * context
	 *
	 */
	public static boolean isConnected(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.isConnected();
	}

	/**
	 * �����Ƿ�����������
	 * context
	 *
	 */
	public static boolean isConnecting(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.getState()==NetworkInfo.State.CONNECTING;
	}

	/**
	 * ��ǰ����ʹ��wifi
	 * context
	 *
	 */
	public static boolean isWifi(Context context){
		return getNetworkType(context)==ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * ��ǰ����ʹ���ƶ�����(2G,3G)
	 * context
	 *
	 */
	public static boolean isMobile(Context context){
		return getNetworkType(context)==ConnectivityManager.TYPE_MOBILE;
	}

	/**
	 * ����ƶ��������
	 * context
	 *
	 */
	public static Proxy getAPNProxy(Context context,Proxy.Type type){
		InetSocketAddress address = getAPNInetSocketAddress(context);
		if(address==null) return null;
		return new Proxy(type, address);
	}

	/**
	 * ����ƶ������ַ
	 * context
	 *
	 */
	public static InetSocketAddress getAPNInetSocketAddress(Context context) {
		final Uri uri = Uri.parse("content://telephony/carriers/preferapn");
		final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			final String address = cursor.getString(cursor.getColumnIndex("proxy"));
			final String port = cursor.getString(cursor.getColumnIndex("port"));
			if (address != null && address.trim().length() > 0)
				return new InetSocketAddress(address, Integer.getInteger(port, 80));
		}
		return null;
	}
}
