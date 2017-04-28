package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * http测试model
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestModel extends BaseJModelImpl {

    private static final String testUrl = "https://test-api.guijk.com/registration/patient/list";
    private static final String jsonContent = "{\"op_type\":3016,\"task_id\":\"\",\"pid\":5,\"sid\":\"1ef91c212e30e14bf125e9374262401f\",\"user_id\":1003087,\"c_type\":1,\"gender\":3,\"c_ver\":5008}";
    public void doHttpTest(final OnResultListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("json", jsonContent);
        JHttpClient client = new JHttpFactory.ClientBuilder()
                .setUrl(testUrl)
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
