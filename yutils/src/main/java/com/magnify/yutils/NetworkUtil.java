package com.magnify.yutils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * 网络工具类
 */
public class NetworkUtil {

	/**
	 * 获得当前的网络信息
	 * context
	 *
	 */
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo();
	}

	/**
	 * 获得当前的网络类型,返回值(0:移动网络,1:Wifi网络,-1:没有可用网络)
	 * context
	 *
	 */
	private static int getNetworkType(Context context) {
		NetworkInfo networkInfo = getNetworkInfo(context);
		return (networkInfo == null || !networkInfo.isConnected()) ? -1 : networkInfo.getType();
	}

	/**
	 * 是否可以获得网络
	 * context
	 *
	 */
	public static boolean isAvailable(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.isAvailable();
	}

	/**
	 * 网络是否已连接
	 * context
	 *
	 */
	public static boolean isConnected(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.isConnected();
	}

	/**
	 * 网络是否正在连接中
	 * context
	 *
	 */
	public static boolean isConnecting(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		return netinfo==null?false:netinfo.getState()==NetworkInfo.State.CONNECTING;
	}

	/**
	 * 当前正在使用wifi
	 * context
	 *
	 */
	public static boolean isWifi(Context context){
		return getNetworkType(context)==ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 当前正在使用移动网络(2G,3G)
	 * context
	 *
	 */
	public static boolean isMobile(Context context){
		return getNetworkType(context)==ConnectivityManager.TYPE_MOBILE;
	}

	/**
	 * 获得移动网络代理
	 * context
	 *
	 */
	public static Proxy getAPNProxy(Context context,Proxy.Type type){
		InetSocketAddress address = getAPNInetSocketAddress(context);
		if(address==null) return null;
		return new Proxy(type, address);
	}

	/**
	 * 获得移动网络地址
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
