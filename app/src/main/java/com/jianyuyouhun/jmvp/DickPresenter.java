package com.jianyuyouhun.jmvp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;

/**
 * 测试presenter
 * Created by wangyu on 2017/3/17.
 */

public class DickPresenter extends BaseJPresenterImpl<DickModel, DickView> {

    private Handler handler;

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        openHandleMsg();
    }

    @Override
    public void onPresenterCreate(JApp app) {
        handler = app.getSuperHandler();
    }

    @Override
    public void handleSuperMsg(Message msg) {
        super.handleSuperMsg(msg);
        switch (msg.what) {
            case 1:
                Logger.i(TAG, "收到了全局消息");
                break;
        }
    }

    @Override
    public void beginPresent() {
        final DickView view = getJView();
        if (view != null) {
            test(view);
        } else {
            testExceptionHandle();
        }
    }

    private void test(final DickView view) {
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
                }, 4000);
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

}
