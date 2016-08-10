/**
 *
 */
package com.magnify.utils.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.magnify.yutils.LoadingWebViewClient;


/**
 * @author 黄艳武; funtion:了解衣联网的页面
 */
public class BaseWebViewActivity extends CurrentBaseActivity {

    /**
     * 显示论坛的页面webView
     */
    private WebView web_bbs;

    /**
     * webView加载客户端助手
     */
    private LoadingWebViewClient web_bbsClient;

    private String URL = "http://m.eelly.com/activity/advpage.html?isInApp=1";
    //传递过来的链接地址
    private static final String SENDURL = "send_url";
    private static final String SENDTITLE = "send_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTopBarTitles();

        if (!TextUtils.isEmpty(getIntent().getStringExtra(SENDURL)))
            URL = getIntent().getStringExtra(SENDURL);

        web_bbs = new WebView(this);
        setContentView(web_bbs, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  /*      mLoadingHelp = new LoadingHelp.Builder(this).create();
        setContentView(mLoadingHelp.createView(R.layout.fragment_bbs));*/

        initWebview();
//        setOnRetryListener();
        web_bbs.loadUrl(URL);
    }


    /***
     * 设置TopBar的标题, 如果获取的标题是, 就不进行设置
     */
    private void setTopBarTitles() {
        String title = getIntent().getStringExtra(SENDTITLE);
        if (TextUtils.isEmpty(title)) {
            getSupportActionBar().hide();
        } else getSupportActionBar().setTitle(getIntent().getStringExtra(SENDTITLE));
    }

    /**
     * 进行webView的初始化
     */
    private void initWebview() {

        WebSettings settings = web_bbs.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);

        web_bbs.setVerticalScrollBarEnabled(false);
        web_bbsClient = new LoadingWebViewClient(web_bbs, new LoadingWebViewClient.LoadingListener() {

            // webView开始加载的页面
            @Override
            public void onPageStarted(WebView view, String url) {
                // mLoadingHelp.showContentView();

            }

            // 加载错误的页面
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl, String detailMessage) {
//                mLoadingHelp.showErrorView(detailMessage);
            }

            // 页面结束的时候
            @Override
            public void onPageFinished(WebView view, String url) {
//                mLoadingHelp.showContentView();

            }

        });
        web_bbs.setWebViewClient(web_bbsClient);
        web_bbs.setWebChromeClient(new WebChromeClient());

    }

    /**
     * @reeturn 跳转到网页的页面, 直接使用这个来
     */
    public static Intent getIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(SENDURL, url);
        intent.putExtra(SENDTITLE, title);
        return intent;
    }
}
