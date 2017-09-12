package com.jianyuyouhun.jmvplib.mvp.model.network;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;
import com.jianyuyouhun.jmvplib.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络管理器
 * Created by wangyu on 2017/9/5.
 */

public class NetworkModel extends BaseJModel<JApp> {

    private static final String TAG = NetworkModel.class.getSimpleName();

    /**
     * 上一次的网络连接类型
     */
    private int lastConnectType = -1;
    private List<OnNetworkChangeListener> onNetworkChangeListeners = new ArrayList<>();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            int currentType = -1;
            if (networkInfo != null && networkInfo.isConnected()) {
                currentType = networkInfo.getType();
            } else {
                currentType = -1;
            }

            if (lastConnectType != currentType) {
                lastConnectType = currentType;
                for (OnNetworkChangeListener onNetworkChangeListener : onNetworkChangeListeners) {
                    onNetworkChangeListener.onNetworkChange(currentType);
                }
            }
        }
    };

    /**
     * 是否有网络
     *
     * @return
     */
    public boolean hasNet() {
        return getNetWorkType() != -1;
    }

    /**
     * 获取当前的网络连接类型 {@link NetworkInfo#getType()}
     * 注意：手机网络并不是只有wifi和移动流量。
     *
     * @return 网络类型包括：wifi，移动数据，蓝牙共享，网线，VPN等等，
     */
    public int getNetWorkType() {
        return lastConnectType;
    }

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
            Logger.w(TAG, "请配置ACCESS_NETWORK_STATE权限，否则无法监听网络状态变化");
        } else {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            app.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    /**
     * 添加网络变化监听器
     */
    public void addOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        onNetworkChangeListeners.add(onNetworkChangeListener);
    }

    /**
     * 移除网络变化监听器
     */
    public void removeOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        onNetworkChangeListeners.remove(onNetworkChangeListener);
    }

}
