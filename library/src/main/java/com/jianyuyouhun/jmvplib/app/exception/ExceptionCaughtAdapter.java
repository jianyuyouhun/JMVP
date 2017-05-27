package com.jianyuyouhun.jmvplib.app.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionCaughtAdapter implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    public ExceptionCaughtAdapter(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ExceptionActivity.showException(ex);
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    }
}
