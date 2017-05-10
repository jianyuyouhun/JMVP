package com.jianyuyouhun.jmvplib.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.injecter.ViewInjectUtil;
import com.jianyuyouhun.jmvplib.utils.permission.PermissionRequester;

/**
 * Fragment基类
 * Created by wangyu on 2017/4/5.
 */

public abstract class BaseFragment extends Fragment {

    private boolean isDestroy = false;
    private long mInsertDt = System.currentTimeMillis();
    private ProgressDialog mProgressDialog;
    protected PermissionRequester permissionRequester;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        permissionRequester = new PermissionRequester();
    }

    /**
     * 请在下面方法中实现逻辑
     * {@link #onCreateView(View, ViewGroup, Bundle)}
     * @param inflater              LayoutInflater
     * @param container             ViewGroup
     * @param savedInstanceState    Bundle
     * @return  view
     */
    @Nullable
    @Deprecated
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ViewInjectUtil.inject(this, view);
        onCreateView(view, container, savedInstanceState);
        return view;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 绑定好layoutId之后的onCreateView
     * @param rootView              rootView
     * @param parent                viewGroup
     * @param savedInstanceState    savedInstanceState
     */
    protected abstract void onCreateView(View rootView, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionRequester.onRequestPermissionsResult(getBaseActivity(), requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionRequester.onActivityResult(getBaseActivity(), requestCode, resultCode, data);
    }
}
