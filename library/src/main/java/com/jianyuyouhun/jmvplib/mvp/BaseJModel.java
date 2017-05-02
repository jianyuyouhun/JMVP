package com.jianyuyouhun.jmvplib.mvp;

import android.os.Message;

import com.jianyuyouhun.jmvplib.app.JApp;

/**
 * BaseJModel接口
 * Created by jianyuyouhun on 2017/3/17.
 */

public interface BaseJModel {
    void onModelCreate(JApp app);
    void onAllModelCreate();
    void handleSuperMsg(Message msg);
    void onModelDestroy();
}
