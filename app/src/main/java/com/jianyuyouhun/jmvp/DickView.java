package com.jianyuyouhun.jmvp;

import com.jianyuyouhun.jmvplib.mvp.BaseJView;

/**
 * Created by wangyu on 2017/3/17.
 */

public interface DickView extends BaseJView {
    void showLoading();
    void hideLoading();
    void onDataSuccess(String s);
}
