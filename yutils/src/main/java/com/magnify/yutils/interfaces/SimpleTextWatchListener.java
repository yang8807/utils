package com.magnify.yutils.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by heinigger on 16/8/22.
 */
public abstract class SimpleTextWatchListener implements TextWatcher {

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
