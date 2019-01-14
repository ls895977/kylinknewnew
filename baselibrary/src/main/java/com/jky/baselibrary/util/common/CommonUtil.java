package com.jky.baselibrary.util.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

public class CommonUtil {

    public static void checkAndroidVersion(int versionCode, @Nullable Runnable onMatchHigher, @Nullable Runnable onLower) {
        if (Build.VERSION.SDK_INT >= versionCode) {
            if (onMatchHigher != null)
                onMatchHigher.run();
        } else {
            if (onLower != null)
                onLower.run();
        }
    }

    public static boolean isValueDifferent(Object o1, Object o2) {
        return (o1 == null && o2 != null)
                || (o1 != null && o2 == null)
                || (o1 != null && o2 != null && !o1.equals(o2));
    }

    /**
     * 评价app
     */
    public static void starApp(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
            Logger.i("getAppVersionName", "当前版本：" + versionName);
        } catch (Exception e) {
            Logger.i("getAppVersionName", e.toString());
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = 1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
            Logger.i("getAppVersionCode", "当前版本号：" + versionCode);
        } catch (Exception e) {
            Logger.i("getAppVersionCode", e.toString());
        }
        return versionCode;
    }
}
