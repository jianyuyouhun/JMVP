package com.jianyuyouhun.jmvp.ui.mvp.logintest;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 登录model
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestModel extends BaseJModel<App> {

    @Override
    public void onModelCreate(App app) {
        super.onModelCreate(app);
    }

    public void doLogin(final String userName, final String password, final OnResultListener<String> listener) {
        getSuperHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onResult(OnResultListener.RESULT_SUCCESS, "user: "+userName + "pwd: "+ password + "Login success!");
            }
        }, 3000);
    }
}
