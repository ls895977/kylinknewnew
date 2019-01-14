package com.maoyongxin.myapplication.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dylandu on 2018/3/29.
 */

public class ImageGetter {
    public static void doCompressBySize(String originalPath, String compressPath) {
        try {
            Bitmap bm = getSmallBitmap(originalPath);
            if (bm == null || bm.isRecycled()) return;

            int degree = readPictureDegree(originalPath);
            if (degree != 0) {
                bm = rotaingBitmap(degree, bm);
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(compressPath));
            if (new File(originalPath).length() > 1024 * 1024) {            // >1M   50
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            } else if (new File(originalPath).length() > 1024 * 512) {        // >0.5M  60
                bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
            } else if (new File(originalPath).length() > 1024 * 256) {        // > 0.25M   75
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            } else if (new File(originalPath).length() > 1024 * 32) {   //  >  0.03125M(约30多KB) 90                                                        //90
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, 95, bos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, 360, 640);
    }

    //从图片文件获取指定大小的Bitmap对象
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    // 计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 读取一张图片的旋转角度属性
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将一张图片旋转一定的角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotateBitmap;
    }

}
