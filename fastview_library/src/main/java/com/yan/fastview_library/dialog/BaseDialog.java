package com.yan.fastview_library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.R;


/**
 * 自定义dialog基础类
 *
 * @author 李欣;
 *         一直感觉将view的初始化,应该放在onCreate中,这样,很多view再调用的时候,就不会再去判断是不是空;
 *         添加了无操作自动关闭的功能
 */
public abstract class BaseDialog extends Dialog {
    //是否需要自动关闭
    private boolean isNeedAutoClose = false;
    //两秒钟自动关闭
    private Handler handler;
    private View view;
    /**
     * 是否位于底部
     */
    private boolean alignBottom = false;
    //自动关闭的ruanable
    private AutoCloseRuanable closeRuanable;
    //设置自动关闭的时间
    private int closeTime = 2000;

    /**
     * 居中显示
     *
     * @param context
     */
    public BaseDialog(Context context) {
        this(context, false);
    }

    /***
     * @return 手指操作, 停止不操作, 超过两秒钟, 自动关闭
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isNeedAutoClose) {
            handler.removeCallbacks(closeRuanable);
            //移除之前的,两秒后再次启动
            handler.postDelayed(closeRuanable, closeTime);
        }
        return super.dispatchTouchEvent(event);
    }

    /***
     * @return 设置自动
     */
    public void setIsNeedAutoClose(boolean isNeedAutoClose) {
        this.isNeedAutoClose = isNeedAutoClose;
        if (isNeedAutoClose) {
            handler = new Handler();
            closeRuanable = new AutoCloseRuanable();
        }
    }

    @Override
    public void show() {
        super.show();
        //弹框一摊出来,就执行此任务
        if (isNeedAutoClose)
            handler.postDelayed(closeRuanable, closeTime);
    }

    /**
     * @param context
     * @param alignBottom 是否位于底部，从底部弹出
     */
    public BaseDialog(Context context, boolean alignBottom) {
        super(context, R.style.BaseDialog);
        iniLayoutPamas(alignBottom);
    }

    /***
     * @return 布局属性
     */
    public BaseDialog(Context context, boolean alignBottom, int theme) {
        super(context, theme);
        iniLayoutPamas(alignBottom);
    }

    private void iniLayoutPamas(boolean alignBottom) {
        this.alignBottom = alignBottom;
        if (alignBottom) {
            WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams.y = 0;
            layoutParams.x = 5;
            onWindowAttributesChanged(layoutParams);
            getWindow().setWindowAnimations(R.style.DialogAnim);
        }
        view = contentView();
    }

    /***
     * @return 设置弹窗的动画
     */
    public void setAnimation(int styleAnimation) {
        getWindow().setWindowAnimations(styleAnimation);
    }

    private void getContentView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_base_layout, null);
        FrameLayout layout = new FrameLayout(getContext());

        if (alignBottom) {
            view.setMinimumWidth(getContext().getResources().getDisplayMetrics().widthPixels - 10);
        }
        layout.addView(view);
        setContentView(layout);
    }

    /**
     * Dialog视图,由子类实现并返回
     *
     * @return
     */
    protected abstract View contentView();

    private class AutoCloseRuanable implements Runnable {

        @Override
        public void run() {
            BaseDialog.this.dismiss();
        }
    }

    public void setMatchParent(View view) {
        Point point = DeviceUtil.getDisplaySize(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(point.x, point.y);
        view.setLayoutParams(lp);
    }

}