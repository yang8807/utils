package com.yan.fastview_library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
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
    //绘制的单位高度
    private int unitWidth;
    //画布的高度
    private int pHeight;
    //画布的宽度
    private int pWidth;
    private Paint mPaint;
    //开始绘制的位置
    private int startTextY;
    private int normalColor = Color.BLACK;
    private int selectColor = Color.GREEN;
    private Drawable mBackDrawable;
    private int mTextSize = 26;
    //选中的位置
    private int selectPosition = 0;
    private onTouchCharactersListerner onTouchCharactersListerner;
    private Drawable mCharacterTouchDrawable;

    public void setOnTouchCharactersListerner(SideBar.onTouchCharactersListerner onTouchCharactersListerner) {
        this.onTouchCharactersListerner = onTouchCharactersListerner;
    }

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
            } else if (attr == R.styleable.SideBar_android_text) {
                String text = tp.getString(attr);
                if (!TextUtils.isEmpty(text)) {
                    defaultSideData = text.trim().toCharArray();
                }
            } else if (attr == R.styleable.SideBar_sb_touch_character_drawable) {
                mCharacterTouchDrawable = tp.getDrawable(attr);
            }
        }

        tp.recycle();
        setClickable(true);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        pHeight = canvas.getHeight() - getPaddingBottom() - getPaddingTop();
        pWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        //每个字母的单元大小
        unitWidth = Math.min(pHeight, pWidth * defaultSideData.length) / defaultSideData.length;
        //绘制的起点位置
        int startX = (pWidth - unitWidth) / 2;
        //绘制文本的起点位置
        startTextY = TextDrawUtils.getBaseLineY(mPaint, unitWidth) + (canvas.getHeight() - unitWidth * defaultSideData.length) / 2;
        //绘制drawable的起点位置
        int startY = (canvas.getHeight() - unitWidth * defaultSideData.length) / 2;
        for (int i = 0; i < defaultSideData.length; i++) {
            //绘制字母被选中后的drwable
            if (selectPosition == i && mCharacterTouchDrawable != null) {
                mCharacterTouchDrawable.setBounds(0, 0, unitWidth, unitWidth);
                canvas.save();
                canvas.translate(startX, startY);
                mCharacterTouchDrawable.draw(canvas);
                canvas.restore();
            }
            //绘制完背景,再绘制文本,才不会被挡住
            String drawText = String.valueOf(defaultSideData[i]);
            mPaint.setColor(selectPosition == i ? selectColor : normalColor);
            canvas.drawText(drawText, (pWidth - mPaint.measureText(drawText)) / 2, startTextY, mPaint);
            startTextY += unitWidth;
            startY += unitWidth;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float downY = event.getY();
        if (downY > getPaddingTop() && downY < getMeasuredHeight() - getPaddingBottom()) {//设置了paddingTop和paddingBootom的处理,没设置,就是0了
            selectPosition = (int) ((downY - getPaddingTop()) / pHeight * defaultSideData.length);
            switch (event.getAction()) {
                default:
                    willInvalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    willInvalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void willInvalidate() {
        if (selectPosition >= 0 && selectPosition < defaultSideData.length) {
            if (onTouchCharactersListerner != null) {
                if (onTouchCharactersListerner.onTouchCharacter(selectPosition, String.valueOf(defaultSideData[selectPosition])))
                    invalidate();//设置了监听事件,返回是true时,才进行重新绘制
            } else
                invalidate();//没有设置监听事件,都可以绘制
            return;
        }
    }

    public void setBackGroundDrawable(Drawable mBackDrawabwle) {
        if (Build.VERSION_CODES.JELLY_BEAN >= Build.VERSION.SDK_INT)
            setBackground(mBackDrawabwle);
        else
            setBackgroundDrawable(mBackDrawabwle);
    }

    public interface onTouchCharactersListerner {
        /**
         * @param position  选中的位置
         * @param touchText 选中的文本
         * @return 是否重新绘制, 比如如果用户选中, 但是数据并没有这一项, 此时可以不绘制
         */
        boolean onTouchCharacter(int position, String touchText);
    }
}
