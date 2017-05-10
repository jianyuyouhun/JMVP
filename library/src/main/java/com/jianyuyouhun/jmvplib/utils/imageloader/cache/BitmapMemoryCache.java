package com.jianyuyouhun.jmvplib.utils.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

/**
 * lru内存缓存， 该类是线程安全的
 * Created by wangyu on 2017/5/10.
 */
public class BitmapMemoryCache implements BitmapLruCache {
    private int maxSize;
    private LruCache<String, Bitmap> memoryCache;

    public BitmapMemoryCache(int maxSize) {
        this.maxSize = maxSize;
        memoryCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public void put(String key, Bitmap value) {
        memoryCache.put(key, value);
    }

    @Override
    public Bitmap get(String key) {
        return memoryCache.get(key);
    }

    @Override
    public void remove(String key) {
        memoryCache.remove(key);
    }

    @Override
    public void clear() {
        memoryCache.evictAll();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public int getCurrentSize() {
        return memoryCache.size();
    }

    @Override
    public List<String> getAllKey() {
        return new ArrayList<>(memoryCache.snapshot().keySet());
    }
}
