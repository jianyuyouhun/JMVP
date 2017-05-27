package com.jianyuyouhun.jmvp.mvp.logintest;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 登录model
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestModel extends BaseJModelImpl {

    private LightBroadcast handler;

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        handler = LightBroadcast.getInstance();
    }

    public void doLogin(String userName, String password, final OnResultListener<String> listener) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onResult(OnResultListener.RESULT_SUCCESS, "Login success!");
                handler.sendEmptyMessage(10);
            }
        }, 5000);
    }
}
