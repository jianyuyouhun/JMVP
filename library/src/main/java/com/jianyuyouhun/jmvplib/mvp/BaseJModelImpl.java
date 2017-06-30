package com.jianyuyouhun.jmvplib.mvp;

import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

/**
 * model基类
 * Created by wangyu on 2017/4/18.
 */

public class BaseJModelImpl implements BaseJModel {
    private LightBroadcast superHandler;
    private boolean isOpenHandleMsg = false;

    @Override
    public void onModelCreate(JApp app) {
        superHandler = LightBroadcast.getInstance();
        ModelInjector.injectModel(this);
    }

    @Override
    public void handleSuperMsg(Message msg) {}

    @Override
    public void onModelDestroy() {}

    @Override
    public void onAllModelCreate() {}

    public <MinorModel extends BaseJModelImpl> MinorModel getModel(Class<MinorModel> model) {
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
