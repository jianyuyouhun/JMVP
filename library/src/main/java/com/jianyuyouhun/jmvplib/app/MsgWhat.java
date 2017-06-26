package com.jianyuyouhun.jmvplib.app;

/**
 * 全局消息
 * Created by wangyu on 2017/6/26.
 */

public enum MsgWhat {

    /** 通知所有Activity关闭自己 */
    ALL_ACTIVITY_CLOSE_YOUR_SELF(1);

    private int value;

    MsgWhat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
