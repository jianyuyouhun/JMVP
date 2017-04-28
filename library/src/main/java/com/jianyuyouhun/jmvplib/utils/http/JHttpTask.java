package com.jianyuyouhun.jmvplib.utils.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * http请求runnable
 * Created by wangyu on 2017/4/28.
 */

public class JHttpTask implements Runnable {
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private JHttpClient client;
    private OnResultListener<String> onResultListener;
    private OnProgressChangeListener onProgressChangeListener;

    public JHttpTask(JHttpClient client, @NonNull OnResultListener<String> onResultListener) {
        this.client = client;
        this.onResultListener = onResultListener;
        this.client.setJHttpResultListener(jHttpResultListener);
    }

    public JHttpTask(JHttpClient client, @NonNull OnProgressChangeListener onProgressChangeListener) {
        this.client = client;
        this.onProgressChangeListener = onProgressChangeListener;
        this.client.setOnProgressChangeListener(onThreadProgressChangeListener);
    }

    @Override
    public void run() {
        client.execute();
    }

    private JHttpResultListener jHttpResultListener = new JHttpResultListener() {
        @Override
        public void onResult(final String result) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResultListener.onResult(OnResultListener.RESULT_SUCCESS, result);
                }
            });
        }

        @Override
        public void onError(final int code, final Exception e) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResultListener.onResult(OnResultListener.RESULT_FAILED, e.getMessage());
                }
            });
        }
    };

    private OnProgressChangeListener onThreadProgressChangeListener = new OnProgressChangeListener() {
        @Override
        public void onStart() {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onProgressChangeListener.onStart();
                }
            });
        }

        @Override
        public void onProgressChanged(final int current, final int total) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onProgressChangeListener.onProgressChanged(current, total);
                }
            });
        }

        @Override
        public void onFinish(final String result) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onProgressChangeListener.onFinish(result);
                }
            });
        }

        @Override
        public void onError(final int code, final Exception e) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onProgressChangeListener.onError(code, e);
                }
            });
        }
    };

}
