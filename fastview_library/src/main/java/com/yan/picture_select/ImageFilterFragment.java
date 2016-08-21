package com.yan.picture_select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseShowChildAdapter;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtil;
import com.yan.bean.ImageFloder;
import com.yan.fastview_library.R;

import java.util.List;

/**
 * Created by heinigger on 16/8/20.
 */
public class ImageFilterFragment extends BaseImageFilterFragment {

    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gridView = getGridView();
        return gridView;
    }

    private GridView getGridView() {
        if (gridView == null) {
            gridView = new GridView(getActivity());
            gridView.setNumColumns(4);
            gridView.setHorizontalSpacing(DeviceUtil.dipToPx(getContext(), 4));
            gridView.setVerticalSpacing(DeviceUtil.dipToPx(getContext(), 4));
        }
        return gridView;
    }

    @Override
    public void onScanImageFinish(List<ImageFloder> mImageFolders) {
        gridView = getGridView();
        gridView.setAdapter(new BaseShowChildAdapter<ImageFloder, String>(mImageFloders, getActivity(), R.layout.item_images_view) {
            @Override
            protected List<String> getChild(ImageFloder imageFloder) {
                return imageFloder.getAllImages();
            }

            @Override
            protected void convert(ViewHolder viewHolder, View convertView, int position, ImageFloder parent, String child) {
                String url = "file://" + parent.getDir() + "/" + child;
                LogUtil.v("mine", url);
                viewHolder.displayImage(url, R.id.image);
            }
        });
    }
}
