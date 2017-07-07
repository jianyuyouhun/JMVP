package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * http测试presenter
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestPresenter extends BaseJPresenter<HttpTestModel, HttpTestView> {

    public void doHttpTest(boolean isPost) {
        if (!isAttach()) {
            throw new RuntimeException("Model已经为空");
        }
        String url = getJView().getUrl();
        if (!(url.startsWith("http://") || url.startsWith("https://"))){
            url = "http://" + url;
        }
        getJView().showDialog();
        if (isPost) {
            mModel.doPost(url, null, new OnResultListener<String>() {
                @Override
                public void onResult(int result, String data) {
                    getJView().hideDialog();
                    if (result == RESULT_SUCCESS) {
                        getJView().showHtml(data);
                    } else {
                        getJView().showError("no data");
                    }
                }
            });
        } else {
            mModel.doGet(url, new OnResultListener<String>() {
                @Override
                public void onResult(int result, String data) {
                    getJView().hideDialog();
                    if (result == RESULT_SUCCESS) {
                        getJView().showHtml(data);
                    } else {
                        getJView().showError("no data");
                    }
                }
            });
        }
    }
}
