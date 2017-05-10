package com.jianyuyouhun.jmvplib.utils.http.executor;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * SyncTask
 * Created by wangyu on 2017/5/10.
 */

public class SimpleSyncTask<T> implements Callable<AsyncResult<T>> {
    private static SingleThreadPool singleThreadPool = new SingleThreadPool(60, TimeUnit.SECONDS, "SingleThreadPool",
            Thread.NORM_PRIORITY - 1);
    private static Handler handler = new Handler(Looper.getMainLooper());

    private AsyncResult<T> mResult = new AsyncResult<T>();

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<T>> execute() {
        return execute(singleThreadPool);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<T>> execute(SingleThreadPool executor) {
        return executor.submit(this);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<T>> execute(ExecutorService executor) {
        return executor.submit(this);
    }

    public AsyncResult<T> call() throws Exception {
        runOnBackground(mResult);
        post();
        return mResult;
    }

    ;

    private void post() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUIThread(mResult);
            }
        });
    }

    /**
     * 该函数在线程中执行
     */
    protected void runOnBackground(AsyncResult<T> asyncResult) {

    }

    /**
     * 该函数在UI线程中执行
     *
     * @param asyncResult {@link #runOnBackground(AsyncResult<T>)}执行后返回的结果
     */
    protected void runOnUIThread(AsyncResult<T> asyncResult) {
    }

}
