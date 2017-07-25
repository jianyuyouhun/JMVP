package com.jianyuyouhun.jmvplib.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.app.exception.InitPresenterException;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;

import java.lang.reflect.ParameterizedType;

/**
 * MVPFragment基类
 * Created by wangyu on 2017/4/25.
 */

public abstract class BaseMVPFragment<MajorPresenter extends BaseJPresenter, MajorModel extends BaseJModel> extends BaseFragment {

    protected MajorPresenter mPresenter;
    protected MajorModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressWarnings("unchecked") Class<MajorPresenter> majorPresenterCls =
                (Class<MajorPresenter>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            mPresenter = majorPresenterCls.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            throw new InitPresenterException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InitPresenterException("请确保" + majorPresenterCls.getSimpleName() + "拥有public的无参构造函数");
        }
        mModel = initModel();
        bindModelAndView();
        if (!mPresenter.isAttach()) {
            throw new InitPresenterException("请为" + mPresenter.getClass().getName() + "绑定数据");
        }
        mPresenter.onCreate(getBaseActivity());
    }

    @NonNull
    protected abstract MajorModel initModel();
    protected abstract void bindModelAndView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
