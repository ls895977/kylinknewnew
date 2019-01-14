package com.maoyongxin.myapplication.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jky.baselibrary.base.BaseActivity;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.manager.Managers;

import java.lang.reflect.Field;


public abstract class AppActivity extends BaseActivity {

    public static final String EXTRA_DATA_KEY_FROM = "EXTRA_DATA_KEY_FROM";
    protected TitleBar mTitleBar;

    private boolean mEnableShare;
    private Handler handler;

    @Override
    protected void initView() {
        super.initView();
        View view = findViewById(R.id.TitleBar_IUSen);
        if (view != null)
            mTitleBar = (TitleBar) view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setBackgroundColor(Color.parseColor("#121212"));
            linear_bar.setVisibility(View.GONE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }
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
