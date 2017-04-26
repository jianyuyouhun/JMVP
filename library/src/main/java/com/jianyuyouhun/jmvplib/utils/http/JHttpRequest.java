package com.jianyuyouhun.jmvplib.utils.http;

import android.support.annotation.IntDef;

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
}
