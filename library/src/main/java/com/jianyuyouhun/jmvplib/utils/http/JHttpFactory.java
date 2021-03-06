package com.jianyuyouhun.jmvplib.utils.http;

import android.os.Handler;
import android.os.Looper;

import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求运行环境
 * Created by jianyuyouhun on 2017/4/26.
 */

public class JHttpFactory {
    private static class JHttpException extends RuntimeException {

        public JHttpException() {
            super("请在application中初始化jHttpTask");
        }

        public JHttpException(String message) {
            super(message);
        }
    }

    private static JHttpFactory jHttpFactory;

    private Handler mainHandler;
    private Executor executor;

    private static boolean hasInited = false;
    private static final Object lock = new Object();

    private JHttpFactory() {
        hasInited = true;
        mainHandler = new Handler(Looper.getMainLooper());
        executor = new ThreadPoolExecutor(0, 5, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static void init() {
        synchronized (lock) {
            if (!hasInited) {
                jHttpFactory = new JHttpFactory();
            }
        }
    }

    public static JHttpFactory getInstance() {
        if (!hasInited) {
            throw new JHttpException();
        }
        return jHttpFactory;
    }

    public void execute(JHttpClient client, OnResultListener<String> onResultListener) {
        JHttpTask task = new JHttpTask(mainHandler, client, onResultListener);
        executor.execute(task);
    }

    public void execute(JHttpClient client, OnProgressChangeListener onProgressChangeListener) {
        JHttpTask task = new JHttpTask(mainHandler, client, onProgressChangeListener);
        executor.execute(task);
    }

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
