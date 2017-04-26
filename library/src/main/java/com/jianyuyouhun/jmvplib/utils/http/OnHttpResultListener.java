package com.jianyuyouhun.jmvplib.utils.http;

/**
 * 网络请求接口回调
 * Created by wangyu on 2017/4/26.
 */

public interface OnHttpResultListener {
    int RESULT_CANNOT_OPEN_CONNECTION = -100;
    int RESULT_CANNOT_PARSE_RESPONSE = -101;
    void onResult(int code, byte[] data);
    void onError(int code, Exception e);
}
