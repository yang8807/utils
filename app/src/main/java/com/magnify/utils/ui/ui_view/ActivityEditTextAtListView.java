package com.magnify.utils.ui.ui_view;

import android.os.Bundle;
import android.widget.ListView;

import com.example.datautils.RandomUtil;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.ui_adapter.EditTextAdapter;

/**
 * Created by heinigger on 16/8/27.
 */
public class ActivityEditTextAtListView extends CurrentBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new EditTextAdapter(self, RandomUtil.getRandomImage(30)));
    }
}
