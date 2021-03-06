package com.jianyuyouhun.jmvplib.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jianyuyouhun.inject.ViewInjector;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.app.broadcast.MsgWhat;
import com.jianyuyouhun.jmvplib.app.broadcast.OnGlobalMsgReceiveListener;
import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.permission.library.EZPermission;

import java.util.List;

/**
 * Activity基类
 * Created by wangyu on 2017/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected ProgressDialog mProgressDialog;
    private boolean mIsDestroy;
    private boolean mIsFinish;
    private long mLastClickTime;
    protected static final boolean IS_DEBUG_MODE = JApp.isDebug();
    private Toast mLastToast;

    private OnGlobalMsgReceiveListener onGlobalMsgReceiveListener = new OnGlobalMsgReceiveListener() {
        @Override
        public void onReceiveGlobalMsg(Message msg) {
            logD("onReceiveGlobalMsg:" + msg.what);
            if (msg.what == MsgWhat.ALL_ACTIVITY_CLOSE_YOUR_SELF.getValue()) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openTransitions();
        mIsDestroy = false;
        mIsFinish = false;
        int layoutId = getLayoutResId();
        if (layoutId != 0) {
            setContentView(getLayoutResId());
        } else {
            View view = buildLayoutView();
            if (view != null) {
                setContentView(view);
            }
        }
        ViewInjector.inject(this);
        ModelInjector.injectModel(this);
        LightBroadcast.getInstance().addOnGlobalMsgReceiveListener(onGlobalMsgReceiveListener);
    }

    private void openTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
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
        cancelToast();
        mLastToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mLastToast.show();
    }

    private void cancelToast() {
        if (mLastToast != null) {
            mLastToast.cancel();
        }
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
        LightBroadcast.getInstance().removeOnGlobalMsgReceiveListener(onGlobalMsgReceiveListener);
        mIsDestroy = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EZPermission.Companion.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EZPermission.Companion.getInstance().onActivityResult(this, requestCode, resultCode, data);
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
     * 启动页面
     * @param cls               需要启动的界面
     * @param transitionView    转场视图
     * @param transitionName    转场动画标记
     */
    public void startActivity(Class<? extends Activity> cls, @NonNull View transitionView, @NonNull String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions option = ActivityOptions
                    .makeSceneTransitionAnimation(this, transitionView, transitionName);
            startActivity(new Intent(this, cls), option.toBundle());
        } else {
            startActivity(cls);
        }
    }

    /**
     * 启动Activity
     * @param cls           需要启动的页面
     * @param requestCode   请求码
     */
    public void startActivityForResult(Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    /**
     * 启动Activity
     * @param cls               需要启动的页面
     * @param transitionView    转场视图
     * @param transitionName    转场动画标记
     * @param requestCode       请求码
     */
    public void startActivityForResult(Class<? extends Activity> cls, @NonNull View transitionView, @NonNull String transitionName, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions option = ActivityOptions
                    .makeSceneTransitionAnimation(this, transitionView, transitionName);
            startActivityForResult(new Intent(this, cls), requestCode, option.toBundle());
        } else {
            startActivityForResult(cls, requestCode);
        }
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
