package com.magnify.yutils.interfaces;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by heinigger on 16/8/22.
 */
public class SimpleTextWatchListener implements TextWatcher {
    private EditText editText;
    public SimpleTextWatchListener(EditText editText) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
