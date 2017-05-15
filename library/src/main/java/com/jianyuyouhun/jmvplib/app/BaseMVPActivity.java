package com.jianyuyouhun.jmvplib.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;

/**
 * MVPActivity基类
 * Created by jianyuyouhun on 2017/3/17.
 */
public abstract class BaseMVPActivity<MajorPresenter extends BaseJPresenterImpl, MajorModel extends BaseJModelImpl> extends BaseActivity {

    protected MajorPresenter mPresenter;
    protected MajorModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter == null) {
            throw new InitPresenterException();
        }
        mModel = initModel();
        mPresenter = bindModelAndView();
        if (mPresenter == null) {
            throw new InitPresenterException("请为presenter绑定数据");
        }
        mPresenter.onCreate(this);
    }

    @NonNull
    protected abstract MajorPresenter getPresenter();
    @NonNull
    protected abstract MajorModel initModel();
    @NonNull
    protected abstract MajorPresenter bindModelAndView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

}
