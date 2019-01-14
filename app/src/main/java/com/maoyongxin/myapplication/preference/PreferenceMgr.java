package com.maoyongxin.myapplication.preference;

import android.text.TextUtils;

import com.jky.baselibrary.util.common.PreferenceUtil;
import com.maoyongxin.myapplication.myapp.AppConstants;


/**
 * Created by dingke on 2017/8/3.
 */

public class PreferenceMgr {
    private static PreferenceUtil sUtil;

    static {
        sUtil = PreferenceUtil.getInstance();
    }

    public static void setString(String key, String value) {
        sUtil.setStringValue(key, value);
    }

    public static String getString(String key, String defaultValue) {
        return sUtil.getStringValue(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        sUtil.setIntValue(key, value);
    }

    public static int getInt(String key, int defaultValue) {
        return sUtil.getIntValue(key, defaultValue);
    }

    public static void setLong(String key, long value) {
        sUtil.setLongValue(key, value);
    }

    public static long getLong(String key, long defaultValue) {
        return sUtil.getLongValue(key, defaultValue);
    }

    public static void setFloat(String key, float value) {
        sUtil.setFloatValue(key, value);
    }

    public static float getFloat(String key, float defaultValue) {
        return sUtil.getFloatValue(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        sUtil.setBooleanValue(key, value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sUtil.getBooleanValue(key, defaultValue);
    }

    public static void setToken(String token) {
        if (TextUtils.isEmpty(token))
            token = "";
        setString(AppConstants.PREFERENCE_KEY_CURRENT_TOKEN, token);
    }

    public static String getToken() {
        return getString(AppConstants.PREFERENCE_KEY_CURRENT_TOKEN, "");
    }
}
