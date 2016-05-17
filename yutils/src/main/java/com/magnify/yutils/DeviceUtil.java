package com.magnify.yutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class DeviceUtil {

    /**
     * �ж�ָ�����������ڽ����Ƿ�Ӧ��������
     *
     * @return true:Ӧ�������� false:��Ӧ����صĶ�������
     */
    public static boolean isAppMainProcess(Context context) {
        // ��ȡӦ�õ�ǰ������
        String processName = getCurrentProcessName(context.getApplicationContext());
        String appProcessName = context.getApplicationInfo().processName;
        // �����������Ӧ�õ���������(Ӧ������������AndroidManifext.xml�е�<appliction
        // android:process="">���Զ���,Ĭ�Ϻ�PackageName������ͬ)��ͬ˵����Ӧ��������,����ͬ˵����һ��������Ӧ�ý���(��AndroidManifext.xml��������android:process���Ե�Activity��Service��receiver��provider)
        return appProcessName.equals(processName);
    }

    /**
     * ��ȡ��ǰ���������ڽ�������
     */
    public static String getCurrentProcessName(Context context) {
        // ��ǰ����id
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * ��ȡActivity��Ϣ
     * @return ��ȡʧ�ܷ���null
     */
    public static ActivityInfo getActivityInfo(Context context, String className) {
        ComponentName cn = new ComponentName(context, className);
        try {
            return context.getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * ��ȡActivity�����ļ�label����
     *
     * @param context ������
     * @param className Ҫ��ȡ��Activity����������
     * @return ��ȡʧ�ܷ���null
     */
    public static String getActivityLabel(Context context, String className) {
        ActivityInfo aInfo = getActivityInfo(context, className);
        if (aInfo == null) {
            return null;
        } else {
            // return aInfo.loadLabel(context.getPackageManager()).toString();
            // �������ϱ�һ�д���㶨�ģ��������activityû��label���᷵��application��label���±ߵĴ������loadLabel��û��labelʱ����className
            if (aInfo.nonLocalizedLabel != null) {
                return aInfo.nonLocalizedLabel.toString();
            }
            if (aInfo.labelRes != 0) {
                CharSequence label = context.getPackageManager().getText(aInfo.packageName, aInfo.labelRes, aInfo.applicationInfo);
                if (label != null) {
                    return label.toString().trim();
                }
            }
            return className;
        }
    }

    /**
     * ��ȡ�ֻ�IMEI:
     * IMEI(International Mobile Equipment Identity)�ǹ����ƶ��豸��������д
     */
    public static String getIMEI(Context appContext) {

        return ((TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

    }

    /**
     * �����ֻ��ķֱ��ʴ� dip �ĵ�λ ת��Ϊpx
     */
    public static int dipToPx(Context appContext, float dpValue) {
        return (int) (dpValue * appContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * �����ֻ��ķֱ��ʴ� px �ĵ�λ ת��Ϊdip
     */
    public static int px2Dip(Context appContext, float pxValue) {
        return (int) (pxValue / appContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * ��pxֵת��Ϊspֵ����֤���ִ�С����
     *
     * @param context ��DisplayMetrics��������scaledDensity��
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * ��spֵת��Ϊpxֵ����֤���ִ�С����
     *
     * @param context ��DisplayMetrics��������scaledDensity��
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * ��ȡ��ʾ�����ش�С
     */
    @SuppressLint("NewApi")
    public static Point getDisplaySize(Context appContext) {
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
            point.set(display.getWidth(), display.getHeight());
        } else {
            display.getSize(point);
        }
        return point;
    }

    /**
     * �������뷨
     *
     * @param context activity��context
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // ����û��focus
            // imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * ��ʾ���뷨
     *
     * @param view ���������view
     */
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * �Զ���ʱ���������
     *
     * @param lateTimer
     */
    public static void showKeyboardLate(final View view, int lateTimer) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, 0);
            }

        }, lateTimer);
    }

    /**
     * ��ȡӦ����Ϣ
     */
    public static PackageInfo getPackageInfo(Context context) {
        // ��ȡpackagemanager��ʵ��
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * ��ȡAndroidManifest.xml�����õ�Ӧ����
     */
    public static String getAppName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo == null) {
            return "";
        }
        return context.getString(packInfo.applicationInfo.labelRes);
    }

    /**
     * ��ȡ��ǰSDK��ϵͳ�汾
     */
    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * ��ȡ״̬���ĸ߶�
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * ActionBar�ĸ߶�
     */
    public static float getActionBarHeight(Context appContext) {
        TypedArray actionbarSizeTypedArray = appContext.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        float height = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        return height;
    }

    /**
     * �����ı������а�
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void copyText(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * �ж�Ӧ������ǰ̨�����ں�̨����
     * @return ��̨true ǰ̨false
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * TODO::��ʱ��Ҫʹ�ñ�����,�����������˸��ƶ���,��Ϊ���˲��ԺͲο�ʹ��,�պ���Ҫ���ԸĽ� ��ȡ��ǰ�豸��ʾ��Ϣjson
     *
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
