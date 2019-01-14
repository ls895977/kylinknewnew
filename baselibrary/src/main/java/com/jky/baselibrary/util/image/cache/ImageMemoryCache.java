package com.jky.baselibrary.util.image.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.jky.baselibrary.util.common.MD5Util;


public class ImageMemoryCache implements ImageCache {
    private LruCache<String, Bitmap> mLruCache;

    public ImageMemoryCache() {
        initMemoryImageCache();
    }

    private void initMemoryImageCache() {
        //获取最大可用内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 4;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mLruCache.put(MD5Util.MD5(url), bitmap);
    }

    @Override
    public Bitmap get(String url) {
        return mLruCache.get(MD5Util.MD5(url));
    }
}
