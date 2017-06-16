package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * http测试model
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestModel extends BaseJModelImpl {

    public void doHttpTest(final OnResultListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl("https://jianyuyouhun.com")
                .setMethod(JHttpRequest.METHOD_GET)
                .setParams(params)
                .build();
        JHttpFactory.getInstance().execute(client, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                listener.onResult(result, data);
            }
        });
    }

    public void postTest(final OnResultListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("json", "{\"op_type\":2002,\"task_id\":\"\",\"pid\":5,\"sid\":\"c3395dd46c34fa7fd8d729d8cf88b7a8\",\"user_id\":1003087,\"c_type\":1,\"gender\":3,\"c_ver\":5009,\"doc_id\":\"1004613\"}\n");
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl(" https://test-bj.guijk.com/inquiry/index.php")
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
