package com.jianyuyouhun.jmvplib.app;

import android.app.Application;

import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.app.exception.ExceptionCaughtAdapter;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.mvp.model.CacheModel;
import com.jianyuyouhun.jmvplib.mvp.model.PermissionModel;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.mvp.model.TimeCountDownModel;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;
import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * app基类
 *
 * {@link #setDebugMode()}
 *
 * 设置调试模式，用于Logger打印日志的控制以及异常捕获页面的开启
 *
 * {@link #initDependencies()}
 * 初始化第三方框架
 *
 * {@link #initCommonModels(List)}
 * 初始化通用model
 *
 * {@link #initModels(List)}
 * 初始化业务需求model
 *
 * {@link #initDebug()}
 * 初始化异常捕获页面，若有引入第三方日志统计，如bugly，
 * 请重写该方法并对{@link #isDebug}进行判断，建议不要在调试模式下开启统计
 *
 * Created by jianyuyouhun on 2017/3/17.
 */

public abstract class JApp extends Application {
    private static final String TAG = "JApp";

    private static JApp mInstance;

    private static boolean isDebug;

    /**
     * 是否是在主线程中
     */
    private boolean mIsMainProcess = false;

    private HashMap<String, BaseJModel> modelsMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance != null) {
            mInstance = this;
            return;
        }
        mInstance = this;
        BuildConfig.setIsDebug(setDebugMode());
        isDebug = BuildConfig.isDebug();
        initDebug();
        initDependencies();
        String pidName = CommonUtils.getUIPName(this);
        mIsMainProcess = pidName.equals(getPackageName());
        initLightBroadCast();
        initJApp();
    }

    /**
     * 初始化第三方库（比如imageLoader等）
     */
    protected void initDependencies() {
        JHttpFactory.init();
        ImageLoader.getInstance().init(this);
    }

    /**
     * 设置调试模式
     */
    public boolean setDebugMode() {
        return true;
    }

    public void initDebug() {
        if (isDebug) {
            Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
            ExceptionCaughtAdapter exceptionCaughtAdapter = new ExceptionCaughtAdapter(handler);
            Thread.setDefaultUncaughtExceptionHandler(exceptionCaughtAdapter);
        }
    }

    private void initJApp() {
        List<BaseJModel> models = new ArrayList<>();
        initCommonModels(models);
        initModels(models);
        for (BaseJModel model : models) {
            long time = System.currentTimeMillis();
            ModelInjector.injectModel(model);
            model.onModelCreate(this);
            Class<? extends BaseJModel> baseModelClass = model.getClass();
            String name = baseModelClass.getName();
            modelsMap.put(name, model);
            // 打印初始化耗时
            long spendTime = System.currentTimeMillis() - time;
            Logger.e(TAG, baseModelClass.getSimpleName() + "启动耗时(毫秒)：" + spendTime);
        }
        for (BaseJModel model : models) {
            model.onAllModelCreate();
        }
        LightBroadcast.getInstance().registerModels(modelsMap);
    }

    /**
     * 初始化通用model
     * @param models models
     */
    public void initCommonModels(List<BaseJModel> models) {
        models.add(new CacheModel());               // 缓存model
        models.add(new TimeCountDownModel());       // 倒计时model
        models.add(new PermissionModel());          // 权限忽略记录
        models.add(new SdcardModel());              // sd管理model
    }

    /**
     * 初始化功能model
     * @param models models
     */
    protected abstract void initModels(List<BaseJModel> models);

    /**
     * 初始化轻量级广播
     */
    private void initLightBroadCast() {
        LightBroadcast.init();
    }

    public static JApp getInstance() {
        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <Model extends BaseJModel> Model getJModel(Class<Model> model) {
        return (Model) modelsMap.get(model.getName());
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public boolean isMainProcess() {
        return mIsMainProcess;
    }
}
