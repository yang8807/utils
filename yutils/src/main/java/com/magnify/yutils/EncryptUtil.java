package com.magnify.yutils;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtil {

	/**
	 * �ַ�����
	 */
	public static final String ENCODE = "UTF-8";

	/**
	 * ���������ĳ���
	 */
	public static final int IV_LENGTH = 8;

	/**
	 * 3DES���ܣ�ģʽΪCBCģʽ
	 *
	 * @param key ������Կ
	 * @param iv ��������
	 * @param data �����ַ���
	 * @throws Exception
	 */
	public static String des3EncodeCBC(String key, String iv, String data) throws Exception {
		byte[] encode = des3EncodeCBC(key.getBytes(ENCODE), createIV(iv), data.getBytes(ENCODE));
		String encodeStr = Base64.encodeToString(encode, Base64.DEFAULT);
		return encodeStr;
	}

	/**
	 * 3DES���ܣ�ģʽΪCBCģʽ
	 *
	 * @param key ������Կ
	 * @param iv ��������
	 * @param data �����ַ���
	 * @throws Exception
	 */
	private static byte[] des3EncodeCBC(byte[] key, byte[] iv, byte[] data) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] out = cipher.doFinal(data);
		return out;
	}

	/**
	 * 3DES���ܣ�ģʽΪCBCģʽ
	 *
	 * @param key ������Կ
	 * @param iv ��������
	 * @param data �����ַ���
	 * @throws Exception
	 */
	public static String des3DecodeCBC(String key, String iv, String data) throws Exception {
		byte[] decodeByte = Base64.decode(data, Base64.DEFAULT);
		byte[] decode = des3DecodeCBC(key.getBytes(ENCODE), createIV(iv), decodeByte);
		return new String(decode, ENCODE);
	}

	/**
	 * 3DES���ܣ�ģʽΪCBCģʽ
	 *
	 * @param key ������Կ
	 * @param iv ��������
	 * @param data �����ַ���
	 * @throws Exception
	 */
	private static byte[] des3DecodeCBC(byte[] key, byte[] iv, byte[] data) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] out = cipher.doFinal(data);
		return out;
	}

	/**
	 * �����ַ���������������
	 * @throws Exception
	 */
	public static byte[] createIV(String pIv) throws Exception {
		byte[] bytes = pIv.getBytes("UTF-8");
		int length = bytes.length / IV_LENGTH;
		if (length * IV_LENGTH < bytes.length) {
			length++;
		}
		byte[] result = new byte[IV_LENGTH];
		System.arraycopy(bytes, 0, result, 0, bytes.length > IV_LENGTH ? IV_LENGTH : bytes.length);
		for (int i = bytes.length; i < result.length; i++) {
			result[i] = 0x00;
		}
		return result;
	}
}
