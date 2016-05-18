package com.magnify.yutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IntentUtil {

	/**
	 * ��ȡ����Intent��Ӧ������Activity��Ϣ�б�
	 */
	public static List<ResolveInfo> queryIntentActivities(Context context, Intent intent) {
		return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	}

	/**
	 * ��װApk
	 */
	public static Intent installApk(File apkFile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		return intent;
	}

	/**
	 * ����ϵͳ���Ź���
	 *
	 * @param phoneNumber �绰����
	 * @param autoCall �Ƿ��Զ���ʼ����
	 *  �����֧�ִ�绰����null(һ��ƽ�岻֧�ִ�绰)
	 */
	public static Intent phoneCall(Context context, String phoneNumber, boolean autoCall) {
		Intent intent;
		if (autoCall) {// �Զ�����
			intent = new Intent(Intent.ACTION_CALL);
		} else {// �򿪲��Ž��治�Զ�����
			intent = new Intent(Intent.ACTION_DIAL);
		}
		intent.setData(Uri.parse("tel:" + phoneNumber));
		List<ResolveInfo> list = queryIntentActivities(context, intent);
		if(list.isEmpty()){
			return null;
		}
		return intent;
	}

	/**
	 * ����ϵͳ�����Ź���
	 *
	 * @param phoneNumber �绰����
	 */
	public static Intent sendSMS(Context context, String phoneNumber) {
		Intent intent = new Intent();
		// ϵͳĬ�ϵ�action��������Ĭ�ϵĶ��Ž���
		intent.setAction(Intent.ACTION_SENDTO);
		// ��Ҫ����Ϣ�ĺ���
		intent.setData(Uri.parse("smsto:" + phoneNumber));
		List<ResolveInfo> list = queryIntentActivities(context, intent);
		if(list.isEmpty()){
			return null;
		}
		return intent;
	}

	/**
	 * ��Google Play��ĳ��Ӧ�õ�ҳ��
	 *
	 * @param packageName Ӧ�ð���
	 */
	public static Intent googlePlay(String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		return new Intent(Intent.ACTION_VIEW, uri);
	}

	/**
	 * ��������<br>
	 * ��ѡ����Ӧ�ã� startActivity(Intent.createChooser(intent, "��ѡ��"));<br>
	 * Ҳ����ָ��ĳ��Ӧ�ã� intent.setPackage(packageName);
	 *
	 * @param text ��������
	 * @param subject ����(��ЩӦ�û����)
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
	 * ����ͼƬ
	 *
	 * @param uri ����ͼƬ��Uri.fromFile(file)����ContentResolver��ȡ
	 */
	public static Intent shareImage(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	/**
	 * ����ͼƬ������
	 *
	 * @param uri ����ͼƬ��Uri.fromFile(file)����ContentResolver��ȡ
	 * @param text ��ʱ��ЩӦ�û��������
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
	 * ������ͼƬ
	 */
	public static Intent shareImage(ArrayList<Uri> uris) {
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	/**
	 * ѡ��ϵͳͼƬ
	 */
	public static Intent imagePick() {
		return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	}

	/**
	 * ����ϵͳ���
	 */
	public static Intent camera() {
		return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	}

	/**
	 * ����ϵͳ���
	 *  ������Ƭ������ļ�Uri
	 */
	public static Intent camera(Uri fileUri) {
		Intent intent = camera();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		return intent;
	}

	/**
	 * ����ϵͳ���
	 *  ������Ƭ������ļ�
	 */
	public static Intent camera(File file) {
		Intent intent = camera();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		return intent;
	}

	/**
	 * ���͹㲥��֪ͨϵͳ����ļ���ý��⣬���������պ�����Ƭ������ͼ��
	 */
	public static Intent addToAlbum(File file) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(Uri.fromFile(file));
		return intent;
	}
}
