package com.jianyuyouhun.jmvplib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

public class AppHelper {
    /**
     * 获取渠道号
     *
     * @param context 上下文
     * @return 渠道号
     */
    public static int getChannelCode(Context context) {
        String code = getMetaData(context, "channel");
        if (code == null) {
            return -1;
        } else {
            int channel = -1;
            try {
                channel = Integer.valueOf(code);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return channel;
        }

    }

    /**
     * 获取META_DATA值
     *
     * @param context 上下文
     * @param key     键
     * @return 当存在key时返回数值否则返回null
     */
    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 模拟Home键
     *
     * @param context 上下文
     */
    public static void imitateHome(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || TextUtils.isEmpty(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkApkExist(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        return list.size() > 0;
    }

    /**
     * 通过文件路径安装程序
     *
     * @param context  上下文
     * @param filaPath 文件路径
     * @return
     */
    public static boolean install(Context context, String filaPath) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(filaPath)), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 返回屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Dip转成对应Px值
     *
     * @param context 上下文
     * @param dip
     * @return
     */
    public static int dipTopx(Context context, float dip) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (dip * s + 0.5f);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     * @return
     */
    public static final boolean hideSoftPad(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            return ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    /**
     * 显示软键盘
     *
     * @param activity
     * @param view
     */
    public static final void showSoftPad(Activity activity, View view) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0); // 显示软键盘
    }

    /**
     * 判断程序是否前段运行
     *
     * @param context     上下文
     * @param packageName 待判断程序包名
     * @return
     */
    public static boolean isAppRunningForeground(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> mRunningTaskInfoList = mActivityManager.getRunningTasks(1);
        if (mRunningTaskInfoList == null || mRunningTaskInfoList.isEmpty())
            return false;
        RunningTaskInfo mRunningTaskInfo = mRunningTaskInfoList.get(0);
        String strPackageName = mRunningTaskInfo.topActivity.getPackageName();
        return strPackageName != null && strPackageName.equals(packageName);
    }

    /**
     * 获取sdk系统版本
     *
     * @return
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机生产商
     *
     * @return
     */
    public static String getVendor() {
        String vendor = android.os.Build.MODEL + android.os.Build.ID;
        return vendor;
    }

    private static ApplicationInfo getMetaDataApplicationInfo(Context context) throws NameNotFoundException {
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
    }

    /**
     * 获取Application的String Meta-Data
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static String getApplicationStringMetaData(Context context, String key, String def) {
        try {
            String data = getMetaDataApplicationInfo(context).metaData.getString(key);

            if (!TextUtils.isEmpty(data)) {
                return data;
            } else {
                return def;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return def;
    }

    /**
     * 获取Application的Int Meta-Data
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static int getApplicationIntMetaData(Context context, String key, int def) {
        try {
            return getMetaDataApplicationInfo(context).metaData.getInt(key, def);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return def;
    }

    /**
     * 获取Application的Boolean Meta-Data
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static boolean getApplicationBooleanMetaData(Context context, String key, boolean def) {
        try {
            return getMetaDataApplicationInfo(context).metaData.getBoolean(key, def);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return def;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getApplicationVersionName(Context context) {
        try {
            PackageManager lPackageManager = context.getPackageManager();
            PackageInfo lPackageInfo = lPackageManager.getPackageInfo(context.getPackageName(), 0);
            return lPackageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }
}
