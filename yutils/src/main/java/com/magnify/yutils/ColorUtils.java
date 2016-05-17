package com.magnify.yutils;

public class ColorUtils {

	/**
	 * ��ȡ��͸����ɫ
	 *
	 * @param alpha ͸���ȣ� 0 ~ 1
	 * @param color ��Ҫ��͸������ɫ
	 * @return ��͸������ɫ
	 */
	public static int getAlphaColor(float alpha, int color) {
		int alphaColor = (int) (alpha * 0xff);
		alphaColor = alphaColor << 24;
		color = color & 0x00ffffff;
		return alphaColor | color;
	}

}
