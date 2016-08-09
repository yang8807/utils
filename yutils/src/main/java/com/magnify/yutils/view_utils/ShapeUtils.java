package com.magnify.yutils.view_utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by 黄艳武 on 2015/11/5.
 * Function:
 */
public class ShapeUtils {
    private static Random random = new Random();

    /**
     * 绘制中间一个圆,圆外一个圈
     */
    public static void drawCycleRingShape(Paint mPaint, Canvas canvas) {
        int width = canvas.getWidth();
        int hegiht = canvas.getHeight();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        canvas.drawOval(new RectF(15, 15, width - 15, hegiht - 15), mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(new RectF(30, 30, width - 30, hegiht - 30), mPaint);
    }

    /**
     * 绘制不规则的圆形散落的图片
     */
    public static void drawRandowCycle(Paint mPaint, Canvas canvas, int count) {
        for (int i = 0; i < count; i++) {
            int width = canvas.getWidth();
            int hegiht = canvas.getHeight();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(random.nextInt(255) + 100);
            mPaint.setColor(Color.argb(getRondomNumber(150, 30), getRgb(), getRgb(), getRgb()));
            canvas.save();
            canvas.translate(getRondomNumber(width - width / 6, width / 6), getRondomNumber(hegiht - width / 6, width / 6));
            int radius = getRondomNumber(width / 6, width / 10);
            canvas.drawOval(new RectF(0, 0, radius, radius), mPaint);
            canvas.restore();
        }
    }

    /**
     * 绘制界面中不规则的图片
     * @return
     */
    public static void drawRandowDrawable(Paint mPaint, Canvas canvas, int count) {
        for (int i = 0; i < count; i++) {
            int width = canvas.getWidth();
            int hegiht = canvas.getHeight();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(random.nextInt(255) + 100);
            mPaint.setColor(Color.argb(getRondomNumber(150, 30), getRgb(), getRgb(), getRgb()));
            canvas.save();
            canvas.translate(getRondomNumber(width - width / 6, width / 6), getRondomNumber(hegiht - width / 6, width / 6));
            int radius = getRondomNumber(width / 6, width / 10);
            canvas.drawOval(new RectF(0, 0, radius, radius), mPaint);
            canvas.restore();
        }
    }

    /**
     * 绘制不规则的圆形散落的图片
     */
    public static void drawStar(Paint mPaint, Canvas canvas) {
        int width = canvas.getWidth() > canvas.getHeight() ? canvas.getHeight() : canvas.getWidth();

    }

    /**
     * 从area到number之间的随机数
     *
     * @return
     */
    public static int getRondomNumber(int number, int area) {
        number = number - area;
        return random.nextInt(number) + area;
    }

    /**
     * get Rgb
     *
     * @return
     */
    public static int getRgb() {
        return getRondomNumber(255, 100);
    }
}