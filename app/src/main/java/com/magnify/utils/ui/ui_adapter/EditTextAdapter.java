package com.magnify.utils.ui.ui_adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.yutils.LogUtils;
import com.magnify.yutils.interfaces.SimpleTextWatchListener;

import java.util.List;

/**
 * Created by heinigger on 16/8/27.
 */
public class EditTextAdapter extends CommonAdapter<String> {

    private SparseArray<String> backString = new SparseArray<>();

    public EditTextAdapter(Context context, List datas) {
        super(context, R.layout.item_edit_text, datas);
    }


    @Override
    public void convert(ViewHolder holder, final int position, String o) {
        LogUtils.v("mine", position + ":" + o);
        mDatas.set(position, TextUtils.isEmpty(backString.get(position)) ? o : backString.get(position));
        holder.setText(R.id.edit_text, TextUtils.isEmpty(backString.get(position)) ? o : backString.get(position)).<EditText>getView(R.id.edit_text);
        holder.<EditText>getView(R.id.edit_text).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                LogUtils.v("mine", "到底什么时候能出来" + textView.getText().toString() + ":点击回车键的时候才会触发");
                return false;
            }
        });


        final EditText mEditext = holder.<EditText>getView(R.id.edit_text);
        mEditext.setTag(position);
        mEditext.setHint(o);
        //还存在一个问题,只有当按删除键的时候,才会触发
        mEditext.addTextChangedListener(new SimpleTextWatchListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    if (TextUtils.isEmpty(backString.get(position))) {
                        mDatas.set(position, editable.toString());
                    }
                } else {
                    mDatas.set(position, editable.toString());
                }
            }
        });
        mEditext.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEv) {
                LogUtils.v("mine", "获取characters", keyEv.getCharacters());
                switch (keyEv.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        LogUtils.v("mine", "按下键盘");
                        break;
                    case KeyEvent.ACTION_UP:
                        LogUtils.v("mine", "松开键盘");
                        break;
                }
                int position = (int) view.getTag();
                String text = ((EditText) view).getText().toString();
                mDatas.set(position, text);
                backString.put(position, text);
                LogUtils.v("mine", "onKeyEvent,我什么时候触发", ((EditText) view).getText().toString());
                return false;
            }
        });

    }
}
