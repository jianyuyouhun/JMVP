package com.jianyuyouhun.jmvplib.utils.task;

import android.os.Handler;
import android.os.Looper;

/**
 * ui线程执行操作
 * Created by wangyu on 2017/5/10.
 */

public abstract class UIThreadTask {
    private static Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUIThread();
        }
    };

    /**
     * 提交任务到UI线程中执行
     */
    public void execute() {
        handler.post(runnable);
    }

    protected abstract void runOnUIThread();
}
