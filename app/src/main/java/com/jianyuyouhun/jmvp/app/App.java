package com.jianyuyouhun.jmvp.app;

import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;

import java.util.List;

/**
 * application
 * Created by wangyu on 2017/3/17.
 */

public class App extends JApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void initModels(List<BaseJModelImpl> models) {
        models.add(new AdapterTestModel());
        models.add(new HttpTestModel());
    }
}
