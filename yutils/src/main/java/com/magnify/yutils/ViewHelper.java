/**
 *
 */
package com.magnify.yutils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * @author 洒笑天涯;View操作的简化
 */
public class ViewHelper {

    private Activity activity;

    public ViewHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 寻找控件
     */
    public <E extends View> E findView(int ids) {
        return (E) activity.findViewById(ids);
    }


    /***
     * @param parentView;父VIew
     * @param listener;监听事件
     * @param ids;控件的id
     */
    public static void setOnClickListener(View parentView, OnClickListener listener, int... ids) {
        for (int i = 0; i < ids.length; i++) {
            parentView.findViewById(ids[i]).setOnClickListener(listener);
        }
    }

    /**
     * 设置控件不可见
     */
    public static void setVisible(boolean isVisible, View... views) {
        int visible;
        if (isVisible) {
            visible = View.VISIBLE;
        } else {
            visible = View.GONE;
        }

        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(visible);
        }
    }

    /**
     * @param b                          是否可见,是:true,否:false
     * @param activity;传入activity,用来寻找控件
     * @param listener;点击事件
     * @param viewids                    ;需要设置点击事件的按钮
     */
    public static void setOnClickListener(boolean b, Activity activity, OnClickListener listener, int... viewids) {
        for (int i = 0; i < viewids.length; i++) {
            View view = activity.findViewById(viewids[i]);
            view.setOnClickListener(listener);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param b             是否可见,是:true,否:false
     * @param listener;点击事件
     * @param viewids       ;需要设置点击事件的按钮
     */
    public void setOnClickListener(boolean b, OnClickListener listener, int... viewids) {
        for (int i = 0; i < viewids.length; i++) {
            View view = activity.findViewById(viewids[i]);
            view.setOnClickListener(listener);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 寻找控件;并设置它的可见性
     */
    public <E extends View> E findView(int ids, boolean b) {

        E e = (E) activity.findViewById(ids);
        if (b) {
            e.setVisibility(View.VISIBLE);
        } else {
            e.setVisibility(View.INVISIBLE);
        }
        return e;
    }

    /**
     * @param textViews:继承自TextVie的控件
     * @param textColor               需要设置的颜色
     */
    public void setTextColor(int textColor, TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(getColor(textColor));
        }
    }

    /**
     * 得到当前的颜色
     */
    public int getColor(int color) {
        return activity.getResources().getColor(color);
    }

    /**
     * 创建一个TextView,一般用来Topbar的中的所用到的TextView
     */
    public TextView newTextView(String string, int color) {
        TextView text = new TextView(activity);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(getColor(color));
        text.setText(string);
        return text;
    }

    /**
     * @param b;是否可见
     * @param ids;控件的ids
     */
    public void setVisible(boolean b, int... ids) {
        int visible;
        if (b) {
            visible = View.VISIBLE;
        } else {
            visible = View.GONE;
        }
        for (int i = 0; i < ids.length; i++) {
            findView(ids[i]).setVisibility(visible);
        }

    }

}
