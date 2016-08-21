package com.yan.picture_select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseShowChildAdapter;
import com.magnify.yutils.DeviceUtil;
import com.yan.bean.ImageFloder;
import com.yan.fastview_library.R;

import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public class ImageAdapter extends BaseShowChildAdapter<ImageFloder, String> {
    public ImageAdapter(List<ImageFloder> folders, Context mContext) {
        super(folders, mContext, R.layout.item_images_view);
    }

    @Override
    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
        ImageView imageView = viewHolder.getView(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int spacing = DeviceUtil.dipToPx(getmContext(), ImagePickerConfiguration.SPACING);
        layoutParams.width = layoutParams.height = (DeviceUtil.getScreenWidth(getmContext()) - spacing * (ImagePickerConfiguration.NUMCOLUMNS - 1)) / ImagePickerConfiguration.NUMCOLUMNS;
        imageView.requestLayout();
    }

    @Override
    protected List<String> getChild(ImageFloder imageFloder) {
        return imageFloder.getAllImages();
    }

    @Override
    protected void convert(ViewHolder viewHolder, View convertView, int position, ImageFloder parent, String child) {
        viewHolder.displayImage("file://" + parent.getDir() + "/" + child, R.id.image);
    }

}

