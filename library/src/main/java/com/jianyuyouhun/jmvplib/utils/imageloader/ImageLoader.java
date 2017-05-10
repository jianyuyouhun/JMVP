package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.content.Context;

/**
 * ImageLoader基类
 * Created by wangyu on 2017/5/10.
 */

public abstract class ImageLoader {
    public static ImageLoader imageLoader;

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            imageLoader = new ImageLoaderImpl();
        } else if (!imageLoader.isInit()) {
            throw new IllegalStateException("ImageLoader未初始化，请在Application.onCreate()中调用init方法初始化");
        }
        return imageLoader;
    }

    /**
     * 初始化配置
     *
     * @param memoryCacheSize       内存缓存大小
     * @param diskCacheSize         硬盘缓存大小
     * @param diskCacheDir          硬盘缓存路径
     * @param downloadThreadSize    下载线程池大小
     */
    public abstract void init(int memoryCacheSize, int diskCacheSize, String diskCacheDir, int downloadThreadSize);

    /**
     * 默认初始化配置
     */
    public abstract void init(Context context);

    /**
     * 加载图片
     *
     * @param imageLoadOptions  图片加载参数
     * @param imageLoadListener 图片加载结果监听器
     */
    public abstract ImageLoadTask loadImage(ImageLoadOptions imageLoadOptions, ImageLoadListener imageLoadListener);

    public abstract boolean isInit();

    /**
     * 获取内存缓存已使用的大小，单位字节
     */
    public abstract int getMemoryCacheSize();

    /**
     * 获取硬盘（缩略图）缓存已使用的大小，单位字节
     */
    public abstract int getDiskCacheSize();

    /** 清除缓存 */
    public abstract void clear(DiskCacheImageLoader.OnClearCacheListener onClearCacheListener);

    /**
     * 移除 文件缓存
     *
     * @param filePath 文件路径， 包括缩略图缓存和内存缓存
     */
    public abstract void removeCache(String filePath);
}
