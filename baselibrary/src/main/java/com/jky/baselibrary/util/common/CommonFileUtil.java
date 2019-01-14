package com.jky.baselibrary.util.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommonFileUtil {

    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSdcardDir() {
        if (!hasSdcard())
            return null;
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        return isFileExist(new File(filePath));
    }

    public static boolean isFileExist(File file) {
        return file != null && file.exists();
    }

    public static boolean isFileNotEmpty(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        return isFileNotEmpty(new File(filePath));
    }

    public static boolean isFileNotEmpty(File file) {
        return isFileExist(file) && file.length() > 0;
    }

    public static boolean isDirExist(String dirPath) {
        if (TextUtils.isEmpty(dirPath))
            return false;
        return isDirExist(new File(dirPath));
    }

    public static boolean isDirExist(File dir) {
        return dir != null && dir.exists();
    }

    public static long fileLength(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return 0;
        return fileLength(new File(filePath));
    }

    public static long fileLength(File file) {
        if (file == null)
            return 0;
        return file.length();
    }

    public static void makeDir(String dirPath) {
        if (!isDirExist(dirPath)) {
            new File(dirPath).mkdirs();
        }
    }

    public static void copy(InputStream is, File des) {
        if (is == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(des);
            byte[] buf = new byte[1024 * 8];
            int length;
            while ((length = is.read(buf)) > 0) {
                fos.write(buf, 0, length);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
