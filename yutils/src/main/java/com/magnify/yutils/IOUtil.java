package com.magnify.yutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;

/**
 * IO������
 *
 * @author ����
 */
public class IOUtil {

	/**
	 * ��������С
	 */
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	/**
	 * Ĭ���ַ�����
	 */
	private static final String DEFAULT_ENCODE = "utf8";

	/**
	 * ����������ȡ�ַ���,ʹ��utf8����
	 *
	 * @see #toString(InputStream, String)
	 * @param input ������
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream input) throws IOException {
		return toString(input, DEFAULT_ENCODE);
	}

	/**
	 * ����������ȡ�ַ���
	 *
	 * @param inputStream ������
	 * @param encode �ַ�����
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream, String encode) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, encode), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}

		return sb.toString();
	}

	/**
	 * �����������������
	 * @param input ������
	 * @param output �����
	 * @return �������ֽ���
	 * @throws IOException
	 */
	public static long copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * �ر�������,���׳��쳣
	 *
	 * @param input ����Ϊnull
	 */
	public static void closeQuietly(InputStream input) {
		if (input == null)
			return;
		try {
			input.close();
		} catch (IOException e) {

		}
	}

	/**
	 * �ر������,���׳��쳣
	 *
	 * @param output ����Ϊnull
	 */
	public static void closeQuietly(OutputStream output) {
		if (output == null)
			return;
		try {
			output.close();
		} catch (IOException e) {

		}
	}

	/**
	 * �ر�д����,���׳��쳣
	 *
	 ����Ϊnull
	 */
	public static void closeQuietly(Writer writer) {
		if (writer == null)
			return;
		try {
			writer.close();
		} catch (IOException e) {

		}
	}

}
