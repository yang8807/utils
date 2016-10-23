package com.magnify.utils.ui.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by heinigger on 16/10/23.
 */

public class CustomerView extends ImageView {
    private Paint mPaint;

    public CustomerView(Context context) {
        this(context, null);
    }

    public CustomerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniPaint();
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        //起点位置=负距离+自己的大小
        canvas.drawArc(-getMeasuredWidth() * 2, -getMeasuredHeight() * 2, getMeasuredWidth() * 3, getMeasuredHeight() * 3, -120, 60, true, mPaint);
        //上下两个方向,差了自己一倍
        canvas.drawLine(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() * 4, getMeasuredHeight() * 4, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(getMeasuredWidth() / 2, getMeasuredHeight() / 2, -getMeasuredWidth() * 3, -getMeasuredHeight() * 3, mPaint);
    }
}
