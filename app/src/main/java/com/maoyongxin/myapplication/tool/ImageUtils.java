package com.maoyongxin.myapplication.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2017/3/10.
 */

public class ImageUtils {
    /**
     * 将bitmap转成file
     * @param bitmap
     * @param filePath//保存路径
     * @param fileName//名称
     * @param quality//压缩质量
     */
    public static void bitmapToFile(final Bitmap bitmap, final String filePath, final String fileName, final int quality, final OnSuccessListener onSuccessListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file1 = new File(filePath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file=new File(filePath+"/"+fileName);//将要保存图片的路径
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                    bos.flush();
                    bos.close();
                    if(onSuccessListener!=null){
                        onSuccessListener.onSuccess();;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(onSuccessListener!=null){
                        onSuccessListener.onFail();
                    }
                }
            }
        }).start();

    }
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 替换本地
     * @param oldFilePath
     * @param newFilePath
     */
    public static void replaceFile(final String oldFilePath, final String newFilePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(oldFilePath);
                if(file.exists()){
                    file.delete();
                }


                File file1 = new File(newFilePath);
                file1.renameTo(new File(oldFilePath));
            }
        }).start();
    }
    public interface OnSuccessListener{
        void onSuccess();
        void onFail();
    }
}
