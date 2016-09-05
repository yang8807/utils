package com.yan.picture_select;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseShowChildAdapter;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.DrawableUtils;
import com.magnify.yutils.ToastUtil;
import com.magnify.yutils.bean.ImageFloder;
import com.yan.fastview_library.R;
import com.yan.picture_select.listeners.OnSelectPictureListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public class ImageAdapter extends BaseShowChildAdapter<ImageFloder, String> implements View.OnClickListener {

    private ArrayList<String> mSelectList = new ArrayList<>();
    private Drawable mNoralDrawable;
    private Drawable mSelectDrawable;
    private OnSelectPictureListener onSelectPictureListner;

    public ImageAdapter(List<ImageFloder> folders, Context mContext) {
        super(folders, mContext, R.layout.item_images_view);
        mNoralDrawable = DrawableUtils.getOvalDrawable(Color.parseColor("#79000000"));
        mSelectDrawable = DrawableUtils.getOvalDrawable(ImagePickerConfiguration.getInstance().getStyle_color());
    }

    @Override
    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
        ImageView imageView = viewHolder.getView(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int spacing = DeviceUtil.dipToPx(getContext(), ImagePickerConfiguration.getInstance().getSpaciing());
        layoutParams.width = layoutParams.height = (DeviceUtil.getScreenWidth(getContext()) - spacing * (ImagePickerConfiguration.getInstance().getNumcloumns() - 1)) / ImagePickerConfiguration.getInstance().getNumcloumns();
        imageView.requestLayout();
        //需要初始化的一些属性值
        ImageView mImageSelect = viewHolder.getView(R.id.imgSelect);
        mImageSelect.setVisibility(View.VISIBLE);
        mImageSelect.setBackgroundDrawable(mNoralDrawable);
    }

    @Override
    protected List<String> getChild(ImageFloder imageFloder) {
        return imageFloder.getAllImages();
    }

    @Override
    protected void convert(ViewHolder viewHolder, View convertView, int position, final ImageFloder parent, final String child) {

        String imagePath = "file://" + parent.getDir() + "/" + child;
        viewHolder.displayImage(imagePath, R.id.image)
                .setOnLongClickListener(R.id.image, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        File file = new File(parent.getDir() + "/" + child);
                        if (file.exists()) file.delete();
                        parent.getAllImages().remove(child);
                        notifyDataSetChanged();
                        ToastUtil.show(getContext(), "删除图片成功");
                        return false;
                    }
                })
                .setOnClickListener(R.id.image, position, ImageAdapter.this);
        //选中按钮的设置
        ImageView mImageSelect = viewHolder.getView(R.id.imgSelect);
        String filePath = parent.getDir() + "/" + child;
        mImageSelect.setOnClickListener(this);
        mImageSelect.setTag(filePath);
        mImageSelect.setBackgroundDrawable(mSelectList.contains(filePath) ? mSelectDrawable : mNoralDrawable);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int position = 0;
        if (id == R.id.imgSelect) {
            String imagePath = (String) view.getTag();
            if (mSelectList.contains(imagePath)) {
                mSelectList.remove(imagePath);
            } else {
                if (ImagePickerConfiguration.getInstance().getType() == ImagePickerConfiguration.ImageType.single)
                    mSelectList.clear();//当是单选的时候,将数据清空,再添加进集合中
                //判断最多可以选中多少个了
                if (mSelectList.size() < ImagePickerConfiguration.getInstance().getSelectCount())
                    mSelectList.add(imagePath);
                else
                    ToastUtil.show(getContext(), String.format(getContext().getString(R.string.picture_component_max_select), ImagePickerConfiguration.getInstance().getSelectCount()));
            }
            if (onSelectPictureListner != null) onSelectPictureListner.onSelectPicture(mSelectList);
            notifyDataSetChanged();
        } else if (id == R.id.image) {
            position = (int) view.getTag(R.id.image);
            getContext().startActivity(BrowseImageActivity.getIntent(getContext(), position, getParentData()));
        }
    }

    public ArrayList<String> getSelectList() {
        return mSelectList;
    }

    public void setOnSelectPictureListner(OnSelectPictureListener onSelectPictureListner) {
        this.onSelectPictureListner = onSelectPictureListner;
    }
}

