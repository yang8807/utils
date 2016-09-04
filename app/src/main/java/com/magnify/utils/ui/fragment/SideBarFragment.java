package com.magnify.utils.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.view.SideBar;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class SideBarFragment extends BaseFragment implements SideBar.onTouchCharactersListerner {

    private TextView mTouchText;
    private ObjectAnimator mObjectAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sidebar, container, false);
        mTouchText = (TextView) view.findViewById(R.id.touchText);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar1);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar2);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar3);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar4);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar5);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar6);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar7);
        findViewAndSetOnTouchCharacterLister(view, R.id.sideBar8);
        return view;
    }

    private void findViewAndSetOnTouchCharacterLister(View view, int sideBar) {
        SideBar mSideBar = (SideBar) view.findViewById(sideBar);
        mSideBar.setOnTouchCharactersListerner(this);
    }

    @Override
    public boolean onTouchCharacter(int position, String touchText) {
        mTouchText.setVisibility(View.VISIBLE);
        //移除上次的任务,开始新的任务
        getObjeAnimator().cancel();
        getObjeAnimator().reverse();
        getObjeAnimator().start();
        mTouchText.setText(touchText);
        return true;
    }

    public ObjectAnimator getObjeAnimator() {
        mTouchText.setVisibility(View.VISIBLE);
        if (mObjectAnimator == null) {
            mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(mTouchText, PropertyValuesHolder.ofFloat("alpha", 1, 0),
                    PropertyValuesHolder.ofFloat("scaleX", 1, 0), PropertyValuesHolder.ofFloat("scaleY", 1, 0));
            mObjectAnimator.setDuration(700);
        }
        return mObjectAnimator;
    }

    @Override
    public void onDestroy() {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
            mObjectAnimator = null;
        }
        super.onDestroy();
    }
}
