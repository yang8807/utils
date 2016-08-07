package com.magnify.basea_dapter_library.abslistview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.magnify.basea_dapter_library.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heinigger
 *         tv = (AdvancedAutoCompleteTextView) findViewById(R.id.tv);
 *         tv.setThreshold(0); adapter = new AutoCompleteAdapter(this, mOriginalValues,
 *         10); tv.setAdapter(adapter); AutocompleteTextView
 *         Inherit this class, can realize automatic popup list some of the data
 */
public abstract class BaseAutoCompleteAdapter<T> extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private ArrayList<T> mOriginalValues;
    private ArrayList<T> newValues;
    private List<T> mObjects;
    private final Object mLock = new Object();
    private int maxMatch = 20;
    private int layoutid;
    private String prefixStrings;

    public BaseAutoCompleteAdapter(int layoutid, ArrayList<T> mOriginalValues, int maxMatch) {
        this.mOriginalValues = mOriginalValues;
        this.layoutid = layoutid;
        this.maxMatch = maxMatch;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    Log.i("tag", "mOriginalValues.size=" + mOriginalValues.size());
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                    return results;
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();
                prefixStrings = prefix.toString();
                final int count = mOriginalValues.size();
                newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    //Choose which as annotations
                    String value = getFiltString(mOriginalValues.get(i));
                    String valueText = value.toLowerCase();
                    if (judgeCondition(prefixString, valueText)) {
                        newValues.add(mOriginalValues.get(i));
                    }
                    if (maxMatch > 0) {
                        if (newValues.size() > maxMatch - 1) {
                            break;
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    /**
     * @return Judgment standard, is to start, or contain, or to the end;
     */
    protected boolean judgeCondition(String prefixString, String valueText) {
        //you can change the judge condition,normal the condition is contain
//        valueText.startsWith(prefixString);
//        valueText.endsWith(prefixString);
        return valueText.contains(prefixString);
    }

    /***
     * Input fields to filtering
     */
    protected abstract String getFiltString(T t);

    public int getCount() {
        return mObjects == null ? 0 : mObjects.size();
    }

    public String getItem(int position) {
        return getFiltString(mObjects.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(parent.getContext(), convertView, parent, layoutid, position);
        setData2View(holder, mObjects.get(position), position, prefixStrings);
        return holder.getConvertView();
    }

    protected abstract void setData2View(ViewHolder holder, T t, int position, String prefixString);

    public ArrayList<T> getAllItems() {
        return mOriginalValues;
    }
}