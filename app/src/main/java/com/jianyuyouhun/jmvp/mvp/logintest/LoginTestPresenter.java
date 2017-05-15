package com.jianyuyouhun.jmvp.mvp.logintest;

import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 登录presenter
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestPresenter extends BaseJPresenterImpl<LoginTestModel, LoginTestView> {

    @Override
    public void beginPresent() {

    }

    public void doLogin() {
        if (getJView() == null) {
            return;
        }
        String userName = getJView().getUserName();
        String password = getJView().getPassword();
        if (TextUtils.isEmpty(userName)) {
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return;
        }
        mModel.doLogin(userName, password, new OnResultListener<String>() {
            @Override
            public void onResult(int result, String data) {
                if (result == RESULT_SUCCESS) {
                    mView.onLoginSuccess(data);
                } else {
                    mView.showError("login failed");
                }
            }
        });
    }
}
