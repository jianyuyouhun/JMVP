package com.jianyuyouhun.jmvplib.utils.http;

import com.jianyuyouhun.jmvplib.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * 网络请求监听器
 * Created by wangyu on 2017/4/26.
 */

public abstract class JHttpResultListener implements OnHttpResultListener {
    private static final String TAG = "http";

    @Deprecated
    @Override
    public final void onResult(int code, byte[] data) {
        try {
            if (code == HttpURLConnection.HTTP_OK) {
                String result = new String(data, "UTF-8");
                onResult(result);
                Logger.i(TAG, "HTTP_STATUS: " + code + "\n" + result);
            } else {
                onError(code, new Exception("HTTP CODE IS NOT 200"));
            }
        } catch (UnsupportedEncodingException e) {
            onError(code, e);
        }
    }

    public abstract void onResult(String result);
}
