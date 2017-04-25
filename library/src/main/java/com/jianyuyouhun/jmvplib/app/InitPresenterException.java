package com.jianyuyouhun.jmvplib.app;

/**
 * 获取presenter异常捕获
 * Created by wangyu on 2017/4/25.
 */

public class InitPresenterException extends RuntimeException {
    public InitPresenterException() {
        this("请初始化对应的Presenter");
    }

    public InitPresenterException(String detailMessage) {
        super(detailMessage);
    }

    public InitPresenterException(Throwable throwable) {
        super(throwable);
    }
}
