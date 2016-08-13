package com.yan.fastview_library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.magnify.yutils.view_utils.TextDrawUtils;
import com.yan.fastview_library.R;

/**
 * Created by heinigger on 16/8/12.
 */
public class SideBar extends View {
    private char[] defaultSideData = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};

    private int unitWidth;
    private int pHeight;
    private int pWidth;

    private Paint mPaint;
    private int startX, startY;
    private int normalColor = Color.BLACK;
    private int selectColor = Color.GREEN;
    private Drawable mBackDrawable;
    private int mTextSize = 26;
    private int selectPosition = 0;

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniProperites(context, attrs);
        iniPaint();
    }

    private void iniProperites(Context context, AttributeSet attrs) {
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.SideBar_android_textColor) {
                ColorStateList colorStateList = tp.getColorStateList(attr);
                normalColor = colorStateList.getColorForState(new int[]{}, normalColor);
                selectColor = colorStateList.getColorForState(new int[]{android.R.attr.state_selected}, selectColor);
            } else if (attr == R.styleable.SideBar_android_textSize) {
                mTextSize = (int) tp.getDimension(attr, 26);
            } else if (attr == R.styleable.SideBar_android_background) {
                mBackDrawable = tp.getDrawable(attr);
                setBackGroundDrawable(mBackDrawable);
            }
        }

        tp.recycle();
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(normalColor);
        mPaint.setTextSize(mTextSize);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        pHeight = canvas.getHeight();
        pWidth = canvas.getWidth();
        unitWidth = Math.min(pHeight, pWidth * defaultSideData.length) / defaultSideData.length;
        //初始绘制的位置
        startX = (int) ((pWidth - unitWidth + mPaint.getTextSize()) / 2);
        startY = TextDrawUtils.getBaseLineY(mPaint, unitWidth) + (pHeight - unitWidth * defaultSideData.length) / 2;
        for (int i = 0; i < defaultSideData.length; i++) {
            String drawText = String.valueOf(defaultSideData[i]);
            mPaint.setColor(selectPosition == i ? selectColor : normalColor);
            canvas.drawText(drawText, (pWidth - mPaint.measureText(drawText)) / 2, startY, mPaint);
            startY += unitWidth;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        selectPosition = (int) (event.getY() / pHeight * defaultSideData.length);
        switch (event.getAction()) {
            default:
                willInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                willInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void willInvalidate() {

        if (selectPosition >= 0 && selectPosition < defaultSideData.length) {
            invalidate();
            return;
        }
    }

    public void setBackGroundDrawable(Drawable mBackDrawabwle) {
        if (Build.VERSION_CODES.JELLY_BEAN >= Build.VERSION.SDK_INT)
            setBackground(mBackDrawabwle);
        else
            setBackgroundDrawable(mBackDrawabwle);
    }
}
