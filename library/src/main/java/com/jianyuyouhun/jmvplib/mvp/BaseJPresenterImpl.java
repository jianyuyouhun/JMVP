package com.jianyuyouhun.jmvplib.mvp;

import android.content.Context;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

import java.lang.ref.WeakReference;

/**
 * BasePresenter实现
 * Created by jianyuyouhun on 2017/3/17.
 */

public abstract class BaseJPresenterImpl<MajorManager extends BaseJModel, MajorView extends BaseJView> implements BaseJPresenter{
    protected MajorManager mModel;
    protected MajorView mView;
    private WeakReference<MajorView> mViewRef;
    private Context context;
    private boolean isDestroy = false;

    protected static final String TAG = BaseJPresenterImpl.class.getSimpleName();

    @Override
    public void onCreate(Context context) {
        this.context = context;
        isDestroy = false;
        ModelInjector.injectModel(this);
    }

    public void onBindModelView(MajorManager mModel, MajorView mView) {
        mViewRef = new WeakReference<>(mView);
        this.mView = mViewRef.get();
        this.mModel = mModel;
    }

    protected MajorView getJView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
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

    public <MinorModel extends BaseJModelImpl> MinorModel getModel(Class<MinorModel> model) {
        return JApp.getInstance().getJModel(model);
    }
}
