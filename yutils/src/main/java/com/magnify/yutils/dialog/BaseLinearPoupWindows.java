/**
 *
 */
package com.magnify.yutils.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by ������ on 2015/10/19;���һ���޸�ʱ��10/19 20:20 Function:
 */
public class BaseLinearPoupWindows extends PopupWindow {

    private View views;

    private Context mContext;
    /**
     * �����ļ�
     */
    private FrameLayout fram;

    private int[] screnLocation = new int[2];

    private Runnable runnable;

    public BaseLinearPoupWindows(Context context, int layoutid) {
        super();
        this.mContext = context;
        views = LayoutInflater.from(context).inflate(layoutid, null);
        setPoupNormalStyle(context);
    }

    public BaseLinearPoupWindows(Context context, View view) {
        super();
        this.mContext = context;
        views = view;
        setPoupNormalStyle(context);
    }

    private void setPoupNormalStyle(Context context) {

        fram = add2FramLayout(context);
        setPoupWindowsSize();
        setContentView(fram);
        setOutsideTouchable(true);
        setFocusable(true);
        BaseLinearPoupWindows.this.setBackgroundDrawable(new BitmapDrawable());

        update();
    }

    /**
     * ��������ӵ�����һ��layout��,����Ϊ�������и����Կ��Կ��������ұ߾��ж೤
     */
    @NonNull
    private FrameLayout add2FramLayout(Context context) {
        fram = new FrameLayout(context);
        fram.addView(views);
        return fram;
    }

    public BaseLinearPoupWindows(Context context) {
        if (onIniViews() == 0) {
            throw new IllegalStateException("�����ò����ļ�,��дonIniViews()���ߵ���getPoupInstance(Context context, int layoutid)");
        }
        views = LayoutInflater.from(context).inflate(onIniViews(), null);
        setPoupNormalStyle(context);
    }

    /**
     * ���ò����ļ���poupwindow�Ĵ�С
     */
    protected void setPoupWindowsSize() {
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * �����ļ���R.layout.dialog_xml
     */
    protected int onIniViews() {
        return 0;
    }

    /**
     * �Կؼ����г�ʼ��,�����ض�Ӧ�Ŀؼ�����
     */
    public <E extends View> E findView(int viewid) {
        return (E) views.findViewById(viewid);
    }

    /**
     * �������ұ߾��ж೤
     */
    public BaseLinearPoupWindows setMarginRight(int marginRight) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.rightMargin = marginRight;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * ������߾��ж೤
     */
    public BaseLinearPoupWindows setMargin(int margin) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(margin, margin, margin, margin);
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * ������߾��ж೤
     */
    public BaseLinearPoupWindows setMargins(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        views.setLayoutParams(lp);
        return this;
    }

	/*public BaseLinearPoupWindows setBackGroundResoures(int resoures) {
        fram.setCardBackgroundColor(resoures);
	    return this;
	}*/

    /**
     * ���ÿؼ��Ĵ�С
     */
    public BaseLinearPoupWindows setViewWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.width = width;
        lp.height = width;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * ͨ�����Ʋ��ֵĴ�С������PoupWindows�Ĵ�С
     */
    public BaseLinearPoupWindows setPoupWindowsSize(int width, int height) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.width = width;
        lp.height = height;
        views.setLayoutParams(lp);
        return this;

    }

    public BaseLinearPoupWindows setpoupWindowWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.width = width;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * ���ñ�����ɫ����,�����Բ�Ǿ��εĻ�,���ð뾶�Ĵ�С
     */
    public BaseLinearPoupWindows setDrawableNRadius(int color, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(mContext.getResources().getColor(color));
        gradientDrawable.setCornerRadius(radius);
        views.setBackgroundDrawable(gradientDrawable);
        return this;
    }

    /**
     * ��ʱ�ر�poupwindows
     */
    public BaseLinearPoupWindows postDelayDismiss(int delaytimes) {
        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                BaseLinearPoupWindows.this.dismiss();
            }
        }, delaytimes);
        return this;
    }

    /***
     * ��ʱ�ر�poupwindows;ʹ������handler,���ӽ�ʡ��Դ
     */
    public BaseLinearPoupWindows postDelayDismiss(final Handler mHandler, int delaytimes) {
        if (runnable == null) {
            runnable = new Runnable() {

                @Override
                public void run() {
                    BaseLinearPoupWindows.this.dismiss();
                }
            };
        }
        mHandler.postDelayed(runnable, delaytimes);
        return this;
    }

    public void showAtViewTop(View v) {
        v.getLocationOnScreen(screnLocation);
        this.showAtLocation(v, Gravity.NO_GRAVITY, (screnLocation[0] + v.getWidth() / 2) - this.getWidth() / 2, screnLocation[1] - this.getHeight());
    }

    /**
     * ����Ļ�м���ʾ
     */
    public void showAtScreenCenter(View view) {
        showAtLocation((View) view.getParent(), Gravity.CENTER, 0, 0);
    }

    public void setBackgroundColor(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setDither(true);
        BaseLinearPoupWindows.this.setBackgroundDrawable(gradientDrawable);
        BaseLinearPoupWindows.this.update();

    }
}