package com.jianyuyouhun.jmvp.mvp.logintest;

import android.os.Handler;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 登录model
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestModel extends BaseJModelImpl {

    private Handler handler;

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        handler = app.getSuperHandler();
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
