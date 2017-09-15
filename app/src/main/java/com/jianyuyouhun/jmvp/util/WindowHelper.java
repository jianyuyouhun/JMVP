package com.jianyuyouhun.jmvp.util;

import android.content.Context;
import android.view.WindowManager;

import com.jianyuyouhun.jmvp.app.App;

/**
 *
 * Created by wangyu on 2017/9/15.
 */

public class WindowHelper {
    private static WindowManager mWindowManager;
    public static WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) App.getApp().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
