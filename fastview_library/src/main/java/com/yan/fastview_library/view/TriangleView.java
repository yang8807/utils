package com.yan.fastview_library.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by heinigger on 16/9/14.
 */
public class TriangleView extends FrameLayout {
    private Paint mPaint;
    private int mColor;


    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniPaint();
    }

    private void iniPaint() {


    }
}
