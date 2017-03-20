package com.jianyuyouhun.jmvp;

import android.os.Handler;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * 测试presenter
 * Created by wangyu on 2017/3/17.
 */

public class DickPresenter extends BaseJPresenterImpl<DickModel, DickView> {

    private Handler handler;

    @Override
    public void onPresenterCreate(JApp app) {
        handler = app.getSuperHandler();
    }

    @Override
    public void beginPresent() {
        final DickView view = getJView();
        if (view != null) {
            test(view);
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
                        view.showError(data);
                        view.onDataSuccess("成功啦！");
                    }
                }, 4000);
            }
        });

    }

}
