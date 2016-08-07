package com.yan.fastview_library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.yan.fastview_library.R;
import com.yan.fastview_library.TextViewExtends;

/**
 * Created by heinigger on 16/8/7.
 */
public class PowerButton extends Button {

    private TextViewExtends textViewExtends;

    public PowerButton(Context context) {
        super(context);
        textViewExtends = new TextViewExtends(this, context);
    }

    public PowerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.TextViewExtends);
        textViewExtends = new TextViewExtends(PowerButton.this, context, tp);
    }

    public PowerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.TextViewExtends);
        textViewExtends = new TextViewExtends(PowerButton.this, context, tp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textViewExtends.forceCenter();
    }

    public TextViewExtends getTextViewExtends() {
        return textViewExtends;
    }
}
