package com.magnify.yutils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

public class ViewUtil {

	/**
	 * ��ȡActivity����ͼ
	 *
	 * @return Activity����ͼ
	 */
	public static ViewGroup getActivityView(Activity activity) {
		return (ViewGroup) activity.getWindow().findViewById(android.R.id.content);
	}

	/**
	 * �����������ģʽ,��Ҫ��������������Զ������������,�����ͻ
	 *
	 *  view
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void disableOverScrollMode(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			view.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	/**
	 * ʹ���������,�ڴ�Ӳ�����ٵ������,����ĳЩView����֧��Ӳ������,�����Ҫ����Ϊ�������
	 *
	 *  view
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void useSoftware(View view, Paint paint) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0��ʼ�д˷���
			view.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
		}
	}

	/**
	 * �򿪵�ǰWindow��Ӳ������
	 *
	 *  window
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static void openHardWare(Window window) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// Android3.0��ʼ�д˷���,������Ч��̫��,����ֻ��4.0���ϰ汾����
			window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
	}

	/**
	 * ��ͼ�Ƿ�֧��Ӳ������
	 *
	 *  view
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean isHardwareAccelerated(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0��ʼ�д˷���
			return view.isHardwareAccelerated();
		}
		return false;
	}

	/**
	 * Canvas�Ƿ�֧��Ӳ������
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean isHardwareAccelerated(Canvas canvas) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0��ʼ�д˷���
			return canvas.isHardwareAccelerated();
		}
		return false;
	}

	/**
	 * ����View����
	 *
	 *  view
	 *  drawable
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void setBackground(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * ȫ�ֲ��ּ����� ͨ��GlobalLayoutListener.onLayout()����flase�����ּ���,����true���Զ��Ƴ�������
	 */
	@SuppressLint("WrongCall")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static abstract class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

		private View view;

		@Override
		final public void onGlobalLayout() {
			if (view != null && view.getViewTreeObserver() != null && onLayout(view)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				view = null;
			}
		}

		abstract public boolean onLayout(View view);

	}

	/**
	 * ���ȫ�ֲ��ּ�����,��Ҫ������Ҫ�ڲ�����ɺ��ȡ��ͼ�߿����Ϣ
	 *
	 *  listener
	 */
	public static void addGlobalLayoutListener(View view, GlobalLayoutListener listener) {
		listener.view = view;
		view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

	/**
	 * ��������ͼδȷ���߿�ǰ"����"��ͼ��width�Լ�height,ע��˷���������ʹ��LayoutInflater.inflate(resid, container, false),����container!=null���������Ч
	 *
	 *  child
	 */
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * �޸�PopupWindow��Android3.0֮ǰ��Ϊ�󶨵�ViewTreeObserver.OnScrollChangedListener��û���ж�null���µĴ���
	 */
	public static void popupWindowFix(final PopupWindow popupWindow) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			try {
				final Field fAnchor = PopupWindow.class.getDeclaredField("mAnchor");
				fAnchor.setAccessible(true);
				Field listener = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
				listener.setAccessible(true);
				final ViewTreeObserver.OnScrollChangedListener originalListener = (ViewTreeObserver.OnScrollChangedListener) listener
						.get(popupWindow);
				ViewTreeObserver.OnScrollChangedListener newListener = new ViewTreeObserver.OnScrollChangedListener() {

					public void onScrollChanged() {
						try {
							View mAnchor = (View) fAnchor.get(popupWindow);
							if (mAnchor == null) {
								return;
							} else {
								originalListener.onScrollChanged();
							}
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				};
				listener.set(popupWindow, newListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int getBottomBarHeight(Activity activity) {
		Rect rectangle = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectangle);

		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		return dm.heightPixels - rectangle.bottom;
	}

	public static int getTopBarHeight(Activity activity) {
		Rect rect = new Rect();
		Window win = activity.getWindow();
		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}

	/**
	 * ���view�ĺ�һ���ֵ�view
	 *
	 *  view
	 */
	public static View getNextSibling(View view) {
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent == null)
			return null;
		int i = parent.indexOfChild(view) + 1;
		return i < parent.getChildCount() ? parent.getChildAt(i) : null;
	}

	/**
	 * ���view��ǰһ���ֵ�view
	 *
	 *  view
	 */
	public static View getPreviousSibling(View view) {
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent == null)
			return null;
		int i = parent.indexOfChild(view) - 1;
		return i >= 0 ? parent.getChildAt(i) : null;
	}

}
