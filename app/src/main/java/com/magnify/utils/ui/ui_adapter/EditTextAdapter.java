package com.magnify.utils.ui.ui_adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.yutils.LogUtils;

import java.util.List;

/**
 * Created by heinigger on 16/8/27.
 */
public class EditTextAdapter extends CommonAdapter<String> {
    public EditTextAdapter(Context context, List datas) {
        super(context, R.layout.item_edit_text, datas);
    }


    @Override
    public void convert(ViewHolder holder, final int position, String o) {
        LogUtils.v("mine", position + ":" + o);
        holder.setText(R.id.edit_text, o).<EditText>getView(R.id.edit_text);
        holder.<EditText>getView(R.id.edit_text).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                LogUtils.v("mine", "到底什么时候能出来" + textView.getText().toString() + ":点击回车键的时候才会触发");
                return false;
            }
        });

        final EditText mEditext = holder.<EditText>getView(R.id.edit_text);
        mEditext.setTag(position);
        mEditext.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEv) {
                int position = (int) view.getTag();
                mDatas.set(position,((EditText)view).getText().toString());
                LogUtils.v("mine", "onKeyEvent,我什么时候触发", ((EditText) view).getText().toString());
                return false;
            }
        });

    }
}
