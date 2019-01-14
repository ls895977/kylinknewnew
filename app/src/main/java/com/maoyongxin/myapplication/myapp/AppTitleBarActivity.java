package com.maoyongxin.myapplication.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.jky.baselibrary.base.BaseActivity;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.manager.Managers;

import java.lang.reflect.Field;


public abstract class AppTitleBarActivity extends BaseActivity {

    public static final String EXTRA_DATA_KEY_FROM = "EXTRA_DATA_KEY_FROM";
    protected TitleBar mTitleBar;

    private boolean mEnableShare;
    private Handler handler;
    public Looper looper = Looper.myLooper();
    /**
     * 回传消息到子类
     */
    protected abstract void handlerPassMsg(int target,int target1, Object obj);
    public MyHandler myHandler = new MyHandler(looper);
    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            handlerPassMsg(msg.what, msg.arg1,msg.obj);
        }
    }
    @Override
    protected void initView() {
        super.initView();
        View view = findViewById(R.id.TitleBar_IUSen);
        if (view != null)
            mTitleBar = (TitleBar) view;
    }
    public SharedPreferences getSp(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo", MODE_PRIVATE);
        return sharedPreferences;
    }

    public void makeSpNoAuto(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("isAuto", false);
        editor.putString("psw", null);
        editor.putString("num", null);
        editor.commit();
    }

    public void makeSpAuto(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("isAuto", true);
        editor.putString("psw", AppApplication.getMyPassword());
        editor.putString("num", AppApplication.getCurrentUserInfo().getUserId());
        editor.commit();
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        Managers.getHttpMgr().cancelTag(getActivityTag());
    }

    public void enableShare() {
        mEnableShare = true;
    }

    public void startActivityThenFinish(Intent intent) {
        startActivity(intent);
        finish();
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
