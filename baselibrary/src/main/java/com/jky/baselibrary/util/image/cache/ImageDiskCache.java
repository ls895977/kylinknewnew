package com.jky.baselibrary.util.image.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jky.baselibrary.LibConfig;
import com.jky.baselibrary.util.common.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDiskCache implements ImageCache {

    private String mCacheDir;

    public ImageDiskCache() {
        mCacheDir = LibConfig.getApplication().getCacheDir().getAbsolutePath();
    }

    public ImageDiskCache setCacheDir(String cacheDir) {
        if (TextUtils.isEmpty(cacheDir))
            return this;
        mCacheDir = cacheDir;
        return this;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File file = new File(getFilePath(url));
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(getFilePath(url));
    }

    private String getFilePath(String url) {
        return mCacheDir + File.separator + MD5Util.MD5(url);
    }
}
