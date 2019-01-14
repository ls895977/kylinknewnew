package com.jky.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.holidaycheck.permissify.PermissifyActivity;
import com.jky.baselibrary.constants.Broadcast;
import com.jky.baselibrary.util.common.Logger;
import com.jky.baselibrary.util.common.ScreenUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


@SuppressWarnings("unused")
public abstract class BaseActivity extends PermissifyActivity {

    private Toast mLastToast;
    private BaseReceiver mSuicideReceiver;
    private BaseReceiver mBaseBroadcastReceiver;

    private float mXDown;
    private float mYDown;

    private boolean mSwipeFinish;

    private boolean mEnableDoublePressExit;
    private String mDoublePressExitHint;
    private long mLastBackMillis;
    private long mDoublePressExitPeriod;

    private long mLastClickTime = System.currentTimeMillis();
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        int layoutResId = bindLayout();
        if (layoutResId != 0)
            setContentView(layoutResId);
        ButterKnife.bind(this);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initView();
        initEvent();
        doBusiness();
    }

    protected abstract int bindLayout();

    protected void initData() {
    }

    protected void initView() {
    }

    protected void initEvent() {
    }

    protected void doBusiness() {
    }

    protected void releaseResource() {
    }

    @Override
    protected void onDestroy() {
        if (mSuicideReceiver != null) {
            unregisterReceiver(mSuicideReceiver);
            mSuicideReceiver = null;
        }
        if (mBaseBroadcastReceiver != null) {
            unregisterReceiver(mBaseBroadcastReceiver);
            mBaseBroadcastReceiver = null;
        }
        super.onDestroy();
        releaseResource();
    }

    @Override
    public void finish() {
        if (mSuicideReceiver != null) {
            unregisterReceiver(mSuicideReceiver);
            mSuicideReceiver = null;
        }
        super.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = event.getX();
                mYDown = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getY() - mYDown) < 0.1f * ScreenUtil.getScreenHeight(this)) {
                    if (Math.abs(event.getX() - mXDown) > 0.1f * ScreenUtil.getScreenWidth(this)) {
                        if (event.getX() - mXDown > 0 && mSwipeFinish) {
                            finish();
                        }
                    }
                }
                return true;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (!mEnableDoublePressExit)
            super.onBackPressed();
        else {
            long now = System.currentTimeMillis();
            if (now - mLastBackMillis > mDoublePressExitPeriod) {
                mLastBackMillis = now;
                showToastShort(mDoublePressExitHint);
            } else
               super.onBackPressed();



        }
    }

    public void enableSwipeFinish(boolean enable) {
        mSwipeFinish = enable;
    }

    public BaseActivity getActivity() {
        return this;
    }

    public void log(String msg) {
        Logger.d(getActivityTag(), msg);
    }

    public void logI(String msg) {
        Logger.i(getActivityTag(), msg);
    }

    public void logW(String msg) {
        Logger.w(getActivityTag(), msg);
    }

    public void logE(String msg) {
        Logger.e(getActivityTag(), msg);
    }

    public void showToastShort(int stringResID) {
        showToastShort(getString(stringResID));
    }

    public void showToastShort(String string) {
        cancelToast();
        mLastToast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        mLastToast.show();
    }

    public void showToastLong(int stringResID) {
        showToastLong(getString(stringResID));
    }

    public void showToastLong(String string) {
        cancelToast();
        mLastToast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        mLastToast.show();
    }

    private void cancelToast() {
        if (mLastToast != null)
            mLastToast.cancel();
    }

    public void enableDoublePressExit(long periodMillis, String hint) {
        mEnableDoublePressExit = true;
        mDoublePressExitPeriod = periodMillis;
        mDoublePressExitHint = hint;
    }

    public void enableSuicide() {
        if (mSuicideReceiver == null) {
            mSuicideReceiver = new BaseReceiver(this) {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Broadcast.SUICIDE)) {
                        finish();
                    }
                }
            };
            mSuicideReceiver.register(Broadcast.SUICIDE);
        }
    }

    public void executeSuicide() {
        sendBroadcast(new Intent(Broadcast.SUICIDE));
    }

    public void handleBroadcast(Context context, Intent intent) {
    }

    public void registerBroadcast(String... actions) {
        if (actions == null)
            return;
        if (mBaseBroadcastReceiver == null) {
            mBaseBroadcastReceiver = new BaseReceiver(this) {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleBroadcast(context, intent);
                }
            };
            IntentFilter filter = new IntentFilter();
            for (String action : actions)
                filter.addAction(action);
            registerReceiver(mBaseBroadcastReceiver, filter);
        }
    }

    public synchronized boolean isQuickClick() {
        long time = System.currentTimeMillis();
        boolean res = time - mLastClickTime < 500;
        mLastClickTime = time;
        return res;
    }

    public String getActivityTag() {
        return getClass().getSimpleName();
    }

    public int getActivityID() {
        return getActivityTag().hashCode();
    }
}
