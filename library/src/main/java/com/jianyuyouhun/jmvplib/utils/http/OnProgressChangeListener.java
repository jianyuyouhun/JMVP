package com.jianyuyouhun.jmvplib.utils.http;

/**
 * 网络请求进度监听
 * Created by wangyu on 2017/4/27.
 */

public interface OnProgressChangeListener {
    void onStart();
    void onProgressChanged(int current, int total);
    void onFinish(String result);
    void onError(int code, Exception e);
}
