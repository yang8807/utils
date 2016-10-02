package com.yan.fastview_library.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.LogUtils;

/**
 * Created by 洒笑天涯 on 2016/6/17.
 * 定向运动弹窗；
 */
public abstract class BaseTargetTranslationDialog extends BaseDialog {
    /*显示的view*/
    private View childView, targetView;
    private ObjectAnimator inAnimator, outAnimatorX, outAnimatorY;
    private AnimatorSet animatorSet;
    /*移动的目标位置*/
    private int[] targetLocation;
    /*初次动画进入的展示位置*/
    private int[] showLocation;
    protected View contentView;
    /*目标View是否在弹窗的上方 */
    private boolean isTargetOnDialogTop = true;
    /*动画运动持续时间*/
    private int duration = 1000;
    /*旋转角度*/
    private int rotationDegreen = 0;

    public BaseTargetTranslationDialog(Context context) {
        super(context);
    }

    @Override
    protected View contentView() {
        contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        setMatchParent(contentView);
        return contentView;
    }

    public View getContentView() {
        return contentView;
    }

    /***
     * 进行动画的View
     */
    public View setAnimationView(View view) {
        this.childView = view;
        return childView;
    }

    protected abstract int getLayoutId();

    /***
     * 判断动画是否执行,执行完,才能去获取view在屏幕中的位置
     */
    public boolean isAnimatorRunning() {
        return (inAnimator != null && inAnimator.isRunning()) || (animatorSet != null && animatorSet.isRunning());
    }

    public BaseTargetTranslationDialog setTarget(View view) {
        this.targetView = view;
        targetLocation = new int[2];
        view.getLocationInWindow(targetLocation);
        //获取到view的中间位置
        targetLocation[0] = targetLocation[0] + targetView.getMeasuredWidth() / 2;
        targetLocation[1] = targetLocation[1] + targetView.getMeasuredHeight() / 2;
        Point point = DeviceUtil.getDisplaySize(getContext());
        LogUtils.v("mine", "targetLocation:" + targetLocation[0] + ":" + targetLocation[1] + "--" + point.x + ":" + point.y);
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (inAnimator == null) {
            inAnimator = getTranslationAimator();
            inAnimator.start();
            inAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (showLocation == null) {
                        showLocation = new int[2];
                        childView.getLocationInWindow(showLocation);
                        showLocation[0] = showLocation[0] + childView.getMeasuredWidth() / 2;
                        showLocation[1] = showLocation[1] + childView.getMeasuredHeight() / 2;
                    }
                    childView.clearAnimation();
                    inAnimator.cancel();
                }
            });
        } else {
            inAnimator.start();
        }
    }

    /***
     * 隐藏输入框
     */
    public void hideDilog() {
        animatorSet = new AnimatorSet();
        outAnimatorX = hideAnimatorX();
        outAnimatorY = hideAnimatorY();
        isTargetOnDialogTop = showLocation[1] > targetLocation[1];//根据在屏幕坐标的位置，获得目标view在弹窗的上方还是下方
        if (isTargetOnDialogTop)//根据所在位置，判断是上抛还是下抛
        {
            outAnimatorX.setInterpolator(new AccelerateInterpolator());
            outAnimatorY.setInterpolator(new DecelerateInterpolator());
        } else {
            outAnimatorX.setInterpolator(new DecelerateInterpolator());
            outAnimatorY.setInterpolator(new AccelerateInterpolator());
        }
        animatorSet.play(outAnimatorX).with(outAnimatorY);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BaseTargetTranslationDialog.this.dismiss();
                childView.clearAnimation();
            }
        });
    }

    /***
     * 水平进入屏幕中间的动画
     */
    public ObjectAnimator getTranslationAimator() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0, 1.0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.0f);
        PropertyValuesHolder translatitonX = PropertyValuesHolder.ofFloat("translationX", DeviceUtil.getDisplaySize(getContext()).x, 0);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(childView, alpha, translatitonX, scaleX, scaleY);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        return objectAnimator;
    }

    /***
     * 隐藏时在X轴上的动画运动
     */
    public ObjectAnimator hideAnimatorX() {
        float fscaleX = targetView.getMeasuredWidth() / (float) childView.getMeasuredWidth();
        float fscaleY = targetView.getMeasuredHeight() / (float) childView.getMeasuredHeight();
        float scale = (fscaleX + fscaleY) / 2;
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0, targetLocation[0] - showLocation[0]);
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, rotationDegreen);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, scale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, scale);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0);
        return ObjectAnimator.ofPropertyValuesHolder(childView, translationX, rotation, scaleX, scaleY, alpha).setDuration(duration);
    }

    /***
     * 隐藏时在Y轴上的动画运动
     */
    public ObjectAnimator hideAnimatorY() {
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0, targetLocation[1] - showLocation[1]);
        return ObjectAnimator.ofPropertyValuesHolder(childView, translationY).setDuration(duration);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleAllAnimation();
    }

    public void cancleAllAnimation() {
        if (inAnimator != null && inAnimator.isRunning()) inAnimator.cancel();
        if (outAnimatorX != null && outAnimatorX.isRunning()) outAnimatorX.cancel();
        if (outAnimatorY != null && outAnimatorY.isRunning()) outAnimatorY.cancel();
        if (animatorSet != null && animatorSet.isRunning()) animatorSet.cancel();
    }

    /***
     * 设置动画持续时间
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /***
     * 设置旋转的度数
     */
    public void setRotationDegreen(int rotationDegreen) {
        this.rotationDegreen = rotationDegreen;
    }
}