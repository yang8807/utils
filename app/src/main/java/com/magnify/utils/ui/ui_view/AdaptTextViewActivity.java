package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.ToastUtil;
import com.yan.fastview_library.view.text.AdaptTextView;

/**
 * Created by heinigger on 16/8/24.
 */
public class AdaptTextViewActivity extends CurrentBaseActivity {
    private AdaptTextView adaptTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptext_view);
        adaptTextView = (AdaptTextView) findViewById(R.id.adapt_textview);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adaptTextView.setText(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ToastUtil.show(self, "我弹出来了");
        return super.onTouchEvent(event);
    }
}
