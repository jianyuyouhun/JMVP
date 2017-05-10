package com.jianyuyouhun.jmvplib.utils.http.executor;

import android.os.Bundle;

/**
 * 异步加载结果
 *
 * @param <T>
 * Created by wangyu on 2017/5/10.
 */
public class AsyncResult<T> {
    private int result = 0;
    private T data;
    private Bundle bundle = new Bundle();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getString(String key, String defaultValue) {
        return bundle.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        bundle.putString(key, value);
    }

    public int getInt(String key, int defaultValue) {
        return bundle.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        bundle.putInt(key, value);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
