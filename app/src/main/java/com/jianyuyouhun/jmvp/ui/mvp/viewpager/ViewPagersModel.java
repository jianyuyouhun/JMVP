package com.jianyuyouhun.jmvp.ui.mvp.viewpager;

import android.support.annotation.NonNull;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/9/11.
 */

public class ViewPagersModel extends BaseJModel<App> {

    @Override
    public void onModelCreate(App app) {
        super.onModelCreate(app);
    }

    public void getViewPagerNames(@NonNull final OnResultListener<List<String>> listener) {
        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            titles.add("fragment" + i);
        }
        getSuperHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onResult(OnResultListener.RESULT_SUCCESS, titles);
            }
        }, 1500);
    }
}
