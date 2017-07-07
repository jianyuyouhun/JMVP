package com.jianyuyouhun.jmvp.mvp.adaptertest;

import android.content.Context;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.app.broadcast.OnGlobalMsgReceiveListener;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;

import java.util.List;

/**
 * 测试presenter
 * Created by wangyu on 2017/3/17.
 */

public class AdapterTestPresenter extends BaseJPresenter<AdapterTestModel, AdapterTestView> {

    private LightBroadcast handler;

    private OnGlobalMsgReceiveListener handlerListener = new OnGlobalMsgReceiveListener() {
        @Override
        public void onReceiveGlobalMsg(Message msg) {
            Logger.i("presenter", "消息" + msg.what);
        }
    };

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        handler = LightBroadcast.getInstance();
        handler.addOnGlobalMsgReceiveListener(handlerListener);
    }

    public void test() {
        if (!isAttach()) {
            throw new RuntimeException("view已经为空了");
        }
        getJView().showLoading();
        mModel.doRequester(new OnResultListener<List<String>>() {
            @Override
            public void onResult(int result, final List<String> data) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJView().hideLoading();
                        getJView().onDataSuccess(data, "success");
                    }
                }, 1000);
            }

        });
    }

    public void testExceptionHandle() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("JView 不能为空");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeOnGlobalMsgReceiveListener(handlerListener);
    }
}
