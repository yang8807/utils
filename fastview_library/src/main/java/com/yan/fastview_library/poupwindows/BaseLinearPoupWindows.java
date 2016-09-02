package com.yan.fastview_library.poupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class BaseLinearPoupWindows extends PopupWindow {

    protected View views;

    private Context mContext;

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
        //手动测量一下view的大小,因为第一次弹出来,大小并未确定,显示存在问题
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        fram.measure(widthSpec, heightSpec);
        update();
    }

    /**
     * Add interface to another layout, this is to give it a properties to control how long the distance from the right margin
     */
    private FrameLayout add2FramLayout(Context context) {
        fram = new FrameLayout(context);
        fram.addView(views);
        return fram;
    }

    public BaseLinearPoupWindows(Context context) {
        if (onIniViews() == 0) {
            throw new IllegalStateException("Please set the layout file, copy (onIniViews) or call getPoupInstance (Context context, int layoutid)");
        }
        views = LayoutInflater.from(context).inflate(onIniViews(), null);
        setPoupNormalStyle(context);
    }

    /**
     * Set the size of the poupwindow of the layout file
     */
    protected void setPoupWindowsSize() {
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Layout file, R.layout.dialog_xml
     */
    protected int onIniViews() {
        return 0;
    }

    /**
     * Initialization of the control, and return to the corresponding type of control
     */
    public <E extends View> E findView(int viewid) {
        return (E) views.findViewById(viewid);
    }

    /**
     * How long is the distance from the right
     */
    public BaseLinearPoupWindows setMarginRight(int marginRight) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.rightMargin = marginRight;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * How long is the setting from the margin
     */
    public BaseLinearPoupWindows setMargin(int margin) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(margin, margin, margin, margin);
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * How long is the setting from the margin
     */
    public BaseLinearPoupWindows setMargins(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        views.setLayoutParams(lp);
        return this;
    }

    public BaseLinearPoupWindows setViewWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.width = width;
        lp.height = width;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * Control the size of the PoupWindows by controlling the size of the layout.
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
     * Set the background color as well, if it is rounded rectangle, then set the size of the radius
     */
    public BaseLinearPoupWindows setDrawableNRadius(@ColorRes int color, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(mContext.getResources().getColor(color));
        gradientDrawable.setCornerRadius(radius);
        views.setBackgroundDrawable(gradientDrawable);
        return this;
    }

    /**
     * Timed shutdown poupwindows
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
     * Time off poupwindows; use already handler, more saving resources
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

    /**
     * @param gravity 在指定View的哪一个方向弹出
     * @param offSet  离要弹出的view的偏移量
     * @param view    要在哪一view显示
     *                暂时只能用于在View的中间显示
     */
    public void showAtCenter(View view, int gravity, int offSet) {
        view.getLocationOnScreen(screnLocation);
        switch (gravity) {

            case Gravity.LEFT:
                showAtLocation(view, Gravity.NO_GRAVITY, screnLocation[0] - fram.getMeasuredWidth() - offSet, screnLocation[1] - (fram.getMeasuredHeight() - view.getMeasuredHeight()) / 2);
                break;
            case Gravity.RIGHT:
                showAtLocation(view, Gravity.NO_GRAVITY, screnLocation[0] + view.getWidth() + offSet, screnLocation[1] - (fram.getMeasuredHeight() - view.getMeasuredHeight()) / 2);
                break;
            case Gravity.TOP:
                showAtLocation(view, Gravity.NO_GRAVITY, screnLocation[0] + (view.getMeasuredWidth() - fram.getMeasuredWidth()) / 2, screnLocation[1] - fram.getMeasuredHeight() - offSet);
                break;
            case Gravity.BOTTOM:
                showAsDropDown(view, (view.getMeasuredWidth() - fram.getMeasuredWidth()) / 2, offSet);
                break;
        }
    }

    public void showAtCenter(View view, int gravity) {
        showAtCenter(view, gravity, 0);
    }

    /**
     * Display in the middle of the screen
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

    /**
     * 设置布局文件的poupwindow的大小
     */
    public void setMatchPoupWindowsSize() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
    }

}
