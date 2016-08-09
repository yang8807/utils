package com.yan.fastview_library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.yan.fastview_library.R;

/**
 * Created by 洒笑天涯 on 2015/10/17.;
 * Function:一个主要用来装饰的控件
 */
public class DecoratedView extends View {

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 所需要绘制先的高度
     */
    private int mLineHeight = (int) dip2px(0.5f);
    /**
     * 是否需要绘制虚线
     */
    private boolean isDot = false;

    /**
     * 选择需要绘制的类型
     *
     * @return
     */
    public class DecotatedType {
        //只绘制线条
        public final static int NONE = 0;
        //绘制图片
        public final static int DRAWABLE = 1;
        //绘制文本
        public final static int TEXT = 2;
    }

    private boolean isPic = false;
    /**
     * 需要绘制的文本
     */
    private String decoratedText = "没有填写";
    /**
     * 需要绘制在中的图片
     */
    private Drawable mDrawaBmp;
    /**
     * 绘制两边线条的颜色
     */
    private int mLineColor;
    /**
     * 文字的默认颜色
     */
    private int mTextColor = getResources().getColor(android.R.color.black);
    //虚线的实线部分的单位长度
    private int baseWidth = (int) dip2px(5);
    //虚线的间距
    private int baseCap = (int) dip2px(0);
    /**
     * 选择绘制的类型
     */
    private int decoratedType = 0;
    /**
     * 离中心的修饰物之间的间距
     */
    private int paddingCenter;
    /**
     * 设置文字的大小
     */
    private int mTextSize = (int) dip2px(25);


    public DecoratedView(Context context) {
        super(context, null);
    }

    public DecoratedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPeropertioes(context, attrs);
    }

    /**
     * @retrun 初始化画笔
     */
    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mLineHeight/2);
        mLineColor = mLineColor == 0 ? mTextColor : mLineColor;
        mPaint.setColor(mTextColor);

    }

    /**
     * 获取在attr中的属性
     *
     * @return
     */
    private void getPeropertioes(Context context, AttributeSet attrs) {
        TypedArray ta = null;
        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.DecoratedView);
            int proCount = ta.getIndexCount();
            for (int i = 0; i < proCount; i++) {
                int attr = ta.getIndex(i);
                if (attr == R.styleable.DecoratedView_android_src) {
                    mDrawaBmp = ta.getDrawable(attr);
                } else if (attr == R.styleable.DecoratedView_android_text) {
                    decoratedText = ta.getString(attr);
                } else if (attr == R.styleable.DecoratedView_android_textColor) {
                    mTextColor = ta.getColor(attr, getResources().getColor(android.R.color.holo_blue_bright));
                } else if (attr == R.styleable.DecoratedView_dev_lineColor) {
                    mLineColor = ta.getColor(attr, getResources().getColor(android.R.color.holo_blue_bright));
                } else if (attr == R.styleable.DecoratedView_dev_paddingCenter) {
                    paddingCenter = (int) ta.getDimension(attr, 0);
                } else if (attr == R.styleable.DecoratedView_android_textSize) {
                    mTextSize = (int) ta.getDimension(attr, 0);
                } else if (attr == R.styleable.DecoratedView_dev_lineHeight) {
                    mLineHeight = (int) ta.getDimension(attr, 0);
                } else if (attr == R.styleable.DecoratedView_dev_lineWidth) {
                    baseWidth = (int) ta.getDimension(attr, 5);
                } else if (attr == R.styleable.DecoratedView_dev_lineCap) {
                    baseCap = (int) ta.getDimension(attr, 0);
                    if (baseCap == 0) isDot = false;
                    else isDot = true;
                } else if (attr == R.styleable.DecoratedView_dev_type) {
                    decoratedType = ta.getInt(R.styleable.DecoratedView_dev_type, 0);
                }
            }
        } finally {
            ta.recycle();
        }
        iniPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int parentWidth = canvas.getWidth();
        int parentHeight = canvas.getHeight();
        int centerWidth = 0;
        //绘制中心装饰的东西
        if (decoratedType == DecotatedType.DRAWABLE) {
            centerWidth = drawDrawable(canvas, parentWidth, parentHeight);
        } else if (decoratedType == DecotatedType.TEXT) {
            //绘制文本
            mPaint.setTextSize(mTextSize);
            int fitCenterBaseLine = getBaseLineY(parentHeight);
            canvas.drawText(decoratedText, (parentWidth - mPaint.measureText(decoratedText)) / 2, fitCenterBaseLine, mPaint);
            centerWidth = (int) mPaint.measureText(decoratedText);
        } else {
            //其他情况,默认是绘制线条
            paddingCenter = baseCap / 2;
        }
        //如果不指定,默认绘制的就是线条;绘制两边装饰的线条
        mPaint.setColor(mLineColor);
        if (isDot) drawDotLine(canvas, parentWidth, parentHeight / 2, centerWidth);
        else drawFullLine(canvas, parentWidth, parentHeight, centerWidth);

    }

    /**
     * 绘制drawable
     *
     * @return
     */
    private int drawDrawable(Canvas canvas, int parentWidth, int parentHeight) {
        int centerWidth;
        if (mDrawaBmp == null)
            throw new NullPointerException("选择drawable属性,请设置android:src=\"\"");
        canvas.save();//记录上一次canvas的状态,当canvas绘制完成之后,需要调用canvas的resotre方法来回到上一次保存的状态;
        //如果ParentHeight的高度太小了,就使用parentHeight的高度来作为图片的高度;
        if (mDrawaBmp.getIntrinsicHeight() > parentHeight) {
            int padding = (int) dip2px(2);
            int mRelWidth = (int) (mDrawaBmp.getIntrinsicWidth() * (parentHeight - padding) / mDrawaBmp.getIntrinsicHeight());
            mDrawaBmp.setBounds(0, 0, mRelWidth, parentHeight - padding);
            canvas.translate(parentWidth / 2 - mRelWidth / 2, parentHeight / 2 - (parentHeight - padding) / 2);
            centerWidth = mRelWidth;
        } else {
            //绘制图片
            mDrawaBmp.setBounds(0, 0, mDrawaBmp.getIntrinsicWidth(), mDrawaBmp.getIntrinsicHeight());
            canvas.translate(parentWidth / 2 - mDrawaBmp.getIntrinsicWidth() / 2, parentHeight / 2 - mDrawaBmp.getIntrinsicHeight() / 2);
            centerWidth = mDrawaBmp.getIntrinsicWidth();
        }
        mDrawaBmp.draw(canvas);
        canvas.restore();

        return centerWidth;
    }

    /**
     * 得到文本居中的baseline,注意canvans的drawText方法中drawText(x,y....);y所对应的是此baseLine点
     *
     * @return
     */
    private int getBaseLineY(int parentHeight) {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (int) (parentHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2);
    }

    /**
     * 绘制虚线
     *
     * @return
     */
    private void drawDotLine(Canvas canvas, int parentWidth, int parentHeight, int showWidth) {
        int count = (parentWidth - showWidth) / (baseWidth * 2 + baseCap * 2);//分别得到两变得count数,应该从中间画起,才不会让人觉得奇怪
        int left = parentWidth / 2 - showWidth / 2 - paddingCenter;
        int right = parentWidth / 2 + showWidth / 2 + paddingCenter;
        //被气死了,参数写错了,一直写成了count,由此可见,脑子不清醒时多么的恐怖
        for (int i = 0; i < count + 1; i++) {
            //Log.v("mine", count + "couont" + baseWidth + ":" + baseCap + "baseCap" + paddingCenter + "paddingCenter");
            canvas.drawLine(left - baseWidth * (i + 1) - baseCap * i, parentHeight, left - (baseCap + baseWidth) * i, parentHeight, mPaint);
            canvas.drawLine(right + (baseCap + baseWidth) * i, parentHeight, right + (baseCap + baseWidth) * (i + 1) - baseCap, parentHeight, mPaint);
        }
    }

    public void setmLineHeight(int mLineHeight) {
        this.mLineHeight = mLineHeight;
        mPaint.setStrokeWidth(mLineHeight);
    }

    public void setIsDot(boolean isDot) {
        this.isDot = isDot;
    }

    /**
     * 绘制实线
     *
     * @reutrn
     */
    private void drawFullLine(Canvas canvas, int parentWidth, int parentHeight, int showWidth) {
        canvas.drawLine(0, parentHeight / 2, parentWidth / 2 - showWidth / 2 - paddingCenter, parentHeight / 2, mPaint);
        canvas.drawLine(parentWidth / 2 + showWidth / 2 + paddingCenter, parentHeight / 2, parentWidth, parentHeight / 2, mPaint);
    }

    public float dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public void setDecoratedText(String decoratedText) {
        this.decoratedText = decoratedText;
    }

    public void setmLineColor(int mLineColor) {
        this.mLineColor = mLineColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        invalidate();
    }

    public void setBaseWidth(int baseWidth) {
        this.baseWidth = baseWidth;
    }

    public void setBaseCap(int baseCap) {
        this.baseCap = baseCap;
    }

    public void setDecoratedType(int decoratedType) {
        this.decoratedType = decoratedType;
    }
}