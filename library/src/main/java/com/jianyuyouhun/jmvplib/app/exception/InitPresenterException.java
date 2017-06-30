package com.jianyuyouhun.jmvplib.app.exception;

/**
 * 获取presenter异常捕获
 * Created by wangyu on 2017/4/25.
 */

public class InitPresenterException extends RuntimeException {
    public InitPresenterException() {
        this("presenter初始化失败！");
    }

    public InitPresenterException(String detailMessage) {
        super(detailMessage);
    }

    public InitPresenterException(Throwable throwable) {
        super(throwable);
    }
}
