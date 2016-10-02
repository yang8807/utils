package com.yan.fastview_library.view.text;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.magnify.yutils.LogUtils;

/**
 * Created by heinigger on 16/8/22.
 * 自适应大小的文本控件
 */
public class AdaptTextView extends TextView {
    private static float DEFAULT_MIN_TEXT_SIZE = 3;
    private static float DEFAULT_MAX_TEXT_SIZE = 20;
    // Attributes
    private Paint testPaint;
    private float minTextSize, maxTextSize;

    public AdaptTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());

        // max size defaults to the intially specified text size unless it is
        // too small
        maxTextSize = this.getTextSize();

        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHegiht) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int avaliableHeight = textHegiht - this.getPaddingBottom() - this.getPaddingTop();
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            LogUtils.v("mine", testPaint.measureText(text) + ":" + availableWidth);

            while ((trySize > minTextSize) && getMaxLines() < (testPaint.measureText(text) / availableWidth + 0.5)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
                LogUtils.v("mine", testPaint.measureText(text) + ":" + availableWidth);
            }/*while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth) && avaliableHeight >= DeviceUtil.sp2px(getContext(), trySize) * getLineCount()) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
                LogUtils.v("mine", testPaint.measureText(text) + ":" + availableWidth);
            }*/
            this.setTextSize(trySize);
        }
    }

    ;

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w, h);
        }
    }
}
