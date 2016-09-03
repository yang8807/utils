package com.magnify.utils.ui.ui_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.bean.FileType;
import com.magnify.utils.ui.ui_view.FileActivity;
import com.magnify.yutils.DeviceUtil;
import com.yan.picture_select.BrowseImageActivity;

import java.io.File;
import java.util.List;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class FileAdapter extends CommonAdapter<File> implements View.OnClickListener {
    public FileAdapter(Context context, List<File> datas) {
        super(context, R.layout.item_image_text, datas);
    }

    @Override
    protected void onPreCreate(ViewHolder holder, int position) {
        ImageView mImageView = holder.<ImageView>getView(R.id.image_user);
        mImageView.setImageResource(R.mipmap.ic_launcher);
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp.height = lp.width = DeviceUtil.getScreenWidth(getContext()) / 3;
        mImageView.requestLayout();
    }

    @Override
    public void convert(ViewHolder holder, int position, File file) {
        holder.setText(R.id.tv_user, file.getName()).setOnClickListener(R.id.image_user, this);
        String imagePath = file.getAbsolutePath();
        FileType fileType = null;
        if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png") || imagePath.endsWith(".gif")) {
            holder.displayImage("file://" + imagePath, R.id.image_user);
            fileType = new FileType(position, FileType.IMAGE);
        } else {
            fileType = new FileType(position, FileType.NORMALFILE);
        }
        holder.setTag(R.id.image_user, R.id.tv_user, fileType);
    }

    @Override
    public void onClick(View view) {
        FileType fileType = (FileType) view.getTag(R.id.tv_user);
        File file1 = mDatas.get(fileType.getPosition());
        if (file1.isDirectory())
            getContext().startActivity(FileActivity.getIntent(file1.getAbsolutePath(), getContext()));
        else if (isImage(file1.getName()))
            getContext().startActivity(BrowseImageActivity.getIntent(getContext(), file1.getParentFile().getAbsolutePath(), file1.getAbsolutePath()));
    }

    private boolean isImage(String name) {
        return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif");
    }
}
