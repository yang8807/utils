package com.magnify.utils.ui.ui_utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.yutils.EncryptUtil;
import com.magnify.yutils.StringUtil;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityEncryptUtil extends CurrentBaseActivity {
    /**
     * 密钥需要24位
     */
    private String key = "jsJYCzaJZQJ3ZxZ@!4xKsR4b";
    private String text = "Android_SDK_V2.3.1 的tools目录下有一个  获取签名.apk ,这个也可以获取,但是我测试发现,只能显示一部分的本机应用,有些应用查不到,就麻烦了..";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_view);
        key = StringUtil.getRandomString(24);
        TextView textView = (TextView) findViewById(R.id.text);
        try {

            String iv = new String(EncryptUtil.createIV(StringUtil.getRandomString(24)));
            String encode = EncryptUtil.des3EncodeCBC(key, iv, text);
            String decode = EncryptUtil.des3DecodeCBC(key, iv, encode);

            textView.setText(text + "\n" + iv + "\n" + encode + "\n" + decode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
