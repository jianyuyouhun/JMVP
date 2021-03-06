package com.jianyuyouhun.jmvplib.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianyuyouhun.inject.ViewInjector;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.permission.library.EZPermission;

/**
 * Fragment基类
 * Created by wangyu on 2017/4/5.
 */

public abstract class BaseFragment extends Fragment {

    private boolean isDestroy = false;
    private long mInsertDt = System.currentTimeMillis();
    protected ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        ModelInjector.injectModel(this);
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
        View view;
        int layoutId = getLayoutResId();
        if (layoutId == 0) {
            view = buildLayoutView();
        } else {
            view = inflater.inflate(getLayoutResId(), container, false);
        }
        ViewInjector.inject(this, view);
        onCreateView(view, container, savedInstanceState);
        return view;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 不想用layoutId的时候可以重写此方法返回一个View
     * @return view
     */
    protected View buildLayoutView() {
        return null;
    }

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
        initProgressDialog();
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (isDestroy) return;
        if (mProgressDialog == null) return;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void initProgressDialog() {
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

    /**
     * 启动页面
     */
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(getBaseActivity(), cls));
    }

    /**
     * 启动页面
     */
    public void startActivityForResult(Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(new Intent(getBaseActivity(), cls), requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EZPermission.Companion.getInstance().onRequestPermissionsResult(getBaseActivity(), requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EZPermission.Companion.getInstance().onActivityResult(getBaseActivity(), requestCode, resultCode, data);
    }
}
