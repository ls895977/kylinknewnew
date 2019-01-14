package com.maoyongxin.myapplication.handler;

import android.os.Message;
import android.support.v4.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTimerTask {

    Timer timer;
    Fragment fragment;
    GlobalHandler globalHandler;
    int itemCount;//计时次数，0为无限循环
    int currentIndex;
    int taskActionCode;

    boolean isStart=false;

    public ScheduleTimerTask(Fragment fragment, GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.fragment = fragment;
    }

    public ScheduleTimerTask(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }

    //启动任务
    public void startTask(int taskActionCode, int itemCount) {
        this.itemCount = itemCount;
        this.taskActionCode = taskActionCode;
        timer = new Timer();
        try {
            switch (taskActionCode) {
                case TC_HomePageBannerViewPager:
                    if (timerTask.scheduledExecutionTime()>= 0) {
                        timer.schedule(timerTask, 0, 2000);
                    }
                    break;
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //停止任务
    public void stopTask() {
        if (timer != null)
        timer.cancel();
        if (timerTask != null)
        timerTask.cancel();
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = Message.obtain(globalHandler);
            try {
                switch (taskActionCode) {
                    case TC_HomePageBannerViewPager:
                        if (currentIndex == (itemCount - 1)) {
                            currentIndex = 0;
                        } else {
                            currentIndex++;
                        }

                        msg.what = TC_HomePageBannerViewPager;
                        msg.arg1 = GlobalConst.HandlerSuccess;
                        msg.obj = currentIndex;
                        break;
                }
            } catch (Exception ex) {
                msg.arg1 = GlobalConst.HandlerFailure;
                msg.obj = ex.getLocalizedMessage();
            }
            globalHandler.sendMessage(msg);
        }
    };

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public static final int TC_HomePageBannerViewPager = 10;//首页滚动图片
}
