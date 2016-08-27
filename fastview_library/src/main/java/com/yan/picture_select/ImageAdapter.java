package com.yan.picture_select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseShowChildAdapter;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.ToastUtil;
import com.magnify.yutils.bean.ImageFloder;
import com.yan.fastview_library.R;

import java.io.File;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public class ImageAdapter extends BaseShowChildAdapter<ImageFloder, String> implements View.OnClickListener {

    public ImageAdapter(List<ImageFloder> folders, Context mContext) {
        super(folders, mContext, R.layout.item_images_view);
    }

    @Override
    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
        ImageView imageView = viewHolder.getView(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int spacing = DeviceUtil.dipToPx(getmContext(), ImagePickerConfiguration.getInstance().getSpaciing());
        layoutParams.width = layoutParams.height = (DeviceUtil.getScreenWidth(getmContext()) - spacing * (ImagePickerConfiguration.getInstance().getNumcloumns() - 1)) / ImagePickerConfiguration.getInstance().getNumcloumns();
        imageView.requestLayout();
    }

    @Override
    protected List<String> getChild(ImageFloder imageFloder) {
        return imageFloder.getAllImages();
    }

    @Override
    protected void convert(ViewHolder viewHolder, View convertView, int position, final ImageFloder parent, final String child) {
        viewHolder.displayImage("file://" + parent.getDir() + "/" + child, R.id.image)
                .setOnLongClickListener(R.id.image, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        File file = new File(parent.getDir() + "/" + child);
                        if (file.exists()) file.delete();
                        parent.getAllImages().remove(child);
                        notifyDataSetChanged();
                        ToastUtil.show(getmContext(),"删除图片成功");
                        return false;
                    }
                })
                .setOnClickListener(R.id.image, position, ImageAdapter.this);
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag(R.id.image);
        getmContext().startActivity(BrowseImageActivity.getIntent(getmContext(), position, getParentData()));
    }

}

