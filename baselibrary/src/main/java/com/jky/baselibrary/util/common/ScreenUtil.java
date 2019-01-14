package com.jky.baselibrary.util.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jky.baselibrary.LibConfig;

@SuppressWarnings("ALL")
public class ScreenUtil {

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int dip2Px(Context context, float dip) {
        if (context == null)
            return 0;

        float s = context.getResources().getDisplayMetrics().density;
        return (int) (dip * s + 0.5f);
    }

    public static int dp2Px(float dp) {
        Context context = LibConfig.getApplication();
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (dp * s + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Logger.i("ScreenUtil", "Status height: " + height);
        return height;
    }
}
