package com.maoyongxin.myapplication.ui.news;

import android.content.Context;

import java.lang.ref.WeakReference;

public class Inject {

    private static WeakReference<Context> weakReference;

    public static void injectContext(Context context){
        weakReference = new WeakReference<Context>(context);
    }

    public static Context provideContext(){
        return weakReference.get();
    }

}
