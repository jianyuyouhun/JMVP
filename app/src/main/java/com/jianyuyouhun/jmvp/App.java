package com.jianyuyouhun.jmvp;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;

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
        models.add(new DickModel());
    }
}
