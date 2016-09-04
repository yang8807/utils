package com.magnify.utils.ui.ui_adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.utils.ui.ui_view.FileActivity;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.DrawableUtils;
import com.yan.fastview_library.poupwindows.BaseLinearPoupWindows;
import com.yan.picture_select.BrowseImageActivity;

import java.io.File;
import java.util.List;

import utils.UiUtils;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class FileAdapter extends CommonAdapter<File> implements View.OnClickListener, View.OnLongClickListener {

    private TextView mTextView;
    private BaseLinearPoupWindows mPoup;

    public FileAdapter(Context context, List<File> datas) {
        super(context, R.layout.item_image_text, datas);
        mPoup = new BaseLinearPoupWindows(getContext(), R.layout.item_header_layout);
        mTextView = mPoup.findView(R.id.tv_header);
        mTextView.setPadding(5, 0, 5, 0);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(14);
        mTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setBackgroundDrawable(DrawableUtils.getCorRectDrawable(UiUtils.getColors(R.color.colorPrimary), 30));
        mTextView.requestLayout();
    }

    @Override
    protected void onPreCreate(ViewHolder holder, int position) {
        ImageView mImageView = holder.getView(R.id.image_user);
        mImageView.setImageResource(R.mipmap.file_folders);
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp.height = lp.width = DeviceUtil.getScreenWidth(getContext()) / 4;
        mImageView.requestLayout();

        TextView mTextView = holder.getView(R.id.tv_user);
        mTextView.setMaxLines(2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
        mTextView.setTextColor(Color.BLACK);
        layoutParams.topMargin = 0;
        layoutParams.height = DeviceUtil.dipToPx(getContext(), 40);
        mTextView.requestLayout();
    }

    @Override
    public void convert(ViewHolder holder, int position, File file) {
        holder.setText(R.id.tv_user, file.getName())
                .setOnClickListener(R.id.image_user, this)
                .setOnLongClickListener(R.id.image_user, this);
        String path = file.getAbsolutePath();
        if (file.isDirectory())
            holder.displayImage(R.mipmap.file_folders, R.id.image_user);
        else if (isImage(path))
            holder.displayImage("file://" + path, R.id.image_user);
        else if (isVideo(path))
            holder.displayImage(R.mipmap.file_video, R.id.image_user);
        else if (isText(path))
            holder.displayImage(R.mipmap.file_text, R.id.image_user);
        else if (isMusic(path))
            holder.displayImage(R.mipmap.file_music, R.id.image_user);
        else
            holder.displayImage(R.mipmap.file_what, R.id.image_user);

        holder.setTag(R.id.image_user, R.id.tv_user, position);
    }


    @Override
    public void onClick(View view) {
        int position = (int) view.getTag(R.id.tv_user);
        File file1 = mDatas.get(position);
        if (file1.isDirectory())
            getContext().startActivity(FileActivity.getIntent(file1.getAbsolutePath(), getContext()));
        else if (isImage(file1.getName()))
            getContext().startActivity(BrowseImageActivity.getIntent(getContext(), file1.getParentFile().getAbsolutePath(), file1.getAbsolutePath()));
    }


    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag(R.id.tv_user);
        File file = mDatas.get(position);
        mTextView.setText(file.getName());
        mPoup.onMeasure();
        mPoup.showAtCenter(view, Gravity.TOP);
        return true;
    }

    public boolean isVideo(String path) {
        return path.endsWith(".mp4") || path.endsWith(".3gp") || path.endsWith(".rmvb") || path.endsWith(".avi") || path.endsWith(".rm");
    }

    public boolean isMusic(String path) {
        return path.endsWith(".mp3") || path.endsWith(".wma") || path.endsWith(".aac") || path.endsWith(".arm");
    }


    private boolean isText(String path) {
        return path.endsWith(".txt") || path.endsWith(".log") || path.endsWith(".java");
    }

    private boolean isImage(String name) {
        return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif");
    }
}
