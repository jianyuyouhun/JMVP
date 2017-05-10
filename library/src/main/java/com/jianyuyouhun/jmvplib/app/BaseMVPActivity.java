package com.jianyuyouhun.jmvplib.app;

import android.os.Bundle;
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
        boolean isPresenterBindFinish = bindModelAndView();
        if (!isPresenterBindFinish) {
            throw new InitPresenterException("请为" + mPresenter.getClass().getName() + "绑定数据");
        }
        mPresenter.onCreate(this);
    }

    protected abstract MajorPresenter getPresenter();
    protected abstract MajorModel initModel();
    protected abstract boolean bindModelAndView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

}
