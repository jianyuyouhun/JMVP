/*
 * Copyright (c) 2014-2015 zengdexing.com
 */
package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 * Created by wangyu on 2017/5/10.
 */
public class BitmapUtils {
    private BitmapUtils() {
        throw new AssertionError();
    }

    /**
     * 解码 图片
     * <p>
     * 该方法会解码原图，如果原图很大，将不能加载，建议使用{@link #decodeFile(String, int, int)}
     * </p>
     *
     * @param pathName 图片本地文件名
     * @return 加载成功将返回加载好的Bitmap，加载失败返回null
     */
    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 解码 图片
     *
     * @param pathName  图片的绝对路径。
     * @param reqWidth  期望输出的图片最小宽度。<strong>该值不能为0或者小于0。</strong>
     * @param reqHeight 期望输出的图片最小高度。<strong>该值不能为0或者小于0。</strong>
     * @return 加载成功将返回Bitmap，加载失败返回null。
     */
    public static Bitmap decodeFile(String pathName, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, opt);
            opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);
            opt.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(pathName, opt);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据图片大小计算缩放比例
     *
     * @param options   图片参数
     * @param reqWidth  输出宽度
     * @param reqHeight 输出高度
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 裁剪图片
     *
     * @param src            原始图片
     * @param reqWidth       新的图片宽度
     * @param reqHeight      新的图片高度
     * @param imageScaleType 图片缩放模式
     * @return 裁剪好的图片
     */
    public static Bitmap scaleBitmap(Bitmap src, int reqWidth, int reqHeight, ImageScaleType imageScaleType)
            throws Exception {

        float scaleX;
        float scaleY;
        float srcWidth = src.getWidth();
        float srcHeight = src.getHeight();
        float newWidth;
        float newHeight;

        switch (imageScaleType) {
            case CENTER_CROP:
                if (srcWidth / srcHeight > (float) reqWidth / reqHeight) {
                    scaleY = (float) reqHeight / srcHeight;
                } else {
                    scaleY = (float) reqWidth / srcWidth;
                }
                if (scaleY > 1.0f) {
                    newWidth = reqWidth / scaleY;
                    newHeight = reqHeight / scaleY;
                    scaleX = scaleY = 1.0f;
                } else {
                    scaleX = scaleY;
                    newWidth = reqWidth;
                    newHeight = reqHeight;
                }
                break;

            case CENTER_INSIDE:
                if (srcWidth / srcHeight < (float) reqWidth / reqHeight) {
                    scaleY = (float) reqHeight / srcHeight;
                } else {
                    scaleY = (float) reqWidth / srcWidth;
                }

                // 不对图片进行放大，占用过多内存
                if (scaleY > 1.0f)
                    scaleY = 1f;

                scaleX = scaleY;
                newWidth = (int) (srcWidth * scaleX);
                newHeight = (int) (srcHeight * scaleY);
                break;

            case FIT_XY:
                scaleX = reqWidth / srcWidth;
                scaleY = reqHeight / srcHeight;
                newWidth = reqWidth;
                newHeight = reqHeight;
                break;

            default:
                throw new Exception("不支持的缩放模式：" + imageScaleType);
        }

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        matrix.postTranslate(-(srcWidth * scaleX - newWidth) / 2, -(srcHeight * scaleY - newHeight) / 2);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        try {
            Config config = src.getConfig();
            if (config == null) {
                config = Config.RGB_565;
            }
            Bitmap bitmap = Bitmap.createBitmap((int) newWidth, (int) newHeight, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, matrix, paint);
            return bitmap;
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

    /**
     * 将Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获得圆角图片的方法
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) throws Exception {
        if (bitmap == null) {
            throw new NullPointerException();
        }

        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (OutOfMemoryError e) {
            throw new Exception("getRoundedCornerBitmap：图片圆角内存溢出了！", e);
        }
    }

    /**
     * 保存图像
     */
    public static boolean saveImage(Bitmap aBitmap, String aStrFileName, int aQuality) {
        if (aBitmap == null) {
            return false;
        }
        boolean result = false;
        FileOutputStream fo = null;
        try {
            if (aQuality > 100 || aQuality < 0)
                aQuality = 90;

            fo = new FileOutputStream(aStrFileName);
            result = aBitmap.compress(Bitmap.CompressFormat.JPEG, aQuality, fo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
