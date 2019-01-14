package com.maoyongxin.myapplication.tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoyongxin on 2017/12/28.
 */

public class CompressPhotoUtils {
    private List<String> fileList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public void CompressPhoto(Context context, List<String> list, CompressCallBack callBack) {
        CompressTask task = new CompressTask(context, list, callBack);
        task.execute();
    }

    class CompressTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;
        private List<String> list;
        private CompressCallBack callBack;

        CompressTask(Context context, List<String> list, CompressCallBack callBack) {
            this.context = context;
            this.list = list;
            this.callBack = callBack;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, null, "处理中...");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i < list.size(); i++) {
                if (!isPic(list.get(i))) {
                    fileList.add(list.get(i));
                    continue;
                }
                Bitmap bitmap = getBitmap(list.get(i));
                String path = SaveBitmap(bitmap, i);
                fileList.add(path);
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.dismiss();
            callBack.success(fileList);
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    /**
     * 从sd卡获取压缩图片bitmap
     */
    public static Bitmap getBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1280f;
        float ww = 720f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    private boolean isPic(String name) {
        if (name.endsWith(".jpg") || name.endsWith(".png")
                || name.endsWith(".jpeg") || name.endsWith(".bmp")
                || name.endsWith(".tiff") || name.endsWith(".gif")) {
            return true;
        }
        return false;
    }

    /**
     * 保存bitmap到内存卡
     */
    public static String SaveBitmap(Bitmap bmp, int num) {
        File file = new File("mnt/sdcard/situ/");
        String path = null;
        if (!file.exists())
            file.mkdirs();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String picName = formatter.format(new java.util.Date());
            path = file.getPath() + "/" + picName + "-" + num + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) { //目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) { //如果是文件，删除
                        file.delete();
                    } else { //目录
                        if (file.listFiles().length == 0) { //目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public interface CompressCallBack {
        void success(List<String> list);
    }
}
