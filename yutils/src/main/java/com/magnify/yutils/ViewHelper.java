/**
 *
 */
package com.magnify.yutils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.magnify.yutils.dialog.BaseLinearPoupWindows;


/**
 * @author ������;View�����ļ�
 */
public class ViewHelper {

    private Activity activity;

    public ViewHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * Ѱ�ҿؼ�
     */
    public <E extends View> E findView(int ids) {
        return (E) activity.findViewById(ids);
    }

    /***
     * ΪPoupWindows���õ���¼�
     */
    public static void setOnClickListener(BaseLinearPoupWindows windows, OnClickListener listener, int... ids) {
        for (int i = 0; i < ids.length; i++) {
            windows.findView(ids[i]).setOnClickListener(listener);
        }
    }

    /***
     * @param parentView;��VIew
     * @param listener;�����¼�
     * @param ids;�ؼ���id
     */
    public static void setOnClickListener(View parentView, OnClickListener listener, int... ids) {
        for (int i = 0; i < ids.length; i++) {
            parentView.findViewById(ids[i]).setOnClickListener(listener);
        }
    }

    /**
     * ���ÿؼ����ɼ�
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
     * @param b                          �Ƿ�ɼ�,��:true,��:false
     * @param activity;����activity,����Ѱ�ҿؼ�
     * @param listener;����¼�
     * @param viewids                    ;��Ҫ���õ���¼��İ�ť
     */
    public static void setOnClickListener(boolean b, Activity activity, OnClickListener listener, int... viewids) {
        for (int i = 0; i < viewids.length; i++) {
            View view = activity.findViewById(viewids[i]);
            view.setOnClickListener(listener);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param b             �Ƿ�ɼ�,��:true,��:false
     * @param listener;����¼�
     * @param viewids       ;��Ҫ���õ���¼��İ�ť
     */
    public void setOnClickListener(boolean b, OnClickListener listener, int... viewids) {
        for (int i = 0; i < viewids.length; i++) {
            View view = activity.findViewById(viewids[i]);
            view.setOnClickListener(listener);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Ѱ�ҿؼ�;���������Ŀɼ���
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
     * @param textViews:�̳���TextVie�Ŀؼ�
     * @param textColor               ��Ҫ���õ���ɫ
     */
    public void setTextColor(int textColor, TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(getColor(textColor));
        }
    }

    /**
     * �õ���ǰ����ɫ
     */
    public int getColor(int color) {
        return activity.getResources().getColor(color);
    }

    /**
     * ����һ��TextView,һ������Topbar���е����õ���TextView
     */
    public TextView newTextView(String string, int color) {
        TextView text = new TextView(activity);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(getColor(color));
        text.setText(string);
        return text;
    }

    /**
     * @param b;�Ƿ�ɼ�
     * @param ids;�ؼ���ids
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
