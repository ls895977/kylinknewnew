package com.maoyongxin.myapplication.server.utils;


import com.jky.baselibrary.LibConfig;
import com.jky.baselibrary.util.common.CommonFileUtil;

import java.io.File;

public class FileUtil extends CommonFileUtil {

    public static final String ROOT_DIR_NAME = "IUSEN";

    private static final String SEPARATOR = File.separator;
    private static final String FILES_DIR_NAME = "files";
    private static final String CACHE_DIR_NAME = "cache";

    private static String sRootDir;
    private static String sFileDir;
    private static String sTempDir;

    static {
        initDir();
    }

    private static void initDir() {
        if (hasSdcard()) {
            sRootDir = getSdcardDir() + SEPARATOR + ROOT_DIR_NAME + SEPARATOR;
            sFileDir = getSdcardDir() + SEPARATOR + ROOT_DIR_NAME + SEPARATOR + FILES_DIR_NAME + SEPARATOR;
            sTempDir = getSdcardDir() + SEPARATOR + ROOT_DIR_NAME + SEPARATOR + CACHE_DIR_NAME + SEPARATOR;
            makeDir(sFileDir);
            makeDir(sTempDir);
        } else {
            sRootDir = LibConfig.getApplication().getCacheDir().getParentFile().getAbsolutePath();
            sFileDir = LibConfig.getApplication().getFilesDir().getAbsolutePath();
            sTempDir = LibConfig.getApplication().getCacheDir().getAbsolutePath();
        }
    }

    public static String getRootDir() {
        return sRootDir;
    }

    public static String getFileDir() {
        return sFileDir;
    }

    public static String getTempDir() {
        return sTempDir;
    }
}
