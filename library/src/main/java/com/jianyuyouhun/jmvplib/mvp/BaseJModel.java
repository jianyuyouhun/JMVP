package com.jianyuyouhun.jmvplib.mvp;

import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

/**
 * model基类
 * Created by wangyu on 2017/4/18.
 */

public class BaseJModel<T extends JApp> {
    private LightBroadcast superHandler;
    private boolean isOpenHandleMsg = false;

    public void onModelCreate(T app) {
        superHandler = LightBroadcast.getInstance();
    }

    public void onModelDestroy() {}

    public void onAllModelCreate() {}

    public void handleSuperMsg(Message msg) {}

    public <MinorModel extends BaseJModel> MinorModel getModel(Class<MinorModel> model) {
        return JApp.getInstance().getJModel(model);
    }

    public boolean isOpenHandleMsg() {
        return isOpenHandleMsg;
    }

    public void openHandleMsg() {
        isOpenHandleMsg = true;
    }

    public void closeHandleMsg() {
        isOpenHandleMsg = false;
    }

    protected LightBroadcast getSuperHandler() {
        return superHandler;
    }
}
