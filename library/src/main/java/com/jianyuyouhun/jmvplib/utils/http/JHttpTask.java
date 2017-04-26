package com.jianyuyouhun.jmvplib.utils.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.net.HttpURLConnection;
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
    private OnResultListener<String> listener;
    private Runnable httpRunnable;
    public JHttpTask(JHttpClient client, OnResultListener<String> listener) {
        this.client = client;
        this.listener = listener;
        client.setJHttpResultListener(jHttpResultListener);
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
                    listener.onResult(OnResultListener.RESULT_SUCCESS, result);
                }
            });
        }

        @Override
        public void onError(final int code, final Exception e) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onResult(OnResultListener.RESULT_FAILED, e.getMessage());
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
        private String contentType;
        private JHttpRequest jHttpRequest;
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
        public ClientBuilder setContentType(String contentType) {
            this.contentType = contentType;
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
            if (!TextUtils.isEmpty(contentType)) {
                request.setContentType(contentType);
            }
            return new JHttpClient(request);
        }
    }
}
