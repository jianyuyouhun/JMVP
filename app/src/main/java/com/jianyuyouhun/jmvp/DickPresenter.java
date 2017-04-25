package com.jianyuyouhun.jmvp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.OnSuperMsgHandlerListener;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;

/**
 * 测试presenter
 * Created by wangyu on 2017/3/17.
 */

public class DickPresenter extends BaseJPresenterImpl<DickModel, DickView> {

    private Handler handler;

    private OnSuperMsgHandlerListener handlerListener = new OnSuperMsgHandlerListener() {
        @Override
        public void onHandleSuperMsg(Message msg) {
            Logger.i("presenter", "消息" + msg.what);
        }
    };

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        handler = JApp.getInstance().getSuperHandler();
        JApp.getInstance().addOnSuperMsgHandlerListener(handlerListener);
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
        JApp.getInstance().removeOnSuperMsgHandlerListener(handlerListener);
    }
}
