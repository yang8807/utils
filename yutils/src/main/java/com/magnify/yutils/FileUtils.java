package com.magnify.yutils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * �ļ��������߰�
 *
 * @author xubing
 */
public class FileUtils {

	/**
	 * ��ȡ�ļ���չ��
	 *
	 * @return ��չ����null
	 */
	public static String getFileExt(String file) {
		if (file == null) {
			return null;
		}
		if (file.endsWith(".")) {
			return null;
		}
		int index = file.lastIndexOf(".");
		if (index == -1) {
			return null;
		}
		return file.substring(index + 1);
	}

	/**
	 * ���ֻ�дͼƬ
	 */
	public static void writeFile(byte[] buffer, String filePath) {
		File file = new File(filePath);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * �����Զ����ļ��������ļ��������ļ�·��
	 *
	 * @param folder �Զ����ļ���
	 * @param fileName �Զ����ļ���
	 */
	public static String getFilePath(Activity mActivity, String folder, String fileName) {

		String folderPath = "";
		if (hasSDCard()) {
			folderPath = getExtPath() + File.separator + folder + File.separator;
		} else {
			folderPath = getPackagePath(mActivity) + File.separator + folder + File.separator;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return folderPath + fileName;
	}

	/**
	 * �ж��Ƿ���sdcard
	 */
	public static boolean hasSDCard() {
		boolean b = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			b = true;
		}
		return b;
	}

	/**
	 * �õ�sdcard·��
	 */
	public static String getExtPath() {
		if (checkSaveLocationExists()) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return "";
	}

	/**
	 * �õ�/data/data/yanbin.imagedownloadĿ¼
	 */
	public static String getPackagePath(Activity mActivity) {
		return mActivity.getFilesDir().toString();
	}

	/**
	 * ����Ƿ�װSD��
	 */
	public static boolean checkSaveLocationExists() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ȡ�ļ�MD5
	 */
	public static String fileMD5(String filename) throws FileNotFoundException, IOException {
		FileInputStream fis = null;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		try {
			fis = new FileInputStream(filename);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				digest.update(buffer, 0, numRead);
			}
			fis.close();
			return StringUtil.toHexString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fis.close();
		}
		return null;
	}

	/**
	 * ɾ��Ŀ¼��������ļ�
	 */
	public static boolean deleteDirectory(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return false;
		}
		for (File temp : file.listFiles()) {
			if (temp.isFile()) {
				temp.delete();
			}
		}
		return true;
	}

	/**
	 * ���ش� file:// ǰ׺��·��
	 */
	public static String getSchemePath(File file) {
		// ���������ģ�����Ҫdecodeһ��
		return Uri.decode(Uri.fromFile(file).toString());
	}

	/**
	 * �����ļ�
	 *
	 * @param source Դ�ļ�
	 * @param target Ŀ���ַ
	 * @return �Ƿ�ɹ�
	 */
	public static boolean copyFile(File source, String target) {
		if (source == null || !source.exists() || !source.isFile() || target == null || target.length() == 0) {
			return false;
		}
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			File parent = new File(target).getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			byte[] buffer = new byte[1024];
			int count;
			while ((count = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, count);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			IOUtil.closeQuietly(inputStream);
			IOUtil.closeQuietly(outputStream);
		}
		return true;
	}

	/**
	 * �����ļ����ܴ�С
	 *
	 * @param dir
	 * @param containsSubDir �Ƿ�������ļ���
	 * @return ����򲻴��ڷ���0
	 */
	public static long getDirSize(File dir, boolean containsSubDir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return 0;
		}
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return 0;
		}
		long size = 0;
		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			} else if (file.isDirectory() && containsSubDir) {
				size += getDirSize(file, containsSubDir);
			}
		}
		return size;
	}
}