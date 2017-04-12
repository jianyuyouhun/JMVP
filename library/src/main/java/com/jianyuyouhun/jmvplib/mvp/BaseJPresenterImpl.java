package com.jianyuyouhun.jmvplib.mvp;

import android.content.Context;

import com.jianyuyouhun.jmvplib.app.JApp;

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

    @Override
    public void onCreate(Context context) {
        this.context = context;
    }

    public abstract void onPresenterCreate(JApp app);

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

    public Context getContext() {
        return context;
    }

    @Override
    public void onDestroy() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public <P extends BaseJPresenterImpl> P getJPresenter(Class<P> presenter) {
        return JApp.getInstance().getJPresenter(presenter);
    }
}
