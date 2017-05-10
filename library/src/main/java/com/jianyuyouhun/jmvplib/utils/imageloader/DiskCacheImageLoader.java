package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.graphics.Bitmap;

import com.jianyuyouhun.jmvplib.utils.http.executor.AsyncResult;
import com.jianyuyouhun.jmvplib.utils.http.executor.SimpleSyncTask;
import com.jianyuyouhun.jmvplib.utils.http.executor.SingleThreadPool;
import com.jianyuyouhun.jmvplib.utils.imageloader.cache.BitmapDiskCache;

import java.io.IOException;
import java.util.List;


/**
 * 缩略图缓存管理
 *
 * @author zdxing 2015年1月23日
 */
public class DiskCacheImageLoader {

    private SingleThreadPool singleThreadPool;
    private BitmapDiskCache bitmapDiskCache;

    public DiskCacheImageLoader(SingleThreadPool singleThreadPool, int diskCacheSize, String diskCacheDir) {
        this.singleThreadPool = singleThreadPool;
        try {
            bitmapDiskCache = new BitmapDiskCache(diskCacheDir, diskCacheSize);
        } catch (IOException e) {
            bitmapDiskCache = null;
            e.printStackTrace();
        }
    }

    public DiskCacheImageLoader(int diskCacheSize, String diskCacheDir) {
        this(new SingleThreadPool(), diskCacheSize, diskCacheDir);
    }

    /**
     * 返回当前已经使用的缓存大小
     *
     * @return 单位 字节
     */
    public int getSize() {
        if (bitmapDiskCache != null)
            return bitmapDiskCache.getCurrentSize();
        return 0;
    }

    /**
     * 从缓存中获取数据
     *
     * @param key                    key
     * @param onGetDiskCacheListener 结果回调
     */
    public void getCache(String key, OnGetDiskCacheListener onGetDiskCacheListener) {
        if (key == null || onGetDiskCacheListener == null) {
            throw new NullPointerException();
        }
        if (bitmapDiskCache != null) {
            singleThreadPool.submit(new GetTask(key, onGetDiskCacheListener));
        } else {
            onGetDiskCacheListener.onGetDiskCache(key, null);
        }
    }

    public void remove(String key) {
        bitmapDiskCache.remove(key);
    }

    public List<String> getAllKey() {
        return bitmapDiskCache.getAllKey();
    }

    /**
     * 移除缓存
     */
    public void removeAsync(final String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (bitmapDiskCache != null) {
            SimpleSyncTask<Void> myAsyncTask = new SimpleSyncTask<Void>() {
                protected void runOnBackground(AsyncResult<Void> asyncResult) {
                    bitmapDiskCache.remove(key);
                }
            };
            myAsyncTask.execute(singleThreadPool);
        }
    }

    /**
     * 清除缓存
     *
     * @param onClearDiskCacheListener 清除缓存监听
     */
    public void clear(OnClearCacheListener onClearDiskCacheListener) {
        if (bitmapDiskCache != null) {
            singleThreadPool.submit(new ClearTask(onClearDiskCacheListener));
        } else {
            onClearDiskCacheListener.onClearCacheFinish();
        }
    }

    /**
     * 放入缓存
     *
     * @param key    放入缓存的key
     * @param bitmap 放入缓存的图片
     */
    public void putCache(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new NullPointerException();
        }
        if (bitmapDiskCache != null) {
            singleThreadPool.execute(new SaveTask(key, bitmap));
        }
    }

    public interface OnGetDiskCacheListener {
        void onGetDiskCache(String key, Bitmap bitmap);
    }

    public interface OnClearCacheListener {
        void onClearCacheFinish();
    }

    private class GetTask extends SimpleSyncTask<Bitmap> {
        String key;
        OnGetDiskCacheListener onGetDiskCacheListener;

        public GetTask(String key, OnGetDiskCacheListener onGetDiskCacheListener) {
            super();
            this.key = key;
            this.onGetDiskCacheListener = onGetDiskCacheListener;
        }

        @Override
        protected void runOnBackground(AsyncResult<Bitmap> asyncResult) {
            asyncResult.setData(bitmapDiskCache.get(key));
        }

        @Override
        protected void runOnUIThread(AsyncResult<Bitmap> asyncResult) {
            onGetDiskCacheListener.onGetDiskCache(key, asyncResult.getData());
        }
    }

    private class ClearTask extends SimpleSyncTask<Void> {
        OnClearCacheListener onClearDiskCacheListener;

        public ClearTask(OnClearCacheListener onClearDiskCacheListener) {
            super();
            this.onClearDiskCacheListener = onClearDiskCacheListener;
        }

        @Override
        protected void runOnBackground(AsyncResult<Void> asyncResult) {
            bitmapDiskCache.clear();
        }

        @Override
        protected void runOnUIThread(AsyncResult<Void> asyncResult) {
            if (onClearDiskCacheListener != null)
                onClearDiskCacheListener.onClearCacheFinish();
        }
    }

    private class SaveTask implements Runnable {
        String key;
        Bitmap bitmap;

        public SaveTask(String key, Bitmap bitmap) {
            this.key = key;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            bitmapDiskCache.put(key, bitmap);
        }
    }
}
