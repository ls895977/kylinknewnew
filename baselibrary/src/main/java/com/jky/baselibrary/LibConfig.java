package com.jky.baselibrary;

import com.jky.baselibrary.base.BaseApplication;

public class LibConfig {

    private static BaseApplication sApplication;
    private static boolean sDebug;

    /**
     * todo  若要使用该Lib的工具，请在App启动时执行该初始化方法
     *
     * @param debug 是否是debug模式
     */
    public static void init(BaseApplication application, boolean debug) {
        sApplication = application;
        sDebug = debug;
    }

    public static BaseApplication getApplication() {
        return sApplication;
    }

    public static boolean isDebug() {
        return sDebug;
    }
}
