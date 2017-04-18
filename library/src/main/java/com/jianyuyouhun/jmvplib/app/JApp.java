package com.jianyuyouhun.jmvplib.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
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
            for (String s : modelsMap.keySet()) {
                BaseJModelImpl model = modelsMap.get(s);
                if (model.isOpenHandleMsg()) {
                    model.handleSuperMsg(msg);
                }
            }
            for (OnSuperMsgHandlerListener listener : handlerListeners) {
                listener.onHandleSuperMsg(msg);
            }
        }
    };

    private List<OnSuperMsgHandlerListener> handlerListeners = new ArrayList<>();

    /**
     * 增加全局消息监听
     * @param listener listener
     */
    public void addOnSuperMsgHandlerListener(OnSuperMsgHandlerListener listener) {
        handlerListeners.add(listener);
    }

    /**
     * 移除全局消息监听
     * @param listener listener
     */
    public void removeOnSuperMsgHandlerListener(OnSuperMsgHandlerListener listener) {
        handlerListeners.remove(listener);
    }

    public Handler getSuperHandler() {
        return mSuperHandler;
    }
    /**
     * 是否是在主线程中
     */
    private boolean mIsMainProcess = false;

    private HashMap<String, BaseJModelImpl> modelsMap = new HashMap<>();

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
        List<BaseJModelImpl> models = new ArrayList<>();
        initModels(models);
        for (BaseJModelImpl model : models) {
            long time = System.currentTimeMillis();
            model.onModelCreate(this);
            Class<? extends BaseJModelImpl> baseModelClass = model.getClass();
            String name = baseModelClass.getName();
            modelsMap.put(name, model);
            // 打印初始化耗时
            long spendTime = System.currentTimeMillis() - time;
            Logger.e(TAG, baseModelClass.getSimpleName() + "启动耗时(毫秒)：" + spendTime);
        }
        for (BaseJModelImpl model : models) {
            model.onAllModelCreate();
        }
    }

    protected abstract void initModels(List<BaseJModelImpl> models);

    public static JApp getInstance() {
        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <M extends BaseJModelImpl> M getJModel(Class<M> model) {
        return (M) modelsMap.get(model.getName());
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public boolean isMainProcess() {
        return mIsMainProcess;
    }
}
