package com.magnify.yutils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IntentUtil {


    /**
     * 微信包名
     */
    private static final String WEIXIN_PACKAGE = "com.tencent.mm";
    private static final String QQ_PACKAGE = "com.tencent.mobileqq";

    /**
     * 微信分享到朋友圈
     */
    private static final String WEIXIN_SHARE_TIME_LINE_COMPONENT = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    /**
     * 微信分享给朋友
     */
    private static final String WEIXIN_SHARE_FRIENDS_COMPONENT = "com.tencent.mm.ui.tools.ShareImgUI";


    /**
     * 获取符合Intent响应条件的Activity信息列表
     */
    public static List<ResolveInfo> queryIntentActivities(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    /**
     * 安装Apk
     */
    public static Intent installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 调用系统拨号功能
     *
     * @param phoneNumber 电话号码
     * @param autoCall    是否自动开始拨号
     *                    如果不支持打电话返回null(一般平板不支持打电话)
     */
    public static Intent phoneCall(Context context, String phoneNumber, boolean autoCall) {
        Intent intent;
        if (autoCall) {// 自动拨号
            intent = new Intent(Intent.ACTION_CALL);
        } else {// 打开拨号界面不自动拨号
            intent = new Intent(Intent.ACTION_DIAL);
        }
        intent.setData(Uri.parse("tel:" + phoneNumber));
        List<ResolveInfo> list = queryIntentActivities(context, intent);
        if (list.isEmpty()) {
            return null;
        }
        return intent;
    }

    /**
     * 调用系统发短信功能
     *
     * @param phoneNumber 电话号码
     */
    public static Intent sendSMS(Context context, String phoneNumber) {
        Intent intent = new Intent();
        // 系统默认的action，用来打开默认的短信界面
        intent.setAction(Intent.ACTION_SENDTO);
        // 需要发短息的号码
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        List<ResolveInfo> list = queryIntentActivities(context, intent);
        if (list.isEmpty()) {
            return null;
        }
        return intent;
    }


    /**
     * 分享文字<br>
     * 可选择多个应用： startActivity(Intent.createChooser(intent, "请选择"));<br>
     * 也可以指定某个应用： intent.setPackage(packageName);
     *
     * @param text    文字内容
     * @param subject 主题(有些应用会忽略)
     */
    public static Intent shareText(String text, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 分享图片
     *
     * @param uri 本地图片，Uri.fromFile(file)或由ContentResolver获取
     */
    public static Intent shareImage(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 分享图片和文字
     *
     * @param uri  本地图片，Uri.fromFile(file)或由ContentResolver获取
     * @param text 此时有些应用会忽略文字
     */
    public static Intent shareImage(Uri uri, String text, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 分享多个图片
     */
    public static Intent shareImage(ArrayList<Uri> uris) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 选择系统图片
     */
    public static Intent imagePick() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * 调用系统相机
     */
    public static Intent camera() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    /**
     * 调用系统相机
     * 拍摄照片保存的文件Uri
     */
    public static Intent camera(Uri fileUri) {
        Intent intent = camera();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        return intent;
    }

    /**
     * 调用系统相机
     * 拍摄照片保存的文件
     */
    public static Intent camera(File file) {
        Intent intent = camera();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    public static Intent CropIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 发送广播以通知系统添加文件到媒体库，常用于拍照后让照片出现在图库
     */
    public static Intent addToAlbum(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        return intent;
    }

    /**
     * 跳转到系统设置页面
     */
    public static Intent goSystemSetting(Uri uri) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + "com.eelly.seller"));
    }

    public static Intent goSystemSetting(String packageName) {
//        "package:" + "com.eelly.seller"
        return goSystemSetting(Uri.parse("package:" + packageName));
    }

    /*前往应用市场*/
    public static void goGooglePlay(Context context) {
        Intent marketIntent = IntentUtil.googlePlay(context.getPackageName());
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketIntent.resolveActivity(context.getPackageManager()) != null) { // 判断应用是否存在
            context.startActivity(marketIntent);
        } else {
            ToastUtil.show(context, "请先安装应用市场");
        }
    }

    /**
     * 打开Google Play上某个应用的页面
     *
     * @param packageName 应用包名
     */
    public static Intent googlePlay(String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * 分享图片到朋友圈
     */
    public static boolean share2WeChatCircle(Context context, File file) {
        boolean isAvliable = isAvailable(context, WEIXIN_PACKAGE);
        if (isAvliable) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(WEIXIN_PACKAGE, WEIXIN_SHARE_TIME_LINE_COMPONENT));
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            context.startActivity(intent);
        } else {
            ToastUtil.show(context, "请安装微信");
        }
        return isAvliable;
    }

    /**
     * 分享多张图片到朋友圈
     */
    public static boolean share2WeChatCircle(Context context, ArrayList<File> files) {
        boolean isAvliable = isAvailable(context, WEIXIN_PACKAGE);
        if (isAvliable) {
            ArrayList<Uri> uris = new ArrayList<>();
            for (File image : files) {
                Uri uri = Uri.fromFile(image);
                uris.add(uri);
            }
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(WEIXIN_PACKAGE, WEIXIN_SHARE_TIME_LINE_COMPONENT));
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            context.startActivity(intent);
        } else {
            ToastUtil.show(context, "请安装微信");
        }
        return isAvliable;
    }

    /**
     * 直接分享图片给好友
     */
    public static boolean share2WeChatFriend(Context context, File file) {
        boolean isAvliable = isAvailable(context, WEIXIN_PACKAGE);
        if (isAvliable) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(WEIXIN_PACKAGE, WEIXIN_SHARE_FRIENDS_COMPONENT));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            context.startActivity(intent);
        } else {
            ToastUtil.show(context, "请安装微信");
        }
        return isAvliable;
    }

    /**
     * 分享图片文件给qq
     */
    public static boolean share2QQ(Context context, File file) {
        boolean isAvaliable = isAvailable(context, QQ_PACKAGE);
        if (isAvaliable) {
            Intent shareIntent = IntentUtil.shareImage(Uri.fromFile(file));
            shareIntent.setPackage("com.tencent.mobileqq");
            ResolveInfo ri = context.getPackageManager().resolveActivity(shareIntent, 0);
            context.startActivity(shareIntent);
        }
        return isAvaliable;
    }

    /**
     * 判断是否有这个包名
     *
     * @param packageName
     * @return
     */
    public static boolean isAvailable(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pInfo.size(); i++) {
            if (pInfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 打开一个网页,类别是Intent.ACTION_VIEW
     */
    public Intent openBrowser(String url) {
//        Uri uri = Uri.parse("http://www.baidu.com/");
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * 打开一个网页,类别是Intent.ACTION_VIEW
     */
    public Intent openBrowser(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * 打开地图并定位到一个点
     */
    public Intent openMap(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * 打开地图并定位到一个点
     */
    public Intent openMap(Point point) {
        Uri uri = Uri.parse("geo:" + point.x + ":" + point.y);
//        Uri uri = Uri.parse("geo:52.76,-79.0342");
        return new Intent(Intent.ACTION_VIEW, uri);
    }
    /**
     * 卸载应用
     * */
    public Intent deleteApp(String packName) {
        Uri uri = Uri.fromParts("package", packName, null);
        return new Intent(Intent.ACTION_DELETE, uri);
    }


}
