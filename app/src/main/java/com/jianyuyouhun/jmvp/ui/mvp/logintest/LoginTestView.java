package com.jianyuyouhun.jmvp.ui.mvp.logintest;

import com.jianyuyouhun.jmvplib.mvp.BaseJView;

/**
 * 登录测试View
 * Created by wangyu on 2017/5/15.
 */
@Deprecated
public interface LoginTestView extends BaseJView {
    String getUserName();
    String getPassword();
    void onLoginSuccess(String loginMsg);
}
