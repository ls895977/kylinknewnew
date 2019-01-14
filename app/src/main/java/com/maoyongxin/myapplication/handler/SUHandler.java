package com.maoyongxin.myapplication.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;


/**
 * Created by michalwan on 15/7/23.
 */

public class SUHandler extends GlobalHandler {

    private SUHandler(Context context) {
        super(context);
    }

    private SUHandler(Fragment fragment) {
        super(fragment);
    }

    public static SUHandler getDefaultHandler(Context context) {
        return new SUHandler(context);
    }

    public static SUHandler getDefaultHandler(Fragment fragment) {
        return new SUHandler(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(Key_OperationCode, msg.what);
        bundleArgs.putInt(Key_ResultCode, msg.arg1);

        switch (msg.what) {
            case ScheduleTimerTask.TC_HomePageBannerViewPager:
                if (msg.arg1 == GlobalConst.HandlerSuccess) {
                    int intCurrentPageIndex = (int) msg.obj;
                    bundleArgs.putInt(Key_ArgumentsData, intCurrentPageIndex);
                }
                break;
        }
        mFragmentListener.onFragmentCallBack(fragment, bundleArgs);
    }
}
