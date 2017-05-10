package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.jianyuyouhun.jmvplib.utils.AppHelper;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;
import com.jianyuyouhun.jmvplib.utils.Encoder;
import com.jianyuyouhun.jmvplib.utils.http.executor.AsyncResult;
import com.jianyuyouhun.jmvplib.utils.http.executor.SimpleSyncTask;
import com.jianyuyouhun.jmvplib.utils.http.executor.SingleThreadPool;
import com.jianyuyouhun.jmvplib.utils.imageloader.cache.BitmapMemoryCache;
import com.jianyuyouhun.jmvplib.utils.imageloader.downloader.ImageDownloader;

import java.io.File;
import java.util.List;

/**
 * imageLoader实现类
 * Created by wangyu on 2017/5/10.
 */

public class ImageLoaderImpl extends ImageLoader {
    private boolean isInit = false;

    /** 内存缓存 */
    private BitmapMemoryCache bitmapMemoryCache;

    /** 磁盘缓存 */
    private DiskCacheImageLoader diskCacheManager;

    /** 磁盘图片加载器 */
    private DiskImageLoader diskBitmapLoader;

    /** 图片下载器 */
    private ImageDownloader imageDownloader;

    /** 图片解码线程池， 单任务 */
    private SingleThreadPool executorService = new SingleThreadPool();

    ImageLoaderImpl() {
        isInit = false;
    }

    private static String getUIPName(Context context) {
        return CommonUtils.getUIPName(context);
    }

    private static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isLargeHeap(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static int getLargeMemoryClass(ActivityManager am) {
        return am.getLargeMemoryClass();
    }

    @Override
    public void init(int memoryCacheSize, int diskCacheSize, String diskCacheDir, int downloadThreadSize) {
        // 初始化缓存
        bitmapMemoryCache = new BitmapMemoryCache(memoryCacheSize);
        diskCacheManager = new DiskCacheImageLoader(executorService, diskCacheSize, diskCacheDir);

        // 本地图片加载器
        diskBitmapLoader = new DiskImageLoader(executorService);

        /** 图片下载器 */
        imageDownloader = new ImageDownloader(downloadThreadSize);

        isInit = true;
    }

    @Override
    public void init(Context context) {

        int memoryCacheSize = getMemoryCacheSize(context);

        int diskCacheSize = 30 * 1024 * 1024;
        String sha1Key = Encoder.encodeBySHA1(getUIPName(context));
        String dirName = "wangyu/imgCache_" + sha1Key;
        String diskCacheDir = new File(context.getCacheDir(), dirName).getAbsolutePath();

        this.init(memoryCacheSize, diskCacheSize, diskCacheDir, 2);
    }

    @Override
    public ImageLoadTask loadImage(ImageLoadOptions imageLoadOptions, ImageLoadListener imageLoadListener) {
        ImageLoadTask imageLoadTask = new ImageLoadTask(imageLoadOptions, imageLoadListener, bitmapMemoryCache,
                diskCacheManager, diskBitmapLoader, imageDownloader);
        imageLoadTask.start();
        return imageLoadTask;
    }

    private int getMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        if (hasHoneycomb() && isLargeHeap(context)) {
            memoryClass = getLargeMemoryClass(am);
        }
        return 1024 * 1024 * memoryClass / 8;
    }

    @Override
    public boolean isInit() {
        return isInit;
    }

    @Override
    public int getMemoryCacheSize() {
        return bitmapMemoryCache.getCurrentSize();
    }

    @Override
    public int getDiskCacheSize() {
        return diskCacheManager.getSize();
    }

    @Override
    public void clear(final DiskCacheImageLoader.OnClearCacheListener onClearCacheListener) {
        SimpleSyncTask<Void> simpleSyncTask = new SimpleSyncTask<Void>() {
            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                bitmapMemoryCache.clear();
                diskCacheManager.clear(onClearCacheListener);
            }
        };
        simpleSyncTask.execute(executorService);
    }

    @Override
    public void removeCache(String filePath) {
        final String encodeKey = ImageLoadOptions.getEncodeKey(filePath);
        List<String> allKey = bitmapMemoryCache.getAllKey();
        for (String key : allKey) {
            if (key.startsWith(encodeKey)) {
                bitmapMemoryCache.remove(key);
            }
        }

        SimpleSyncTask<Void> simpleSyncTask = new SimpleSyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                List<String> allKey = diskCacheManager.getAllKey();
                for (String key : allKey) {
                    if (key.startsWith(encodeKey)) {
                        diskCacheManager.remove(key);
                    }
                }
            }
        };
        simpleSyncTask.execute(executorService);
    }
}
