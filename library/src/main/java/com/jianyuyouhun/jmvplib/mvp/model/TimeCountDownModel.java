package com.jianyuyouhun.jmvplib.mvp.model;

import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 通用倒计时model
 * Created by wangyu on 2017/4/25.
 */

public class TimeCountDownModel extends BaseJModelImpl {

    private static HashMap<String, PayTimerData> mPayTimerDataMap = new HashMap<>();

    /**
     * 添加监听类
     *
     * @param tag      唯一标识  可以用页面类名
     * @param listener 监听回调变化
     */
    public void addPayTimeChangeByTag(String tag, OnPayTimeChangeListener listener) {
        addPayTimeChangeListerByOrderId(tag, tag, listener);

    }

    /**
     * 移除监听类 在页面销毁时必须调用  不然timer无法释放 会有内存泄露问题
     *
     * @param tag 唯一标识  可以用页面类名
     */
    public void removePayTimeByTag(String tag) {
        if (mPayTimerDataMap.containsKey(tag)) {
            mPayTimerDataMap.get(tag).release();
            mPayTimerDataMap.remove(tag);
        }

    }

    /**
     * 重置时间为0
     *
     * @param tag 唯一标识
     */
    public void resetTime(String tag) {
        if (mPayTimerDataMap.containsKey(tag)) {
            mPayTimerDataMap.get(tag).resetTime();
        }

    }

    /**
     * 添加监听  此方法订单列表专用   适用于有多个监听器
     *
     * @param tag      唯一标识页面
     * @param orderId  订单id
     * @param listener 监听
     */
    public void addPayTimeChangeListerByOrderId(String tag, String orderId, OnPayTimeChangeListener listener) {

        if (mPayTimerDataMap.containsKey(tag)) {
            mPayTimerDataMap.get(tag).addPayTimeChangeListener(orderId, listener);
        } else {
            PayTimerData timerData = new PayTimerData();
            timerData.addPayTimeChangeListener(orderId, listener);
            mPayTimerDataMap.put(tag, timerData);
        }
    }

    /**
     * 移除监听  与addPayTimeChangeListerByOrderId 配对使用
     *
     * @param tag     唯一标识  可以用页面类名  列表页使用
     * @param orderId 订单id
     */
    public void removePayTimeChangeListenerByOrderId(String tag, String orderId) {
        if (mPayTimerDataMap.containsKey(tag)) {
            mPayTimerDataMap.get(tag).removePayTimeChangeListener(orderId);
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
        if (mPayTimerDataMap.containsKey(tag)) {
            PayTimerData payTimerData = mPayTimerDataMap.get(tag);
            int changCount = (int) ((System.currentTimeMillis() - payTimerData.count) / 1000);
            return changCount;
        } else {
            return 0;
        }
    }

    public class PayTimerData {
        private HashMap<String, OnPayTimeChangeListener> onPayTimeChangeListenerHashMap = new HashMap<>();
        private Timer timer;
        private PayTimeTask payTimeTask;
        private long count;

        public PayTimerData() {
            startTimer();
        }

        private void startTimer() {
            timer = new Timer();
            payTimeTask = new PayTimeTask();
            count = System.currentTimeMillis();
            timer.schedule(payTimeTask, 1000, 1000);
        }

        private void cancelTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (payTimeTask != null) {
                payTimeTask = null;
            }
        }

        private void resetTime() {
            count = System.currentTimeMillis();
        }

        private void release() {
            cancelTimer();
        }

        public void addPayTimeChangeListener(String orderId, OnPayTimeChangeListener listener) {
            if (!onPayTimeChangeListenerHashMap.containsKey(orderId)) {
                onPayTimeChangeListenerHashMap.put(orderId, listener);
            }
        }

        public void removePayTimeChangeListener(String orderId) {
            if (onPayTimeChangeListenerHashMap.containsKey(orderId)) {
                onPayTimeChangeListenerHashMap.remove(orderId);
            }
        }


        private void notifyTimeChange(int changCount) {
            HashMap<String, OnPayTimeChangeListener> tempMap = new HashMap<>(onPayTimeChangeListenerHashMap);
            Iterator iter = tempMap.entrySet().iterator();
            if (iter == null) {
                return;
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                if (entry == null) {
                    continue;
                }
                OnPayTimeChangeListener listener = (OnPayTimeChangeListener) entry.getValue();
                listener.onPayTimeChange(changCount);
            }
        }

        public class PayTimeTask extends TimerTask {

            @Override
            public void run() {
                getSuperHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        int changCount = (int) ((System.currentTimeMillis() - count) / 1000);
                        notifyTimeChange(changCount);
                    }
                });
            }
        }

    }

    public interface OnPayTimeChangeListener {

        void onPayTimeChange(int changeCount);
    }
}
