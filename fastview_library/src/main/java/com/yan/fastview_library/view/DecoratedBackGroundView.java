package com.yan.fastview_library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.magnify.yutils.view_utils.ShapeUtils;


/**
 * Created by 黄艳武 on 2015/11/5.
 * Function:
 */
public class DecoratedBackGroundView  extends View{
    private Paint mPaint;

    public DecoratedBackGroundView(Context context) {
        super(context,null);
    }

    public DecoratedBackGroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        ShapeUtils.drawRandowCycle(mPaint, canvas, 15);
    }
}