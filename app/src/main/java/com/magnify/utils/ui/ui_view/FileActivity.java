package com.magnify.utils.ui.ui_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.GridView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.ui_adapter.FileAdapter;
import com.magnify.yutils.app.ThreadManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ${洒笑天涯} on 2016/9/4.
 */
public class FileActivity extends CurrentBaseActivity {
    private GridView mGridView;
    private String path;
    private ArrayList<File> datas = new ArrayList<>();
    public static final String FILE_PATH = "file_path";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof String) {
                String currentPath = String.valueOf(msg.obj);
                if (currentPath.equals(path)) {
                    onScanFinish();
                }
            }
        }
    };

    private void onScanFinish() {
        mGridView.setAdapter(new FileAdapter(self, datas));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_show);
        setVisibility(false, R.id.rly_parent);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setNumColumns(4);
        setTopTiltls();
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                path = getIntent().getStringExtra(FILE_PATH);
                if (TextUtils.isEmpty(path)) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                traverseFile(new File(path));
                Message message = new Message();
                message.obj = path;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 设置每个开启页面的标题
     */
    private void setTopTiltls() {
        String tilts = getIntent().getStringExtra(FILE_PATH);
        if (!TextUtils.isEmpty(tilts)) {
            String[] names = tilts.split("/");
            if (names != null && names.length > 0)
                getSupportActionBar().setTitle(names[names.length - 1]);
        }
    }

    public static Intent getIntent(String path, Context mContext) {
        Intent intent = new Intent(mContext, FileActivity.class);
        intent.putExtra(FILE_PATH, path);
        return intent;
    }

    private void traverseFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    datas.add(files[i]);
                }
            }
        }
    }

}
