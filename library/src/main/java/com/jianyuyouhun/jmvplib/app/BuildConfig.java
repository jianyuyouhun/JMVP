package com.jianyuyouhun.jmvplib.app;

/**
 *
 * Created by jianyuyouhun on 2017/3/17.
 */

public class BuildConfig {
    private static boolean IS_DEBUG = true;
    public static void setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }
    public static boolean isDebug() {
        return IS_DEBUG;
    }
}
