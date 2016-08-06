package com.yan.fastview_library.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.yan.fastview_library.R;
import com.yan.fastview_library.static_userful.Oriention;
import com.yan.fastview_library.static_userful.ShapeType;

import utils.DrawableUtils;

/**
 * Function:Can be set up and down or so on both sides of the pictures, but not the right click the close button
 */
public class PowerAutoCompleteText extends AutoCompleteTextView {
    /***
     * In the text left, right, on, under the provisions of the image size
     */
    private int drawableLeftWidth;
    private int drawableRightWidth;
    private int drawableTopWidth;
    private int drawableBottomWidth;
    /***
     * The background of the button
     */
    private Drawable mBackdrawable;
    /***
     * Button type of character
     */
    private int shapeType;
    /***
     * The background color of the button
     */
    private int backGroundColor = Color.parseColor("#545454");
    /**
     * The radius of the rounded rectangle by default button
     */
    private int defaultRadius = dip2Px(5);
    /**
     *The size of the set mPaddDrawable
     */
    private int drawable_width = dip2Px(50);
    /***
     * Displayed in the middle of the paddingDrawable
     */
    private Drawable mPaddDrawable;
    /***
     * To cope with some buttons, need also need the picture on the left
     */
    private Drawable mLeftDrawable;
    /**
     * Whether in the middle
     */
    private boolean paddingCenter = true;
    /**
     * To paint the specified image in the specified color
     */
    private int tintColor = Color.parseColor("#ff8854");
    /***
     * Is horizontal or vertical direction: true: according to horizontal direction, or false on behalf of the vertical direction
     */
    private boolean isHorizontal = true;
    private int mOriention = 0;

    public PowerAutoCompleteText(Context context) {
        super(context, null);
    }

    public PowerAutoCompleteText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PowerButton);
            getProperties(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    public PowerAutoCompleteText(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PowerButton);
            getProperties(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /***
     * Get attributes of the XML file here
     */
    private void getProperties(TypedArray typedArray) {
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.PowerButton_android_background) {
                backGroundColor = typedArray.getColor(attr, backGroundColor);
            } else if (attr == R.styleable.PowerButton_shapeType) {
                shapeType = typedArray.getInteger(attr, 1);
                getRelativDrawable(shapeType);
            } else if (attr == R.styleable.PowerButton_drawable_width) {
                drawable_width = (int) typedArray.getDimension(attr, dip2Px(60));
            } else if (attr == R.styleable.PowerButton_paddingCenter) {
                paddingCenter = typedArray.getBoolean(attr, true);
            } else if (attr == R.styleable.PowerButton_android_tint) {
                tintColor = typedArray.getColor(attr, tintColor);
            } else if (attr == R.styleable.PowerButton_android_drawableLeft) {
                mOriention = Oriention.left;
                mPaddDrawable = typedArray.getDrawable(attr);
                mLeftDrawable = mPaddDrawable;
            } else if (attr == R.styleable.PowerButton_android_drawableRight) {
                mOriention = Oriention.right;
                mPaddDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.PowerButton_android_drawableTop) {
                mOriention = Oriention.top;
                mPaddDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.PowerButton_android_drawableBottom) {
                mOriention = Oriention.bottom;
                mPaddDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.PowerButton_leftDrawableWidth) {
                drawableLeftWidth = (int) typedArray.getDimension(attr, drawableLeftWidth);
            } else if (attr == R.styleable.PowerButton_righDrawableWidth) {
                drawableRightWidth = (int) typedArray.getDimension(attr, drawableRightWidth);
            } else if (attr == R.styleable.PowerButton_topDrawableWidth) {
                drawableTopWidth = (int) typedArray.getDimension(attr, drawableTopWidth);
            } else if (attr == R.styleable.PowerButton_bottomDrawableWidth) {
                drawableBottomWidth = (int) typedArray.getDimension(attr, drawableBottomWidth);
            }
        }
        setDrawableSize();
    }

    /**
     * To set the drawable properties
     **/
    private void setDrawableSize() {
        if (mPaddDrawable != null) {
            mPaddDrawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            mPaddDrawable.setBounds(0, 0, drawable_width, drawable_width);
            if (mOriention == Oriention.left) {
                isHorizontal = true;
                if (drawableLeftWidth != 0)
                    mPaddDrawable.setBounds(0, 0, drawableLeftWidth, drawableLeftWidth);
                this.setCompoundDrawables(mPaddDrawable, null, null, null);
            } else if (mOriention == Oriention.bottom) {
                isHorizontal = false;
                if (drawableBottomWidth != 0)
                    mPaddDrawable.setBounds(0, 0, drawableBottomWidth, drawableBottomWidth);
                this.setCompoundDrawables(null, null, null, mPaddDrawable);
            } else if (mOriention == Oriention.right) {
                isHorizontal = true;
                if (drawableRightWidth != 0)
                    mPaddDrawable.setBounds(0, 0, drawableRightWidth, drawableRightWidth);
                if (mLeftDrawable != null && drawableLeftWidth != 0)
                    mLeftDrawable.setBounds(0, 0, drawableLeftWidth, drawableLeftWidth);
                this.setCompoundDrawables(mLeftDrawable, null, mPaddDrawable, null);
            } else if (mOriention == Oriention.top) {
                isHorizontal = false;
                if (drawableTopWidth != 0)
                    mPaddDrawable.setBounds(0, 0, drawableTopWidth, drawableTopWidth);
                this.setCompoundDrawables(null, mPaddDrawable, null, null);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**If in the code set the center property, for the left and right padding in the layout is no longer work*/
        if (paddingCenter) {
            if (isHorizontal) {
                onMeasureHorizontal();
            } else {
                onMeasureVertical();
            }
        }
    }

    private void onMeasureVertical() {
        int parentHeight = getMeasuredHeight();
        int paddingTopOrBottom = (int) (parentHeight - drawable_width - this.getCompoundDrawablePadding() - this.getPaint().getTextSize() * 1.5) / 2;
        this.setPadding(0, paddingTopOrBottom, 0, paddingTopOrBottom);
    }

    /***
     *  Horizontal distance measurement, which is centered horizontally
     */
    private void onMeasureHorizontal() {
        int parentwidth = getMeasuredWidth();
        /**
         * Here to measure; Through the brush, how long is a measure of the length of the text; This saves a parent control of resources;
         * */
        int paddingLeftOrRight = (int) (parentwidth - drawable_width - this.getCompoundDrawablePadding() - this.getPaint().measureText(getText().toString())) / 2;
        this.setPadding(paddingLeftOrRight, 0, paddingLeftOrRight, 0);
    }

    /**
     * According to the corresponding data types, get the corresponding item
     */
    private void getRelativDrawable(int shapeType) {
        if (shapeType == ShapeType.RECT) {
            mBackdrawable = DrawableUtils.getStateRectDrawable(backGroundColor);
        } else if (shapeType == ShapeType.OVAL) {
            mBackdrawable = DrawableUtils.getStateOvalDrawable(backGroundColor);
        } else if (shapeType == ShapeType.CORNERRECT) {
            mBackdrawable = DrawableUtils.getStateCorRectDrawable(backGroundColor, defaultRadius);
        }
        this.setBackgroundDrawable(mBackdrawable);
    }

    private int dip2Px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
