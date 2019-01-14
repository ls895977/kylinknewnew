package com.jky.baselibrary.util.common;

import android.text.TextUtils;

/**
 * 正则表达式工具
 */

public class RexUtil {
    public static boolean matchPhone(String str) {
        return !TextUtils.isEmpty(str) && str.replace(" ", "").matches("1[1-9][0-9]{9}");
    }
}
