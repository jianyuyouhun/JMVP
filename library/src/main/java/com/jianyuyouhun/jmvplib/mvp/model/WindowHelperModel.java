package com.jianyuyouhun.jmvplib.mvp.model;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/8/17.
 */

public class WindowHelperModel extends BaseJModel {

    private List<OnGlobalLayoutListener> onGlobalLayoutListeners = new ArrayList<>();
    /**
     * 键盘变化回调
     */
    public interface IKeyBoardVisibleListener{
        /**
         * 回调参数
         * @param visible           键盘是否打开
         * @param windowBottom      可见窗体底部坐标
         * @param visibleHeight     可见窗体高度
         */
        void onSoftKeyBoardVisible(boolean visible , int windowBottom, int visibleHeight);
    }

    /**
     * 增加键盘高度变化监听器，注意，如果在dialog调用地点应该是onAttachedToWindow方法而不是onCreate
     * @param activity      activity
     * @param listener      回调
     */
    public void addOnSoftKeyBoardVisibleListener(Activity activity, IKeyBoardVisibleListener listener) {
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener(decorView, listener);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        onGlobalLayoutListeners.add(onGlobalLayoutListener);
    }

    /**
     * 移除键盘高度变化监听器，注意，如果在dialog调用地点应该是onDetachedFromWindow而不是onDestroy
     * @param listener      回调
     */
    public void removeOnSoftKeyBoardVisibleListener(IKeyBoardVisibleListener listener) {
        for (OnGlobalLayoutListener onGlobalLayoutListener : onGlobalLayoutListeners) {
            if (onGlobalLayoutListener.getListener() == listener) {
                onGlobalLayoutListeners.remove(onGlobalLayoutListener);
                break;
            }
        }
    }

    private class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private View decorView;
        boolean isVisibleForLast = false;

        public IKeyBoardVisibleListener getListener() {
            return listener;
        }

        private IKeyBoardVisibleListener listener;

        OnGlobalLayoutListener(View decorView, final IKeyBoardVisibleListener listener) {
            this.decorView = decorView;
            this.listener = listener;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHeight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int height = decorView.getHeight() - rect.top;
            //获得键盘高度
            int keyboardHeight = height - displayHeight;
            boolean visible = (double) displayHeight / height < 0.8;
            if (visible != isVisibleForLast) {
                listener.onSoftKeyBoardVisible(visible, keyboardHeight, displayHeight + rect.top);
            }
            isVisibleForLast = visible;
        }
    }
}
