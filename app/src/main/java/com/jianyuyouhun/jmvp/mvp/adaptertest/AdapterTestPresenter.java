package com.jianyuyouhun.jmvp.mvp.adaptertest;

import android.content.Context;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.app.broadcast.OnGlobalMsgReceiveListener;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;

/**
 * 测试presenter
 * Created by wangyu on 2017/3/17.
 */

public class AdapterTestPresenter extends BaseJPresenterImpl<AdapterTestModel, AdapterTestView> {

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

    @Override
    public void beginPresent() {
        final AdapterTestView view = getJView();
        if (view != null) {
            test(view);
        } else {
            testExceptionHandle();
        }
    }

    private void test(final AdapterTestView view) {
        view.showLoading();
        mModel.doRequester(new OnResultListener<String>() {
            @Override
            public void onResult(int result, final String data) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.hideLoading();
                        view.onDataSuccess(data);
                    }
                }, 1000);
            }
        });
    }

    private void testExceptionHandle() {
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
