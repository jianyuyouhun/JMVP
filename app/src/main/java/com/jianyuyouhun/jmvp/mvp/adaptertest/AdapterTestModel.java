package com.jianyuyouhun.jmvp.mvp.adaptertest;

import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.http.JHttpClient;
import com.jianyuyouhun.jmvplib.utils.http.JHttpRequest;
import com.jianyuyouhun.jmvplib.utils.http.JHttpTask;

/**
 * 测试model
 * Created by wangyu on 2017/3/17.
 */

public class AdapterTestModel extends BaseJModelImpl {

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        openHandleMsg();
    }

    @Override
    public void handleSuperMsg(Message msg) {
        super.handleSuperMsg(msg);
        Logger.i("model", "消息" + msg.what);
    }

    public void doRequester(final OnResultListener<String> listener) {
        getSuperHandler().sendEmptyMessageDelayed(1, 1000);
        listener.onResult(OnResultListener.RESULT_SUCCESS, "successful");
    }

}
