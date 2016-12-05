package com.example.devel.newsclient.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Description:菜单栏的根适配器
 * Created by devel on 11/26/2016.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public List<T> mData;
    public Context mContext;

    public MyBaseAdapter(List<T> mData, Context mContext) {
        super();
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
