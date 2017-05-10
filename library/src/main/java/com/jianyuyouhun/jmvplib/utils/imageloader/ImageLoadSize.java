package com.jianyuyouhun.jmvplib.utils.imageloader;

/**
 * 图片加载的结果参数
 *
 * Created by wangyu on 2017/5/10.
 */
public class ImageLoadSize {
    private int width = -1;
    private int height = -1;
    private ImageScaleType imageScaleType = ImageScaleType.CENTER_INSIDE;

    public ImageLoadSize() {
    }

    public ImageLoadSize(int width, int height, ImageScaleType imageScaleType) {
        this.width = width;
        this.height = height;
        this.imageScaleType = imageScaleType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImageScaleType getImageScaleType() {
        return imageScaleType;
    }

    public void setImageScaleType(ImageScaleType imageScaleType) {
        if (imageScaleType == null) {
            throw new NullPointerException("imageScaleType != null");
        }
        this.imageScaleType = imageScaleType;
    }

    @Override
    public String toString() {
        return "ImageLoadSize [width=" + width + ", height=" + height + ", imageScaleType=" + imageScaleType + "]";
    }
}
