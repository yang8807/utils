package com.yan.fastview_library.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yan.fastview_library.R;

import utils.DisplayUtil;


/**
 * Created by With smile to the end of the world on 2015/10/7 0007.
 * Function: given the Editext an attribute, make drawbale set pictures can fit; After the problem, when setting the fill_parent;
 * Here after, all is for centered; Only by controlling the paddingcenter: / / set the padding in the middle
 */
public class DrawableTextView extends TextView {

    private int drawableWidth = DisplayUtil.dip2px(getContext(), 25);
    private Drawable drawable;
    /**
     * Whether the center display
     */
    private boolean isPaddingCenter;

    public DrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.DrawableEditText);
        try {
            getProperities(tp, context);
        } finally {
            tp.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**If in the code set the center property, for the left and right padding in the layout is no longer work*/
        if (isPaddingCenter) {

            int parentwidth = getMeasuredWidth();
            /**
             * Here to measure; Through the brush, how long is a measure of the length of the text; This saves a parent control of resources;
             * */
            int paddingLeftOrRight = (int) (parentwidth - drawableWidth - this.getCompoundDrawablePadding() - this.getPaint().measureText(getText().toString())) / 2;
            this.setPadding(paddingLeftOrRight, 0, paddingLeftOrRight, 0);
        }
    }


    private void getProperities(TypedArray tp, Context context) {

        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.DrawableEditText_paddingCenter) {
                isPaddingCenter = tp.getBoolean(attr, false);
            } else if (attr == R.styleable.DrawableEditText_drawable_width) {
                drawableWidth = (int) tp.getDimension(attr, drawableWidth);
            } else if (attr == R.styleable.DrawableEditText_android_drawableBottom) {
                drawable = tp.getDrawable(attr);
                drawable.setBounds(0, 0, drawableWidth, drawableWidth);
                this.setCompoundDrawables(null, null, null, drawable);
            } else if (attr == R.styleable.DrawableEditText_android_drawableLeft) {
                drawable = tp.getDrawable(attr);
                drawable.setBounds(0, 0, drawableWidth, drawableWidth);
                this.setCompoundDrawables(drawable, null, null, null);
            } else if (attr == R.styleable.DrawableEditText_android_drawableRight) {
                drawable = tp.getDrawable(attr);
                int widthRight = DisplayUtil.dip2px(getContext(), 15);
                drawable.setBounds(0, 0, widthRight, widthRight);
                this.setCompoundDrawables(null, null, drawable, null);
            } else if (attr == R.styleable.DrawableEditText_android_drawableTop) {
                int widthTop = DisplayUtil.dip2px(getContext(), 70);
                drawable = tp.getDrawable(attr);
                drawable.setBounds(0, 0, widthTop, widthTop);
                this.setCompoundDrawables(null, drawable, null, null);
            }
        }
    }


    public void setTopDrawableWidth(int width) {
        if (drawable == null) return;
        width = DisplayUtil.dip2px(getContext(), width);
        drawable.setBounds(0, 0, width, width);
        this.setCompoundDrawables(null, drawable, null, null);
    }

    public DrawableTextView(Context context) {
        super(context, null);
    }
}
