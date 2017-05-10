package com.jianyuyouhun.jmvplib.utils.imageloader;

import android.graphics.Bitmap;

import com.jianyuyouhun.jmvplib.utils.http.executor.AsyncResult;
import com.jianyuyouhun.jmvplib.utils.http.executor.SimpleSyncTask;
import com.jianyuyouhun.jmvplib.utils.http.executor.SingleThreadPool;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiskImageLoader {
    private SingleThreadPool executorService;

    public DiskImageLoader() {
        this(new SingleThreadPool());
    }

    public DiskImageLoader(SingleThreadPool executorService) {
        this.executorService = executorService;
    }

    /**
     * 加载本地图片
     *
     * @param imageLoadOptions     加载参数
     * @param onLoadResultListener 加载结果监听器
     * @return 加载任务
     */
    public LoadTask loadBitmap(ImageLoadOptions imageLoadOptions, OnLoadResultListener onLoadResultListener) {
        DiskLoadTask diskLoadTask = new DiskLoadTask(imageLoadOptions, onLoadResultListener);
        diskLoadTask.future = diskLoadTask.execute(executorService);
        ;
        return diskLoadTask;
    }

    /**
     * 加载结果监听器
     *
     */
    public static interface OnLoadResultListener {
        /** LOAD_FAILED */
        public static final int RESULT_LOAD_FAILED = -1;
        /** 加载成功 */
        public static final int RESULT_LOAD_SUCCESSFUL = 1;
        /** 要加载的文件不存在 */
        public static final int RESULT_FILE_NOT_EXISTS = -2;

        /**
         * 加载结果
         *
         * @param result 1表示加载成功
         * @param bitmap 加载成功的图片，当且仅当result=1时，该值有意义
         */
        void onLoadResult(int result, Bitmap bitmap);
    }

    public static interface LoadTask {
        public void cancel();

        public Bitmap get();
    }

    private class DiskLoadTask extends SimpleSyncTask<Bitmap> implements LoadTask {
        public AtomicBoolean isCancel = new AtomicBoolean(false);
        private ImageLoadOptions imageLoadOptions;
        private OnLoadResultListener onLoadResultListener;
        private Future<AsyncResult<Bitmap>> future;

        public DiskLoadTask(ImageLoadOptions imageLoadOptions, OnLoadResultListener onLoadResultListener) {
            super();
            this.imageLoadOptions = imageLoadOptions;
            this.onLoadResultListener = onLoadResultListener;
        }

        @Override
        protected void runOnBackground(AsyncResult<Bitmap> asyncResult) {
            if (isCancel.get()) {
                return;
            }

            File file = new File(imageLoadOptions.getFilePath());
            if (file.exists()) {
                // 文件存在，开始加载
                int width = imageLoadOptions.getImageLoadSize().getWidth();
                int height = imageLoadOptions.getImageLoadSize().getHeight();

                Bitmap bitmap;
                if (width > 0 || height > 0) {
                    bitmap = BitmapUtils.decodeFile(file.getAbsolutePath(), width, height);
                    if (isCancel.get()) {
                        return;
                    }
                    if (bitmap != null) {
                        try {
                            ImageLoadSize imageLoadSize = imageLoadOptions.getImageLoadSize();
                            bitmap = BitmapUtils.scaleBitmap(bitmap, width, height, imageLoadSize.getImageScaleType());
                        } catch (Exception e) {
                            e.printStackTrace();
                            bitmap = null;
                        }
                    }
                } else {
                    bitmap = BitmapUtils.decodeFile(imageLoadOptions.getFilePath());
                }
                if (bitmap == null) {
                    asyncResult.setResult(OnLoadResultListener.RESULT_LOAD_FAILED);
                } else {
                    asyncResult.setResult(OnLoadResultListener.RESULT_LOAD_SUCCESSFUL);
                    asyncResult.setData(bitmap);
                }
            } else {
                // 文件不存在
                asyncResult.setResult(OnLoadResultListener.RESULT_FILE_NOT_EXISTS);
            }
        }

        @Override
        protected void runOnUIThread(AsyncResult<Bitmap> asyncResult) {
            if (!isCancel.get()) {
                onLoadResultListener.onLoadResult(asyncResult.getResult(), asyncResult.getData());
            }
        }

        @Override
        public void cancel() {
            isCancel.set(true);
        }

        @Override
        public Bitmap get() {
            try {
                return future.get().getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
