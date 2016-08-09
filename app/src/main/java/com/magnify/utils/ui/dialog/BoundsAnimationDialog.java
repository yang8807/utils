package com.magnify.utils.ui.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.utils.R;
import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.aniamtion.KickBackAnimator;
import com.yan.fastview_library.dialog.BaseDialog;

/**
 * Created by heinigger on 16/8/8.
 */
public class BoundsAnimationDialog extends BaseDialog implements View.OnClickListener {
    private ViewGroup ceterViewGroup;
    private Handler mHandler = new Handler();

    public BoundsAnimationDialog(Context context) {
        super(context, false, R.style.WhiteColorDialog);
    }

    @Override
    protected View contentView() {
        View parentView = LayoutInflater.from(getContext()).inflate(R.layout.item_bound_animatioin, null);
        ceterViewGroup = (ViewGroup) parentView.findViewById(R.id.ceterViewGroup);
        parentView.findViewById(R.id.img_cancle).setOnClickListener(this);
        return parentView;
    }

    @Override
    public void show() {
        setChildVisible(false);
        super.show();
        translateAnimation(true);

    }

    @Override
    public void dismiss() {
        translateAnimation(false);
    }

    private void translateAnimation(final boolean isIn) {
        ceterViewGroup.setVisibility(View.VISIBLE);
        for (int i = 0; i < ceterViewGroup.getChildCount(); i++) {
            final int finalI = i;
            mHandler.postDelayed(() -> {
                final View childView = ceterViewGroup.getChildAt(finalI);
                childView.setVisibility(View.VISIBLE);
                int translationY = DeviceUtil.dipToPx(getContext(), 300);
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(childView, "translationY", isIn ? new float[]{
                        translationY, 0} : new float[]{0, translationY});
                fadeAnim.setDuration(600);
                KickBackAnimator kickAnimator = new KickBackAnimator();
                kickAnimator.setDuration(600);
                fadeAnim.setEvaluator(kickAnimator);
                fadeAnim.start();
                fadeAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //只有当动画都执行完了,才关闭
                        animation.cancel();
                        //当动画是最后一个view的时候,就关闭弹窗,等最后一个动画收场再关,有点慢,改成倒数第二个动画收场就关闭弹窗
                        if (!isIn && finalI == ceterViewGroup.getChildCount() - 3)
                            BoundsAnimationDialog.super.dismiss();
                        if (!isIn)//退出的时候,所有试图不可见,避免onResum后,重新启动动画,出现不自然的动画
                            childView.setVisibility(View.GONE);
                    }
                });
            }, i * 150);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancle:
                dismiss();
                break;
        }
    }

    public void setChildVisible(boolean childVisible) {
        if (ceterViewGroup != null) {
            for (int i = 0; i < ceterViewGroup.getChildCount(); i++) {
                ceterViewGroup.getChildAt(i).setVisibility(childVisible ? View.VISIBLE : View.GONE);
            }
        }
    }
}
