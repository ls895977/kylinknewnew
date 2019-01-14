package com.maoyongxin.myapplication.myapp;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jky.baselibrary.base.BaseActivity;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.manager.Managers;

import java.lang.reflect.Field;


public abstract class AppHandlerActivity extends BaseActivity {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }
//        AppApplication.setOnAddressGetListener(new AppApplication.OnAddressGetListener() {
//            @Override
//            public void getAddress(boolean isGetAddress) {
//                if (isGetAddress == false) {
//                    showToastShort(getString(R.string.no_lacation));
//                }
//            }
//        });
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
