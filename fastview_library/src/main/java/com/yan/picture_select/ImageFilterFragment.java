package com.yan.picture_select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.bean.ImageFloder;
import com.magnify.yutils.data.ImageScanner;
import com.yan.fastview_library.R;
import com.yan.picture_select.listeners.OnSelectPictureListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/20.
 */
public class ImageFilterFragment extends Fragment implements View.OnClickListener {

    private GridView gridView;
    private TextView tvName;
    private List<ImageFloder> mFolders;
    private List<ImageFloder> mAllFolders;
    private View rly_parent;
    private ImageAdapter mImageAdapter;
    private OnSelectPictureListener onSelectPictureListener;
    private SelectFoldersDialogFragment selectFoldersDialogFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new ImageScanner(getActivity(), new ImageScanner.OnScanImageListener() {
            @Override
            public void onScanFinish(List<ImageFloder> mImageFloder, int totalCount) {
                onScanImageFinish(mImageFloder, totalCount);
            }
        });

        View parentView = inflater.inflate(R.layout.grid_show, null);
        gridView = (GridView) parentView.findViewById(R.id.grid_view);
        tvName = (TextView) parentView.findViewById(R.id.tv_name);
        rly_parent = parentView.findViewById(R.id.rly_parent);
        rly_parent.setOnClickListener(this);
        changeViewPropeties();
        return parentView;
    }

    //改变gridView的布局布局属性
    private void changeViewPropeties() {
        //初始化这个的默认颜色
        gridView.setNumColumns(ImagePickerConfiguration.getInstance().getNumcloumns());
        int SPACING = DeviceUtil.dipToPx(getContext(), ImagePickerConfiguration.getInstance().getSpaciing());
        gridView.setHorizontalSpacing(SPACING);
        gridView.setVerticalSpacing(SPACING);
        rly_parent.setBackgroundColor(ImagePickerConfiguration.getInstance().getStyle_color());
    }

    public void onScanImageFinish(final List<ImageFloder> mImageFolders, final int totalCount) {
        mFolders = mImageFolders;
        mAllFolders = mImageFolders;
        tvName.setText(String.format("所有图片 %d 张", totalCount));
        mImageAdapter = new ImageAdapter(mFolders, getContext());
        if (onSelectPictureListener != null)
            mImageAdapter.setOnSelectPictureListner(onSelectPictureListener);
        gridView.setAdapter(mImageAdapter);

        selectFoldersDialogFragment = new SelectFoldersDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectFoldersDialogFragment.FOLDER_KEY, (Serializable) mAllFolders);
        selectFoldersDialogFragment.setArguments(bundle);
        selectFoldersDialogFragment.setOnFloderSelectListener(new SelectFoldersDialogFragment.OnFolderSelectListener() {
            @Override
            public void onSelectFolders(int position) {
                if (position < 0) {
                    mImageAdapter.setDatas(mAllFolders);
                    tvName.setText(String.format("所有图片 %d 张", totalCount));
                } else {
                    ImageFloder imageFloder = mAllFolders.get(position);
                    mImageAdapter.setDatas(imageFloder);
                    tvName.setText(String.format("%s  %d张", imageFloder.getDir(), imageFloder.getCount()));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rly_parent) {
            selectFoldersDialogFragment.show(getChildFragmentManager(), selectFoldersDialogFragment.getClass().getName());
        }
    }

    public ArrayList<String> getSelectList() {
        if (mImageAdapter != null) {
            return mImageAdapter.getSelectList();
        }
        return null;
    }

    public void setOnSelectPictureListener(OnSelectPictureListener onSelectPictureListener) {
        this.onSelectPictureListener = onSelectPictureListener;
        if (mImageAdapter != null)
            mImageAdapter.setOnSelectPictureListner(onSelectPictureListener);
    }
}
