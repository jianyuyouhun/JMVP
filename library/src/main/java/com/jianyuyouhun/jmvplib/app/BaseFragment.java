package com.jianyuyouhun.jmvplib.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Fragment基类
 * Created by wangyu on 2017/4/5.
 */

public class BaseFragment extends Fragment {
    private boolean isDestroy = false;
    private long mInsertDt = System.currentTimeMillis();
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public Intent getStartActivityIntent(Class<? extends Activity> targetCls) {
        Intent intent = new Intent(getContext(), targetCls);
        if (!(getContext() instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
}
