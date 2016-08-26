package com.magnify.basea_dapter_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;
    private static ImageLoaderInterface imageLoaderInterface;
    private int type;

    public static void setImageLoaderInterface(ImageLoaderInterface imageLoaderInterface) {
        ViewHolder.imageLoaderInterface = imageLoaderInterface;
    }

    public ViewHolder(Context context, View itemView, ViewGroup parent, int position) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mPosition = position;
        mViews = new SparseArray<View>();
        mConvertView.setTag(this);

    }


    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            ViewHolder holder = new ViewHolder(context, itemView, parent, position);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public static ImageLoaderInterface getImageLoaderInterface() {
        return imageLoaderInterface;
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, Spanned text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    public ViewHolder displayImage(String url, ImageView imageView, int defaultDrawable) {
        if (imageLoaderInterface != null && defaultDrawable > 0) {
            imageLoaderInterface.displayImage(url, imageView, defaultDrawable);
        } else if (imageLoaderInterface != null && defaultDrawable < 0) {
            imageLoaderInterface.displayImage(url, imageView);
        } else {
            Log.e("ViewHolder", "请在全局中初始化ImageLoaderInterface");
        }
        return this;
    }

    public ViewHolder displayImage(int drawableId, ImageView imageView) {
        if (imageLoaderInterface != null) {
            imageLoaderInterface.displayImage(drawableId, imageView);
        } else if (imageLoaderInterface != null) {
            imageLoaderInterface.displayImage(drawableId, imageView);
        } else {
            Log.e("ViewHolder", "请在全局中初始化ImageLoaderInterface");
        }
        return this;
    }

    public ViewHolder displayImage(String url, int viewId) {
        return displayImage(url, (ImageView) getView(viewId), -1);
    }

    public ViewHolder displayImage(int drawableId, int viewId) {
        return displayImage(drawableId, (ImageView) getView(viewId));
    }

    public ViewHolder displayImage(String url, int viewId, int defaultDrawable) {
        return displayImage(url, (ImageView) getView(viewId), defaultDrawable);
    }

    public ViewHolder displayRoundImage(String url, int viewID, int defaultDrawable) {
        return displayRoundImage(url, (ImageView) getView(viewID), defaultDrawable);
    }

    public ViewHolder displayRoundImage(int url, int viewID) {
        return displayRoundImage(url, (ImageView) getView(viewID));
    }

    public ViewHolder displayRoundImage(String url, ImageView imageView, int defaultDrawable) {

        if (imageLoaderInterface != null && defaultDrawable > 0) {
            imageLoaderInterface.displayRoundImage(url, imageView, defaultDrawable);
        } else if (imageLoaderInterface != null && defaultDrawable < 0) {
            imageLoaderInterface.displayRoundImage(url, imageView);
        } else {
            Log.e("ViewHolder", "请在全局中初始化ImageLoaderInterface");
        }
        return this;
    }

    public ViewHolder displayRoundImage(int drawableID, ImageView imageView) {

        if (imageLoaderInterface != null) {
            imageLoaderInterface.displayRoundImage(drawableID, imageView);
        } else if (imageLoaderInterface != null) {
            imageLoaderInterface.displayRoundImage(drawableID, imageView);
        } else {
            Log.e("ViewHolder", "请在全局中初始化ImageLoaderInterface");
        }
        return this;
    }

    public ViewHolder displayRoundImage(String url, int viewId) {
        return displayRoundImage(url, (ImageView) getView(viewId), -1);
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setVisible(boolean visible, int... viewIds) {
        for (int i = 0; i < viewIds.length; i++) {
            getView(viewIds[i]).setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }


    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnClickListener(View.OnClickListener listener, int... viewIds) {
        for (int i = 0; i < viewIds.length; i++) {
            getView(viewIds[i]).setOnClickListener(listener);
        }
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public void updatePosition(int position) {
        mPosition = position;
    }

    public int getmPosition() {
        return mPosition;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setOnClickListener(int imageid, int position, View.OnClickListener onClickListener) {
        ImageView imageView = getView(imageid);
        imageView.setTag(imageid, position);
        imageView.setOnClickListener(onClickListener);
    }
}
