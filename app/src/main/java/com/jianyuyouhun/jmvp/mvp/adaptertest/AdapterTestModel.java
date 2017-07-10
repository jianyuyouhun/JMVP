package com.jianyuyouhun.jmvp.mvp.adaptertest;

import android.os.Message;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试model
 * Created by wangyu on 2017/3/17.
 */

public class AdapterTestModel extends BaseJModel<App> {

    @Override
    public void onModelCreate(App app) {
        super.onModelCreate(app);
        openHandleMsg();
    }

    @Override
    public void handleSuperMsg(Message msg) {
        super.handleSuperMsg(msg);
        Logger.i("model", "消息" + msg.what);
    }

    public void doRequester(final OnResultListener<List<String>> listener) {
        getSuperHandler().sendEmptyMessageDelayed(1, 1000);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("测试数据" + i);
        }
        listener.onResult(OnResultListener.RESULT_SUCCESS, list);
    }

}
