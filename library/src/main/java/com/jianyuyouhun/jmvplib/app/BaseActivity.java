package com.jianyuyouhun.jmvplib.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.util.Logger;

/**
 * BaseActivity类
 * Created by jianyuyouhun on 2017/3/17.
 */
public abstract class BaseActivity<P extends BaseJPresenterImpl, M extends BaseJModel> extends AppCompatActivity {
    public static final String TAG = "JApp";
    public P mPresenter;
    public M mModel;

    private boolean isMainActivityOn = false;
    private ProgressDialog mProgressDialog;
    private boolean mIsDestroy;
    private boolean mIsFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDestroy = false;
        mIsFinish = false;
        setContentView(getLayoutResId());
        initDialog();
        mPresenter = getPresenter();
        if (mPresenter == null) {
            Logger.e(TAG, "请在你的App中初始化对应的Presenter");
            finish();
            return;
        }
        mModel = initModel();
        boolean isPresenterBindFinish = bindModelAndView();
        if (!isPresenterBindFinish) {
            Logger.e(TAG, "请为" + mPresenter.getClass().getName() + "绑定数据");
            finish();
            return;
        }
    }

    private void initDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
    }

    public void showToast(@StringRes int msgId) {
        showToast(getString(msgId));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public abstract int getLayoutResId();
    public abstract P getPresenter();
    protected abstract M initModel();
    public abstract boolean bindModelAndView();

    public boolean isMainActivityOn() {
        return isMainActivityOn;
    }

    public void setIsMainOn(boolean flag) {
        isMainActivityOn = flag;
    }
    /**
     * DIP 转 PX
     */
    public static int dipToPx(float dip) {
        return dipToPx(JApp.getInstance(), dip);
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    public final Context getContext() {
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        mIsFinish = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
        mPresenter.onDestroy();
    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
