package com.jianyuyouhun.jmvplib.utils.http;

import android.support.annotation.NonNull;

import com.jianyuyouhun.jmvplib.utils.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求
 * Created by wangyu on 2017/4/26.
 */

public class JHttpClient {
    private JHttpRequest httpRequest;
    private HttpURLConnection httpURLConnection;
    private JHttpResultListener listener;

    private JHttpClient() {}

    public JHttpClient(@NonNull JHttpRequest request) {
        this.httpRequest = request;
    }

    public JHttpClient(@NonNull JHttpResultListener listener) {
        this.listener = listener;
    }

    public void execute() {
        switch (httpRequest.getMethod()) {
            case JHttpRequest.METHOD_GET:
                doGet();
                break;
            case JHttpRequest.METHOD_POST:
                doPost();
                break;
        }
    }

    private void doGet() {
        try {
            httpURLConnection = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(httpRequest.getConnectTimeout());
            httpURLConnection.setReadTimeout(httpRequest.getReadTimeout());
            httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
            httpURLConnection.connect();
            parseResult();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e);
            }
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }

    private void doPost() {

    }

    private void parseResult() {
        if (listener == null) {
            return;
        }

    }

}
