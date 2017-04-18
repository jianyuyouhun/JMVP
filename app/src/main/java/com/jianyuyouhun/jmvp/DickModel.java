package com.jianyuyouhun.jmvp;

import android.os.Message;
import android.util.Log;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 测试model
 * Created by wangyu on 2017/3/17.
 */

public class DickModel extends BaseJModelImpl {

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        openHandleMsg();
    }

    @Override
    public void handleSuperMsg(Message msg) {
        super.handleSuperMsg(msg);
        Log.i("model", "消息" + msg.what);
    }

    public void doRequester(OnResultListener<String> listener) {
        listener.onResult(OnResultListener.RESULT_SUCCESS, "aaaa");
        getSuperHandler().sendEmptyMessageDelayed(1, 1000);
    }
}
