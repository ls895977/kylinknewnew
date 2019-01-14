package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;


public class GroupJianjieFragment extends HeaderViewPagerFragment {
    private TextView tv_groupJianjie;
    private View view;
    public static GroupJianjieFragment newInstance() {
        return new GroupJianjieFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_chat_jianjie, container, false);
        tv_groupJianjie= (TextView) view.findViewById(R.id.tv_groupJianjie);
        initView();
        return view;
    }

    private void initView() {
        tv_groupJianjie.setText("这是我们的产业交流群，欢迎大家加入交流");
    }

    @Override
    public View getScrollableView() {
        return view;
    }

}
