package com.jianyuyouhun.jmvplib.utils.http;

import android.os.Handler;
import android.os.Looper;

import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 网络请求运行环境
 * Created by jianyuyouhun on 2017/4/26.
 */

public class JHttpTask {
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newCachedThreadPool();
    private JHttpClient client;
    private OnResultListener<String> onResultListener;
    private OnProgressChangeListener onProgressChangeListener;
    private Runnable httpRunnable;

    public JHttpTask(JHttpClient client) {
        this.client = client;
    }
    public JHttpTask(JHttpClient client, OnResultListener<String> onResultListener) {
        this.client = client;
        this.onResultListener = onResultListener;
        client.setJHttpResultListener(jHttpResultListener);
        initHttpRunnable();
    }

    public JHttpTask(JHttpClient client, OnProgressChangeListener onProgressChangeListener) {
        this.client = client;
        this.onProgressChangeListener = onProgressChangeListener;
        client.setOnProgressChangeListener(onThreadProgressChangeListener);
        initHttpRunnable();
    }


    private void initHttpRunnable() {
        httpRunnable = new Runnable() {
            @Override
            public void run() {
                client.execute();
            }
        };
    }

    public void execute() {
        executor.execute(httpRunnable);
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

    public static class ClientBuilder {
        private String url;
        private String extra;
        private Map<String, String> headers;
        private Map<String, String> params;
        @JHttpRequest.HttpMethod
        private int method = JHttpRequest.METHOD_GET;
        private int readTimeout = 5000;
        private int connectTimeout = 10000;
        private JHttpRequest jHttpRequest;
        private String filePath;
        public ClientBuilder() {
            if (jHttpRequest == null) {
                jHttpRequest = new JHttpRequest();
            }
        }
        public ClientBuilder setUrl(String url) {
            this.url = url;
            return this;
        }
        public ClientBuilder setExtra(String extra) {
            this.extra = extra;
            return this;
        }
        public ClientBuilder setMethod(@JHttpRequest.HttpMethod int method) {
            this.method = method;
            return this;
        }
        public ClientBuilder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }
        public ClientBuilder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }
        public ClientBuilder setTimeOut(int readTimeout, int connectTimeout) {
            this.readTimeout = readTimeout;
            this.connectTimeout = connectTimeout;
            return this;
        }
        public ClientBuilder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }
        public JHttpClient build() {
            JHttpRequest request = new JHttpRequest();
            request.setUrl(url);
            request.setExtra(extra);
            request.setHeaders(headers);
            request.setParams(params);
            request.setMethod(method);
            request.setReadTimeout(readTimeout);
            request.setConnectTimeout(connectTimeout);
            request.setFilePath(filePath);
            return new JHttpClient(request);
        }
    }
}
