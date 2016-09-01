package com.yan.fastview_library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.magnify.yutils.LogUtil;

import java.util.Random;

/**
 * Created by heinigger on 16/9/1.
 */
public class RippleCircleViews extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    /**
     * 与SurfaceHolder绑定的Canvas
     */
    private Canvas mCanvas;
    /**
     * 用于绘制的线程
     */
    private Thread t;
    /**
     * 线程的控制开关
     */
    private boolean isRunning;
    private int mRadius;
    private int[] mDrawableRaidus;
    private int[] mDelayTime;
    private int mStrokeWidth = 10;
    private int mCenterX;
    private int mCenterY;
    private int mRippleCount;
    private Paint[] mPaint;
    private boolean hasCreate = false;
    private int mRippleColor = Color.BLUE;
    private int mRippleStorkWidth = 10;
    private Random mRandom;

    public RippleCircleViews(Context context) {
        this(context, null);

    }

    public RippleCircleViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRandom = new Random();

        mHolder = getHolder();
        mHolder.addCallback(this);
        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常亮
        this.setKeepScreenOn(true);
        mDrawableRaidus = new int[mRippleCount];
        mPaint = new Paint[mRippleCount];
        mDelayTime = new int[mRippleCount];

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 开启线程
        isRunning = true;
        t = new Thread(this);
        t.start();
        for (int i = 0; i < mDrawableRaidus.length; i++) {
            mPaint[i] = iniPaint();
            mDrawableRaidus[i] = 0;
            getHandler().postDelayed(this, i * 1000);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 通知关闭线程
        isRunning = false;
    }

    private Paint iniPaint() {
        Paint mPaint = new Paint();
        mPaint.setColor(mRippleColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRippleStorkWidth);
        return mPaint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int measureHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mRadius = Math.min(measureWidth, measureHeight) / 2 - mStrokeWidth;
        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
    }

    @Override
    public void run() {
        // 不断的进行draw
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            // 获得canvas
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawCircle(mCenterX, mCenterY, 100, mPaint[0]);
                for (int i = 0; i < mDrawableRaidus.length; i++) {
                    mDrawableRaidus[i] += 10;
                    mPaint[i].setAlpha(mDrawableRaidus[i] * 255 / mRadius);
                    mCanvas.drawCircle(mCenterX, mCenterY, mDrawableRaidus[i], mPaint[i]);
                    if (mDrawableRaidus[i] >= mRadius) mDrawableRaidus[i] = mRadius;
                    LogUtil.v("mine", "我不断的创建");
                }
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}