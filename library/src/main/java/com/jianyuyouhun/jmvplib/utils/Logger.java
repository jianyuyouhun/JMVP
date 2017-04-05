package com.jianyuyouhun.jmvplib.utils;

import android.util.Log;

import com.jianyuyouhun.jmvplib.app.JApp;

/**
 *
 * Created by jianyuyouhun on 2017/3/17.
 */

public class Logger {
    public static void e(String tag, String msg) {
        if (JApp.isDebug()) {
            Log.e(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (JApp.isDebug()) {
            Log.e(tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if (JApp.isDebug()) {
            Log.e(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (JApp.isDebug()) {
            Log.e(tag, msg);
        }
    }
}
