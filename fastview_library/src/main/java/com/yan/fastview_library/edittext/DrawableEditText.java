package com.yan.fastview_library.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;
import com.yan.fastview_library.R;
import utils.DisplayUtil;
/**
 * Created by heinigger on 2015/10/7 0007.
 * function:In a given Editext an attribute, make drawbale set pictures can fit
 */
@Deprecated
public class DrawableEditText extends EditText {
    private int drawableWidth;
    private Drawable drawable;
    protected int tintColor;

    public DrawableEditText(Context context) {
        super(context, null);
    }

    public DrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context, attrs);
    }

    public DrawableEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        ini(context, attrs);
    }

    private void ini(Context context, AttributeSet attrs) {
        drawableWidth = DisplayUtil.dip2px(getContext(), 25);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.DrawableEditText);
        try {
            getProperities(tp, context);
        } finally {
            tp.recycle();
        }
    }

    /***
     *Get all attributes in the layout
     */
    private void getProperities(TypedArray tp, Context context) {

        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.DrawableEditText_drawable_width) {
                drawableWidth = (int) tp.getDimension(attr, drawableWidth);
            } else if (attr == R.styleable.DrawableEditText_android_tint) {
                tintColor = tp.getColor(attr, tintColor);
            } else if (attr == R.styleable.DrawableEditText_android_drawableBottom) {
                reSetDrawable(tp, attr);
                this.setCompoundDrawables(null, null, null, drawable);
            } else if (attr == R.styleable.DrawableEditText_android_drawableLeft) {
                reSetDrawable(tp, attr);
                this.setCompoundDrawables(drawable, null, null, null);
            } else if (attr == R.styleable.DrawableEditText_android_drawableRight) {
                reSetDrawable(tp, attr);
                this.setCompoundDrawables(null, null, drawable, null);
            } else if (attr == R.styleable.DrawableEditText_android_drawableTop) {
                reSetDrawable(tp, attr);
                this.setCompoundDrawables(null, drawable, null, null);
            }
        }

    }

    /***
     * Color rendering of the specified image, and redesign
     */
    private void reSetDrawable(TypedArray tp, int attr) {
        drawable = tp.getDrawable(attr);
        drawable.setBounds(0, 0, drawableWidth, drawableWidth);
        if (tintColor != 0) {
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }

}
