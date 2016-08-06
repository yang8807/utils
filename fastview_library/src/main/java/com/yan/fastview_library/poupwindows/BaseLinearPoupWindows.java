package com.yan.fastview_library.poupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.yan.fastview_library.R;

public class BaseLinearPoupWindows extends PopupWindow {
    private View views;
    private Context mContext;
    private FrameLayout fram;

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
        setBackgroundDrawable(new BitmapDrawable());

        update();
    }

    /**
     * Add interface to another layout, this is to give it a properties to control how long the distance from the right margin
     */
    @NonNull
    private FrameLayout add2FramLayout(Context context) {
        fram = new FrameLayout(context);
        fram.addView(views);
        return fram;
    }

    public BaseLinearPoupWindows(Context context) {
        if (onIniViews() == 0) {
            throw new IllegalStateException(context.getString(R.string.errorMsg));
        }
        views = LayoutInflater.from(context).inflate(onIniViews(), null);
        setPoupNormalStyle(context);
    }

    /**
     * Set the size of the layout file poupwindow
     */
    protected void setPoupWindowsSize() {
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Layout file, R.l ayout. Dialog_xml
     */
    protected int onIniViews() {
        return 0;
    }

    /**
     *To initialize the control, and returns the corresponding type of pricing
     *
     */
    public <E> E findView(int viewid) {
        return (E) views.findViewById(viewid);
    }

    /**
     * How long is set from the right margin
     */
    public BaseLinearPoupWindows setMarginRight(int marginRight) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.rightMargin = marginRight;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * How long is set from the margins
     */
    public BaseLinearPoupWindows setMargin(int margin) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(margin, margin, margin, margin);
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * How long is set from the margins
     */
    public BaseLinearPoupWindows setMargins(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        views.setLayoutParams(lp);
        return this;
    }


    public BaseLinearPoupWindows setBackGroundResoures(int resoures) {
        fram.setBackgroundColor(resoures);
        return this;
    }

    /**
     *Set the size of the controls
     */
    public BaseLinearPoupWindows setViewWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) views.getLayoutParams();
        lp.width = width;
        lp.height = width;
        views.setLayoutParams(lp);
        return this;
    }

    /**
     * By controlling the size of the layout to control the size of the PoupWindows
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
     * Set the background color and, if is rounded rectangle, set the radius size
     */
    public BaseLinearPoupWindows setDrawableNRadius(int color, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(mContext.getResources().getColor(color));
        gradientDrawable.setCornerRadius(radius);
        views.setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public BaseLinearPoupWindows setCardColorNRadius(int color, int radius) {
        fram.setBackgroundColor(color);
        return this;
    }

    /**
     * Timing closure poupwindows
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
     * Timing closure poupwindows; Use the existing handler and save resources
     */
    public BaseLinearPoupWindows postDelayDismiss(Handler mHandler, int delaytimes) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseLinearPoupWindows.this.dismiss();
            }
        }, delaytimes);
        return this;
    }


}