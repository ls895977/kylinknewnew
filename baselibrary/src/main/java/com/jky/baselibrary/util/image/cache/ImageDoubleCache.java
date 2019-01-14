package com.jky.baselibrary.util.image.cache;

import android.graphics.Bitmap;

public class ImageDoubleCache implements ImageCache {

    private ImageMemoryCache mMemoryCache = new ImageMemoryCache();
    private ImageDiskCache mDiskCache = new ImageDiskCache();

    public ImageDoubleCache setCacheDir(String cacheDir) {
        mDiskCache.setCacheDir(cacheDir);
        return this;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null)
            bitmap = mDiskCache.get(url);
        return bitmap;
    }
}
