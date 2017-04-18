package com.jianyuyouhun.jmvplib.mvp;

import android.content.Context;
import android.os.Message;

/**
 * BaseJPresenter接口
 * Created by jianyuyouhun on 2017/3/17.
 */

public interface BaseJPresenter {
    void onCreate(Context context);
    Context getContext();
    void onDestroy();
}
