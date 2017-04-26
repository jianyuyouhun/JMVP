package com.jianyuyouhun.jmvplib.utils.http;

/**
 * 网络请求接口回调
 * Created by wangyu on 2017/4/26.
 */

public interface OnHttpResultListener {
    void onResult(int code, byte[] data);
    void onError(Exception e);
}
