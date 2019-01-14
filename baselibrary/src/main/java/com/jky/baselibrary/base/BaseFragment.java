package com.jky.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jky.baselibrary.util.common.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    private boolean mIsForeground;
    Unbinder unbinder;

    protected void initData() {
    }

    protected abstract int bindLayout();

    protected void initView() {
    }

    protected void initEvent() {
    }

    protected void doBusiness() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logI("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logI("onCreate");
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logI("onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == mRootView)
            mRootView = inflater.inflate(bindLayout(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    protected View findViewById(int id) {
        if (mRootView == null)
            return null;
        else {
            return mRootView.findViewById(id);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logI("onActivityCreated");
        initView();
        initEvent();
        doBusiness();
    }

    @Override
    public void onStart() {
        super.onStart();
        logI("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        logI("onResume");
        mIsForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        logI("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        logI("onStop");
        mIsForeground = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logI("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logI("onDestroy");
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logI("onDetach");
    }

    public boolean isForeground() {
        return mIsForeground;
    }

    public void logI(String msg) {
        Logger.i(getFragmentTag(), msg);
    }

    public String getFragmentTag() {
        return this.getClass().getSimpleName();
    }
}
