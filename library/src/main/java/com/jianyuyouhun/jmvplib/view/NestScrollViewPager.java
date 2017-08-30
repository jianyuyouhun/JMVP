package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * viewPager封装
 * Created by WangYu on 2016/10/22.
 */
public class NestScrollViewPager extends ViewPager {
    private boolean isScrollAble = true;//是否可滑动

    public NestScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestScrollViewPager(Context context) {
        this(context, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return isScrollAble && super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollAble && super.onTouchEvent(ev);
    }

    public boolean isScrollAble() {
        return isScrollAble;
    }

    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }
}
