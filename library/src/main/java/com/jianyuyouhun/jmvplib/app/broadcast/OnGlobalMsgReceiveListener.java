package com.jianyuyouhun.jmvplib.app.broadcast;

import android.os.Message;

/**
 * 全局的消息监听
 * Created by wangyu on 2017/4/18.
 */

public interface OnGlobalMsgReceiveListener {
    void onReceiveGlobalMsg(Message msg);
}
