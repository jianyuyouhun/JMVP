package com.jianyuyouhun.jmvplib.utils.http;

import android.support.annotation.NonNull;

import com.jianyuyouhun.jmvplib.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;

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
        try {
            httpURLConnection = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(httpRequest.getConnectTimeout());
            httpURLConnection.setReadTimeout(httpRequest.getReadTimeout());
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.addRequestProperty("Content-Type", httpRequest.getContentType());
            httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
            appendHeaders(httpURLConnection, httpRequest.getHeaders());
            httpURLConnection.connect();
            appendParams(httpURLConnection, httpRequest.getParams());
            parseResult();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private void parseResult() {
        if (listener == null) {
            return;
        }
        try {
            int code = httpURLConnection.getResponseCode();
            InputStream inputStream = getInputStream(httpURLConnection, code);
            byte[] data = IOUtils.readInputStream(inputStream);
            inputStream.close();
            listener.onResult(code, data);
        } catch (IOException e) {
            listener.onError(e);
        }
    }

    private static InputStream getInputStream(HttpURLConnection httpURLConnection, int code)
            throws IOException {
        InputStream inputStream;
        if (code == HttpURLConnection.HTTP_OK)
            inputStream = httpURLConnection.getInputStream();
        else
            inputStream = httpURLConnection.getErrorStream();

        String contentEncoding = httpURLConnection.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip"))
            inputStream = new GZIPInputStream(inputStream);
        return inputStream;
    }

    private void appendHeaders(HttpURLConnection httpURLConnection, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private void appendParams(HttpURLConnection httpURLConnection, Map<String, String> params) throws IOException {
        OutputStream outputStream = httpURLConnection.getOutputStream();
        if (params != null)
            outputStream.write(changeParams(params).getBytes("UTF-8"));
        outputStream.close();
    }

    public static String changeParams(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!isFirst)
                stringBuilder.append("&");
            else
                isFirst = false;
            try {
                stringBuilder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
