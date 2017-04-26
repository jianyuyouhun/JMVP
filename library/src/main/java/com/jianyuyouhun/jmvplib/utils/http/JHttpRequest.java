package com.jianyuyouhun.jmvplib.utils.http;

import android.support.annotation.IntDef;

import java.util.Map;

/**
 * http请求头
 * Created by wangyu on 2017/4/26.
 */

public class JHttpRequest {

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;

    @IntDef({METHOD_GET, METHOD_POST})
    public @interface HttpMethod {}

    private String url;
    private String extra;
    @HttpMethod
    private int method = METHOD_GET;
    private int readTimeout = 5000;
    private int connectTimeout = 10000;
    private String contentType = "application/x-www-form-urlencoded; charset=UTF-8";// 拉取数据的时候默认, 上传时为application/octet-stream
    private Map<String, String> params;
    private Map<String, String> headers;

    public String getUrl() {
        if (extra != null) {
            url = url + "?" + extra;
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @HttpMethod
    public int getMethod() {
        return method;
    }

    public void setMethod(@HttpMethod int method) {
        this.method = method;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
