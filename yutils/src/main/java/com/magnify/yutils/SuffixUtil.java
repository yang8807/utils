package com.magnify.yutils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;


/**
 * �±��ƶ��࣬ͨ������moveTo�޸Ĵ˱��� ,�±�ĸ�������ʹ��RelativeLayout
 *
 * @author �½�
 */
public class SuffixUtil {

	private View slideView;

	private View slideView2;

	private int displayWidth;

	private int duration = 500;

	private int toX;

	public int currentX = 0;

	private int dividerWidth = 0;

	/**
	 * @param group ��Ҫ����±��View(��һ����View),����ʹ��LinearLayout����ƽ��View
	 * @param slide_view �±�View
	 */
	public SuffixUtil(final ViewGroup group, final View slide_view) {
		this(group, slide_view, 0, 500);
	}

	/**
	 * @param group ��Ҫ����±��View(��һ����View),����ʹ��LinearLayout����ƽ��View
	 * @param slide_view �±�View
	 * @param delta �»��߱��Ϸ����ֳ������ĳ���(����)
	 */
	public SuffixUtil(final ViewGroup group, final View slide_view, int delta) {
		this(group, slide_view, delta, 500);
	}

	/**
	 * @param group ��Ҫ����±��View(��һ����View),����ʹ��LinearLayout����ƽ��View
	 * @param slide_view �±�View
	 * @param delta �»��߱��Ϸ����ֳ������ĳ���(����)
	 * @param duration �ƶ�ʱ��(����)
	 */
	public SuffixUtil(final ViewGroup group, final View slide_view, final int delta, int duration) {
		slideView = slide_view;
		this.duration = duration;
		final View child = group.getChildAt(0);
		ViewUtil.addGlobalLayoutListener(child, new ViewUtil.GlobalLayoutListener() {

			@Override
			public boolean onLayout(View view) {
				displayWidth = group.getWidth();
				int childWidth = child.getWidth();
				if (displayWidth > childWidth && childWidth > 0) {
					RelativeLayout.LayoutParams pars = (RelativeLayout.LayoutParams) slideView.getLayoutParams();
					pars.width = childWidth + (delta * 2);
					pars.leftMargin = (displayWidth - pars.width) / 2;
					slideView.setLayoutParams(pars);
					if (slideView2 != null) {
						RelativeLayout.LayoutParams pars2 = (RelativeLayout.LayoutParams) slideView2.getLayoutParams();
						pars2.width = pars.width;
						pars2.leftMargin = pars.leftMargin;
						slideView2.setLayoutParams(pars2);
					}
					return true;
				}
				return false;
			}
		});
	}

	public void setSlideView2(View slideView2) {
		this.slideView2 = slideView2;
	}

	/**
	 * ��������tab֮��ķָ��ߵĿ�ȣ��Ծ�ȷ��λ
	 */
	public void setDividerWidth(int width) {
		if (width >= 0)
			dividerWidth = width;
	}

	private void suffixTo() {
		TranslateAnimation ta = new TranslateAnimation(currentX, toX, 0, 0);
		currentX = toX;
		ta.setDuration(duration);
		ta.setFillAfter(true);
		slideView.startAnimation(ta);
		if (slideView2 != null) {
			slideView2.startAnimation(ta);
		}
	}

	public void moveTo(int index) {
		this.toX = this.displayWidth * index + (index > 0 ? (index - 1) * dividerWidth : 0);
		suffixTo();
	}

}
