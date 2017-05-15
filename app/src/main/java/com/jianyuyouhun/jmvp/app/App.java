package com.jianyuyouhun.jmvp.app;

import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.mvp.logintest.LoginTestModel;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;

import java.util.List;

/**
 * application
 * Created by wangyu on 2017/3/17.
 */

public class App extends JApp {

    @Override
    protected void initModels(List<BaseJModelImpl> models) {
        models.add(new AdapterTestModel());
        models.add(new HttpTestModel());
        models.add(new LoginTestModel());
    }

    public static App getApp() {
        return (App) getInstance();
    }

    @Override
    protected void initDependencies() {
        super.initDependencies();
        JHttpFactory.init();
        ImageLoader.getInstance().init(this);
    }
}
