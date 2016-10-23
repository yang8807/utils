package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;

/**
 * Created by heinigger on 16/10/22.
 */

public class DrawFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.ondraw_witch_favoriate_view, null);
        return mParentView;
    }
}
