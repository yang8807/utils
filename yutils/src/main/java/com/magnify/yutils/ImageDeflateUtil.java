package com.magnify.yutils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;


/**
 * ͼƬ��������<br>
 * �����ϴ�ͼƬʱ����СͼƬ������ͼƬ��������С�ļ���С
 *
 * @author ����
 */
public class ImageDeflateUtil {

	private static final String[] IMAGE_SUFFIXES = new String[] { "bmp", "jpg", "jpeg", "png" }; // ����gif

	/** ��顢ѹ��ѡ�� */
	private DeflateOptions mOptions;

	/** ��ѹ��ͼƬ�б� */
	private ArrayList<File> mDeflatedImages;

	/**
	 * ��Ĭ��ѡ�ѹ��ͼƬ�洢��ԭͼĿ¼
	 *
	 * @see #ImageDeflateUtil(DeflateOptions, File)
	 */
	public ImageDeflateUtil() {
		this(null, null);
	}

	/**
	 * @param options ��顢ѹ��ѡ�null��ȡĬ��ֵ
	 * @param tempPath ѹ��ͼƬ�洢Ŀ¼��null�򱣴���ͼƬͬĿ¼
	 */
	public ImageDeflateUtil(DeflateOptions options, File tempPath) {
		mOptions = options == null ? new DeflateOptions() : options;
		if (tempPath != null) {
			if (!tempPath.isDirectory())
				throw new IllegalArgumentException("tempPath should be a directory");
			if (!tempPath.exists())
				tempPath.mkdirs();
		}
		mOptions.outPath = tempPath;
		mDeflatedImages = new ArrayList<>();
	}

	/**
	 * ��顢ѹ��ѡ��<br>
	 * ��������ޣ�����Ӧֵ�ﵽ�趨ֵʱ��Ϊ�Ǵ�ͼƬ������֮���ǻ�Ĺ�ϵ��
	 */
	public static class DeflateOptions {

		/** �����ޣ��ļ���С������0�ż�� */
		public int inFileSize = 512000;

		/** �����ޣ�������������0�ż�� */
		public int inPixel = 512000;

		/** �����ޣ��߳�(�����)������0�ż�� */
		public int inSide = 800;

		/** ѹ��������߳�������0����Ч */
		public int outSide = 800;

		/** ѹ�����λͼģʽ�����Ҫ����ѹ���ȿ���Config.RGB_565 */
		public Config outConfig = Config.ARGB_8888;

		/** ѹ�����ͼƬ��ʽ */
		public CompressFormat outFormat = CompressFormat.JPEG;

		/** ѹ�����ͼƬ������0-100 */
		public int outQuality = 75;

		/** ѹ��ͼƬ�洢·�� */
		private File outPath;

	}

	/**
	 * ����ѹ������ͼƬ
	 */
	public ArrayList<File> getDeflatedImages() {
		return mDeflatedImages;
	}

	/**
	 * ɾ��ѹ������ͼƬ
	 */
	public void clean() {
		if (!mDeflatedImages.isEmpty()) {
			for (File file : mDeflatedImages) {
				file.delete();
			}
			mDeflatedImages.clear();
		}
	}

	/**
	 * �Ƿ�ͼƬ�ļ�<br>
	 * ����û��ContentResolver����Ϊ��ֻ��ʶcontent://��ͷ��uri��Ҳû���ļ�ͷ��Ϣ�жϣ���Ч�ʵ���ú�׺��
	 */
	public boolean isImage(File file) {
		if (file == null || !file.exists() || !file.isFile())
			return false;
		String name = file.getName();
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex < 0 || dotIndex == name.length() - 1) {
			return false;
		}
		String suffix = name.substring(dotIndex + 1, name.length()).toLowerCase(Locale.getDefault());
		for (int i = 0; i < IMAGE_SUFFIXES.length; i++) {
			if (suffix.equals(IMAGE_SUFFIXES[i]))
				return true;
		}
		return false;
	}

	/**
	 * ����Ƿ��ͼƬ<br>
	 */
	public boolean isLargeImage(File file) {
		if (!isImage(file))
			return false;
		if (mOptions.inFileSize > 0 && mOptions.inFileSize < file.length())
			return true;
		if (mOptions.inPixel <= 0 && mOptions.inSide <= 0)
			return false;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int w = options.outWidth;
		int h = options.outHeight;
		if (w <= 0 || h <= 0)
			return false;
		if (mOptions.inPixel > 0 && mOptions.inPixel < w * h)
			return true;
		if (mOptions.inSide > 0 && (mOptions.inSide < w || mOptions.inSide < h))
			return true;
		return false;
	}

	/**
	 * ����б�����û�д�ͼƬ
	 *
	 * @see #isLargeImage(File)
	 */
	public boolean hasLargeImage(List<File> files) {
		if (files != null && !files.isEmpty())
			for (File file : files)
				if (isLargeImage(file))
					return true;
		return false;
	}

	/**
	 * �����������û�д�ͼƬ
	 *
	 * @see #isLargeImage(File)
	 */
	public boolean hasLargeImage(File... files) {
		if (files != null && files.length > 0)
			for (File file : files)
				if (isLargeImage(file))
					return true;
		return false;
	}

	/**
	 * FileUploadAsyncTaskר�ã�����ϴ�ͼƬ��û�д�ͼƬ
	 *
	 * @see #isLargeImage(File)
	 */
	@SuppressWarnings("unchecked")
	public boolean hasLargeImage(HashMap<String, Object> uploadFileMap) {
		if (uploadFileMap == null || uploadFileMap.isEmpty())
			return false;
		for (Entry<String, Object> entry : uploadFileMap.entrySet()) {
			Object obj = entry.getValue();
			if (obj instanceof File) {
				if (isLargeImage((File) obj)) {
					return true;
				}
			} else {
				ArrayList<File> uploadFiles = (ArrayList<File>) obj;
				for (File file : uploadFiles) {
					if (isLargeImage(file)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * ѹ��ͼƬ���˲�����Ӧ�������߳�<br>
	 *
	 * @return ����ѹ�����ͼƬ������ѹ���Ļ�����ԭͼ��ʧ�ܵĻ�Ҳ����ԭͼ
	 */
	public File deflate(File input) {
		if (!isLargeImage(input))
			return input;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(input.getAbsolutePath(), options);
		int w = options.outWidth;
		int h = options.outHeight;
		int side = Math.max(w, h); // ���߳�

		options.inJustDecodeBounds = false;
		options.inPreferredConfig = mOptions.outConfig;

		// ���ƴ�С
		boolean zoom = mOptions.outSide > 0 && mOptions.outSide < side; // ����С
		if (zoom) { // ����SampleSize����С��Ŀ��ߴ��2�����ڣ���ֹͼƬ̫�����oom
			int power = 1;
			int side2 = mOptions.outSide << 1; // Ŀ��ߴ��2��
			while (side >= side2) { // �������Ŀ��ߴ��2�����ͼ�����С
				power <<= 1;
				side >>= 1;
				w >>= 1;
				h >>= 1;
			}
			options.inSampleSize = power;
		}
		Bitmap bitmap = BitmapFactory.decodeFile(input.getAbsolutePath(), options);
		if (bitmap == null)
			return input;
		if (zoom && side > mOptions.outSide) { // �ñ�������С
			float scale = 1F * mOptions.outSide / side;
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		}

		// ѹ������
		String fileName = UUID.randomUUID().toString() + ".jpg"; // ��Ƭ����
		File path = mOptions.outPath;
		if (path == null)
			path = input.getParentFile();
		File output = new File(path, fileName);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(output));
			bitmap.compress(mOptions.outFormat, mOptions.outQuality, bos);
			bos.flush();
			mDeflatedImages.add(output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		} finally {
			IOUtil.closeQuietly(bos);
			bitmap.recycle();
		}
	}

	/**
	 * ѹ��ͼƬ���˲�����Ӧ�������߳�<br>
	 *
	 * @return ����files���������ѹ��ͼƬ����files��ֱ�Ӹ���
	 * @see #deflate(File)
	 */
	public List<File> deflate(List<File> files) {
		if (files != null && !files.isEmpty())
			for (int i = 0; i < files.size(); i++)
				files.set(i, deflate(files.get(i)));
		return files;
	}

	/**
	 * ѹ��ͼƬ���˲�����Ӧ�������߳�<br>
	 *
	 * @return files���ɵ����顣�����ѹ��ͼƬ�������������ѹ�������ͼƬ
	 * @see #deflate(File)
	 */
	public File[] deflate(File... files) {
		if (files != null && files.length > 0)
			for (int i = 0; i < files.length; i++)
				files[i] = deflate(files[i]);
		return files;
	}

	/**
	 * FileUploadAsyncTaskר�ã�ѹ���ϴ��ļ���Ĵ�ͼƬ���˲�����Ӧ�������߳�
	 *
	 * @return ��������
	 * @see #deflate(File)
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> deflate(HashMap<String, Object> uploadFileMap) {
		if (uploadFileMap == null || uploadFileMap.isEmpty())
			return uploadFileMap;
		for (Entry<String, Object> entry : uploadFileMap.entrySet()) {
			Object obj = entry.getValue();
			if (obj instanceof File) {
				entry.setValue(deflate((File) obj));
			} else {
				ArrayList<File> uploadFiles = (ArrayList<File>) obj;
				for (int i = 0; i < uploadFiles.size(); i++) {
					File file = uploadFiles.get(i);
					uploadFiles.set(i, deflate(file));
				}
			}
		}
		return uploadFileMap;
	}

}
