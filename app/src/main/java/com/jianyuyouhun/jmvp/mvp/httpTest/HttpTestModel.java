package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;
import com.jianyuyouhun.jmvplib.utils.http.JHttpTask;

/**
 * http测试model
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestModel extends BaseJModelImpl {

    public void doHttpTest(final OnResultListener<String> listener) {
        JHttpClient client = new JHttpTask.ClientBuilder()
                .setUrl("https://jianyuyouhun.com")
                .setMethod(JHttpRequest.METHOD_GET)
                .build();
        new JHttpTask(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        }).execute();
    }
}
