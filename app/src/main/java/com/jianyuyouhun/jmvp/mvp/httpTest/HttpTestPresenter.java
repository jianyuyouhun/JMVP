package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

/**
 * http测试presenter
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestPresenter extends BaseJPresenterImpl<HttpTestModel, HttpTestView> {

    public void doHttpTest() {
        final HttpTestView view = getJView();
        if (view != null) {
            view.showDialog();
            mModel.doHttpTest(new OnResultListener<String>() {
                @Override
                public void onResult(int result, String data) {
                    view.hideDialog();
                    if (result == RESULT_SUCCESS) {
                        view.showHtml(data);
                    } else {
                        view.showError("no data");
                    }
                }
            });
        }
    }
}
