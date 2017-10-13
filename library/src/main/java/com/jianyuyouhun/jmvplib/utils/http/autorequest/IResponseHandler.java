package com.jianyuyouhun.jmvplib.utils.http.autorequest;

public interface IResponseHandler {
    void onResult(int code, byte[] data);

    void onError(Exception exception);
}
