package com.jianyuyouhun.jmvplib.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.app.exception.InitPresenterException;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenter;

import java.lang.reflect.ParameterizedType;

/**
 * MVPActivity基类
 * Created by jianyuyouhun on 2017/3/17.
 */
public abstract class BaseMVPActivity<MajorPresenter extends BaseJPresenter, MajorModel extends BaseJModel> extends BaseActivity {

    protected MajorPresenter mPresenter;
    protected MajorModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressWarnings("unchecked") Class<MajorPresenter> majorPresenterCls =
                (Class<MajorPresenter>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            if (mPresenter == null) //当应用重启重走生命周期的时候决定是否要重新初始化presenter
                mPresenter = majorPresenterCls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new InitPresenterException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InitPresenterException("请确保" + majorPresenterCls.getSimpleName() + "拥有public的无参构造函数");
        }
        mModel = initModel();
        bindModelAndView(mPresenter);
        if (!mPresenter.isAttach()) {
            throw new InitPresenterException("请为" + mPresenter.getClass().getName() + "绑定数据");
        }
        mPresenter.onCreate(this);
    }

    @NonNull
    protected abstract MajorModel initModel();

    protected abstract void bindModelAndView(MajorPresenter mPresenter);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

}
