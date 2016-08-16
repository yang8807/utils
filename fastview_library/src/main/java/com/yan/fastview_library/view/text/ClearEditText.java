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
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtil;
import com.magnify.yutils.data.ImageUtils;
import com.yan.fastview_library.R;


public class ClearEditText extends PowerEditText implements OnFocusChangeListener, TextWatcher {
    /**
     * clear button
     */
    private Drawable mClearDrawable;
    /**
     * widget has focus?
     */
    private boolean hasFoucs;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.ClearAutoCompleteText);
        init(tp);
        tp.recycle();

    }

    private void init(TypedArray tp) {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.ic_highlight_remove_red_500_36dp);
        }
        int tintColor = -1;
        tintColor = tp.getColor(R.styleable.ClearAutoCompleteText_clear_tint, -1);
        mClearDrawable = ImageUtils.bitmap2Drawable(ImageUtils.drawable2Bitmap(mClearDrawable));
        if (tintColor != -1)
            mClearDrawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        int width = DeviceUtil.dipToPx(getContext(), 25);
        mClearDrawable.setBounds(0, 0, width, width);
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mClearDrawable, getCompoundDrawables()[3]);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * Because we can not directly to the EditText to set up the event, so we use to remember the location of our press to simulate the click event
     * When we press the position in the EditText width - the icon to the right of the control to the distance between the right of the icon and the width of the icon
     * EditText width - the distance between the icon to the right of the control, we even click the icon, the vertical direction is not considered
     */
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

    /**
     * When the focus of the ClearEditText changes, the length of the string to determine the length of the display to remove the icon and hide
     */
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
     * Set the display and hide the icon, call setCompoundDrawables to draw up the EditText
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * When the input box changes the contents of the time the callback method
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
        LogUtil.v("mine", s.toString() + "onTextChanged");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        //The last text editor
        LogUtil.v("mine", s.toString() + "beforeTextChanged");
    }

    @Override
    public void afterTextChanged(Editable s) {

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