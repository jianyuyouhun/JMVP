package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.mvp.logintest.LoginTestModel;
import com.jianyuyouhun.jmvp.mvp.logintest.LoginTestPresenter;
import com.jianyuyouhun.jmvp.mvp.logintest.LoginTestView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

/**
 * 登录测试activity
 * Created by wangyu on 2017/5/15.
 */

public class LoginTestActivity extends BaseMVPActivity<LoginTestPresenter, LoginTestModel> implements LoginTestView {

    @FindViewById(R.id.username)
    private EditText mUsernameText;

    @FindViewById(R.id.password)
    private EditText mPasswordText;

    @FindViewById(R.id.login_btn)
    private Button mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerListener();
    }

    private void registerListener() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doLogin();
            }
        });
    }

    @NonNull
    @Override
    protected LoginTestPresenter getPresenter() {
        return new LoginTestPresenter();
    }

    @NonNull
    @Override
    protected LoginTestModel initModel() {
        return App.getInstance().getJModel(LoginTestModel.class);
    }

    @NonNull
    @Override
    protected LoginTestPresenter bindModelAndView() {
        mPresenter.onBindModelView(mModel, this);
        return mPresenter;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login_test;
    }

    @Override
    public void showError(String error) {
        showToast(error);
    }

    @Override
    public String getUserName() {
        String userName = mUsernameText.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            showError("请输入用户名");
        }
        return userName;
    }

    @Override
    public String getPassword() {
        String password = mPasswordText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
        }
        if (password.length() < 6) {
            showToast("请输入正确的密码");
        }
        return password;
    }

    @Override
    public void onLoginSuccess(String loginMsg) {
        showToast(loginMsg);
    }
}
