package com.yan.fastview_library.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yan.fastview_library.R;

import java.util.ArrayList;

public class CenterViewGroup extends ViewGroup {
    private int numberColumn = 3;
    private int[] numberColumnArray;
    private boolean isMultiColumn = false;
    private int verticalSpacing;
    private ArrayList<Line> lines = new ArrayList<>();


    public CenterViewGroup(Context context) {
        super(context, null);
    }

    public CenterViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        getProperties(context, attrs);
    }

    public CenterViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getProperties(context, attrs);
    }

    private void getProperties(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CenterViewGroup);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CenterViewGroup_cv_numberColumn) {
                numberColumn = typedArray.getInteger(attr, 3);

            } else if (attr == R.styleable.CenterViewGroup_cv_numberColumnArray) {
                getNumberColumnArray(typedArray, attr);

            } else if (attr == R.styleable.CenterViewGroup_cv_verticalSpacing) {
                verticalSpacing = (int) typedArray.getDimension(attr, 5);

            }
        }
        typedArray.recycle();
    }

    private void getNumberColumnArray(TypedArray typedArray, int attr) {
        String text = typedArray.getString(attr);
        if (!TextUtils.isEmpty(text)) {
            try {
                String[] texts = text.split(",");
                if (texts != null && texts.length > 0) {
                    int[] numberColoums = new int[texts.length];
                    for (int j = 0; j < texts.length; j++) {
                        numberColoums[j] = Integer.parseInt(texts[j]);
                    }
                    numberColumnArray = numberColoums;
                    isMultiColumn = true;
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("your input numberColumnArray exist problems");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        lines.clear();
        lastPostion = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        int childeWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        int childeHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childeWidthMode, parentWidth);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childeHeightMode, parentHeight);

        Line line = null;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            childView.getMeasuredWidth();
            FristCreate fristCreate = getCurrentNumberColumn(i);
            int currentNumberColumn = isMultiColumn ? numberColumnArray[fristCreate.getFristCreatePosition()] : numberColumn;
            if (fristCreate.isFrist()) {
                line = new Line(parentWidth);
            }
            if (line != null)
                line.addView(childView);
            if (!isMultiColumn && line != null && (line.getChildCount() == currentNumberColumn || i == getChildCount() - 1)) {
                lines.add(line);
                line = null;
            } else if (isMultiColumn && line != null && line.getChildCount() == currentNumberColumn) {
                lines.add(line);
                line = null;
            }
        }
        int totalHeight = 0;
        for (int i = 0; i < lines.size(); i++) totalHeight += lines.get(i).getHeight();
        totalHeight += ((lines.size() + 1) * verticalSpacing);

        setMeasuredDimension(parentWidth + getPaddingLeft() + getPaddingRight(), resolveSize(totalHeight + getPaddingBottom() + getPaddingTop(), heightMeasureSpec));
    }

    private int lastPostion;

    public FristCreate getCurrentNumberColumn(int i) {
        if (!isMultiColumn) {
            return new FristCreate(i % numberColumn == 0, 0);
        }
        boolean isboolean = true;
        int createPosition = 0;
        int total = numberColumnArray[0];
        int currentIndex = 0;
        for (int j = 1; j <= numberColumnArray.length; j++) {
            if (i >= total) {
                total += numberColumnArray[j];
                createPosition = j;
                if (lastPostion < createPosition) lastPostion = createPosition;
                else createPosition = 0;
            } else {
                currentIndex = j - 1;
                break;
            }
        }
        if (createPosition != 0 || (i == 0 && createPosition == 0)) isboolean = true;
        else isboolean = false;
        return new FristCreate(isboolean, currentIndex);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t = getPaddingTop() + verticalSpacing;
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(l, t);
            t = t + line.getHeight() + verticalSpacing;
        }
    }

    private class Line {
        private ArrayList<View> views = new ArrayList<>();
        private int maxHeight;
        private int useWidth;
        private int parentWidth;

        public Line(int parentWidth) {
            this.parentWidth = parentWidth;
        }

        public void addView(View view) {
            maxHeight = maxHeight > view.getMeasuredHeight() ? maxHeight : view.getMeasuredHeight();
            useWidth += view.getMeasuredWidth();
            views.add(view);
        }

        public int getChildCount() {
            return views.size();
        }

        public void layout(int l, int t) {
            int horozontalSpacing = (parentWidth - useWidth) / (getChildCount() + 1);
            for (int i = 0; i < getChildCount(); i++) {
                View childView = views.get(i);
                l += horozontalSpacing;
                childView.layout(l, t, l + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
                l += childView.getMeasuredWidth();
            }
        }

        public int getHeight() {
            return maxHeight;
        }
    }

    private class FristCreate {
        private boolean isFrist = false;
        private int fristCreatePosition;

        public FristCreate(boolean isFrist, int fristCreatePosition) {
            this.isFrist = isFrist;
            this.fristCreatePosition = fristCreatePosition;
        }

        public boolean isFrist() {
            return isFrist;
        }

        public void setFrist(boolean frist) {
            isFrist = frist;
        }

        public int getFristCreatePosition() {
            return fristCreatePosition;
        }

        public void setFristCreatePosition(int fristCreatePosition) {
            this.fristCreatePosition = fristCreatePosition;
        }
    }
}