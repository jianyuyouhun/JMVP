package com.jianyuyouhun.jmvplib.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.injecter.ViewInjectUtil;
import com.jianyuyouhun.jmvplib.utils.permission.PermissionRequester;

import java.util.List;

/**
 * Activity基类
 * Created by wangyu on 2017/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private boolean isMainActivityOn = false;
    protected ProgressDialog mProgressDialog;
    private boolean mIsDestroy;
    private boolean mIsFinish;
    private long mLastClickTime;
    protected static final boolean IS_DEBUG_MODE = BuildConfig.isDebug();
    protected PermissionRequester permissionRequester;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDestroy = false;
        mIsFinish = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionRequester = new PermissionRequester();
        }
        int layoutId = getLayoutResId();
        if (layoutId != 0) {
            setContentView(getLayoutResId());
        } else {
            View view = buildLayoutView();
            if (view != null) {
                setContentView(view);
            }
        }
        ViewInjectUtil.inject(this);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 不想用layoutId的时候可以重写此方法返回一个View
     *
     * @return view
     */
    protected View buildLayoutView() {
        return null;
    }

    @Deprecated
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Deprecated
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Deprecated
    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    public void showToast(@StringRes int msgId) {
        showToast(getString(msgId));
    }

    public void showToast(String msg) {
        if (mIsDestroy) return;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionRequester.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionRequester.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    public void showProgressDialog() {
        if (mIsDestroy) return;
        initProgressDialog();
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mIsDestroy) return;
        if (mProgressDialog == null) return;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
        }
    }

    /**
     * 是否在500毫秒内快速点击
     *
     * @return true 快速点击， false 非快速点击
     */
    public synchronized boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (mLastClickTime > time) {
            mLastClickTime = time;
            return false;
        }
        if (time - mLastClickTime < 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    //判断程序是否在前台运行
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 启动Activity
     *
     * @param cls 需要启动的界面
     */
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 获取BaseActivity上下文
     *
     * @return BaseActivity上下文
     */
    public BaseActivity getActivity() {
        return this;
    }

    /**
     * 启动设置界面
     */
    public void startSystemSettingActivity(int settingsRequestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, settingsRequestCode);
    }

    /**
     * 以类名为标签打印E级别日志
     *
     * @param msg 日志内容
     */
    public void logE(String msg) {
        Logger.e(getClass().getSimpleName(), msg);
    }

    /**
     * 以类名为标签打印I级别日志
     *
     * @param msg 日志内容
     */
    public void logI(String msg) {
        Logger.i(getClass().getSimpleName(), msg);
    }

    /**
     * 以类名为标签打印W级别日志
     *
     * @param msg 日志内容
     */
    public void logW(String msg) {
        Logger.w(getClass().getSimpleName(), msg);
    }

    /**
     * 以类名为标签打印d级别日志
     *
     * @param msg 日志内容
     */
    public void logD(String msg) {
        Logger.d(getClass().getSimpleName(), msg);
    }
}
