package com.magnify.utils.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;

/**
 * Created by heinigger on 16/8/5.
 */
public class WebViewActivity extends CurrentBaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    public static Intent getIntent(Context context, String url, String title, String subTitle) {
        Intent ittent = new Intent(context, WebViewActivity.class);
        ittent.putExtra(CurrentBaseActivity.TITLE, title);
        ittent.putExtra(CurrentBaseActivity.SUBTITLE, subTitle);
        ittent.putExtra("url", url);
        return ittent;
    }
}
