package com.magnify.utils.ui.fragment.utilfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;

/**
 * Created by heinigger on 16/10/15.
 */

public class UtilsStringFormatUtils extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.activity_listview, null);
        TextView mTextView = (TextView) parentView.findViewById(R.id.text);



        return parentView;
    }
}
