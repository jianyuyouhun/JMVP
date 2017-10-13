package com.jianyuyouhun.jmvplib.utils.http.autorequest;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求基类
 * Created by wangyu on 2017/10/11.
 */

public class AutoHttpRequester {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_NO_DATA = -8;
    public static final int RESULT_EXCEPTION = -100; // 请求发生异常，请求失败

    public static final int POST = 1;
    public static final int GET = 2;

    private Map<String, String> params = new HashMap<>();

    private String url = "";

    @Method
    private int requestMethod = POST;

    @Nullable
    private AutoResultListener autoResultListener;

    private IResponseHandler handler = new StringResponseHandler() {
        @Override
        public void onResult(int code, String content) {
            log("response GET code = %d, content = %s", code, content);
            if (code == 200) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    int state = jsonObject.optInt("error", RESULT_SUCCESS);
                    if (autoResultListener != null) {
                        autoResultListener.onResult(state, jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (autoResultListener != null) {
                        autoResultListener.onResult(RESULT_EXCEPTION, null);
                    }
                }
            } else {
                if (autoResultListener != null) {
                    autoResultListener.onResult(RESULT_EXCEPTION, null);
                }
            }
        }

        @Override
        public void onError(Exception exception) {
            exception.printStackTrace();
            if (autoResultListener != null) {
                autoResultListener.onResult(RESULT_EXCEPTION, null);
            }
        }
    };

    public AutoHttpRequester(String url) {
        this(url, true);
    }

    /**
     * WEB请求
     *
     * @param url                请求url
     * @param needPutPublicParam 是否需要放入公共参数
     */
    public AutoHttpRequester(String url, boolean needPutPublicParam) {
        this.url = url;
        if (needPutPublicParam) {
            putPublicParam();
        }
    }

    /**
     * 放入参数到请求中
     *
     * @param key   参数的key
     * @param value 参数值，为空时移除该参数
     */
    public AutoHttpRequester putParam(String key, Object value) {
        if (value == null) {
            params.remove(key);
        } else {
            params.put(key, value.toString());
        }
        return this;
    }

    /**
     * 批量加入参数
     * @param params
     * @return
     */
    public AutoHttpRequester putParamsMap(Map<String, Object> params) {
        return putParamsMap(params, false);
    }

    /**
     * 批量加入参数
     * @param params        参数map
     * @param isReplace     是否完全替换
     * @return  AutoHttpRequester实例
     */
    public AutoHttpRequester putParamsMap(Map<String, Object> params, boolean isReplace) {
        if (isReplace) {
            params.clear();
        }
        for (String key : params.keySet()) {
            putParam(key, params.get(key));
        }
        return this;
    }

    /**
     * 设置请求模式
     * @param method    get/post
     * @return  AutoHttpRequester实例
     */
    public AutoHttpRequester setMethod(@Method int method) {
        this.requestMethod = method;
        return this;
    }

    /**
     * 放入公共参数
     */
    protected void putPublicParam() {

    }

    public void execute(@NonNull AutoResultListener autoResultListener) {
        this.autoResultListener = autoResultListener;
        switch (requestMethod) {
            case GET:
                doGet();
                break;
            case POST:
                doPost();
                break;
        }
    }

    private void doGet() {
        HttpClient.get(url, params, handler);
    }

    private void doPost() {
        HttpClient.post(url, params, handler);
    }

    private void log(String msg, Object... args) {
        if (args.length > 0) {
            Logger.d("HTTP", String.format(msg, args));
        } else {
            Logger.d("HTTP", msg);
        }
    }

    @IntDef({POST, GET})
    @interface Method{}
}
