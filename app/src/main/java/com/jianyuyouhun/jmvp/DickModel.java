package com.jianyuyouhun.jmvp;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 测试model
 * Created by wangyu on 2017/3/17.
 */

public class DickModel implements BaseJModel {
    public void doRequester(OnResultListener<String> listener) {
        listener.onResult(OnResultListener.RESULT_SUCCESS, "aaaa");
        JApp.getInstance().getSuperHandler().sendEmptyMessageDelayed(1, 1000);
    }
}
