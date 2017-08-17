package com.jianyuyouhun.jmvp.app;

import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.mvp.common.WindowHelperModel;
import com.jianyuyouhun.jmvp.mvp.db.DataBaseModel;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvp.mvp.logintest.LoginTestModel;
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
        models.add(new WindowHelperModel());
    }

    public static App getApp() {
        return (App) getInstance();
    }

}
