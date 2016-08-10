package com.magnify.yutils.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
/**
 * 代表当前应用程序
 */
public class BaseApplication extends Application {
	private static BaseApplication application;
	private static int mainTid;
	private static Handler handler;
	private static Context context;

	@Override
//  在主线程运行的
	public void onCreate() {
		super.onCreate();
		context = super.getApplicationContext();
		application=this;
		mainTid = android.os.Process.myTid();
		handler=new Handler();
	}
	public static Context getApplication() {
		return application;
	}
	public static int getMainTid() {
		return mainTid;
	}
	public static Handler getHandler() {
		return handler;
	}
	public static Context getAppContext() {
		return context;
	}

}