package com.yan.fastview_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.widget.TextView;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.DrawableUtils;
import com.magnify.yutils.data.ImageUtils;
import com.yan.fastview_library.static_userful.ShapeType;

/**
 * Created by heinigger on 16/8/7.
 */
public class TextViewExtends {
    private TextView textView;
    private Context mContext;

    private int backgroundColor = -1;
    private int radius = 0;
    private int shapeType = 0;
    private int drawableWidth, drawableHeight, leftDrawableWidth, rightDrawableWidth, topDrawableWidth, bottomDrawableWidth;
    private Drawable leftDrawable, rightDrawable, topDrawable, bottomDrawable;
    //图片渲染的颜色
    private int tintColor = -1;
    //强制居中
    private boolean forceCenter = false;
    private boolean willUseDrawable = true;

    public TextViewExtends(TextView textView, Context context) {
        this.textView = textView;
        this.mContext = context;
    }


    public TextViewExtends(TextView textView, Context context, TypedArray tp) {
        this.textView = textView;
        this.mContext = context;
        iniProperties(tp);
        tp.recycle();
        applyProperties();
    }

    /*应用属性*/
    private void applyProperties() {
        //设置背景
        setBackGround(backgroundColor, shapeType, radius);
        leftDrawable = setDrawableBounds(leftDrawable, leftDrawableWidth);
        rightDrawable = setDrawableBounds(rightDrawable, rightDrawableWidth);
        topDrawable = setDrawableBounds(topDrawable, topDrawableWidth);
        bottomDrawable = setDrawableBounds(bottomDrawable, bottomDrawableWidth);
        textView.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    public void forceCenter() {
        forceCenter(forceCenter);
    }

    public void forceCenter(boolean forceCenter) {
        if (forceCenter) {
            int drawablePadding = textView.getCompoundDrawablePadding();
            int measureWidth = textView.getMeasuredWidth();
            int measureHeight = textView.getMeasuredHeight();
            int leftDrawableWidths = 0;
            if (leftDrawable != null)
                leftDrawableWidths = leftDrawableWidth > 0 ? leftDrawableWidth : drawableWidth > 0 ? drawableWidth : leftDrawable.getIntrinsicWidth();
            int rightDrawableWidths = 0;
            if (rightDrawable != null)
                rightDrawableWidths = rightDrawableWidth > 0 ? rightDrawableWidth : drawableWidth > 0 ? drawableWidth : rightDrawable.getIntrinsicWidth();
            int topDrawableHeight = 0;
            if (topDrawable != null)
                topDrawableHeight = topDrawableWidth > 0 ? topDrawable.getIntrinsicHeight() / topDrawable.getIntrinsicWidth() * topDrawableWidth : drawableWidth > 0 ? drawableHeight : topDrawable.getIntrinsicHeight();
            int bottomDrawableHeight = 0;
            if (bottomDrawable != null)
                bottomDrawableHeight = bottomDrawableWidth > 0 ? bottomDrawable.getIntrinsicHeight() / bottomDrawable.getIntrinsicWidth() * leftDrawableWidth : drawableWidth > 0 ? drawableHeight : bottomDrawable.getIntrinsicHeight();
            int drawable_width = 0;
            int drawable_height = 0;

            textView.setGravity(Gravity.CENTER);

            if (leftDrawable != null) drawable_width += leftDrawableWidths;
            if (rightDrawable != null) drawable_width += rightDrawableWidths;
            if (topDrawable != null) drawable_height += topDrawableHeight;
            if (bottomDrawable != null) drawable_height += bottomDrawableHeight;

            int paddingLeftOrRight = (int) (measureWidth - drawable_width - drawablePadding - textView.getPaint().measureText(textView.getText().toString())) / 2;
            int paddingTopOrBottom = (int) (measureHeight - drawable_height - drawablePadding - textView.getTextSize() * 1.5) / 2;
            textView.setPadding(paddingLeftOrRight, paddingTopOrBottom, paddingLeftOrRight, paddingTopOrBottom);
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
//            lp.leftMargin = lp.rightMargin = lp.bottomMargin = lp.topMargin = 0;
//            textView.requestLayout();
        }
    }

    private Drawable setDrawableBounds(Drawable drawable, int directionDrawableWidth) {
        if (drawable != null) {
            if (leftDrawableWidth > 0) {
                drawable.setBounds(0, 0, directionDrawableWidth, directionDrawableWidth * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth());
            } else if (drawableWidth > 0) {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight > 0 ? drawableHeight : drawableWidth);
            } else {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            if (tintColor != -1)
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }

    /*初始化属性*/
    private void iniProperties(TypedArray tp) {
        int count = tp.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = tp.getIndex(i);
            if (attr == R.styleable.TextViewExtends_android_background) {//获取背景颜色
                backgroundColor = tp.getColor(attr, -1);
                if (backgroundColor == -1) {
                    Drawable drawable = tp.getDrawable(attr);
                    textView.setBackgroundDrawable(drawable);
                }
            } else if (attr == R.styleable.TextViewExtends_drawable_width) {//设置图片的大小,没有特别指定大小的话,所有的图片的大小都需要设置
                drawableWidth = drawableHeight = (int) tp.getDimension(attr, 0);
            } else if (attr == R.styleable.TextViewExtends_leftDrawableWidth) {//左边图片的大小
                leftDrawableWidth = (int) tp.getDimension(attr, -1);
            } else if (attr == R.styleable.TextViewExtends_righDrawableWidth) {//右边图片的大小
                rightDrawableWidth = (int) tp.getDimension(attr, -1);
            } else if (attr == R.styleable.TextViewExtends_topDrawableWidth) {//顶部图片的大小
                topDrawableWidth = (int) tp.getDimension(attr, -1);
            } else if (attr == R.styleable.TextViewExtends_bottomDrawableWidth) {//底部图片大小
                bottomDrawableWidth = (int) tp.getDimension(attr, -1);
            } else if (attr == R.styleable.TextViewExtends_android_tint) {//设置图片渲染的颜色
                tintColor = tp.getColor(attr, -1);
            } else if (attr == R.styleable.TextViewExtends_forceCenter) {//强制居中
                forceCenter = tp.getBoolean(attr, false);
            } else if (attr == R.styleable.TextViewExtends_shapeType) {//背景的形状
                shapeType = tp.getInt(attr, 1);
                radius = shapeType == ShapeType.CORNERRECT ? DeviceUtil.dipToPx(mContext, 8) : 0;
            } else if (attr == R.styleable.TextViewExtends_tv_radius) {//设置是圆角背景背景时的半径
                radius = (int) tp.getDimension(attr, DeviceUtil.dipToPx(mContext, 8));
            } else if (attr == R.styleable.TextViewExtends_drawable_height) {
                drawableHeight = (int) tp.getDimension(attr, 0);
            } else if (attr == R.styleable.TextViewExtends_willUseSameDrawable) {
                willUseDrawable = tp.getBoolean(attr, true);
            }
        }

        if (willUseDrawable) {
            //设置底部图片
            bottomDrawable = tp.getDrawable(R.styleable.TextViewExtends_android_drawableBottom);
            //设置左边图片
            leftDrawable = tp.getDrawable(R.styleable.TextViewExtends_android_drawableLeft);
            //设置右边图片
            rightDrawable = tp.getDrawable(R.styleable.TextViewExtends_android_drawableRight);
            //设置顶部图片
            topDrawable = tp.getDrawable(R.styleable.TextViewExtends_android_drawableTop);
        } else {
            bottomDrawable = createNewDrawable(tp.getDrawable(R.styleable.TextViewExtends_android_drawableBottom));
            leftDrawable = createNewDrawable(tp.getDrawable(R.styleable.TextViewExtends_android_drawableLeft));
            rightDrawable = createNewDrawable(tp.getDrawable(R.styleable.TextViewExtends_android_drawableRight));
            topDrawable = createNewDrawable(tp.getDrawable(R.styleable.TextViewExtends_android_drawableTop));
        }

    }

    private Drawable createNewDrawable(Drawable drawable) {
        if (drawable != null)
            return ImageUtils.bitmap2Drawable(ImageUtils.drawable2Bitmap(drawable));
        return null;
    }

    /*-------------------------------设置背景---------------------------------------*/
    public TextViewExtends setBackGround(@ColorInt int color) {
        return setBackGround(color, ShapeType.RECT, 0);
    }

    public TextViewExtends setCircyeBackGround(@ColorInt int color) {
        return setBackGround(color, ShapeType.OVAL, 0);
    }

    public TextViewExtends setRoundBackGround(@ColorInt int color, int radius) {
        return setBackGround(color, ShapeType.CORNERRECT, radius);
    }

    private TextViewExtends setBackGround(@ColorInt int color, int shapeType, int radius) {
        this.backgroundColor = color;
        this.radius = radius;
        this.shapeType = shapeType;
        if (backgroundColor != -1) {
            Drawable drawable = null;
            boolean clickable = textView.isClickable();
            if (this.shapeType == ShapeType.RECT) {
                drawable = clickable ? DrawableUtils.getStateRectDrawable(backgroundColor) : DrawableUtils.getRectDrawable(backgroundColor);
            } else if (this.shapeType == ShapeType.OVAL) {
                drawable = clickable ? DrawableUtils.getStateOvalDrawable(backgroundColor) : DrawableUtils.getOvalDrawable(backgroundColor);
            } else if (this.shapeType == ShapeType.CORNERRECT) {
                drawable = clickable ? DrawableUtils.getStateCorRectDrawable(backgroundColor, this.radius) : DrawableUtils.getCorRectDrawable(backgroundColor, this.radius);
            }
            textView.setBackgroundDrawable(drawable);
        }
        return this;
    }

}
