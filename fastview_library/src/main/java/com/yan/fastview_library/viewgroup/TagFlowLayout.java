package com.yan.fastview_library.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.magnify.yutils.DeviceUtil;
import com.yan.fastview_library.R;

import java.util.ArrayList;
import java.util.List;


public class TagFlowLayout extends ViewGroup {

    private boolean isAssaginSpace = true;

    private SingleLine currentLine;

    private int mVetivalSpace, mHorizontalSpace;

    private List<SingleLine> mLines = new ArrayList<TagFlowLayout.SingleLine>();

    private int useWidth;

    private int totalHeight;

    private int mParentWidth;

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mVetivalSpace = DeviceUtil.dipToPx(context, 5);
        mHorizontalSpace = DeviceUtil.dipToPx(context, 5);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
            getPeroperties(context, a);
        } finally {
            a.recycle();
        }
    }

    private void getPeroperties(Context context, TypedArray a) {
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.TagFlowLayout_isAssaginSpace) {
                isAssaginSpace = a.getBoolean(attr, isAssaginSpace);

            } else {
            }
        }
    }

    public TagFlowLayout(Context context) {
        super(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        useWidth = 0;
        currentLine = null;
        mLines.clear();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        mParentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int mParentHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int childeWidthMode;
        int childeHeightMode;
        childeWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        childeHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;

        int ChildMeasureWithSpec = MeasureSpec.makeMeasureSpec(mParentWidth, childeWidthMode);
        int ChildMeasureHeightSpec = MeasureSpec.makeMeasureSpec(mParentHeight, childeHeightMode);

        parentMeasureDimension(heightMeasureSpec, mParentWidth, ChildMeasureWithSpec, ChildMeasureHeightSpec);
    }


    private void parentMeasureDimension(int heightMeasureSpec, int mParentWidth, int ChildMeasureWithSpec, int ChildMeasureHeightSpec) {
        measrueChild(mParentWidth, ChildMeasureWithSpec, ChildMeasureHeightSpec);

        totalHeight = mVetivalSpace * (mLines.size()/* - 1*/) + currentLine.LineHeight * (mLines.size()) + getPaddingBottom() + getPaddingTop();

        setMeasuredDimension(mParentWidth + getPaddingLeft() + getPaddingRight(), resolveSize(totalHeight, heightMeasureSpec));
    }

    private void measrueChild(int mParentWidth, int ChildMeasureWithSpec, int ChildMeasureHeightSpec) {

        int mChildCount = getChildCount();
        currentLine = new SingleLine();
        for (int i = 0; i < mChildCount; i++) {
            View view = getChildAt(i);
            view.measure(ChildMeasureWithSpec, ChildMeasureHeightSpec);
            int mChildWidth = view.getMeasuredWidth();
            int mChildHeight = view.getMeasuredHeight();
            useWidth = mChildWidth + mHorizontalSpace / 2 + useWidth;
            if (useWidth <= mParentWidth) {
                currentLine.LineHeight = mChildHeight;
                currentLine.addToLine(view);
            } else {
                newLine(view);
            }
        }
        if (!mLines.contains(currentLine))
            mLines.add(currentLine);

    }


    private void newLine(View view) {
        mLines.add(currentLine);
        currentLine = new SingleLine();
        currentLine.addToLine(view);
        useWidth = 0;
        useWidth += view.getMeasuredWidth();
    }

    public void setIsAssaginSpace(boolean isAssaginSpace) {
        this.isAssaginSpace = isAssaginSpace;
        postInvalidate();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t += getPaddingTop();
        for (SingleLine line : mLines) {
            line.childLayout(l, t);
            t = t + line.getLineHeight() + mVetivalSpace;
        }
    }

    class SingleLine {

        public int LineHeight = 0;

        int lineWidth = 0;

        ArrayList<View> ViewList = new ArrayList<View>();

        public void addToLine(View tView) {
            if (LineHeight < tView.getMeasuredHeight()) {
                LineHeight = tView.getMeasuredHeight();
            }
            lineWidth += tView.getMeasuredWidth();
            ViewList.add(tView);
        }

        public void childLayout(int l, int t) {

            lineWidth += mHorizontalSpace * (ViewList.size());
            int SurplusSpace = mParentWidth - lineWidth;
            int sur = 0;
            if (isAssaginSpace) {
                sur = SurplusSpace / ViewList.size();
            }

            for (int i = 0; i < ViewList.size(); i++) {
                View view = ViewList.get(i);
                view.layout(l, t, l + view.getMeasuredWidth() + sur, t + view.getMeasuredHeight());
                l += mHorizontalSpace + view.getMeasuredWidth() + sur;
                view.setPadding(view.getPaddingLeft() + sur / 2, view.getPaddingTop(), view.getPaddingRight() + sur / 2, view.getPaddingBottom());
            }
        }

        public ArrayList<View> getViewList() {
            return ViewList;
        }

        public int getLineHeight() {
            return LineHeight;
        }

        public int getLineWidth() {
            return lineWidth;
        }
    }

}