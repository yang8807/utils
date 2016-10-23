package com.yan.fastview_library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtils;
import com.magnify.yutils.data.ImageUtils;
import com.yan.fastview_library.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by heinigger on 16/9/11.
 * 所在的父布局需设置clipseChildren=false
 * 半成品..很多都不能改了,只能是这种向上的效果
 */
public class FavoriateView extends ImageView implements View.OnClickListener {
    private PathMeasure mPathMeasure;
    private OnActionListener onActionListener;
    private OnClickListener onClickListener;
    //运动的最大距离
    private int mMaxDistance = DeviceUtil.dipToPx(getContext(), 300);
    //生成运动的路径
    private ArrayList<FavoriteInfos> mFavorites = new ArrayList<>();

    private Random mRandom;

    private Paint mPaint;

    private int duration = 2000;
    private int mIncreasingSpeed = 50;
    //绘制的图片
    private Drawable mDrawable;
    //当前绘制的点位置
    private float[] mCurrentDrawaPoint = new float[2];

    //绘制任务
    private DrawableRunnale mDrawTask;
    private int mIntervalTime = 40;

    //随机生成路径的角度
    //开始绘制的角度
    private int mStartAngle = -120;
    //扫过的角度
    private int mSwipeAngle = 60;

    public FavoriateView(Context context) {
        this(context, null);
    }

    public FavoriateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavoriateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnClickListener(this);
        mPathMeasure = new PathMeasure();
        mRandom = new Random();
        obtainStyle(context, attrs);
        iniPaint();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void obtainStyle(Context context, AttributeSet attrs) {
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.FavoriateView);
        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.FavoriateView_fv_drwable) {
                mDrawable = tp.getDrawable(attr);
            } else if (attr == R.styleable.FavoriateView_fv_duration) {
                duration = tp.getInt(attr, 5000);
            } else if (attr == R.styleable.FavoriateView_fv_max_distance) {
                mMaxDistance = (int) tp.getDimension(attr, DeviceUtil.dipToPx(getContext(), 300));
            }
        }
        //每50毫秒做一次动画
        mIncreasingSpeed = mMaxDistance * mIntervalTime / duration;
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) return;
        canvas.save();
        for (int i = 0; i < mFavorites.size(); i++) {
            FavoriteInfos mFavoriteInfo = mFavorites.get(i);
            mPaint.setColor(mFavoriteInfo.getColor());
            mPaint.setAlpha((int) (mFavoriteInfo.getScale() * 255));
//            canvas.drawPath(mFavoriteInfo.getPath(), mPaint);
            mPathMeasure.setPath(mFavoriteInfo.getPath(), true);
            mPathMeasure.getPosTan(mFavoriteInfo.getDistance(), mCurrentDrawaPoint, null);
            Bitmap mBitmap = mFavoriteInfo.getBitmap();
            canvas.drawBitmap(mBitmap, mCurrentDrawaPoint[0] - mBitmap.getWidth() / 2, mCurrentDrawaPoint[1] - mBitmap.getWidth() / 2, mPaint);
            if (mFavoriteInfo.isRemove()) {//资源回收
                mFavorites.remove(mFavoriteInfo);
                mFavoriteInfo.setBitmap(null);
                mBitmap.recycle();
            }
        }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @Override
    public void onClick(View view) {
        if (mDrawTask == null) mDrawTask = new DrawableRunnale();
        mFavorites.add(new FavoriteInfos(mMaxDistance));
        invalidate();
        postDelayed(mDrawTask, mIntervalTime);
        if (onClickListener != null) onClick(view);
        if (onActionListener != null)
            onActionListener.onAction(view, mFavorites.size());
    }

    class FavoriteInfos {
        //绘制图片的颜色
        private int color;
        //运动的距离
        private int distance;
        //运动的路径
        private Path mPath;
        //运动长度所占总长度的比例
        private float scale;
        private int mMaDistance;
        private Bitmap mBitmap;

        public FavoriteInfos(int mMaxDistance) {
            this.mMaDistance = mMaxDistance;
            //趋向亮色系
            this.color = Color.argb(255, mRandom.nextInt(200) + 55, mRandom.nextInt(200) + 55, mRandom.nextInt(200) + 55);
            //生成路径
            mPath = new Path();
            //随机生成的角度
            int mAngle = mRandom.nextInt(mSwipeAngle) + mStartAngle;
            int mTargertX = (int) (mMaxDistance * Math.cos(mAngle));
            int mTargertY = -Math.abs((int) (mMaxDistance * Math.sin(mAngle))) - DeviceUtil.dipToPx(getContext(), 1000);
            LogUtils.v("mine", mMaxDistance, mAngle, mTargertX, mTargertY);
            mPath.moveTo(FavoriateView.this.getMeasuredWidth() / 2, 0);
            //二次贝塞尔曲线
            mPath.quadTo((float) (mMaxDistance * Math.cos(mAngle + mRandom.nextInt(5)) / 5),
                    (float) (mMaxDistance * Math.sin(mAngle + mRandom.nextInt(5)) / 5),
                    mTargertX + FavoriateView.this.getMeasuredWidth() / 2,
                    mTargertY + FavoriateView.this.getMeasuredHeight() / 2);
            mBitmap = ImageUtils.createRGBImage(mDrawable, color);

        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public void setBitmap(Bitmap mBitmap) {
            this.mBitmap = mBitmap;
        }

        public float getScale() {
            return 1 - distance / (float) mMaDistance;
        }

        public int getDistance() {
            return distance += mIncreasingSpeed;
        }

        public int getColor() {
            return color;
        }

        //是否可以进行移除
        public boolean isRemove() {
            return distance >= mMaDistance;
        }

        public Path getPath() {
            return mPath;
        }
    }

    private class DrawableRunnale implements Runnable {

        @Override
        public void run() {
            postInvalidate();
            removeCallbacks(this);//防止被多次启动
            if (mFavorites.size() > 0)
                postDelayed(this, mIntervalTime);
        }
    }

    public interface OnActionListener {
        public void onAction(View view, int count);
    }
}
