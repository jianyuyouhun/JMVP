package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;

import java.util.Map;

/**
 * http测试model
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestModel extends BaseJModelImpl {

    public void doGet(String url, final OnResultListener<String> listener) {
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl(url)
                .setMethod(JHttpRequest.METHOD_GET)
                .build();
        JHttpFactory.getInstance().execute(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        });
    }

    public void doPost(String url, Map<String, String> params, final OnResultListener<String> listener) {
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl(url)
                .setMethod(JHttpRequest.METHOD_POST)
                .setParams(params)
                .build();
        JHttpFactory.getInstance().execute(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        });
    }
}
