package com.jianyuyouhun.jmvplib.mvp;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;

/**
 * model基类
 * Created by wangyu on 2017/4/18.
 */

public class BaseJModel<T extends JApp> {
    private LightBroadcast superHandler;

    public void onModelCreate(T app) {
        superHandler = LightBroadcast.getInstance();
    }

    public void onModelDestroy() {}

    public void onAllModelCreate() {}

    public <MinorModel extends BaseJModel> MinorModel getModel(Class<MinorModel> model) {
        return JApp.getInstance().getJModel(model);
    }

    protected LightBroadcast getSuperHandler() {
        return superHandler;
    }
}
