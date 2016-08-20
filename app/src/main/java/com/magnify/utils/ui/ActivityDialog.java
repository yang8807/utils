package com.magnify.utils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.magnify.utils.R;
import com.magnify.utils.base.CurrentBaseActivity;
import com.magnify.utils.ui.dialog.BoundsAnimationDialog;
import com.magnify.utils.ui.dialog.HomeBannerDialog;
import com.yan.fastview_library.poupwindows.BaseLinearPoupWindows;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by heinigger on 16/8/8.
 */
public class ActivityDialog extends CurrentBaseActivity {
    private BaseLinearPoupWindows mAnyWherePoupLeft, mAnyWherePoupRight, mAnyWherePoupTop, mAnyWherePoupBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //定向运动的弹窗
        findViewById(R.id.ptv_show_translation_dialog).setOnClickListener(v -> new HomeBannerDialog(self).setTarget(v).show());
        //弹性弹窗
        findViewById(R.id.ptv_show_bound).setOnClickListener(v -> new BoundsAnimationDialog(self).show());

        mAnyWherePoupLeft = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupRight = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupRight.setDrawableNRadius(R.color.colorAccent, 10);
        mAnyWherePoupTop = new BaseLinearPoupWindows(self, R.layout.item_poup_image);
        mAnyWherePoupBottom = new BaseLinearPoupWindows(self, R.layout.item_poup_image);

        //任意方向的弹窗
        findViewById(R.id.btn_show_anywhere).setOnClickListener(view -> {
            mAnyWherePoupRight.showAtCenter(view, Gravity.RIGHT);
            mAnyWherePoupLeft.showAtCenter(view, Gravity.LEFT);
            mAnyWherePoupTop.showAtCenter(view, Gravity.TOP);
            mAnyWherePoupBottom.showAtCenter(view, Gravity.BOTTOM);
        });
        findViewById(R.id.btn_show_anywheres).setOnClickListener(view -> {
            mAnyWherePoupRight.showAtCenter(view, Gravity.RIGHT, 50);
            mAnyWherePoupLeft.showAtCenter(view, Gravity.LEFT, 50);
            mAnyWherePoupTop.showAtCenter(view, Gravity.TOP, 50);
            mAnyWherePoupBottom.showAtCenter(view, Gravity.BOTTOM, 50);
        });

        findViewById(R.id.btn_show_bottom).setOnClickListener(view -> {
            mAnyWherePoupRight.showAtCenter(view, Gravity.RIGHT);
            mAnyWherePoupLeft.showAtCenter(view, Gravity.LEFT);
            mAnyWherePoupTop.showAtCenter(view, Gravity.TOP);
            mAnyWherePoupBottom.showAtCenter(view, Gravity.BOTTOM);
        });
        findViewById(R.id.btn_show_bottom_s).setOnClickListener(view -> {
            mAnyWherePoupRight.showAtCenter(view, Gravity.RIGHT, 50);
            mAnyWherePoupLeft.showAtCenter(view, Gravity.LEFT, 50);
            mAnyWherePoupTop.showAtCenter(view, Gravity.TOP, 50);
            mAnyWherePoupBottom.showAtCenter(view, Gravity.BOTTOM, 50);
        });
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("SHARE");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}
