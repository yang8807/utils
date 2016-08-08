package com.yan.fastview_library.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.magnify.yutils.ImageUtils;
import com.yan.fastview_library.R;

import utils.DisplayUtil;

public class ClearAutoCompleteText extends PowerAutoCompeleteEditText implements View.OnFocusChangeListener {
    /**
     * clear button
     */
    private Drawable mClearDrawable;
    /**
     * widget has focus?
     */
    private boolean hasFoucs;

    public ClearAutoCompleteText(Context context) {
        super(context, null);
    }


    public ClearAutoCompleteText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.ClearAutoCompleteText);
        init(tp);
        tp.recycle();

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (hasFoucs) {
                    setClearIconVisible(s.length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void init(TypedArray tp) {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            //throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.ic_highlight_remove_red_500_36dp);
            //According to the color of the incoming, to render the Drawable
//            mClearDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)

        }
        int tintColor = -1;
        tintColor = tp.getColor(R.styleable.ClearAutoCompleteText_clear_tint, -1);
        mClearDrawable = ImageUtils.bitmap2Drawable(ImageUtils.drawable2Bitmap(mClearDrawable));
        if (tintColor != -1)
            mClearDrawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        int width = DisplayUtil.dip2px(getContext(), 25);
        mClearDrawable.setBounds(0, 0, width, width);
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mClearDrawable, getCompoundDrawables()[3]);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
    }

    /**
     * Set the display and hide the icon, call setCompoundDrawables to draw up the EditText
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * Set rock animation
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * rock animation
     *
     * @param counts 1 seconds to wobble
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
