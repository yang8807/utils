package com.yan.fastview_library.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.magnify.yutils.DeviceUtil;
import com.magnify.yutils.data.ColorUtils;
import com.yan.fastview_library.R;


/**
 * DialogFragment基类,用于显示dialog，从底部弹出<br>
 *
 * @author 林钊平
 */
public class BaseDialogFragment extends DialogFragment {

    private Toast mToast;

    private boolean isAlignBottom = true;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 从底部弹出
        if (!isAlignBottom) {
            return;
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setLayout(DeviceUtil.getDisplaySize(getActivity()).x, attributes.height);
        window.setBackgroundDrawable(new ColorDrawable(ColorUtils.getAlphaColor(0.4f, Color.BLACK)));
        window.setWindowAnimations(R.style.DialogAnim);
    }

    /**
     * 吐司 信息
     *
     * @param s
     */
    public void showToast(CharSequence s) {
        showToast(s, Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司
     *
     * @param s
     * @param time
     */
    public void showToast(CharSequence s, int time) {
        mToast.setText(s);
        mToast.setDuration(time);
        mToast.show();
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    protected void setAlignBottom(boolean isAlignBottom) {
        this.isAlignBottom = isAlignBottom;
    }
}
