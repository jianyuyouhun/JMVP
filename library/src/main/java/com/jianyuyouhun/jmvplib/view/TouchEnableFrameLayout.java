package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 帧布局
 * 设置enable的同时屏蔽内部成员的所有事件
 * Created by wangyu on 2017/5/9.
 */

public class TouchEnableFrameLayout extends FrameLayout {
    public TouchEnableFrameLayout(@NonNull Context context) {
        super(context);
    }

    public TouchEnableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEnableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled() || super.onInterceptTouchEvent(ev);
    }
}
