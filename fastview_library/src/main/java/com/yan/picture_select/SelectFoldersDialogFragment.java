package com.yan.picture_select;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter;
import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.DrawableUtils;
import com.magnify.yutils.bean.ImageFloder;
import com.yan.fastview_library.R;
import com.yan.fastview_library.fragment.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinigger on 16/8/21.
 */
public class SelectFoldersDialogFragment extends BaseDialogFragment {
    private RecyclerView mRecyler;
    private List<ImageFloder> imageFolders = new ArrayList<>();
    private int lastSelect = 0;
    private CommonAdapter<ImageFloder> mFoldersAdapter;
    private OnFolderSelectListener onFloderSelectListener;
    public static String FOLDER_KEY = "folder_values";

    public void setOnFloderSelectListener(OnFolderSelectListener onFloderSelectListener) {
        this.onFloderSelectListener = onFloderSelectListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        imageFolders.clear();
        List<ImageFloder> imageFolders = (List<ImageFloder>) getArguments().getSerializable(FOLDER_KEY);
        this.imageFolders.add(new ImageFloder(imageFolders.get(0).getFristImage(), "所有图片"));
        this.imageFolders.addAll(imageFolders);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.library_view_recyler, null);

        mRecyler = (RecyclerView) parentView.findViewById(R.id.recylers);
        mRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        ViewGroup.LayoutParams layoutParams = mRecyler.getLayoutParams();
        layoutParams.height = (int) (DeviceUtil.getScreenHeight(getContext()) * ImagePickerConfiguration.getInstance().getDialogRatio());
        mRecyler.requestLayout();

        setImageFolders();
        return parentView;
    }

    private void setImageFolders() {
        if (imageFolders != null && !imageFolders.isEmpty()) {
            //设置适配器
            if (mFoldersAdapter == null) {
                mFoldersAdapter = new CommonAdapter<ImageFloder>(getContext(), R.layout.item_folders, imageFolders) {
                    @Override
                    protected void onPreCreate(ViewHolder viewHolder, View convertView) {
                        ImageView imageView = viewHolder.getView(R.id.image_select_tag);
                        imageView.setBackgroundDrawable(DrawableUtils.getOvalDrawable(ImagePickerConfiguration.getInstance().getStyle_color()));
                    }

                    @Override
                    public void convert(final ViewHolder holder, int position, ImageFloder imageFloder) {
                        holder.displayImage(imageFloder.getFristImage(), R.id.img_frsit_image)
                                .setText(R.id.tv_folder_names, imageFloder.getDir())
                                .setVisible(lastSelect == position, R.id.image_select_tag)
                                .setVisible(position != imageFolders.size() - 1, R.id.view_line)
                                .getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lastSelect = holder.getmPosition();
                                SelectFoldersDialogFragment.this.dismiss();
                                if (onFloderSelectListener != null)
                                    onFloderSelectListener.onSelectFolders(lastSelect - 1);
                                notifyDataSetChanged();
                            }
                        });
                    }
                };
            }

            mRecyler.setAdapter(mFoldersAdapter);
        }
    }

    public static interface OnFolderSelectListener {
        public void onSelectFolders(int position);
    }


}
