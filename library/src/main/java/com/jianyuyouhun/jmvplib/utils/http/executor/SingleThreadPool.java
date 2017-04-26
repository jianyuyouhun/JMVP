package com.jianyuyouhun.jmvplib.utils.http.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单任务线程池（先进先执行）
 * Created by jianyuyouhun on 2017/4/26.
 */

public class SingleThreadPool implements Executor {
    private ExecutorService mExecutorService;

    public SingleThreadPool() {
        this(60, TimeUnit.SECONDS);
    }

    /**
     * 单任务线程池，默认优先级为（Thread.NORM_PRIORITY - 1）。当线程空闲超过设定时间时会回收线程，有新的任务时再起
     *
     * @param keepAliveTime 允许线程最大空闲时间
     * @param timeUnit      线程空闲时间单位
     */
    public SingleThreadPool(int keepAliveTime, TimeUnit timeUnit) {
        this(keepAliveTime, timeUnit, "SingleThreadPool", Thread.NORM_PRIORITY - 1);
    }

    /**
     * 单任务线程池，当线程空闲超过设定时间时会回收线程，有新的任务时再起
     *
     * @param keepAliveTime  允许线程最大空闲时间
     * @param timeUnit       线程空闲时间单位
     * @param threadName     线程池使用线程的名字，不可为空，可以为任意值，方便DDMS查看线程状态
     * @param threadPriority 线程优先级
     */
    public SingleThreadPool(int keepAliveTime, TimeUnit timeUnit, final String threadName, final int threadPriority) {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable, threadName);
                thread.setPriority(threadPriority);
                return thread;
            }
        };
        LinkedBlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<>();
        mExecutorService = new ThreadPoolExecutor(0, 1, keepAliveTime, timeUnit, linkedBlockingDeque, threadFactory);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        mExecutorService.execute(runnable);
    }

    public <V> Future<V> submit(Callable<V> task) {
        return mExecutorService.submit(task);
    }
}
