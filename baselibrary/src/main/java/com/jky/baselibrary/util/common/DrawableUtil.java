package com.jky.baselibrary.util.common;

import android.graphics.drawable.Drawable;

import com.jky.baselibrary.LibConfig;

public class DrawableUtil {
    public static Drawable getDrawableFromResId(int resId) {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = LibConfig.getApplication().getResources().getDrawable(resId, null);
        } else {
            drawable = LibConfig.getApplication().getResources().getDrawable(resId);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
