package com.maoyongxin.myapplication.handler;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;


/**
 * Created by michalwan on 15/6/14.
 */
public class GlobalHandler extends Handler {

    protected Context context;
    protected Fragment fragment;
    protected OnFragmentListener mFragmentListener;


    protected GlobalHandler(Context context) {
        this.context = context;
       if(context instanceof OnFragmentListener) this.mFragmentListener= (OnFragmentListener) context;
    }

    protected GlobalHandler(Fragment fragment) {
        this.fragment = fragment;
        if(fragment instanceof OnFragmentListener) this.mFragmentListener= (OnFragmentListener) fragment;
    }

    public static GlobalHandler getDefaultHandler(Context context) {
        return new GlobalHandler(context);
    }

    public static GlobalHandler getDefaultHandler(Fragment fragment) {
        return new GlobalHandler(fragment);
    }

    public void setFragmentListener(OnFragmentListener mFragmentListener) {
        this.mFragmentListener = mFragmentListener;
    }

    public final static String Key_OperationCode="Key_OperationCode";//操作代码
    public final static String Key_ResultCode="Key_ResultCode";//结果代码
    public final static String Key_ArgumentsData="Key_ArgumentsData";//核心参数数据
}
