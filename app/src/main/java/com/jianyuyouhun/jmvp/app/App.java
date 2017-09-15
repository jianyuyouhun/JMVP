package com.jianyuyouhun.jmvp.app;

import com.jianyuyouhun.jmvp.BuildConfig;
import com.jianyuyouhun.jmvp.ui.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.ui.mvp.db.DataBaseModel;
import com.jianyuyouhun.jmvp.ui.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvp.ui.mvp.logintest.LoginTestModel;
import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagersModel;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.util.List;

/**
 * application
 * Created by wangyu on 2017/3/17.
 */

public class App extends JApp {

    @Override
    public void initCommonModels(List<BaseJModel> models) {
        super.initCommonModels(models);
        models.add(new DataBaseModel());
    }

    @Override
    protected void initModels(List<BaseJModel> models) {
        models.add(new AdapterTestModel());
        models.add(new HttpTestModel());
        models.add(new LoginTestModel());
    }

    public static App getApp() {
        return (App) getInstance();
    }

    @Override
    public boolean setIsDebug() {
        return BuildConfig.DEBUG;
    }
}
