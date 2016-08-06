package com.yan.fastview_library.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import utils.TextViewUtil;
public class ViewHolder {
    private SparseArray<View> mListView = new SparseArray<>();
    private View parentView;

    public ViewHolder(Context context, int layoutid) {
        parentView = LayoutInflater.from(context).inflate(layoutid, null);
        parentView.setTag(this);
    }

    public <E extends View> E findView(int viewid) {
        View childView = mListView.get(viewid);
        if (childView == null) {
            childView = parentView.findViewById(viewid);
            mListView.put(viewid, childView);
        }
        return (E) childView;
    }

    public View getParentView() {
        return parentView;
    }

    public void setText(int viewid, String s) {
        ((TextView) findView(viewid)).setText(s);
    }

    public void setBuilder(int viewid, SpannableStringBuilder spannableStringBuilder) {
        ((TextView) findView(viewid)).setText(spannableStringBuilder);
    }

    public void setBuilder(int viewid, String allText, String prefixStrings, int color) {
        ((TextView) findView(viewid)).setText(TextViewUtil.GetBuilder(allText, prefixStrings, color));
    }
}
