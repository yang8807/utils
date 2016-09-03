package com.magnify.utils.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.magnify.yutils.app.ThreadManager;
import com.yan.fastview_library.fragment.BaseFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class FilePathFragment extends BaseFragment {
    private ListView mListView;
    private ArrayList<String> datas = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mListView.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item_header_layout, datas) {
                @Override
                protected void onPreCreate(ViewHolder holder, int position) {
                    holder.<TextView>getView(R.id.tv_header).setTextSize(13);
                }

                @Override
                public void convert(ViewHolder holder, int position, String s) {
                    holder.setText(R.id.tv_header, s);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.activity_listview, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
//                File file = Environment.getRootDirectory();
                File file = Environment.getRootDirectory();
//                File file=Environment.getDataDirectory();//这个路劲需要权限才能获取
                traverseFile(file);
                mHandler.sendEmptyMessage(0);
            }
        });
        return view;
    }

    private void traverseFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                datas.add(file.getAbsolutePath());
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File childFile = files[i];
                    traverseFile(childFile);
                }
            }
        }
    }
}
