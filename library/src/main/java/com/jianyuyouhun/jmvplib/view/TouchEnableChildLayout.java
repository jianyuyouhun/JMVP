package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可设置enable的同时屏蔽内部成员的所有事件
 * Created by wangyu on 2017/9/13.
 */

public class TouchEnableChildLayout extends ViewGroup {

    public TouchEnableChildLayout(Context context) {
        super(context);
    }

    public TouchEnableChildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEnableChildLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0)
            throw new IllegalStateException("TouchEnableChildLayout can host only one direct child");
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0)
            throw new IllegalStateException("TouchEnableChildLayout can host only one direct child");
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("TouchEnableChildLayout can host only one direct child");
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("TouchEnableChildLayout can host only one direct child");
        super.addView(child, index, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();

            int left = (width - viewWidth) / 2;
            int top = (height - viewHeight) / 2;
            int right = left + viewWidth;
            int bottom = top + viewHeight;

            view.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);

        int newWidth = (int) (width);
        int newHeight = (int) (height);

        if (getChildCount() > 0)
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(newWidth, newHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled() || super.onInterceptTouchEvent(ev);
    }
}
