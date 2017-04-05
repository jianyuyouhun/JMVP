package com.jianyuyouhun.jmvplib.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by wangyu on 2017/4/5.
 */

public class BaseFragment extends Fragment {
    private Handler mSuperHandler;
    private boolean isDestroy = false;
    private long mInsertDt = System.currentTimeMillis();
    private boolean mIsSingleReport = false;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSuperHandler = JApp.getInstance().getSuperHandler();
        isDestroy = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    public boolean isDestroy() {
        return isDestroy;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public Context getContext() {
        return getBaseActivity();
    }

    public long getInsertDt() {
        return mInsertDt;
    }

    public void showProgressDialog() {
        if (isDestroy) return;
        initDialog();
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (isDestroy) return;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getBaseActivity());
            mProgressDialog.setCancelable(false);
        }
    }
}
