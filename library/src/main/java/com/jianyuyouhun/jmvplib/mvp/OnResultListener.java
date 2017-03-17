package com.jianyuyouhun.jmvplib.mvp;

import android.os.Handler;

/**
 * 结果回调
 * Created by wangyu on 2017/3/17.
 */

public interface OnResultListener<Info> {
    /** 请求成功 */
    int RESULT_SUCCESS = 0;

    /** 请求失败 */
    int RESULT_FAILED = -1;

    /** 无数据 */
    int RESULT_NO_DATA = -2;

    /**
     * 请求结果回调
     *
     * @param result 请求结果
     * @param data   结果
     */
    void onResult(int result, Info data);

    abstract class OnDelayResultListener<Info> implements OnResultListener<Info> {
        private static final Handler handler = new Handler();
        private long delay;

        public OnDelayResultListener(long delay) {
            this.delay = delay;
        }

        @Override
        public final void onResult(final int result, final Info data) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDelayResult(result, data);
                }
            }, delay);
        }

        public abstract void onDelayResult(int result, Info data);
    }
}
