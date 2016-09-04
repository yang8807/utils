package com.magnify.utils.ui.component;

import android.content.Intent;
import android.os.Bundle;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.yan.picture_select.ImageFilterFragment;
import com.yan.picture_select.ImagePickerConfiguration;
import com.yan.picture_select.listeners.OnSelectPictureListener;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/20.
 */
public class ActivityPictureActivity extends CurrentBaseActivity {

    private ImageFilterFragment mImageFileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framlayout);
        setTopTitle("图片选择");
        setContentId(R.id.fragment_container);
        ImagePickerConfiguration.getInstance().setStyleColor(getColors(R.color.colorPrimary));
        mImageFileFragment = new ImageFilterFragment();
        mImageFileFragment.setOnSelectPictureListener(new OnSelectPictureListener() {
            @Override
            public void onSelectPicture(ArrayList<String> mSelectPicttures) {
                if (mSelectPicttures != null && mSelectPicttures.size() > 1)
                    getSupportActionBar().setTitle(String.format("图片选择(%d)", mSelectPicttures.size()));
            }
        });
        switchFragment(mImageFileFragment);
    }

    @Override
    public void finish() {
        if (mImageFileFragment != null) {
            ArrayList<String> datas = mImageFileFragment.getSelectList();
            if (datas != null || !datas.isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra("data", datas);
                setResult(RESULT_OK, intent);
            }
        }
        super.finish();
    }
}
