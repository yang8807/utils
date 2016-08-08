package com.yan.fastview_library.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.yan.fastview_library.R;
import com.yan.fastview_library.TextViewExtends;

/**
 * Created by heinigger on 16/8/7.
 */
public class PowerAutoCompeleteEditText extends AutoCompleteTextView {

    private TextViewExtends textViewExtends;

    public PowerAutoCompeleteEditText(Context context) {
        super(context);
        textViewExtends = new TextViewExtends(this, context);
    }

    public PowerAutoCompeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.TextViewExtends);
        textViewExtends = new TextViewExtends(PowerAutoCompeleteEditText.this, context, tp);
    }

    public PowerAutoCompeleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.TextViewExtends);
        textViewExtends = new TextViewExtends(PowerAutoCompeleteEditText.this, context, tp);
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
