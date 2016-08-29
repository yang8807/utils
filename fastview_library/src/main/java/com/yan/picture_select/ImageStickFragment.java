package com.yan.picture_select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.bean.ImageFloder;
import com.magnify.yutils.data.ImageScanner;
import com.yan.fastview_library.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.viewgroup.sticky_gridview.BaseStickAdapter;
import com.yan.fastview_library.viewgroup.sticky_gridview.StickyGridHeadersGridView;

import java.util.List;

/**
 * Created by heinigger on 16/8/29.
 */
public class ImageStickFragment extends BaseFragment {
    private StickyGridHeadersGridView mStickyGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.stick_grid_view, container, false);
        mStickyGridView = (StickyGridHeadersGridView) view.findViewById(R.id.mStickyGridView);
        new ImageScanner(getActivity(), new ImageScanner.OnScanImageListener() {
            @Override
            public void onScanFinish(List<ImageFloder> mImageFloder, int totalCount) {
                mStickyGridView.setAdapter(new BaseStickAdapter<ImageFloder, String>(mImageFloder, getActivity(), R.layout.stick_grid_header, R.layout.item_images_view) {
                    @Override
                    protected void convertHeaderView(ViewHolder mHeaderViewHolder, ImageFloder imageFloder, int position) {
                        mHeaderViewHolder.setText(R.id.tv_title, imageFloder.getDir() + " 总共:" + imageFloder.getCount());
                    }

                    @Override
                    protected void convertChildView(ViewHolder mChildHolder, ImageFloder parent, String mChild, int position) {
                        mChildHolder.displayImage("file://" + parent.getDir() + "/" + mChild, R.id.image);
                    }

                    @Override
                    protected void onPreChildCreate(ViewHolder mChildHolder, View view) {
                        ImageView mImageView = mChildHolder.getView(R.id.image);
                        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
                        layoutParams.width = layoutParams.height = (DeviceUtil.getScreenWidth(getContext()) - DeviceUtil.dipToPx(getContext(), 6)) / 4;
                        mImageView.requestLayout();
                    }

                    @Override
                    protected List<String> getChild(ImageFloder imageFloder) {
                        return imageFloder.getAllImages();
                    }
                });
            }
        });
        return view;
    }
}
