package com.jianyuyouhun.jmvplib.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;
import com.jianyuyouhun.jmvplib.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * app基类
 * Created by jianyuyouhun on 2017/3/17.
 */

public abstract class JApp extends Application {
    private static final String TAG = "JApp";

    private static JApp mInstance;

    private static boolean isDebug = BuildConfig.IS_DEBUG;

    private Handler mSuperHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public Handler getSuperHandler() {
        return mSuperHandler;
    }
    /**
     * 是否是在主线程中
     */
    private boolean mIsMainProcess = false;

    private HashMap<String, BaseJPresenterImpl> presentersMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance != null) {
            mInstance = this;
            return;
        }
        mInstance = this;
        String pidName = CommonUtils.getUIPName(this);
        mIsMainProcess = pidName.equals(getPackageName());
        initJApp();
        initDebug();
    }

    public void initDebug() {
        if (isDebug) {
            Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
            ExceptionCaughtAdapter exceptionCaughtAdapter = new ExceptionCaughtAdapter(handler);
            Thread.setDefaultUncaughtExceptionHandler(exceptionCaughtAdapter);
        }
    }

    private void initJApp() {
        List<BaseJPresenterImpl> presenters = new ArrayList<>();
        initPresenter(presenters);
        for (BaseJPresenterImpl presenter : presenters) {
            long time = System.currentTimeMillis();
            presenter.onPresenterCreate(this);
            Class<? extends BaseJPresenterImpl> basePresenterClass = presenter.getClass();
            String name = basePresenterClass.getName();
            presentersMap.put(name, presenter);
            // 打印初始化耗时
            long spendTime = System.currentTimeMillis() - time;
            Logger.e(TAG, basePresenterClass.getSimpleName() + "启动耗时(毫秒)：" + spendTime);
        }
    }

    /**
     * 初始化presenter
     */
    public abstract void initPresenter(List<BaseJPresenterImpl> presenters);

    public static JApp getInstance() {
        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <P extends BaseJPresenterImpl> P getJPresenter(Class<P> presenter) {
        return (P) presentersMap.get(presenter.getName());
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public boolean isMainProcess() {
        return mIsMainProcess;
    }
}
