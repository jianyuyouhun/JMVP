package com.jianyuyouhun.jmvplib.mvp.model;

import android.os.Handler;
import android.os.Looper;

import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 通用倒计时model
 * Created by wangyu on 2017/4/25.
 */

public class TimeCountDownModel extends BaseJModel {

    private Handler handler = new Handler(Looper.getMainLooper());
    private static HashMap<String, TimerData> mTimerDataMap = new HashMap<>();

    /**
     * 添加监听类
     *
     * @param tag      唯一标识  可以用页面类名
     * @param listener 监听回调变化
     */
    public void addTimeChangeByTag(String tag, OnTimeChangeListener listener) {
        addTimeChangeListenerById(tag, tag, listener);

    }

    /**
     * 移除监听类 在页面销毁时必须调用  不然timer无法释放 会有内存泄露问题
     *
     * @param tag 唯一标识  可以用页面类名
     */
    public void removePayTimeByTag(String tag) {
        if (mTimerDataMap.containsKey(tag)) {
            mTimerDataMap.get(tag).release();
            mTimerDataMap.remove(tag);
        }

    }

    /**
     * 重置时间为0
     *
     * @param tag 唯一标识
     */
    public void resetTime(String tag) {
        if (mTimerDataMap.containsKey(tag)) {
            mTimerDataMap.get(tag).resetTime();
        }

    }

    /**
     * 添加监听  此方法订单列表专用   适用于有多个监听器
     *
     * @param tag      唯一标识页面
     * @param orderId  订单id
     * @param listener 监听
     */
    public void addTimeChangeListenerById(String tag, String orderId, OnTimeChangeListener listener) {

        if (mTimerDataMap.containsKey(tag)) {
            mTimerDataMap.get(tag).addTimeChangeListener(orderId, listener);
        } else {
            TimerData timerData = new TimerData();
            timerData.addTimeChangeListener(orderId, listener);
            mTimerDataMap.put(tag, timerData);
        }
    }

    /**
     * 移除监听  与addPayTimeChangeListerByOrderId 配对使用
     *
     * @param tag     唯一标识  可以用页面类名  列表页使用
     * @param orderId 订单id
     */
    public void removeTimeChangeListenerById(String tag, String orderId) {
        if (mTimerDataMap.containsKey(tag)) {
            mTimerDataMap.get(tag).removeTimeChangeListener(orderId);
        }
    }

    /**
     * 显示时间转换
     *
     * @param totolSeconds 秒数
     * @return 返回转换好的时间格式
     */
    public String getShowTime(int totolSeconds) {
        String showTime;
        int modleSeconds = totolSeconds % 60;
        if (totolSeconds >= 60) {
            int minute = totolSeconds / 60;
            if (minute >= 10) {
                if (modleSeconds >= 10) {
                    showTime = minute + ":" + modleSeconds;
                } else {
                    showTime = minute + ":0" + modleSeconds;
                }
            } else {
                if (modleSeconds >= 10) {
                    showTime = "0" + minute + ":" + modleSeconds;
                } else {
                    showTime = "0" + minute + ":0" + modleSeconds;
                }
            }
        } else {
            if (modleSeconds >= 10) {
                showTime = "00:" + modleSeconds;
            } else {
                showTime = "00:0" + modleSeconds;
            }
        }

        return showTime;
    }

    /**
     * 根据tag获取当前时钟的count
     * @param tag   时钟tag
     * @return timeStamp 秒
     */
    public int getTimeCurrentCountByTag(String tag) {
        if (mTimerDataMap.containsKey(tag)) {
            TimerData timerData = mTimerDataMap.get(tag);
            int changCount = (int) ((System.currentTimeMillis() - timerData.count) / 1000);
            return changCount;
        } else {
            return 0;
        }
    }

    public class TimerData {
        private HashMap<String, OnTimeChangeListener> onTimeChangeListenerHashMap = new HashMap<>();
        private Timer timer;
        private TimeTask timeTask;
        private long count;

        public TimerData() {
            startTimer();
        }

        private void startTimer() {
            timer = new Timer();
            timeTask = new TimeTask();
            count = System.currentTimeMillis();
            timer.schedule(timeTask, 1000, 1000);
        }

        private void cancelTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timeTask != null) {
                timeTask = null;
            }
        }

        private void resetTime() {
            count = System.currentTimeMillis();
        }

        private void release() {
            cancelTimer();
        }

        public void addTimeChangeListener(String orderId, OnTimeChangeListener listener) {
            if (!onTimeChangeListenerHashMap.containsKey(orderId)) {
                onTimeChangeListenerHashMap.put(orderId, listener);
            }
        }

        public void removeTimeChangeListener(String orderId) {
            if (onTimeChangeListenerHashMap.containsKey(orderId)) {
                onTimeChangeListenerHashMap.remove(orderId);
            }
        }


        private void notifyTimeChange(int changCount) {
            HashMap<String, OnTimeChangeListener> tempMap = new HashMap<>(onTimeChangeListenerHashMap);
            Iterator iter = tempMap.entrySet().iterator();
            if (iter == null) {
                return;
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                if (entry == null) {
                    continue;
                }
                OnTimeChangeListener listener = (OnTimeChangeListener) entry.getValue();
                listener.onTimeChange(changCount);
            }
        }

        public class TimeTask extends TimerTask {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int changCount = (int) ((System.currentTimeMillis() - count) / 1000);
                        notifyTimeChange(changCount);
                    }
                });
            }
        }

    }

    public interface OnTimeChangeListener {

        void onTimeChange(int changeCount);
    }
}
