package com.jky.baselibrary.base;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * BaseViewHolder for BaseAdapter.
 * SubClass must be public.
 *
 * @see MultiTypeBaseAdapter
 * @see SingleTypeBaseAdapter
 */
public abstract class BaseViewHolder<T> {
    protected Context mContext;
    protected ArrayList<T> mData;
    protected BaseAdapter mAdapter;

    public BaseViewHolder(Context context, ArrayList<T> data, BaseAdapter adapter) {
        mContext = context;
        mData = data;
        mAdapter = adapter;
    }

    public Context getContext() {
        return mContext;
    }

    public ArrayList<T> getData() {
        return mData;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    protected abstract void initView(View view);

    protected abstract void initState();

    protected abstract void fillData(int position);
}
