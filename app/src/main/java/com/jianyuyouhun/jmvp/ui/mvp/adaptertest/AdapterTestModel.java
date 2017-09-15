package com.jianyuyouhun.jmvp.ui.mvp.adaptertest;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

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
    }

    public void doRequester(final OnResultListener<List<String>> listener) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("测试数据" + i);
        }
        listener.onResult(OnResultListener.RESULT_SUCCESS, list);
    }

}
