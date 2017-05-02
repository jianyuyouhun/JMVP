package com.jianyuyouhun.jmvplib.mvp;

import android.os.Handler;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;

/**
 * model基类
 * Created by wangyu on 2017/4/18.
 */

public class BaseJModelImpl implements BaseJModel {
    private Handler superHandler;
    private boolean isOpenHandleMsg = false;

    @Override
    public void onModelCreate(JApp app) {
        superHandler = app.getSuperHandler();
    }

    @Override
    public void handleSuperMsg(Message msg) {}

    @Override
    public void onModelDestroy() {}

    @Override
    public void onAllModelCreate() {}

    public <M extends BaseJModelImpl> M getModel(Class<M> model) {
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

    protected Handler getSuperHandler() {
        return superHandler;
    }
}
