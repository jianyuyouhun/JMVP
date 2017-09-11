package com.jianyuyouhun.jmvp.ui.mvp.viewpager;

import android.content.Context;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.List;

/**
 *
 * Created by wangyu on 2017/9/11.
 */

public class ViewPagersPresenter extends BaseJPresenter<ViewPagersModel, ViewPagersView> {

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
    }

    public void onStart() {
        if (isAttach()) {
            mModel.getViewPagerNames(new OnResultListener<List<String>>() {
                @Override
                public void onResult(int result, List<String> data) {
                    if (result == RESULT_SUCCESS) {
                        mView.onGetTitlesSuccess(data);
                    } else {
                        mView.showError("get data error");
                    }
                }
            });
        }
    }
}
