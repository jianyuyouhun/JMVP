package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 线性布局
 * 设置enable的同时屏蔽内部成员的所有事件
 * Created by wangyu on 2017/5/9.
 */

public class TouchEnableLinearLayout extends LinearLayout {
    public TouchEnableLinearLayout(Context context) {
        super(context);
    }

    public TouchEnableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEnableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled() || super.onInterceptTouchEvent(ev);
    }
}
