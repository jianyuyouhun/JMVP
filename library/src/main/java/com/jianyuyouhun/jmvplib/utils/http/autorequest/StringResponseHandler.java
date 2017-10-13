package com.jianyuyouhun.jmvplib.utils.http.autorequest;

import java.io.UnsupportedEncodingException;

public abstract class StringResponseHandler implements IResponseHandler {
    @Deprecated
    @Override
    public final void onResult(int code, byte[] data) {
        try {
            String string = new String(data, "UTF-8");
            onResult(code, string);
        } catch (UnsupportedEncodingException e) {
            onError(e);
        }
    }

    public abstract void onResult(int code, String content);
}
