package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;

/**
 * Created by heinigger on 16/9/2.
 */
public class ThrowAnimationView extends BaseFragment {

    private Button btn_start;
    private Button btn_end;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_throw_animtion_view, container, false);
        btn_end = (Button) view.findViewById(R.id.btn_end);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        return view;
    }
}
