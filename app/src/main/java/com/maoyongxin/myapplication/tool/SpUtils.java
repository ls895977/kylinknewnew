package com.maoyongxin.myapplication.tool;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static final String FILE_NAME = "share_data";
    public static void putBoolean(Context context, String key , boolean b){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,b);
        edit.apply();
    }

    public static Boolean getBoolean(Context context, String key , boolean b){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
       return sp.getBoolean(key,b);

    }
}
