package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 相对布局
 * 设置enable的同时屏蔽内部成员的所有事件
 * Created by wangyu on 2017/5/9.
 */

public class TouchEnableRelativeLayout extends RelativeLayout {
    public TouchEnableRelativeLayout(Context context) {
        super(context);
    }

    public TouchEnableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEnableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled() || super.onInterceptTouchEvent(ev);
    }
}
