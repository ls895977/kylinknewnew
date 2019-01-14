package com.maoyongxin.myapplication.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dylandu on 2017/8/7.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{
    public Activity activity;
    public BaseHolder(Activity activity, View itemView){
        super(itemView);
        this.activity=activity;
    }

    //绑定数据
    public abstract void bindData(T data,int position);

    //绑定监听
    public abstract void bindListener();
}
