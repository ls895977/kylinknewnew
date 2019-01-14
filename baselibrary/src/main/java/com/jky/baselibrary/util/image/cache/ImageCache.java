package com.jky.baselibrary.util.image.cache;

import android.graphics.Bitmap;

public interface ImageCache {
    void put(String url, Bitmap bitmap);

    Bitmap get(String url);
}
