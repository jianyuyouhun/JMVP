package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.graphics.Bitmap;

import com.jianyuyouhun.jmvplib.utils.imageloader.cache.BitmapMemoryCache;
import com.jianyuyouhun.jmvplib.utils.imageloader.downloader.ImageDownloadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.downloader.ImageDownloader;

/**
 * 图片加载任务
 * Created by wangyu on 2017/5/10.
 */

public class ImageLoadTask {

    private ImageLoadOptions imageLoadOptions;
    private ImageLoadListener imageLoadListener;

    /** 内存缓存 */
    private BitmapMemoryCache bitmapMemoryCache;
    /** 磁盘缓存 */
    private DiskCacheImageLoader diskCacheManager;

    /** 磁盘图片加载器 */
    private DiskImageLoader diskBitmapLoader;

    /** 图片下载器 */
    private ImageDownloader imageDownloader;

    /**
     * 当前的加载状态
     */
    private LoadState loadState = LoadState.LOAD_UNSTART;
    private DiskImageLoader.LoadTask loadTask;
    private ImageDownloader.ImageDownLoadTask imageDownLoadTask;

    public ImageLoadTask(ImageLoadOptions imageLoadOptions, ImageLoadListener imageLoadListener,
                         BitmapMemoryCache bitmapMemoryCache, DiskCacheImageLoader diskCacheManager,
                         DiskImageLoader diskBitmapLoader, ImageDownloader imageDownloader) {
        this.imageLoadOptions = imageLoadOptions;
        this.imageLoadListener = imageLoadListener;
        this.bitmapMemoryCache = bitmapMemoryCache;
        this.diskCacheManager = diskCacheManager;
        this.diskBitmapLoader = diskBitmapLoader;
        this.imageDownloader = imageDownloader;
        loadState = LoadState.LOAD_UNSTART;
    }

    /**
     * 开始加载
     */
    void start() {
        if (loadState != LoadState.LOAD_UNSTART) {
            throw new IllegalStateException("该方法只能调用一次");
        }


        if (imageLoadOptions.isMemoryCacheEnable()) {
            Bitmap memoryBitmap = bitmapMemoryCache.get(imageLoadOptions.getKey());
            if (memoryBitmap != null) {
                imageLoadListener.onLoadSuccessful(ImageLoadListener.BitmapSource.MEMORY_CACHE, memoryBitmap);
                loadState = LoadState.LOAD_FINISHED;
                return;
            }
        }

        if (imageLoadOptions.isDiskCacheEnable()) {
            loadState = LoadState.LOADING_DISK_CACHE;
            diskCacheManager.getCache(imageLoadOptions.getKey(), new DiskCacheImageLoader.OnGetDiskCacheListener() {
                @Override
                public void onGetDiskCache(String key, Bitmap bitmap) {
                    if (loadState == LoadState.LOAD_CANCELED) {
                        return;
                    }

                    if (bitmap != null) {
                        if (imageLoadOptions.isMemoryCacheEnable()) {
                            bitmapMemoryCache.put(imageLoadOptions.getKey(), bitmap);
                        }
                        imageLoadListener.onLoadSuccessful(ImageLoadListener.BitmapSource.DISK_CACHE, bitmap);
                        loadState = LoadState.LOAD_FINISHED;
                    } else {
                        loadSource();
                    }
                }
            });
        } else {
            loadSource();
        }

    }

    private void loadSource() {
        if (imageLoadOptions.hasUrl() && imageDownloader.isDownloading(imageLoadOptions.getFilePath())) {
            loadBitmapFromNet();
        } else {
            loadBitmapFromDisk(false);
        }
    }

    private void loadBitmapFromDisk(final boolean ifFromNet) {
        loadState = LoadState.LOADING_DISK;
        loadTask = diskBitmapLoader.loadBitmap(imageLoadOptions, new DiskImageLoader.OnLoadResultListener() {
            @Override
            public void onLoadResult(int result, Bitmap bitmap) {
                if (loadState == LoadState.LOAD_CANCELED)
                    return;

                loadTask = null;
                if (result == RESULT_LOAD_SUCCESSFUL) {
                    String key = imageLoadOptions.getKey();
                    if (imageLoadOptions.isDiskCacheEnable()) {
                        diskCacheManager.putCache(key, bitmap);
                    }

                    if (imageLoadOptions.isMemoryCacheEnable()) {
                        bitmapMemoryCache.put(key, bitmap);
                    }
                    if (ifFromNet)
                        imageLoadListener.onLoadSuccessful(ImageLoadListener.BitmapSource.NETWORK, bitmap);
                    else
                        imageLoadListener.onLoadSuccessful(ImageLoadListener.BitmapSource.FILE, bitmap);
                    loadState = LoadState.LOAD_FINISHED;
                } else if (result == RESULT_FILE_NOT_EXISTS && imageLoadOptions.hasUrl()
                        && !ifFromNet) {
                    // 加载网络图片
                    loadBitmapFromNet();
                } else {
                    imageLoadListener.onLoadFailed("本地图片文件加载失败！");
                    loadState = LoadState.LOAD_FAILED;
                }
            }
        });
    }

    private void loadBitmapFromNet() {
        loadState = LoadState.LOADING_NET;
        // 开始网络加载
        ImageDownloadListener imageDownloadListener = new ImageDownloadListener() {
            @Override
            public void onProgressChange(String filePath, int totalSize, int currentSize) {
                if (loadState == LoadState.LOAD_CANCELED)
                    return;

                imageLoadListener.onLoadProgressChange(totalSize, currentSize);
            }

            @Override
            public void onDownloadFinish(String filePath, DownloadResult downloadResult) {
                if (loadState == LoadState.LOAD_CANCELED)
                    return;

                imageDownLoadTask = null;
                if (downloadResult == DownloadResult.SUCCESSFUL || downloadResult == DownloadResult.FILE_EXISTS) {
                    loadBitmapFromDisk(true);
                } else {
                    imageLoadListener.onLoadFailed("网络图片加载失败");
                    loadState = LoadState.LOAD_FAILED;
                }
            }
        };

        String filePath = imageLoadOptions.getFilePath();
        String url = imageLoadOptions.getUrl();
        imageDownLoadTask = imageDownloader.download(filePath, url, imageDownloadListener);
        imageLoadListener.onStartDownload(); // 通知， 开始下载了
    }

    public void cancle() {
        if (loadState != LoadState.LOAD_CANCELED && loadState != LoadState.LOAD_FINISHED && loadState != LoadState.LOAD_FAILED) {
            if (loadState == LoadState.LOADING_DISK) {
                loadTask.cancel();
                loadTask = null;
            } else if (loadState == LoadState.LOADING_NET) {
                imageDownLoadTask.cancel();
                imageDownLoadTask = null;
            }
            loadState = LoadState.LOAD_CANCELED;
        }
    }

    /**
     * 获取加载状态
     */
    public LoadState getLoadState() {
        return loadState;
    }

    /**
     * 当前状态
     */
    public enum LoadState {
        LOAD_FINISHED, LOADING_DISK_CACHE, LOADING_DISK, LOADING_NET, LOAD_UNSTART, LOAD_CANCELED, LOAD_FAILED;
    }
}
