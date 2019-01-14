package com.maoyongxin.myapplication.myapp;

import android.content.Context;

import com.jky.baselibrary.base.BaseFragment;

import java.lang.reflect.Field;


public abstract class AppFragment extends BaseFragment {

    private static AppActivity mAppActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppActivity = (AppActivity) getActivity();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public AppActivity getAppActivity() {
        return mAppActivity;
    }

    public void showToastShort(int stringResID) {
        showToastShort(getString(stringResID));
    }

    public static void showToastShort(String string) {
        mAppActivity.showToastShort(string);
    }

    public void showToastLong(int stringResID) {
        showToastLong(getString(stringResID));
    }

    public void showToastLong(String string) {
        mAppActivity.showToastLong(string);
    }

    public String getActivityTag() {
        return mAppActivity.getActivityTag();
    }
    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getActivity().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
