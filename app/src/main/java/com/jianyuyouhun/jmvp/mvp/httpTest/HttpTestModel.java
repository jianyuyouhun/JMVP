package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;

/**
 * http测试model
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestModel extends BaseJModelImpl {

    private static final String testUrl = "https://jianyuyouhun.com";
    public void doHttpTest(final OnResultListener<String> listener) {
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl(testUrl)
                .setMethod(JHttpRequest.METHOD_GET)
                .build();
        JHttpFactory.getInstance().execute(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        });
    }
}
