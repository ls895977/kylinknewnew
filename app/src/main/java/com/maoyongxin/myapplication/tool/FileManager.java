package com.maoyongxin.myapplication.tool;

import android.os.Environment;

import java.io.File;

/**
 * Created by admin on 2017/3/10.
 */

public class FileManager {
    public static final String APPSDCARDPATH = Environment.getExternalStorageDirectory() + "/situ";
    public static final String APPIMAGEPATH = Environment.getExternalStorageDirectory() + "/situ/.image";
    public static final String APPFILEPATH = Environment.getExternalStorageDirectory() + "/situ/files";
    public static String getAppFilePath(){
        String sdDir = null;
        boolean isSDcardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (isSDcardExist) {
            sdDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdDir = Environment.getRootDirectory().getAbsolutePath();
        }
        return sdDir;
    }

    /**
     * 本地图片保存位置
     * @return
     */
    public static String getAppImageFilePath(){
        return getAppFilePath()+ "/situ/image";
    }
    /**
     * 本地图片保存位置
     * @return
     */
    public static String getAppApkFilePath(){
        return getAppFilePath()+ "/situ/apk";
    }

    /**
     * 用户头像本地地址获取
     * @param phone
     * @return
     */
    public static String getUserHeadImage(String phone){
        String filePath = getAppImageFilePath()+"/"+phone+".jpg";
        File file = new File(filePath);
        if(file.exists()){
            return filePath;
        }else {
            return null;
        }
    }
}
