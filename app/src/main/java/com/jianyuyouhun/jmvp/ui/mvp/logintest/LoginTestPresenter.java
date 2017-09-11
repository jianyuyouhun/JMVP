package com.jianyuyouhun.jmvp.ui.mvp.logintest;

import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 登录presenter
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestPresenter extends BaseJPresenter<LoginTestModel, LoginTestView> {

    public void doLogin() {
        if (getJView() == null) {
            return;
        }
        String userName = getJView().getUserName();
        String password = getJView().getPassword();
        if (TextUtils.isEmpty(userName)) {
            getJView().showError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)){
            getJView().showError("请输入密码");
            return;
        }
        if (password.length() < 6) {
            getJView().showError("请输入正确的密码");
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
