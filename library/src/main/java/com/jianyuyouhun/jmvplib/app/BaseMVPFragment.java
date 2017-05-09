package com.jianyuyouhun.jmvplib.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;

/**
 * MVPFragment基类
 * Created by wangyu on 2017/4/25.
 */

public abstract class BaseMVPFragment<MainPresenter extends BaseJPresenterImpl, MainModel extends BaseJModelImpl> extends BaseFragment {

    protected MainPresenter mPresenter;
    protected MainModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter == null) {
            throw new InitPresenterException();
        }
        mModel = initModel();
        boolean isPresenterBindFinish = bindModelAndView();
        if (!isPresenterBindFinish) {
            throw new InitPresenterException("请为" + mPresenter.getClass().getName() + "绑定数据");
        }
        mPresenter.onCreate(getBaseActivity());
    }

    protected abstract MainPresenter getPresenter();
    protected abstract MainModel initModel();
    protected abstract boolean bindModelAndView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
