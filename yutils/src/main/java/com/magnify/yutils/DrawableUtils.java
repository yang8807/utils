package com.magnify.yutils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;

/**
 * Created by ������ on 2015/11/10. Function:
 */
public class DrawableUtils {

	/**
	 * @param radius Բ�Ǿ��εİ뾶
	 * @return �õ�����Բ�Ǿ���, ���е��Ч���İ�ť,������������ɫ, �õ�һ��stateDrawable
	 */
	public static StateListDrawable getStateCorRectDrawable(int color, int radius) {
		return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(color), radius));
	}

	/**
	 * @return �õ�����Բ�Ǿ���, ���е��Ч���İ�ť,������������ɫ, �õ�һ��stateDrawable
	 */
	public static StateListDrawable getStateCorRectDrawable(int color, int pressColor, int radius) {
		return addToStateDrawable(getCorRectDrawable(color, radius), getCorRectDrawable(getPressColor(pressColor), radius));
	}

	/**
	 * @param color ��ͨ״̬����ɫ
	 * @return �õ�����Բ��, ���е��Ч���İ�ť,������������ɫ, �õ�һ��stateDrawable
	 */
	public static StateListDrawable getStateOvalDrawable(int color) {
		return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(color)));
	}

	/**
	 * @param color ��ͨ״̬����ɫ
	 * @param pressColor ����ı����ɫ
	 * @return �õ�����Բ��, ���е��Ч���İ�ť,������������ɫ, �õ�һ��stateDrawable
	 */
	public static StateListDrawable getStateOvalDrawable(int color, int pressColor) {
		return addToStateDrawable(getOvalDrawable(color), getOvalDrawable(getPressColor(pressColor)));
	}

	/**
	 * @param color normal color
	 * @param pressColor pressColor
	 * @return �õ�һ�����εİ�ť�İ�ť
	 */
	@NonNull
	public static StateListDrawable getStateRectDrawable(int color, int pressColor) {
		return addToStateDrawable(getRectDrawable(color), getRectDrawable(pressColor));
	}

	/**
	 * @param color �Զ���������һ����ɫ
	 * @return �õ�һ�����εİ�ť�İ�ť
	 */
	@NonNull
	public static StateListDrawable getStateRectDrawable(int color) {
		return addToStateDrawable(getRectDrawable(color), getRectDrawable(getPressColor(color)));
	}

	/**
	 * @return Ϊÿ�����״̬���Drawable
	 */
	@NonNull
	private static StateListDrawable addToStateDrawable(GradientDrawable normalDrawable, GradientDrawable pressDrawable) {
		StateListDrawable sd = new StateListDrawable();
		sd.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, pressDrawable);
		sd.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressDrawable);
		sd.addState(new int[] { android.R.attr.state_focused }, pressDrawable);
		sd.addState(new int[] { android.R.attr.state_pressed }, pressDrawable);
		sd.addState(new int[] { android.R.attr.state_enabled }, normalDrawable);
		sd.addState(new int[] {}, normalDrawable);
		return sd;
	}

	/**
	 * @return ������������һ����ɫ, �õ�ǳһ�����ɫ
	 */
	public static int getPressColor(int color) {
		int alpha = (int) (Color.alpha(color) * 0.8);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		if (red > 200 && green > 200 && blue > 200) {
			red = (int) (red * 0.9);
			green = (int) (green * 0.9);
			blue = (int) (blue * 0.9);
		}
		color = Color.argb(alpha, red, green, blue);
		return color;
	}

	/***
	 * @return �õ�һ�����εı���
	 */
	public static GradientDrawable getRectDrawable(int color) {
		return getGradientDrawable(ShapeType.RECT, color, 0);
	}

	/***
	 * @return �õ�һ��Բ�εı���
	 */
	public static GradientDrawable getOvalDrawable(int color) {
		return getGradientDrawable(ShapeType.OVAL, color, 0);
	}

	/***
	 * @return �õ�һ��Բ�Ǿ��εİ�ť
	 */
	public static GradientDrawable getCorRectDrawable(int color, int radius) {
		return getGradientDrawable(ShapeType.CORNERRECT, color, radius);
	}

	/**
	 * @return ������ɫ���а뾶, �õ�һ��gradientDrawable
	 */
	@NonNull
	public static GradientDrawable getGradientDrawable(int shapeType, int color, int radius) {
		GradientDrawable gradientDrawable = new GradientDrawable();
		if (shapeType == ShapeType.RECT) {
			gradientDrawable.setShape(GradientDrawable.RECTANGLE);
		} else if (shapeType == ShapeType.OVAL) {
			gradientDrawable.setShape(GradientDrawable.OVAL);
		} else if (shapeType == ShapeType.CORNERRECT) {
			gradientDrawable.setShape(GradientDrawable.RECTANGLE);
			gradientDrawable.setCornerRadii(new float[] { radius, radius, radius, radius });
		}
		gradientDrawable.setDither(true);
		gradientDrawable.setColor(color);
		gradientDrawable.setCornerRadius(radius);
		return gradientDrawable;
	}
	/**
	 * RECT Բ��; OVAL Բ��; CORNERRECT Բ�Ǿ���;
	 */
	public static class ShapeType {

		public static final int RECT = 1;

		public static final int OVAL = 2;

		public static final int CORNERRECT = 3;
	}
}
