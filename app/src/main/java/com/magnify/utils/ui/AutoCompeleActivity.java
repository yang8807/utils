package com.magnify.utils.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.example.datautils.RandomUserUtil;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.BaseAutoCompleteAdapter;
import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.StringUtil;

import java.util.ArrayList;

/**
 * Created by heinigger on 16/8/7.
 */
public class AutoCompeleActivity extends CurrentBaseActivity {
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_auto_compelete);
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            users.add(RandomUserUtil.createRandomUser());
        }

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_textview);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(new BaseAutoCompleteAdapter<User>(R.layout.item_child_layout, users, 50) {
            @Override
            protected String getFiltString(User user) {
                return user.getUserName();
            }

            @Override
            protected boolean judgeCondition(String prefixString, String valueText) {
                return valueText.contains(prefixString);
            }

            @Override
            protected void setData2View(ViewHolder holder, User child, int position, final String prefixString) {
                holder.setText(R.id.tv_userName, StringUtil.formatColor(child.getUserName().replace(prefixString, "[" + prefixString + "]"), Color.RED))
                        .setText(R.id.tv_age, child.getSex() + "  " + child.getAddress())
                        .setText(R.id.tv_phone, child.getPhone())
                        .displayRoundImage(child.getImageAvator(), R.id.img_avators);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast(prefixString);
                    }
                });
            }
        });
    }
}
