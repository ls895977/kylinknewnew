package com.maoyongxin.myapplication.ui.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoyongxin.myapplication.R;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class Fragment5 extends Fragment {

    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment5, null);
        return mView;
    }
}
