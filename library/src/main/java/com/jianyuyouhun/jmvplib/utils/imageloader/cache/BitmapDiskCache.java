package com.jianyuyouhun.jmvplib.utils.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 图片缩略图硬件缓存,该类是线程安全的
 * Created by wangyu on 2017/5/10.
 */
public class BitmapDiskCache implements BitmapLruCache {
    private int maxSize;
    private DiskLruCache diskLruCache;

    public BitmapDiskCache(String directoryPath, int maxSize) throws IOException {
        this.maxSize = maxSize;

        File directory = new File(directoryPath);
        diskLruCache = DiskLruCache.open(directory, 1, 1, maxSize);
    }

    @Override
    public synchronized void put(String key, Bitmap value) {
        try {
            DiskLruCache.Editor edit = diskLruCache.edit(key);
            if (edit != null) {
                OutputStream newOutputStream = edit.newOutputStream(0);
                value.compress(CompressFormat.JPEG, 90, newOutputStream);
                edit.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Bitmap get(String key) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null)
                snapshot.close();
        }

        return bitmap;
    }

    @Override
    public synchronized void remove(String key) {
        try {
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void clear() {
        try {
            diskLruCache.setMaxSize(0);
            diskLruCache.flush();
            diskLruCache.setMaxSize(maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized int getMaxSize() {
        return maxSize;
    }

    @Override
    public synchronized int getCurrentSize() {
        return (int) diskLruCache.size();
    }

    @Override
    public synchronized List<String> getAllKey() {
        return diskLruCache.getAllKey();
    }
}
