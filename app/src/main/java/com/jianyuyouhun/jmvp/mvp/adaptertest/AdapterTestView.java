package com.jianyuyouhun.jmvp.mvp.adaptertest;

import com.jianyuyouhun.jmvplib.mvp.BaseJView;

/**
 * 适配器测试view
 * Created by wangyu on 2017/3/17.
 */

public interface AdapterTestView extends BaseJView {
    void showLoading();
    void hideLoading();
    void onDataSuccess(String s);
}
