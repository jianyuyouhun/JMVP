package com.jianyuyouhun.jmvplib.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * BasePresenter实现
 * Created by jianyuyouhun on 2017/3/17.
 */

public abstract class BaseJPresenterImpl<M extends BaseJModel, V extends BaseJView> implements BaseJPresenter{
    protected M mModel;
    private V mView;
    private WeakReference<V> mViewRef;
    private Context context;
    private boolean isDestroy = false;

    protected static final String TAG = BaseJPresenterImpl.class.getSimpleName();

    @Override
    public void onCreate(Context context) {
        this.context = context;
        isDestroy = false;
    }

    public abstract void beginPresent();

    public void onBindModelView(M mModel, V mView) {
        mViewRef = new WeakReference<V>(mView);
        this.mView = mViewRef.get();
        this.mModel = mModel;
    }

    protected V getJView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    protected boolean isAttach() {
        return mModel != null && mViewRef.get() != null;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onDestroy() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        isDestroy = true;
    }

    public boolean isDestroy() {
        return isDestroy;
    }
}
