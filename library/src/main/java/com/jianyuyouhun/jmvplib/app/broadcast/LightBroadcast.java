package com.jianyuyouhun.jmvplib.app.broadcast;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 轻量级广播（使用handler机制实现）
 *
 * 使用方式
 * 1、初始化调用{@link #init()}
 * 2、获取实例调用{@link #getInstance()}
 * 3、在任意位置启用监听，调用本实例的{@link #addOnGlobalMsgReceiveListener(OnGlobalMsgReceiveListener)}
 *    当然，需要在离开的时候调用{@link #removeOnGlobalMsgReceiveListener(OnGlobalMsgReceiveListener)}
 *
 * Created by wangyu on 2017/5/27.
 */

public class LightBroadcast {

    private List<OnGlobalMsgReceiveListener> handlerListeners = new ArrayList<>();

    private static boolean hasInit = false;

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (OnGlobalMsgReceiveListener listener : handlerListeners) {
                listener.onReceiveGlobalMsg(msg);
            }
        }
    };

    private static LightBroadcast lightBroadcast;

    private LightBroadcast() {}

    public static LightBroadcast getInstance() {
        if (hasInit) {
            return lightBroadcast;
        } else {
            throw new RuntimeException("请在App中初始化 LightBroadcast");
        }
    }

    public synchronized static void init() {
        if (hasInit) {
            return;
        }
        if (lightBroadcast == null) {
            lightBroadcast = new LightBroadcast();
        }
        hasInit = true;
    }

    /**
     * 增加全局消息监听
     * @param listener listener
     */
    public void addOnGlobalMsgReceiveListener(OnGlobalMsgReceiveListener listener) {
        handlerListeners.add(listener);
    }

    /**
     * 移除全局消息监听
     * @param listener listener
     */
    public void removeOnGlobalMsgReceiveListener(OnGlobalMsgReceiveListener listener) {
        handlerListeners.remove(listener);
    }

    public void sendEmptyMessage(int msgWhat) {
        uiHandler.sendEmptyMessage(msgWhat);
    }

    public void post(Runnable r) {
        uiHandler.post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        uiHandler.postDelayed(r, delayMillis);
    }

    public void sendMessage(Message message) {
        uiHandler.sendMessage(message);
    }

    public void sendEmptyMessageDelayed(int msgWhat, long delayMillis) {
        uiHandler.sendEmptyMessageDelayed(msgWhat, delayMillis);
    }

    public void sendEmptyMessageAtTime(int msgWhat, long uptimeMillis) {
        uiHandler.sendEmptyMessageAtTime(msgWhat, uptimeMillis);
    }

    public void sendMessageAtFrontOfQueue(Message message) {
        uiHandler.sendMessageAtFrontOfQueue(message);
    }

    public void sendMessageAtTime(Message message, long uptimeMillis) {
        uiHandler.sendMessageAtTime(message, uptimeMillis);
    }

    public void sendMessageDelayed(Message message, long delayMillis) {
        uiHandler.sendMessageDelayed(message, delayMillis);
    }
}
