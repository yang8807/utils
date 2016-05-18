package com.magnify.yutils;

import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;

public class StorageUtil {

	/**
	 * ��ȡ�û�Ŀ¼
	 *
	 */
	static public File getUsesrDir(){
		return Environment.getDataDirectory();
	}

	/**
	 * ����ⲿ�洢�豸�Ƿ��д
	 *
	 */
	static public boolean externalStorageWriteable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * ����ⲿ�洢�豸ָ��·���Ƿ��д
	 * @param path ���·��
	 *
	 */
	static public boolean externalStorageWriteable(File path){
		return EnvironmentCompat.getStorageState(path).equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * �����ⲿ�洢�豸״̬
	 *  one of MEDIA_UNKNOWN, MEDIA_REMOVED, MEDIA_UNMOUNTED, MEDIA_CHECKING, MEDIA_NOFS, MEDIA_MOUNTED, MEDIA_MOUNTED_READ_ONLY, MEDIA_SHARED, MEDIA_BAD_REMOVAL, or MEDIA_UNMOUNTABLE
	 */
	static public String getExternalStorageState(){
		return Environment.getExternalStorageState();
	}

	/**
	 * �����ⲿ�洢�豸ָ��·��״̬
	 *  one of MEDIA_UNKNOWN, MEDIA_REMOVED, MEDIA_UNMOUNTED, MEDIA_CHECKING, MEDIA_NOFS, MEDIA_MOUNTED, MEDIA_MOUNTED_READ_ONLY, MEDIA_SHARED, MEDIA_BAD_REMOVAL, or MEDIA_UNMOUNTABLE
	 */
	static public String getExternalStorageState(File path){
		return EnvironmentCompat.getStorageState(path);
	}

	/**
	 * ��ȡ���ػ���Ŀ¼
	 *  {@link File}
	 */
	static public File getDownloadCacheDir(){
		return Environment.getDownloadCacheDirectory();
	}

	/**
	 * ��ȡ���ô洢�豸Ŀ¼,���ܱ�����Ӧ�÷���,��������Ӧ��ж�ض�ɾ�� (����û��豸ÿ���û�����)
	 *
	 */
	static public File getExternalDir(){
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * ��ȡ���ô洢�豸Ŀ¼,���ܱ�����Ӧ�÷���,��������Ӧ��ж�ض�ɾ�� (����û��豸ÿ���û�����)
	 *
	 */
	static public File getExternalDir(String path){
		return new File(Environment.getExternalStorageDirectory().getPath(),path);
	}

	/**
	 * ��ȡ���ô洢���ù���Ŀ¼,�ɱ�����Ӧ�÷��ʣ���������Ӧ��ж�ض�ɾ�� (����û��豸ÿ���û�����)
	 * @param type
	 *
	 */
	static public File getExternalPublicDir(String type){
		return Environment.getExternalStoragePublicDirectory(type);
	}

	/**
	 * ϵͳ����������ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirALARMS(){
		return getExternalPublicDir(Environment.DIRECTORY_ALARMS);
	}

	/**
	 * ���������Ƭ����Ƶ�ı�׼Ŀ¼
	 *
	 */
	static public File getDirDCIM(){
		return getExternalPublicDir(Environment.DIRECTORY_DCIM);
	}

	/**
	 * ���ִ�ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirMUSIC(){
		return getExternalPublicDir(Environment.DIRECTORY_MUSIC);
	}

	/**
	 * ���صı�׼Ŀ¼
	 *
	 */
	static public File getDirDOWNLOADS(){
		return getExternalPublicDir(Environment.DIRECTORY_DOWNLOADS);
	}

	/**
	 * ͼƬ��ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirPICTURES(){
		return getExternalPublicDir(Environment.DIRECTORY_PICTURES);
	}

	/**
	 * ��Ӱ��ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirMOVIES(){
		return getExternalPublicDir(Environment.DIRECTORY_MOVIES);
	}

	/**
	 * ϵͳ֪ͨ������ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirNOTIFICATIONS(){
		return getExternalPublicDir(Environment.DIRECTORY_NOTIFICATIONS);
	}

	/**
	 * ϵͳ�㲥��ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirPODCASTS(){
		return getExternalPublicDir(Environment.DIRECTORY_PODCASTS);
	}

	/**
	 * ϵͳ������ŵı�׼Ŀ¼
	 *
	 */
	static public File getDirRINGTONES(){
		return getExternalPublicDir(Environment.DIRECTORY_RINGTONES);
	}

}
