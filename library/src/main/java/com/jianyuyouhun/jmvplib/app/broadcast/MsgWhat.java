package com.jianyuyouhun.jmvplib.app.broadcast;

/**
 * 全局消息
 * Created by wangyu on 2017/6/26.
 */

public enum MsgWhat {

    /** 通知所有BaseActivity关闭自己 */
    ALL_ACTIVITY_CLOSE_YOUR_SELF(-1),
    /** 通知所有BaseService关闭自己 */
    ALL_SERVICE_STOP_YOUR_SELF(-2);

    private int value;

    MsgWhat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
