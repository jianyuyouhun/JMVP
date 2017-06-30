package com.jianyuyouhun.jmvplib.utils.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.utils.FileUtils;
import com.jianyuyouhun.jmvplib.utils.IOUtils;
import com.jianyuyouhun.jmvplib.utils.Logger;

import java.io.DataOutputStream;
import java.io.FileInputStream;
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
    private OnProgressChangeListener onProgressChangeListener;

    private JHttpClient() {}

    public JHttpClient(@NonNull JHttpRequest request) {
        this.httpRequest = request;
    }

    public void execute() {
        switch (httpRequest.getMethod()) {
            case JHttpRequest.METHOD_GET:
                doGet();
                break;
            case JHttpRequest.METHOD_POST:
                doPost();
                break;
            case JHttpRequest.METHOD_UPLOAD:
                doUpload();
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
            if (listener != null) {
                listener.onError(OnHttpResultListener.RESULT_CANNOT_OPEN_CONNECTION, e);
            } else {
                Logger.e(OnHttpResultListener.TAG, "网络请求回调监听为空");
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
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            appendHeaders(httpURLConnection, httpRequest.getHeaders());
            httpURLConnection.connect();
            appendParams(httpURLConnection, httpRequest.getParams());
            parseResult();
        } catch (IOException e) {
            if (listener != null) {
                listener.onError(OnHttpResultListener.RESULT_CANNOT_OPEN_CONNECTION, e);
            } else {
                Logger.e(OnHttpResultListener.TAG, "网络请求回调监听为空");
            }
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private void doUpload() {
        if (onProgressChangeListener == null) {
            Logger.e(OnHttpResultListener.TAG, "进度监听为空");
            return;
        }
        onProgressChangeListener.onStart();
        String filePath = httpRequest.getFilePath();
        if (TextUtils.isEmpty(filePath)) {
            onProgressChangeListener.onError(OnHttpResultListener.RESULT_CANNOT_OPEN_CONNECTION,
                    new UploadException("filePath为空！"));
        } else {
            try {
                String url = httpRequest.getUrl();
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setConnectTimeout(httpRequest.getConnectTimeout());
                httpURLConnection.setReadTimeout(httpRequest.getReadTimeout());
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Live");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
                httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
                appendHeaders(httpURLConnection, httpRequest.getHeaders());
                httpURLConnection.connect();
                appendParams(httpURLConnection, httpRequest.getParams());
                DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
                FileInputStream fStream = new FileInputStream(filePath);
                byte[] fileByte = new byte[1024];
                int length = -1;
                int totalLength = httpURLConnection.getContentLength();
                int currentSize = 0;
                long lastNotify = System.currentTimeMillis();
                while ((length = fStream.read(fileByte)) != -1) {
                    outStream.write(fileByte, 0, length);
                    currentSize+=length;
                    if ((System.currentTimeMillis() - lastNotify) > 300) {
                        onProgressChangeListener.onProgressChanged(currentSize, totalLength);
                        lastNotify = System.currentTimeMillis();
                    }
                }
                fStream.close();
                outStream.flush();
                outStream.close();
                parseUploadResult();
            } catch (IOException e) {
                onProgressChangeListener.onError(OnHttpResultListener.RESULT_CANNOT_OPEN_CONNECTION, e);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }

    private void parseUploadResult() {
        try {
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String m_strResult = null;
                InputStream iStream = null;
                iStream = httpURLConnection.getInputStream();
                String m_strContentEncoding = httpURLConnection.getContentEncoding();
                if (m_strContentEncoding != null && m_strContentEncoding.contains("gzip")) {
                    GZIPInputStream gzipIs = new GZIPInputStream(iStream);
                    m_strResult = FileUtils.getStringByStream(gzipIs);
                } else {
                    m_strResult = FileUtils.getStringByStream(iStream);
                }
                onProgressChangeListener.onFinish(m_strResult);
            } else {
                onProgressChangeListener.onError(httpURLConnection.getResponseCode(),
                        new UploadException("上传结果异常"));
            }
        } catch (IOException e) {
            onProgressChangeListener.onError(OnHttpResultListener.RESULT_CANNOT_PARSE_RESPONSE, e);
        }
    }

    private void parseResult() {
        if (listener == null) {
            Logger.e(OnHttpResultListener.TAG, "网络请求回调监听为空");
            return;
        }
        try {
            int code = httpURLConnection.getResponseCode();
            InputStream inputStream = getInputStream(httpURLConnection, code);
            if (inputStream == null) {
                listener.onResult(code, null);
            } else {
                byte[] data = IOUtils.readInputStream(inputStream);
                inputStream.close();
                listener.onResult(code, data);
            }
        } catch (IOException e) {
            listener.onError(OnHttpResultListener.RESULT_CANNOT_PARSE_RESPONSE, e);
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

    /**
     * 该步骤位于子线程，请使用HttpTask来操作
     * @param listener          回调监听
     */
    @Deprecated
    public void setJHttpResultListener(@NonNull JHttpResultListener listener) {
        this.listener = listener;
    }

    /**
     * 该步骤位于子线程，请使用HttpTask来操作
     * @param onProgressChangeListener      进度回调
     */
    @Deprecated
    public void setOnProgressChangeListener(@NonNull OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
