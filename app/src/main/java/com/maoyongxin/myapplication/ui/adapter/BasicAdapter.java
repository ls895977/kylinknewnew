package com.maoyongxin.myapplication.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by dylandu on 2017/8/7.
 * adaper基类
 */

public abstract class BasicAdapter<T> extends Adapter {
    public ArrayList<T> list;
    public Activity activity;


    public BasicAdapter(Activity activity, ArrayList<T> list) {
        this.list = list;
        this.activity=activity;
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }
    @Override
    public int getItemViewType(int position) {
        return getType(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder<T> baseHolder = null;
        baseHolder = getHolder(getHolderView(parent,viewType));//获取holder,由子类实现

//        //增加滑动特效
//        View holderView = getHolderView(parent,viewType);
//        //缩小holder
//        holderView.setScaleX(0.6f);
//        holderView.setScaleY(0.6f);
//        //让holder放大
//        ViewCompat.animate(holderView).scaleX(1f).scaleY(1f)
//                .setInterpolator(new OvershootInterpolator())
//                .setDuration(400).start();

        return baseHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseHolder baseHolder = (BaseHolder) holder;
        //获取数据
        T t = list.get(position);
        //绑定数据
        baseHolder.bindData(t,position);
        //绑定数据
        baseHolder.bindListener();
    }

    protected abstract View getHolderView(ViewGroup parent, int type);//获取视图

    protected abstract  int getType(int position) ;

    protected abstract BaseHolder<T> getHolder(View holderView);
}
